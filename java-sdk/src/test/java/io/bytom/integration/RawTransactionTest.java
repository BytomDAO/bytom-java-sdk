package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.RawTransaction;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class RawTransactionTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static RawTransaction rawTransaction;


    @Test
    public void testRawTxDecode() throws Exception {
        String rawTxId = "070100010161015f4894fb81ee71b343a1990dae5655cb99d74abf99d16cf06aad6a3095865ccf9bfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffed8fcf5050001160014e933e5a0545c63ff2c28eaedb47c831cac3b16cf006302408b50ffb295d4571e320d3d28982e42ba61d939210be8b5dc4e0d5cd2f45f2b79fb06d98b6f6024208fe9194878e6824c70811f44a1f4fc911f37fd8604de05062006ec50c0c335cd6c4d06b08d12f89ca5af3bd099b159544d4c8818e7c9d6b8af0201003effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffaeadaea30501160014e933e5a0545c63ff2c28eaedb47c831cac3b16cf000001003dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd0d1894901160014d69556c7ae252fbcc38fd34a34a3ba21dd0537c10000";
        rawTransaction = RawTransaction.decode(client, rawTxId);
        Assert.assertNotNull(rawTransaction);
    }


}
