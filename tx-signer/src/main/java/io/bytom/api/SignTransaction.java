package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.ExpandedPrivateKey;
import io.bytom.common.ParameterizedTypeImpl;
import io.bytom.common.SuccessRespon;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2018/10/24.
 */
public class SignTransaction {

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

    private static Logger logger = Logger.getLogger(SignTransaction.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static SignTransaction fromJson(String json) {
        return Utils.serializer.fromJson(json, SignTransaction.class);
    }

    public static SignTransaction fromSuccessRespon(String json) {
        Type responType = new ParameterizedTypeImpl(SuccessRespon.class, new Class[]{SignTransaction.class});
        SuccessRespon<SignTransaction> result = Utils.serializer.fromJson(json, responType);
        return result.dataObject;
    }

    public static SignTransaction decode(Client client, String txId) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("raw_transaction", txId);
        SignTransaction SignTransaction =
                client.request("decode-raw-transaction", req, SignTransaction.class);

        logger.info("decode-raw-transaction:");
        logger.info(SignTransaction.toJson());

        return SignTransaction;
    }

    public class AnnotatedInput {

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

        /**
         * The definition of the asset being issued or spent (possibly null).
         */
        @SerializedName("asset_definition")
        private Map<String, Object> assetDefinition;

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
        private String type;

        public String sourceId;

        public long sourcePosition;

        public String chainPath;

        @SerializedName("witness_component")
        public InputWitnessComponent witnessComponent;

        @Override
        public String toString() {
            return Utils.serializer.toJson(this);
        }

    }

    public class AnnotatedOutput {

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
