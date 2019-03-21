package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.RawTransaction;
import io.bytom.api.Wallet;
import io.bytom.offline.exception.BytomException;
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
        String rawTxId = "070100010161015f30e052cd50e385951936c08fb5642bd12b727da958960249ddad8c9a77e5371fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8099c4d59901000116001412a47b53b3bbdaf9f3510e1d8c19d69f81ca91426302405a8b5adebd2ab63ba1ac55a7bbadc1fe246806c37732df0442b827fa4e06058137711d9d012becdc9de507a8ad0de0dd50780c0503c0dcff2dc03d7592e31a08206e1efce70e2b29efa348aec7c148edc2beb72edc0d4422a03cfb0f40e6e4cfc602013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80d3bdc6980101160014a9c0ea4abe4d09546197bac3c86f4dd39fde1afb00013cffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8084af5f011600144cc6edb1f4077c740e0201bb3688e6efba4a098c00";
        rawTransaction = RawTransaction.decode(client, rawTxId);
        Assert.assertNotNull(rawTransaction);
    }


}
