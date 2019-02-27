package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.ParameterizedTypeImpl;
import io.bytom.common.SuccessRespon;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2018/10/24.
 */

public class Transaction {

    @SerializedName("tx_id")
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
    @SerializedName("time_range")
    public Integer timeRange;

    /**
     * status
     */
    public Integer fee;

    /**
     * List of specified inputs for a transaction.
     */
    public List<AnnotatedInput> inputs;

    /**
     * List of specified outputs for a transaction.
     */
    public List<AnnotatedOutput> outputs;

    //    public InputWitnessComponent inputWitnessComponent;
    private static Logger logger = Logger.getLogger(Transaction.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static Transaction fromJson(String json) {
        return Utils.serializer.fromJson(json, Transaction.class);
    }

    public static Transaction fromSuccessRespon(String json) {
        Type responType = new ParameterizedTypeImpl(SuccessRespon.class, new Class[]{Transaction.class});
        SuccessRespon<Transaction> result = Utils.serializer.fromJson(json, responType);
        return result.dataObject;
    }

    public static Transaction decode(Client client, String txId) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("raw_transaction", txId);
        Transaction Transaction =
                client.request("decode-raw-transaction", req, Transaction.class);

        logger.info("decode-raw-transaction:");
        logger.info(Transaction.toJson());

        return Transaction;
    }

    public static class Builder {
        @SerializedName("tx_id")
        public String txID;
        public Integer version;
        public Integer size;
        @SerializedName("time_range")
        public Integer timeRange;

        Transaction tx;
        List<AnnotatedInput> inputs;
        List<AnnotatedOutput> outputs;

        public Builder() {
            this.inputs = new ArrayList<>();
            this.outputs = new ArrayList<>();
        }

        public Builder addInput(AnnotatedInput input) {
            this.inputs.add(input);
            return this;
        }

        public Builder addOutput(AnnotatedOutput output) {
            this.outputs.add(output);
            return this;
        }


        public Transaction build(Integer version, Integer timeRange, Integer size) {
            tx = new Transaction();
            tx.inputs = this.inputs;
            tx.outputs = this.outputs;
            tx.version = version;
            tx.timeRange = timeRange;
            tx.size = size;
            return tx;
        }

        public Transaction build(int timeRange) {
            tx = new Transaction();
            tx.inputs = this.inputs;
            tx.outputs = this.outputs;
            tx.version = 1;
            tx.size = 0;
            tx.timeRange = timeRange;
            return tx;
        }
    }

    public static class AnnotatedInput {

        @SerializedName("input_id")
        public String inputID;
        /**
         * address
         */
        public String address;

        /**
         * The number of units of the asset being issued or spent.
         */
        public long amount;

        //        /**
//         * The definition of the asset being issued or spent (possibly null).
//         */
//        @SerializedName("asset_definition")
//        private Map<String, Object> assetDefinition;
        @SerializedName("asset_definition")
        public String assetDefinition;

        /**
         * The id of the asset being issued or spent.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The control program which must be satisfied to transfer this output.
         */
        @SerializedName("control_program")
        public String controlProgram;

        /**
         * The id of the output consumed by this input. Null if the input is an
         * issuance.
         */
        @SerializedName("spent_output_id")
        public String spentOutputId;

        /**
         * The type of the input.<br>
         * Possible values are "issue" and "spend".
         */
        public int type;

        public String sourceId;

        public long sourcePosition;

        public String nonce;

        private int controlProgramIndex;
        private boolean change;

        public int keyIndex;
        public String chainPath;

        @SerializedName("witness_component")
        public InputWitnessComponent witnessComponent;

        @Override
        public String toString() {
            return Utils.serializer.toJson(this);
        }

        public int getControlProgramIndex() {
            return controlProgramIndex;
        }

        public AnnotatedInput setControlProgramIndex(int controlProgramIndex) {
            this.controlProgramIndex = controlProgramIndex;
            return this;
        }

        public boolean isChange() {
            return change;
        }

        public AnnotatedInput setChange(boolean change) {
            this.change = change;
            return this;
        }

        public AnnotatedInput setAmount(long amount) {
            this.amount = amount;
            return this;
        }

        public AnnotatedInput setAssetId(String assetId) {
            this.assetId = assetId;
            return this;
        }

        public AnnotatedInput setControlProgram(String controlProgram) {
            this.controlProgram = controlProgram;
            return this;
        }

        public AnnotatedInput setType(int type) {
            this.type = type;
            return this;
        }

        public AnnotatedInput setSourceId(String sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public AnnotatedInput setSourcePosition(long sourcePosition) {
            this.sourcePosition = sourcePosition;
            return this;
        }

        public AnnotatedInput setNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public AnnotatedInput setAssetDefinition(String assetDefinition) {
            this.assetDefinition = assetDefinition;
            return this;
        }

        public int getKeyIndex() {
            return keyIndex;
        }

        public AnnotatedInput setKeyIndex(int keyIndex) {
            this.keyIndex = keyIndex;
            return this;
        }

    }

    public static class AnnotatedOutput {

        /**
         * address
         */
        public String address;

        /**
         * The number of units of the asset being controlled.
         */
        public long amount;

        /**
         * The definition of the asset being controlled (possibly null).
         */
        @SerializedName("asset_definition")
        public Map<String, Object> assetDefinition;

        /**
         * The id of the asset being controlled.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The control program which must be satisfied to transfer this output.
         */
        @SerializedName("control_program")
        public String controlProgram;

        /**
         * The id of the output.
         */
        @SerializedName("id")
        public String id;

        /**
         * The output's position in a transaction's list of outputs.
         */
        public Integer position;

        /**
         * The type the output.<br>
         * Possible values are "control" and "retire".
         */
        public String type;

        public AnnotatedOutput setAddress(String address) {
            this.address = address;
            return this;
        }

        public AnnotatedOutput setAmount(long amount) {
            this.amount = amount;
            return this;
        }

        public AnnotatedOutput setAssetId(String assetId) {
            this.assetId = assetId;
            return this;
        }

        public AnnotatedOutput setControlProgram(String controlProgram) {
            this.controlProgram = controlProgram;
            return this;
        }

        public AnnotatedOutput setPosition(Integer position) {
            this.position = position;
            return this;
        }
    }

    /**
     * A single witness component, holding information that will become the input
     * witness.
     */
    public static class InputWitnessComponent {

        /**
         * The list of signatures made with the specified keys (null unless type is
         * "signature").
         */
        public String[] signatures;
    }
}
