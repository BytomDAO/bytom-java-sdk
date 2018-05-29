package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.NetInfo;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class NetInfoTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static NetInfo netInfo;


    @Test
    public void testNetInfo() throws Exception {
        netInfo = NetInfo.getNetInfo(client);
        Assert.assertNotNull(netInfo);
    }



}
