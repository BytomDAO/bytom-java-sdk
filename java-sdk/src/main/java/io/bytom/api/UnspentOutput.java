package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.ParameterizedTypeImpl;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;

public class UnspentOutput {
    /**
     * The id of the account controlling this output (possibly null if a control program
     * is specified).
     */
    @SerializedName("account_id")
    public String accountId;

    /**
     * The alias of the account controlling this output (possibly null if a control
     * program is specified).
     */
    @SerializedName("account_alias")
    public String accountAlias;

    /**
     * The id of the asset being controlled.
     */
    @SerializedName("asset_id")
    public String assetId;

    /**
     * The alias of the asset being controlled.
     */
    @SerializedName("asset_alias")
    public String assetAlias;

    /**
     * The number of units of the asset being controlled.
     */
    public long amount;

    /**
     * address of account
     */
    public String address;

    /**
     * whether the account address is change
     */
    public boolean change;

    /**
     * The ID of the output.
     */
    @SerializedName("id")
    public String id;

    /**
     * The control program which must be satisfied to transfer this output.
     */
    @SerializedName("program")
    public String program;

    @SerializedName("control_program_index")
    public String controlProgramIndex;

    /**
     * source unspent output id
     */
    @SerializedName("source_id")
    public String sourceId;

    /**
     * position of source unspent output id in block
     */
    @SerializedName("source_pos")
    public int sourcePos;

    /**
     * The definition of the asset being controlled (possibly null).
     */
    @SerializedName("valid_height")
    public int validHeight;

    private static Logger logger = Logger.getLogger(UnspentOutput.class);

    /**
     * Serializes the Address into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static class QueryBuilder {

        /**
         * id of unspent output.
         */
        public String id;

        public QueryBuilder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * call list-unspent-outputs api
         *
         * @param client client object that makes requests to the core
         * @return
         * @throws BytomException BytomException
         */
        public List<UnspentOutput> list(Client client) throws BytomException {

            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{UnspentOutput.class});
            List<UnspentOutput> unspentOutputList = client.request("list-unspent-outputs", this, listType);
            logger.info("list-unspent-outputs:");
            logger.info("size of unspentOutputList:" + unspentOutputList.size());
            for (UnspentOutput UTXO : unspentOutputList) {
                logger.info(UTXO.toJson());
            }

            return unspentOutputList;
        }

    }
}
