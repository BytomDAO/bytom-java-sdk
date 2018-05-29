package io.bytom.api;

import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionFeed {
    /**
     * name of the transaction feed.
     */
    public String alias;
    /**
     * filter, filter of the transaction feed.
     */
    public String filter;

    /**
     * param, param of the transaction feed.
     */
    public TransactionFeedParam param;

    private static Logger logger = Logger.getLogger(Transaction.class);

    /**
     * Serializes the TransactionFeed into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the TransactionFeed object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static class Builder {

        public String alias;

        public String filter;

        public Builder() {
        }

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder setFilter(String filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Call create-transaction-feed api
         *
         * @param client
         * @throws BytomException
         */
        public void create(Client client) throws BytomException {
            client.request("create-transaction-feed", this);
            logger.info("create-transaction-feed");
        }
    }

    /**
     * Call get-transaction-feed api
     *
     * @param client
     * @param alias
     * @return
     * @throws BytomException
     */
    public static TransactionFeed get(Client client, String alias) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("alias", alias);

        // the return param contains txfeed.
        TransactionFeed transactionFeed = client.requestGet("get-transaction-feed",
                                                req, "txfeed", TransactionFeed.class);
        logger.info("get-transaction-feed.");
        logger.info(transactionFeed.toJson());

        return transactionFeed;
    }

    /**
     * Call update-transaction-feed api
     *
     * @param client
     * @param alias
     * @param filter
     * @throws BytomException
     */
    public static void update(Client client, String alias, String filter) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("alias", alias);
        req.put("filter", filter);
        client.request("update-transaction-feed", req);
        logger.info("update-transaction-feed successfully");
    }

    /**
     * Call list-transaction-feeds api
     * @param client
     * @return
     * @throws BytomException
     */
    public static List<TransactionFeed> list(Client client) throws BytomException {

        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{TransactionFeed.class});
        List<TransactionFeed> transactionFeedList = client.request("list-transaction-feeds", null, listType);

        logger.info("list-transaction-feeds:");
        logger.info("size of transactionList:" + transactionFeedList.size());
        for (int i =0 ;i < transactionFeedList.size();i++) {
            logger.info(transactionFeedList.get(i).toJson());
        }
        return transactionFeedList;
    }

    /**
     * Call delete-transaction-feed api
     *
     * @param client
     * @param alias
     * @throws BytomException
     */
    public static void delete(Client client, String alias) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("alias", alias);
        client.request("delete-transaction-feed", req);
        logger.info("delete-transaction-feed successfully");
    }



    public class TransactionFeedParam {

        /**
         * assetid
         */
        public String assetid;

        /**
         * lowerlimit
         */
        public long lowerlimit;

        /**
         * upperlimit
         */
        public long upperlimit;

    }
}
