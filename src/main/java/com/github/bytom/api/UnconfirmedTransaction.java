package com.github.bytom.api;

import com.google.gson.annotations.SerializedName;
import com.github.bytom.common.Utils;
import com.github.bytom.exception.BytomException;
import com.github.bytom.http.Client;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnconfirmedTransaction {
    /**
     * Unique identifier, or transaction hash, of a transaction.
     */
    private String id;

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
    @SerializedName("time_range")
    private Integer timeRange;

    /**
     * status
     */
    @SerializedName("status_fail")
    private boolean statusFail;

    /**
     * List of specified inputs for a transaction.
     */
    private List<AnnotatedInput> inputs;

    /**
     * List of specified outputs for a transaction.
     */
    private List<AnnotatedOutput> outputs;

    private static Logger logger = Logger.getLogger(UnconfirmedTransaction.class);

    /**
     * Serializes the UnconfirmedTransaction into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the UnconfirmedTransaction object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * Call get-unconfirmed-transaction api
     *
     * @param client
     * @param txId
     * @return
     * @throws BytomException
     */
    public static UnconfirmedTransaction get(Client client, String txId) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("tx_id", txId);
        UnconfirmedTransaction UCTX = client.request("get-unconfirmed-transaction",
                req, UnconfirmedTransaction.class);

        logger.info("get-unconfirmed-transaction:");
        logger.info(UCTX.toJson());

        return UCTX;
    }

    public static UTXResponse list(Client client) throws BytomException {
        UTXResponse utxResponse =
                client.request("list-unconfirmed-transactions", null, UTXResponse.class);

        logger.info("list-unconfirmed-transactions:");
        logger.info(utxResponse.toJson());

        return utxResponse;
    }

    public static class UTXResponse {

        @SerializedName("total")
        public Integer total;

        @SerializedName("tx_ids")
        public List<String> txIds;

        public String toJson() {
            return Utils.serializer.toJson(this);
        }
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
