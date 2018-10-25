package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Account;
import io.bytom.api.Asset;
import io.bytom.http.Client;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AssetTest {

    static Client client;
    static Account account;
    static Asset asset;

    @Test
    public void testAssetCreate() throws Exception {
        client = TestUtils.generateClient();

        List<Account> accountList = Account.list(client);
        String alias = "GOLD";

        List<String> xpubs = accountList.get(0).xpubs;

        Asset.Builder builder = new Asset.Builder()
                                    .setAlias(alias)
                                    .setQuorum(1)
                                    .setRootXpubs(xpubs);
        asset = builder.create(client);
        assertNotNull(asset);
    }

    @Test
    public void testAssetGet() throws Exception {
        client = TestUtils.generateClient();
        Asset.QueryBuilder queryBuilder = new Asset.QueryBuilder();
        String id = queryBuilder.list(client).get(1).id;
        queryBuilder.setId(id);
        Asset asset = queryBuilder.get(client);
    }

    @Test
    public void testAssetList() throws Exception {
        client = TestUtils.generateClient();
        Asset.QueryBuilder queryBuilder = new Asset.QueryBuilder();
        List<Asset> assetList = queryBuilder.list(client);
        assertEquals(2, assetList.size());
    }

    @Test
    public void testUpdateAssetAlias() throws Exception {
        client = TestUtils.generateClient();

        Asset.QueryBuilder queryBuilder = new Asset.QueryBuilder();
        String id = queryBuilder.list(client).get(1).id;

        String alias = "HELLOWORLD";


        Asset.AliasUpdateBuilder aliasUpdateBuilder =
                new Asset.AliasUpdateBuilder()
                        .setAlias(alias)
                        .setAssetId(id);
        aliasUpdateBuilder.update(client);
    }

}
