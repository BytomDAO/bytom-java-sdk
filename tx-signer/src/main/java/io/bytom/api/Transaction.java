package io.bytom.api;

import io.bytom.common.Utils;
import io.bytom.types.*;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2018/10/24.
 */

public class Transaction {

    public String txID;
    /**
     * version
     */
    public Integer version;
    /**
     * size
     */
    public Integer size;
    /**
     * time_range
     */
    public Integer timeRange;

    /**
     * List of specified inputs for a transaction.
     */
    public List<BaseInput> inputs;

    /**
     * List of specified outputs for a transaction.
     */
    public List<Output> outputs;

    public Transaction(Builder builder) {
        this.inputs = builder.inputs;
        this.outputs = builder.outputs;
        this.version = builder.version;
        this.size = builder.size;
        this.timeRange = builder.timeRange;
        mapTx();
        sign();
    }

    public static class Builder {

        private String txID;

        private Integer version = 1;

        private Integer size = 0;

        private Integer timeRange;

        private List<BaseInput> inputs;
        private List<Output> outputs;

        public Builder() {
            this.inputs = new ArrayList<>();
            this.outputs = new ArrayList<>();
        }

        public Builder addInput(BaseInput input) {
            this.inputs.add(input);
            return this;
        }

        public Builder addOutput(Output output) {
            this.outputs.add(output);
            return this;
        }

        public Builder setTimeRange(int timeRange) {
            this.timeRange = timeRange;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    private void sign() {
        for (BaseInput input : inputs) {
            try {
                input.buildWitness(txID);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public String rawTransaction() {
        String rawTransaction;
        //开始序列化
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(7);
            // version
            if (null != version)
                Utils.writeVarint(version, stream);
            if (null != timeRange)
                Utils.writeVarint(timeRange, stream);
            //inputs
            if (null != inputs && inputs.size() > 0) {
                Utils.writeVarint(inputs.size(), stream);
                for (BaseInput input : inputs) {
                    System.out.println(Hex.toHexString(input.serializeInput()));
                    stream.write(input.serializeInput());
                }
            }

            //outputs
            if (null != outputs && outputs.size() > 0) {
                Utils.writeVarint(outputs.size(), stream);
                for (Output output : outputs) {
                    stream.write(output.serializeOutput());
                }
            }
            byte[] data = stream.toByteArray();
            rawTransaction = Hex.toHexString(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rawTransaction;
    }

    private void mapTx() {
        Map<Hash, Entry> entryMap = new HashMap<>();
        ValueSource[] muxSources = new ValueSource[inputs.size()];
        List<InputEntry> inputEntries = new ArrayList<>();

        try {
            for (int i = 0; i < inputs.size(); i++) {
                BaseInput input = inputs.get(i);
                InputEntry inputEntry =  input.convertInputEntry(entryMap, i);
                Hash spendID = addEntry(entryMap, inputEntry);
                input.setInputID(spendID.toString());

                muxSources[i] = new ValueSource(spendID, input.getAssetAmount(), 0);
                inputEntries.add(inputEntry);
            }

            Mux mux = new Mux(muxSources, new Program(1, new byte[]{0x51}));
            Hash muxID = addEntry(entryMap, mux);
            for (InputEntry inputEntry : inputEntries) {
                inputEntry.setDestination(muxID, inputEntry.ordinal, entryMap);
            }

            List<Hash> resultIDList = new ArrayList<>();
            for (int i = 0; i < outputs.size(); i++) {
                Output output = outputs.get(i);

                AssetAmount amount = new AssetAmount(new AssetID(output.assetId), output.amount);
                ValueSource src = new ValueSource(muxID, amount, i);

                Hash resultID;
                if (output.controlProgram.startsWith("6a")) {
                    Retirement retirement = new Retirement(src, i);
                    resultID = addEntry(entryMap, retirement);
                } else {
                    Program prog = new Program(1, Hex.decode(output.controlProgram));
                    OutputEntry oup = new OutputEntry(src, prog, i);
                    resultID = addEntry(entryMap, oup);
                }

                resultIDList.add(resultID);
                output.id = resultID.toString();

                ValueDestination destination = new ValueDestination(resultID, src.value, 0);
                mux.witnessDestinations.add(destination);
            }

            TxHeader txHeader = new TxHeader(version, size, timeRange, resultIDList.toArray(new Hash[]{}));
            Hash txID = addEntry(entryMap, txHeader);
            this.txID = txID.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Hash addEntry(Map<Hash, Entry> entryMap, Entry entry) {
        Hash id = entry.entryID();
        entryMap.put(id, entry);
        return id;
    }
}
