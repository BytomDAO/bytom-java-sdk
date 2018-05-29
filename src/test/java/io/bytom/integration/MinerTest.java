package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Miner;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class MinerTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static Miner miner;
    static Miner.MinerWork minerWork;

    @Test
    public void testIsMining() throws Exception {
        Boolean isMining = Miner.isMining(client);
        Assert.assertEquals(false, isMining);
    }

    @Test
    public void testSetMining() throws Exception {
        Miner.setMining(client, false);

        Boolean isMining = Miner.isMining(client);
        Assert.assertEquals(false, isMining);
    }

    @Test
    public void testGetWork() throws Exception {
        minerWork = Miner.getWork(client);
        Assert.assertNotNull(minerWork);
    }

}
