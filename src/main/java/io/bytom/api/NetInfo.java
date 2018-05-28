package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

public class NetInfo {

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
     * peer_count, current count of connected peers.
     */
    @SerializedName("peer_count")
    public int peerCount;

    /**
     * current_block, current block height in the node's blockchain.
     */
    @SerializedName("current_block")
    public long currentBlock;

    /**
     * highest_block, current highest block of the connected peers.
     */
    @SerializedName("highest_block")
    public long highestBlock;

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

    private static Logger logger = Logger.getLogger(NetInfo.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

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
}
