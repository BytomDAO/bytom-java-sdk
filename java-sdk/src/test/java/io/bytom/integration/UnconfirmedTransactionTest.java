package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.UnconfirmedTransaction;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class UnconfirmedTransactionTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static UnconfirmedTransaction unconfirmedTransaction;
    static UnconfirmedTransaction.UTXResponse utxResponse;

    @Test
    public void testUCTXList() throws Exception {
        utxResponse = UnconfirmedTransaction.list(client);
        Assert.assertNotNull(utxResponse);
    }

    @Test
    public void testUCTXGet() throws Exception {
        utxResponse = UnconfirmedTransaction.list(client);
        unconfirmedTransaction = UnconfirmedTransaction.get(client, utxResponse.txIds.get(0));
        Assert.assertNotNull(unconfirmedTransaction);
    }

}
