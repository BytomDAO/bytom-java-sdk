package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.offline.common.Utils;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawTransaction {
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

    private static Logger logger = Logger.getLogger(RawTransaction.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }


    public static RawTransaction decode(Client client, String txId) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("raw_transaction", txId);
        RawTransaction rawTransaction =
                client.request("decode-raw-transaction", req, RawTransaction.class);

        logger.info("decode-raw-transaction:");
        logger.info(rawTransaction.toJson());

        return rawTransaction;
    }

    public static class AnnotatedInput {

        /**
         * address
         */
        private String address;

        /**
         * The number of units of the asset being issued or spent.
         */
        private long amount;

        /**
         * The definition of the asset being issued or spent (possibly null).
         */
        @SerializedName("asset_definition")
        private Map<String, Object> assetDefinition;

        /**
         * The id of the asset being issued or spent.
         */
        @SerializedName("asset_id")
        private String assetId;

        /**
         * The control program which must be satisfied to transfer this output.
         */
        @SerializedName("control_program")
        private String controlProgram;

        /**
         * The id of the output consumed by this input. Null if the input is an
         * issuance.
         */
        @SerializedName("spent_output_id")
        private String spentOutputId;

        /**
         * The type of the input.<br>
         * Possible values are "issue" and "spend".
         */
        private String type;
    }

    public static class AnnotatedOutput {

        /**
         * address
         */
        private String address;

        /**
         * The number of units of the asset being controlled.
         */
        private long amount;

        /**
         * The definition of the asset being controlled (possibly null).
         */
        @SerializedName("asset_definition")
        private Map<String, Object> assetDefinition;

        /**
         * The id of the asset being controlled.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The control program which must be satisfied to transfer this output.
         */
        @SerializedName("control_program")
        private String controlProgram;

        /**
         * The id of the output.
         */
        @SerializedName("id")
        private String id;

        /**
         * The output's position in a transaction's list of outputs.
         */
        private Integer position;

        /**
         * The type the output.<br>
         * Possible values are "control" and "retire".
         */
        private String type;

    }
}
