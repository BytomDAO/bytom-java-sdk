package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.UnspentOutput;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
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
