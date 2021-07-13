package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.util.List;

public class Block {

    public String hash;

    public Integer size;

    public Integer version;

    public Integer height;

    @SerializedName("previous_block_hash")
    public String previousBlockHash;

    public Integer timestamp;

    @SerializedName("transaction_merkle_root")
    public String transactionsMerkleRoot;

    public List<Transaction> transactions;


    private static Logger logger = Logger.getLogger(Block.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * Call get-block-count api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static Integer getBlockCount(Client client) throws BytomException {
        Integer blockCount =
                client.requestGet("get-block-count", null, "block_count", Integer.class);

        logger.info("get-block-count:"+blockCount);
        return blockCount;
    }

    /**
     * Call get-block-hash api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static String getBlockHash(Client client) throws BytomException {
        String blockHash =
                client.requestGet("get-block-hash", null, "block_hash", String.class);

        logger.info("get-block-hash:"+blockHash);

        return blockHash;
    }

    public static class QueryBuilder {

        /**
         * block_height, height of block.
         */
        @SerializedName("block_height")
        public int blockHeight;
        /**
         * block_hash, hash of block.
         */
        @SerializedName("block_hash")
        public String blockHash;

        public QueryBuilder setBlockHeight(int blockHeight) {
            this.blockHeight = blockHeight;
            return this;
        }

        public QueryBuilder setBlockHash(String blockHash) {
            this.blockHash = blockHash;
            return this;
        }

        /**
         * Call get-block api
         *
         * @param client
         * @return
         * @throws BytomException
         */
        public Block getBlock(Client client) throws BytomException {

            Block block = client.request("get-block", this, Block.class);

            logger.info("get-block:");
            logger.info(block.toJson());

            return block;
        }

        /**
         * Call get-block-header api
         *
         * @param client
         * @return
         * @throws BytomException
         */
        public BlockHeader getBlockHeader(Client client) throws BytomException {
            BlockHeader blockHeader =
                    client.request("get-block-header", this, BlockHeader.class);

            logger.info("get-block-header:");
            logger.info(blockHeader.toJson());

            return blockHeader;
        }

    }

    public static class BlockHeader {

        @SerializedName("block_header")
        public String blockHeader;

        @SerializedName("reward")
        public Integer reward;

        public String toJson() {
            return Utils.serializer.toJson(this);
        }

    }

}
