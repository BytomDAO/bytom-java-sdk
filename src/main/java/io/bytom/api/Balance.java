package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Balance {

    /**
     * account id
     */
    @SerializedName("account_id")
    public String accountId;

    /**
     * name of account
     */
    @SerializedName("account_alias")
    public String accountAlias;

    /**
     * sum of the unspent outputs.
     * specified asset balance of account.
     */
    public long amount;

    /**
     * asset id
     */
    @SerializedName("asset_id")
    public String assetId;

    /**
     * name of asset
     */
    @SerializedName("asset_alias")
    public String assetAlias;

    @SerializedName("asset_definition")
    public Map<String, Object> definition;

    private static Logger logger = Logger.getLogger(Balance.class);

    /**
     * Serializes the Balance into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }


    public static class QueryBuilder {

        /**
         * Call list-Balances api
         *
         * @param client
         * @return
         * @throws BytomException
         */
        public List<Balance> list(Client client) throws BytomException {

            // TODO: 2018/5/23 need tx and test
            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Balance.class});
            List<Balance> balanceList = client.request("list-balances", null, listType);
            logger.info("list-balances:");
            logger.info("size of :" + balanceList.size());
            logger.info(balanceList.get(0).toJson());

            return balanceList;
        }
    }
}
