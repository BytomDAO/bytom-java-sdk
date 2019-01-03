package com.github.bytom.integration;

import com.github.bytom.TestUtils;
import com.github.bytom.api.UnspentOutput;
import com.github.bytom.exception.BytomException;
import com.github.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UTXOTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testBalanceList() throws Exception {
        List<UnspentOutput> UTXOList = new UnspentOutput.QueryBuilder().list(client);
        Assert.assertNotNull(UTXOList);
    }

}
