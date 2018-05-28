package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.*;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransactionTest {

    static Client client;

    static Key senderKey;
    static Key receiverKey;
    static Account senderAccount;
    static Account receiverAccount;
    static Receiver senderReceiver;
    static Receiver receiverReceiver;
    static Address senderAddress;
    static Address receiverAddress;
    static Asset senderAsset;
    static Asset receiverAsset;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }


    private static Logger logger = Logger.getLogger(TransactionTest.class);

    @Test
    public void testCreateAll() throws Exception {

        testSenderKeyCreate();
        testReceiverKeyCreate();

        testSenderAccountCreate();
        testReceiverAccountCreate();

//        testSenderReceiverCreate();
        testReceiverReceiverCreate();

//        testSenderAssetCreate();
//        testReceiverAssetCreate();

    }

    @Test
    public void testGetAll() throws Exception {

        senderKey = Key.list(client).get(0);
        receiverKey = Key.list(client).get(1);
        logger.info("senderKey:"+senderKey.toJson());
        logger.info("receiverKey:"+receiverKey.toJson());

        senderAccount = Account.list(client).get(0);
        receiverAccount = Account.list(client).get(1);
        logger.info("senderAccount:"+senderAccount.toJson());
        logger.info("receiverAccount:"+receiverAccount.toJson());

        receiverAddress = new Account.AddressBuilder()
                .setAccountAlias(receiverAccount.alias)
                .setAccountId(receiverAccount.id)
                .list(client).get(0);
        logger.info("receiver-address:"+receiverAddress.toJson());

        senderAsset = new Asset.QueryBuilder().list(client).get(0);
        receiverAsset = new Asset.QueryBuilder().list(client).get(1);
        logger.info("senderAsset:"+senderAsset.toJson());
        logger.info("receiverAsset:"+receiverAsset.toJson());
    }

    @Test
    public void testTransactionAll() throws Exception {
        testGetAll();

        logger.info("before transaction:");

        List<Balance> balanceList = new Balance.QueryBuilder().list(client);

        logger.info("transaction:");

        Transaction.Template controlAddress = new Transaction.Builder()
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId(senderAccount.id)
                                .setAssetId(senderAsset.id)
                                .setAmount(300000000)
                )
                .addAction(
                        new Transaction.Action.ControlWithAddress()
                                .setAddress(receiverAddress.address)
                                .setAssetId(senderAsset.id)
                                .setAmount(200000000)
                ).build(client);

        Transaction.Template singer = new Transaction.SignerBuilder().sign(client,
                controlAddress, "123456");

        logger.info("singer:"+singer.toJson());

        Transaction.SubmitResponse txs = Transaction.submit(client, singer);

        logger.info("txs:"+txs.toJson());

        logger.info("after transaction.");

        balanceList = new Balance.QueryBuilder().list(client);

    }

    @Test
    public void testListTransactions() throws Exception {
        String tx_id = "b9a1baa1ae391b502fe5543a9559e6d98dc6cf77f9327daaebcb7222c3bebda6";
        List<Transaction> transactionList =
                new Transaction.QueryBuilder().setTxId(tx_id).list(client);
        for (Transaction tx: transactionList
             ) {
            if (tx.txId.equalsIgnoreCase(tx_id)) {
                logger.info(tx.toJson());
            }
        }
    }

    public void testSenderKeyCreate() throws Exception {
        String alias = "sender-key";
        String password = "123456";

        Key.Builder builder = new Key.Builder().setAlias(alias).setPassword(password);
        senderKey = Key.create(client, builder);

        logger.info("create-sender-key:"+senderKey.toJson());
    }

    public void testReceiverKeyCreate() throws Exception {
        String alias = "receiver-key";
        String password = "123456";

        Key.Builder builder = new Key.Builder().setAlias(alias).setPassword(password);
        receiverKey = Key.create(client, builder);

        logger.info("create-receiver-key:"+receiverKey.toJson());
    }

    public void testSenderAccountCreate() throws Exception {
        String alias = "sender-account";
        Integer quorum = 1;
        List<String> root_xpubs = new ArrayList<String>();
        root_xpubs.add(senderKey.xpub);

        Account.Builder builder = new Account.Builder().setAlias(alias).setQuorum(quorum).setRootXpub(root_xpubs);

        logger.info(builder.toString());

        senderAccount = Account.create(client, builder);

        logger.info("create-sender-account:"+senderAccount.toJson());
    }

    public void testReceiverAccountCreate() throws Exception {
        String alias = "receiver-account";
        Integer quorum = 1;
        List<String> root_xpubs = new ArrayList<>();
        root_xpubs.add(receiverKey.xpub);

        Account.Builder builder = new Account.Builder().setAlias(alias).setQuorum(quorum).setRootXpub(root_xpubs);
        receiverAccount = Account.create(client, builder);

        logger.info("create-receiver-account:"+receiverAccount.toJson());
    }

    public void testSenderReceiverCreate() throws Exception {
        String alias = senderAccount.alias;
        String id = senderAccount.id;

        Account.ReceiverBuilder receiverBuilder = new Account.ReceiverBuilder().setAccountAlias(alias).setAccountId(id);
        senderReceiver = receiverBuilder.create(client);

        logger.info("create-receiver:"+senderReceiver.toJson());
        logger.info("receiver-address:"+senderReceiver.address);
    }

    public void testReceiverReceiverCreate() throws Exception {
        String alias = receiverAccount.alias;
        String id = receiverAccount.id;

        Account.ReceiverBuilder receiverBuilder = new Account.ReceiverBuilder().setAccountAlias(alias).setAccountId(id);
        receiverReceiver = receiverBuilder.create(client);

        logger.info("create-receiver:"+receiverReceiver.toJson());
        logger.info("receiver-address:"+receiverReceiver.address);
    }

    public void testSenderAssetCreate() throws Exception {
        String alias = "sender-asset";

        List<String> xpubs = senderAccount.xpubs;

        Asset.Builder builder = new Asset.Builder()
                .setAlias(alias)
                .setQuorum(1)
                .setRootXpubs(xpubs);
        senderAsset = builder.create(client);

        logger.info("create-sender-asset:"+senderAsset.toJson());
    }

    public void testReceiverAssetCreate() throws Exception {
        String alias = "receiver-asset";

        List<String> xpubs = receiverAccount.xpubs;

        Asset.Builder builder = new Asset.Builder()
                .setAlias(alias)
                .setQuorum(1)
                .setRootXpubs(xpubs);
        receiverAsset = builder.create(client);

        logger.info("create-receiver-asset:"+receiverAsset.toJson());
    }

    /**
     * build + sign + submit transaction
     *
     * @throws BytomException
     */
    public void testBasicTransaction() throws BytomException {
        logger.info("before transaction:");

        List<Balance> balanceList = new Balance.QueryBuilder().list(client);

        logger.info(balanceList.get(0).toJson());
        logger.info(balanceList.get(1).toJson());

        Transaction.Template controlAddress = new Transaction.Builder()
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId(senderAccount.id)
                                .setAssetId(senderAsset.id)
                                .setAmount(300000000)
                )
                .addAction(
                        new Transaction.Action.ControlWithAddress()
                                .setAddress(receiverAddress.address)
                                .setAssetId(receiverAsset.id)
                                .setAmount(200000000)
                ).build(client);

        logger.info("after transaction.");

        balanceList = new Balance.QueryBuilder().list(client);

        logger.info(balanceList.get(0).toJson());
        logger.info(balanceList.get(1).toJson());

    }

}
