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

    @Test
    public void testNetInfo() throws Exception {
        netInfo = CoreConfig.getNetInfo(client);
        Assert.assertNotNull(netInfo);
    }



}
