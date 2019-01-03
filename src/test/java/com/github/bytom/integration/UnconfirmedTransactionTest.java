package com.github.bytom.integration;

import com.github.bytom.TestUtils;
import com.github.bytom.api.UnconfirmedTransaction;
import com.github.bytom.exception.BytomException;
import com.github.bytom.http.Client;
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
