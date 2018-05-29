package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.TransactionFeed;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TransactionFeedTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static TransactionFeed transactionFeed;

    @Test
    public void testTXFeedCreate() throws Exception {
        String filter = "asset_id='57fab05b689a2b8b6738cffb5cf6cffcd0bf6156a04b7d9ba0173e384fe38c8c' AND amount_lower_limit = 50 AND amount_upper_limit = 100";
        String alias = "test1";
        new TransactionFeed.Builder()
                    .setAlias(alias)
                    .setFilter(filter)
                    .create(client);
    }

    @Test
    public void testTXFeedGet() throws Exception {
        String alias = "test2";
        transactionFeed = TransactionFeed.get(client, alias);

        Assert.assertNotNull(transactionFeed);
    }

    @Test
    public void testTXFeedUpdate() throws Exception {
        String filter = "asset_id='57fab05b689a2b8b6738cffb5cf6cffcd0bf6156a04b7d9ba0173e384fe38c8c' AND amount_lower_limit = 50 AND amount_upper_limit = 100";
        String alias = "test2";

        TransactionFeed.update(client, alias, filter);
    }

    @Test
    public void testTXFeedList() throws Exception {
        List<TransactionFeed> txFeedList = TransactionFeed.list(client);
        Assert.assertNotNull(txFeedList);
    }

    @Test
    public void testTXFeedDelete() throws Exception {
        String alias = "test2";
        TransactionFeed.delete(client, alias);
    }


}
