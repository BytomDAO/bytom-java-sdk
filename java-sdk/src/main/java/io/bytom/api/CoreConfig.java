package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

public class CoreConfig {


    private static Logger logger = Logger.getLogger(CoreConfig.class);

    /**
     * Call net-info api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static NetInfo getNetInfo(Client client) throws BytomException {
        NetInfo netInfo = client.request("net-info", null, NetInfo.class);

        logger.info("net-info:");
        logger.info(netInfo.toJson());

        return netInfo;
    }

    /**
     * Call gas-rate api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static Integer getGasRate(Client client) throws BytomException {
        Integer gas = client.requestGet("gas-rate", null, "gas_rate", Integer.class);

        logger.info("gas-rate:");
        logger.info(gas);

        return gas;
    }

    public static class NetInfo {
        /**
         * listening, whether the node is listening.
         */
        public boolean listening;

        /**
         * syncing, whether the node is syncing.
         */
        public boolean syncing;

        /**
         * mining, whether the node is mining.
         */
        public boolean mining;

        /**
         * nodeXPub, the node xpub.
         */
        @SerializedName("node_xpub")
        public String nodeXPub;

        /**
         * peer_count, current count of connected peers.
         */
        @SerializedName("peer_count")
        public int peerCount;

        /**
         * highest_height, current highest block of the connected peers.
         */
        @SerializedName("highest_height")
        public long highestHeight;

        /**
         * network_id, network id.
         */
        @SerializedName("network_id")
        public String networkID;

        /**
         * version, bytom version.
         */
        @SerializedName("version")
        public String version;

        public String toJson() {
            return Utils.serializer.toJson(this);
        }
    }

    /**
     * Call chain-status api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static ChainStatus getChainStatus(Client client) throws BytomException {
        ChainStatus chainStatus = client.request("chain-status", null, ChainStatus.class);

        logger.info("chain-status:");
        logger.info(chainStatus.toJson());

        return chainStatus;
    }

    public static class ChainStatus {
        /**
         * current_height, current block height in the node's blockchain.
         */
        @SerializedName("current_height")
        public long currentHeight;

        /**
         * current_hash, current block hash in the node's blockchain.
         */
        @SerializedName("current_hash")
        public String currentHash;

        /**
         * finalized_height, finalized block height in the node's blockchain.
         */
        @SerializedName("finalized_height")
        public long finalizedHeight;

        /**
         * finalized_hash, finalized block hash in the node's blockchain.
         */
        @SerializedName("finalized_hash")
        public String finalizedHash;

        /**
         * justified_height, justified block height in the node's blockchain.
         */
        @SerializedName("justified_height")
        public long justifiedHeight;

        /**
         * justified_hash, justified block hash in the node's blockchain.
         */
        @SerializedName("justified_hash")
        public String justifiedHash;

        public String toJson() {
            return Utils.serializer.toJson(this);
        }
    }
}
