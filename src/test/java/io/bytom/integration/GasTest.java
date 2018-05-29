package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Gas;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class GasTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static Gas gas;

    @Test
    public void testWalletBackUp() throws Exception {
        gas = Gas.gasRate(client);
        Assert.assertNotNull(gas);
    }

}
