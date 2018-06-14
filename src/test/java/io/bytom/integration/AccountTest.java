package io.bytom.integration;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.bytom.TestUtils;
import io.bytom.api.Account;
import io.bytom.api.Receiver;
import io.bytom.http.BatchResponse;
import io.bytom.http.Client;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountTest {

    static Client client;
    static Account account;

    @Test
    public void testAccountCreate() throws Exception {
        client = TestUtils.generateClient();

        String alias = "AccountTest.testAccountCreate.002";
        Integer quorum = 1;
        List<String> root_xpubs = new ArrayList<>();
        root_xpubs.add("c4b25825e92cd8623de4fd6a35952ad0efb2ed215fdb1b40754f0ed12eff7827d147d1e8b003601ba2f78a4a84dcc77e93ed282633f2679048c5d5ac5ea10cb5");


//        Key.Builder builder = new Key.Builder().setAlias(alias).setPassword(password);
//        key = Key.create(client, builder);

        Account.Builder builder = new Account.Builder().setAlias(alias).setQuorum(quorum).setRootXpub(root_xpubs);
        account = Account.create(client, builder);

        assertNotNull(account.id);
        assertEquals(alias.toLowerCase(), account.alias);
    }

    @Test
    public void testAccountCreateBatch() throws Exception {
        client = TestUtils.generateClient();
        String xpub = "c7c5306236d4fcc491615c6cb8bbfb80f60582f44c6757252e84d744632fc0235015459db19e42e262530bffc91ebbc6fef949b0eefda657758b3c6c0e7824a4";
        List<Account.Builder> builders = new ArrayList<>();
        builders.add(new Account.Builder()
                .setAlias("batch005")
                .setQuorum(1)
                .addRootXpub(xpub));
        builders.add(new Account.Builder()
                .setAlias("batch002")
                .setQuorum(1)
                .addRootXpub(xpub));
        builders.add(new Account.Builder()
                .setAlias("batch006")
                .setQuorum(1)
                .addRootXpub(xpub));
        builders.add(new Account.Builder()
                .setAlias("batch004")
                .setQuorum(1)
                .addRootXpub(xpub));
        BatchResponse<Account> accountBatchResponse = Account.createBatch(client, builders);
        System.out.println(accountBatchResponse.toJson());
    }

    @Test
    public void testAccountDeleteBatch() throws Exception {
        client = TestUtils.generateClient();
        List<String> accountInfos = new ArrayList<>();
        accountInfos.add("batch001");
        accountInfos.add("batch002");
        accountInfos.add("batch007");
        accountInfos.add("batch004");
        accountInfos.add("batch005");
        accountInfos.add("batch006");
        accountInfos.add("batch006");
        accountInfos.add("batch003");

        BatchResponse<Boolean> booleanBatchResponse = Account.deleteBatch(client, accountInfos);
        System.out.println(booleanBatchResponse.toJson());
    }

    @Test
    public void testAccountList() throws Exception {
        client = TestUtils.generateClient();
        List<Account> accountList = Account.list(client);
    }

    @Test
    public void testAccountDelete() throws Exception {
        client = TestUtils.generateClient();
        List<Account> accountList = Account.list(client);
        String alias = accountList.get(accountList.size() - 1).alias;
        //delete the last Account Object
        Account.delete(client, alias);
    }

    @Test
    public void testReceiverCreate() throws Exception {
        client = TestUtils.generateClient();
        List<Account> accountList = Account.list(client);
        String alias = accountList.get(accountList.size() - 1).alias;
        String id = accountList.get(accountList.size() - 1).id;

        Account.ReceiverBuilder receiverBuilder = new Account.ReceiverBuilder().setAccountAlias(alias).setAccountId(id);
        Receiver receiver = receiverBuilder.create(client);

        assertNotNull(receiver.address);
        assertNotNull(receiver.controlProgram);
    }

    @Test
    public void testAddressList() throws Exception {
        client = TestUtils.generateClient();
        List<Account> accountList = Account.list(client);
        String alias = accountList.get(accountList.size() - 1).alias;
        String id = accountList.get(accountList.size() - 1).id;

        Account.AddressBuilder addressBuilder = new Account.AddressBuilder().setAccountId(id).setAccountAlias(alias);
        List<Account.Address> addressList = addressBuilder.list(client);

        assertNotNull(addressList);
    }

    @Test
    public void testAddressValidate() throws Exception {
        client = TestUtils.generateClient();

        List<Account> accountList = Account.list(client);
        String alias = accountList.get(accountList.size() - 1).alias;
        String id = accountList.get(accountList.size() - 1).id;

        Account.AddressBuilder addressBuilder = new Account.AddressBuilder().setAccountId(id).setAccountAlias(alias);
        List<Account.Address> addressList = addressBuilder.list(client);

        Account.Address address = addressBuilder.validate(client, addressList.get(0).address);
        assertEquals(true, address.is_local);
    }

}
