package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.CoreConfig;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class CoreConfigTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static CoreConfig.NetInfo netInfo;

    static Integer gasRate;

    @Test
    public void testNetInfo() throws Exception {
        netInfo = CoreConfig.getNetInfo(client);
        Assert.assertNotNull(netInfo);
    }

    @Test
    public void testChainStatus() throws Exception {
        CoreConfig.ChainStatus chainStatus = CoreConfig.getChainStatus(client);
        Assert.assertNotNull(chainStatus);
    }

    @Test
    public void testGasRate() throws Exception {
        gasRate = CoreConfig.getGasRate(client);
        Assert.assertNotNull(gasRate);
    }

}
