package com.github.bytom.integration;

import com.github.bytom.TestUtils;
import com.github.bytom.api.CoreConfig;
import com.github.bytom.exception.BytomException;
import com.github.bytom.http.Client;
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
    public void testGasRate() throws Exception {
        gasRate = CoreConfig.getGasRate(client);
        Assert.assertNotNull(gasRate);
    }



}
