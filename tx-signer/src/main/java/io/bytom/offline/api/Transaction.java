package io.bytom.offline.api;

import io.bytom.offline.common.Utils;
import io.bytom.offline.exception.MapTransactionException;
import io.bytom.offline.exception.SerializeTransactionException;
import io.bytom.offline.exception.SignTransactionException;
import io.bytom.offline.types.*;
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

    private String txID;
    /**
     * version
     */
    private Integer version;
    /**
     * size
     */
    private Integer size;
    /**
     * time_range
     */
    private Integer timeRange;

    /**
     * List of specified inputs for a transaction.
     */
    private List<BaseInput> inputs;

    /**
     * List of specified outputs for a transaction.
     */
    private List<OriginalOutput> outputs;

    public Transaction(Builder builder) {
        this.inputs = builder.inputs;
        this.outputs = builder.outputs;
        this.version = builder.version;
        this.size = builder.size;
        this.timeRange = builder.timeRange;

        this.validate();
        this.mapTx();
        this.sign();
    }

    public static class Builder {

        private Integer version = 1;

        private Integer size = 0;

        private Integer timeRange;

        private List<BaseInput> inputs;
        private List<OriginalOutput> outputs;

        public Builder() {
            this.inputs = new ArrayList<>();
            this.outputs = new ArrayList<>();
        }

        public Builder addInput(BaseInput input) {
            this.inputs.add(input);
            return this;
        }

        public Builder addOutput(OriginalOutput output) {
            this.outputs.add(output);
            return this;
        }

        public Builder setTimeRange(int timeRange) {
            this.timeRange = timeRange;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    public void sign() {
        for (BaseInput input : inputs) {
            try {
                input.buildWitness(txID);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SignTransactionException(e);
            }
        }
    }

    public String rawTransaction() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(7);

            Utils.writeVarint(version, stream);

            Utils.writeVarint(timeRange, stream);

            Utils.writeVarint(inputs.size(), stream);
            for (BaseInput input : inputs) {
                stream.write(input.serializeInput());
            }

            Utils.writeVarint(outputs.size(), stream);
            for (OriginalOutput output : outputs) {
                stream.write(output.serializeOutput());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializeTransactionException(e);
        }
        return Hex.toHexString(stream.toByteArray());
    }

    private void validate() {
        if (version == null) {
            throw new IllegalArgumentException("the version of transaction must be specified.");
        }
        if (timeRange == null) {
            throw new IllegalArgumentException("the time range of transaction must be specified.");
        }
        if (size == null) {
            throw new IllegalArgumentException("the size range of transaction must be specified.");
        }

        for (BaseInput input : inputs) {
            input.validate();
        }
    }

    private void mapTx() {
        Map<Hash, Entry> entryMap = new HashMap<>();
        ValueSource[] muxSources = new ValueSource[inputs.size()];
        List<InputEntry> inputEntries = new ArrayList<>();

        try {
            for (int i = 0; i < inputs.size(); i++) {
                BaseInput input = inputs.get(i);
                InputEntry inputEntry =  input.toInputEntry(entryMap, i);
                Hash spendID = addEntry(entryMap, inputEntry);
                input.setInputID(spendID.toString());

                muxSources[i] = new ValueSource(spendID, input.getAssetAmount(), 0);
                inputEntries.add(inputEntry);
            }

            Mux mux = new Mux(muxSources, new Program(1, new byte[]{0x51}));
            Hash muxID = addEntry(entryMap, mux);
            for (InputEntry inputEntry : inputEntries) {
                inputEntry.setDestination(muxID, inputEntry.getOrdinal(), entryMap);
            }

            List<Hash> resultIDList = new ArrayList<>();
            for (int i = 0; i < outputs.size(); i++) {
                OriginalOutput output = outputs.get(i);

                AssetAmount amount = new AssetAmount(new AssetID(output.getAssetId()), output.getAmount());
                ValueSource src = new ValueSource(muxID, amount, i);

                Hash resultID;
                if (output.getControlProgram().startsWith("6a")) {
                    Retirement retirement = new Retirement(src, i);
                    resultID = addEntry(entryMap, retirement);
                } else {
                    Program program = new Program(1, Hex.decode(output.getControlProgram()));
                    Output oup = new Output(src, program, i,output.stateData.toByteArray());
                    resultID = addEntry(entryMap, oup);
                }

                resultIDList.add(resultID);
                output.setId(resultID.toString());

                ValueDestination destination = new ValueDestination(resultID, src.getValue(), 0);
                mux.getWitnessDestinations().add(destination);
            }

            TxHeader txHeader = new TxHeader(version, size, timeRange, resultIDList.toArray(new Hash[]{}));
            Hash txID = addEntry(entryMap, txHeader);
            this.txID = txID.toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new MapTransactionException(e);
        }
    }

    private Hash addEntry(Map<Hash, Entry> entryMap, Entry entry) {
        Hash id = entry.entryID();
        entryMap.put(id, entry);
        return id;
    }

    public String getTxID() {
        return txID;
    }

    public Integer getVersion() {
        return version;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getTimeRange() {
        return timeRange;
    }

    public List<BaseInput> getInputs() {
        return inputs;
    }

    public List<OriginalOutput> getOutputs() {
        return outputs;
    }
}
