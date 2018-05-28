package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Miner {

    private static Logger logger = Logger.getLogger(Miner.class);

    /**
     * Call is-mining api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static boolean isMining(Client client) throws BytomException {
        return client.requestGet("is-mining", null, "is_mining", Boolean.class);
    }

    /**
     * Call set-mining api
     *
     * @param client
     * @param isMining
     * @throws BytomException
     */
    public static void setMining(Client client, boolean isMining) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("is_mining", isMining);
        client.request("set-mining", req);
        // TODO: 2018/5/22
    }

    /**
     * Call get-work api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static MinerWork getWork(Client client) throws BytomException {
        MinerWork minerWork = client.request("get-work", null, MinerWork.class);

        logger.info("get-work:");
        logger.info(minerWork.toJson());

        return minerWork;
    }

    /**
     * Call submit-work api
     *
     * @param client
     * @param blockHeader
     * @throws BytomException
     */
    public static void submiWork(Client client, String blockHeader) throws BytomException
    {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("block_header", blockHeader);
        client.request("submit-work", req);
        logger.info("submit-work.");
    }

    public static class MinerWork {

        /**
         * block_header, raw block header.
         */
        @SerializedName("block_header")
        public String blockHeader;

        /**
         *  seed, seed.
         */
        @SerializedName("seed")
        public String seed;

        public String toJson() {
            return Utils.serializer.toJson(this);
        }


    }
}
