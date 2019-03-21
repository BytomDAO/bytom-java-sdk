package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Asset;
import io.bytom.api.Block;
import io.bytom.api.Wallet;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class BlockTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static Block block;
    static Block.BlockHeader blockHeader;
    static Block.BlockDifficulty blockDifficulty;
    static Block.BlockHashRate blockHashRate;

    @Test
    public void testBlockCountGet() throws Exception {
        int count = Block.getBlockCount(client);
        Assert.assertEquals(158, count);
    }

    @Test
    public void testBlockHashGet() throws Exception {
        String blockHash = Block.getBlockHash(client);
        Assert.assertNotNull(blockHash);
    }

    @Test
    public void testBlockGet() throws Exception {
        int height = Block.getBlockCount(client);
        String blockHash = Block.getBlockHash(client);

        block = new Block.QueryBuilder()
                .setBlockHeight(height)
                .setBlockHash(blockHash)
                .getBlock(client);
    }

    @Test
    public void testBlockHeader() throws Exception {
        int height = Block.getBlockCount(client);
        String blockHash = Block.getBlockHash(client);

        blockHeader = new Block.QueryBuilder()
                .setBlockHeight(height)
                .setBlockHash(blockHash)
                .getBlockHeader(client);
    }

    @Test
    public void testBlockDifficulty() throws Exception {
        int height = Block.getBlockCount(client);
        String blockHash = Block.getBlockHash(client);

        blockDifficulty = new Block.QueryBuilder()
                .setBlockHeight(height)
                .setBlockHash(blockHash)
                .getBlockDifficulty(client);
    }

    @Test
    public void testBlockHashRate() throws Exception {
        int height = Block.getBlockCount(client);
        String blockHash = Block.getBlockHash(client);

        blockHashRate = new Block.QueryBuilder()
                .setBlockHeight(height)
                .setBlockHash(blockHash)
                .getHashRate(client);
    }

}
