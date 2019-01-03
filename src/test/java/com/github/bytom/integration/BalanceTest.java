package com.github.bytom.integration;

import com.github.bytom.TestUtils;
import com.github.bytom.api.Balance;
import com.github.bytom.exception.BytomException;
import com.github.bytom.http.Client;
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
        Balance balance = new Balance.QueryBuilder().listByAssetAlias(client, "BTM");
        Assert.assertNotNull(balance);
    }

    @Test
    public void testBalanceByAccountAlias() throws Exception {
        List<Balance> balanceList = new Balance.QueryBuilder().listByAccountAlias(client, "test");
        Assert.assertNotNull(balanceList);
    }

}
