package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Balance;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BalanceTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static Balance balance;

    @Test
    public void testBalanceList() throws Exception {
        List<Balance> balanceList = new Balance.QueryBuilder().list(client);
        Assert.assertNotNull(balanceList);
    }

    @Test
    public void testBalanceByAssetAlias() throws Exception {
        long amount = new Balance.QueryBuilder().listByAssetAlias(client, "BTM");
        Assert.assertNotNull(amount);
    }

    @Test
    public void testBalanceByAccountAlias() throws Exception {
        List<Balance> balanceList = new Balance.QueryBuilder().listByAccountAlias(client, "test");
        Assert.assertNotNull(balanceList);
    }

}
