package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.*;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransactionTest {

    static Client client;
    static Client otherCoreClient;

    static Key senderKey;
    static Key receiverKey;
    static Account senderAccount;
    static Account receiverAccount;
    static Account.Address receiverAddress;
    static Asset senderAsset;
    static Asset receiverAsset;

    static Transaction.Feed transactionFeed;

    static {
        try {
            client = TestUtils.generateClient();
            otherCoreClient = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }


    private static Logger logger = Logger.getLogger(TransactionTest.class);


    @Test
    public void testGetAll() throws Exception {

        senderKey = Key.list(client).get(0);
        receiverKey = Key.list(client).get(1);
        logger.info("senderKey:" + senderKey.toJson());
        logger.info("receiverKey:" + receiverKey.toJson());

        senderAccount = Account.list(client).get(0);
        receiverAccount = Account.list(client).get(1);
        logger.info("senderAccount:" + senderAccount.toJson());
        logger.info("receiverAccount:" + receiverAccount.toJson());

        receiverAddress = new Account.AddressBuilder()
                .setAccountAlias(receiverAccount.alias)
                .setAccountId(receiverAccount.id)
                .list(client).get(0);
        logger.info("receiver-address:" + receiverAddress.toJson());

        senderAsset = new Asset.QueryBuilder().list(client).get(0);
        receiverAsset = new Asset.QueryBuilder().list(client).get(1);
        logger.info("senderAsset:" + senderAsset.toJson());
        logger.info("receiverAsset:" + receiverAsset.toJson());
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

        logger.info("rawTransaction:" + singer.rawTransaction);

        logger.info("singer:" + singer.toJson());

        Transaction.SubmitResponse txs = Transaction.submit(client, singer);

        logger.info("txs:" + txs.toJson());

        logger.info("after transaction.");

        balanceList = new Balance.QueryBuilder().list(client);

    }

    //Asset issuance
    //Issue 1000 units of gold to Alice.
    @Test
    public void testAssetIssue() throws BytomException {
        Transaction.Template issuance = new Transaction.Builder()
                .addAction(new Transaction.Action.Issue()
                        .setAssetAlias("gold")
                        .setAmount(1000)
                ).addAction(
                        new Transaction.Action.ControlWithAccount()
                                .setAccountAlias("alice")
                                .setAssetAlias("golad")
                                .setAmount(1000)
                ).build(client);

        Transaction.Template signedIssuance = new Transaction.SignerBuilder().sign(client,
                issuance, "123456");

        Transaction.submit(client, signedIssuance);
    }

    //Between two Chain Cores
    @Test
    public void testAssetIssueBetween() throws BytomException {
        Receiver bobIssuanceReceiver = new Account.ReceiverBuilder()
                .setAccountAlias("bob")
                .create(otherCoreClient);
        String bobIssuanceReceiverSerialized = bobIssuanceReceiver.toJson();


        Transaction.Template issuanceToReceiver = new Transaction.Builder()
                .addAction(new Transaction.Action.Issue()
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.ControlWithReceiver()
                        .setReceiver(Receiver.fromJson(bobIssuanceReceiverSerialized))
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).build(client);

        Transaction.Template signedIssuanceToReceiver = new Transaction.SignerBuilder().sign(client,
                issuanceToReceiver, "123456");

        Transaction.submit(client, signedIssuanceToReceiver);
    }

    //Simple payment
    //Alice pays 10 units of gold to Bob.
    @Test
    public void testSimplePayment() throws BytomException {
        Transaction.Template payment = new Transaction.Builder()
                .addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.ControlWithAccount()
                        .setAccountAlias("bob")
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).build(client);

        Transaction.Template signedPayment = new Transaction.SignerBuilder().sign(client,
                payment, "123456");

        Transaction.submit(client, signedPayment);
    }

    //Between two Chain Cores
    @Test
    public void testSimplePaymentBetween() throws BytomException {
        Receiver bobPaymentReceiver = new Account.ReceiverBuilder()
                .setAccountAlias("bob")
                .create(otherCoreClient);
        String bobPaymentReceiverSerialized = bobPaymentReceiver.toJson();


        Transaction.Template paymentToReceiver = new Transaction.Builder()
                .addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.ControlWithReceiver()
                        .setReceiver(Receiver.fromJson(bobPaymentReceiverSerialized))
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).build(client);

        Transaction.Template signedPaymentToReceiver = new Transaction.SignerBuilder().sign(client,
                paymentToReceiver, "123456");

        Transaction.submit(client, signedPaymentToReceiver);
    }

    //Multi-asset payment
    //Alice pays 10 units of gold and 20 units of silver to Bob.
    @Test
    public void testMultiAssetPayment() throws BytomException {
        Transaction.Template multiAssetPayment = new Transaction.Builder()
                .addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("silver")
                        .setAmount(20)
                ).addAction(new Transaction.Action.ControlWithAccount()
                        .setAccountAlias("bob")
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.ControlWithAccount()
                        .setAccountAlias("bob")
                        .setAssetAlias("silver")
                        .setAmount(20)
                ).build(client);

        Transaction.Template signedMultiAssetPayment = new Transaction.SignerBuilder().sign(client,
                multiAssetPayment, "123456");

        Transaction.submit(client, signedMultiAssetPayment);
    }

    //Between two Chain Cores
    @Test
    public void testMultiAssetPaymentBetween() throws BytomException {
        Receiver bobGoldReceiver = new Account.ReceiverBuilder()
                .setAccountAlias("bob")
                .create(otherCoreClient);
        String bobGoldReceiverSerialized = bobGoldReceiver.toJson();

        Receiver bobSilverReceiver = new Account.ReceiverBuilder()
                .setAccountAlias("bob")
                .create(otherCoreClient);
        String bobSilverReceiverSerialized = bobSilverReceiver.toJson();


        Transaction.Template multiAssetToReceiver = new Transaction.Builder()
                .addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("silver")
                        .setAmount(20)
                ).addAction(new Transaction.Action.ControlWithReceiver()
                        .setReceiver(Receiver.fromJson(bobGoldReceiverSerialized))
                        .setAssetAlias("gold")
                        .setAmount(10)
                ).addAction(new Transaction.Action.ControlWithReceiver()
                        .setReceiver(Receiver.fromJson(bobSilverReceiverSerialized))
                        .setAssetAlias("silver")
                        .setAmount(20)
                ).build(client);

        Transaction.Template signedMultiAssetToReceiver = new Transaction.SignerBuilder().sign(client,
                multiAssetToReceiver, "123456");;

        Transaction.submit(client, signedMultiAssetToReceiver);
    }

    //Asset retirement
    //Alice retires 50 units of gold from her account.
    @Test
    public void testRetirement() throws BytomException {
        Transaction.Template retirement = new Transaction.Builder()
                .addAction(new Transaction.Action.SpendFromAccount()
                        .setAccountAlias("alice")
                        .setAssetAlias("gold")
                        .setAmount(50)
                ).addAction(new Transaction.Action.Retire()
                        .setAssetAlias("gold")
                        .setAmount(50)
                ).build(client);

        Transaction.Template signedRetirement = new Transaction.SignerBuilder().sign(client,
                retirement, "123456");

        Transaction.submit(client, signedRetirement);
    }

    //TransactionFeed
    @Test
    public void testTXFeedCreate() throws Exception {
        String filter = "asset_id='57fab05b689a2b8b6738cffb5cf6cffcd0bf6156a04b7d9ba0173e384fe38c8c' AND amount_lower_limit = 50 AND amount_upper_limit = 100";
        String alias = "test1";
        new Transaction.Feed.Builder()
                .setAlias(alias)
                .setFilter(filter)
                .create(client);
    }

    @Test
    public void testTXFeedGet() throws Exception {
        String alias = "test2";
        transactionFeed = Transaction.Feed.getByAlias(client, alias);

        Assert.assertNotNull(transactionFeed);
    }

    @Test
    public void testTXFeedUpdate() throws Exception {
        String filter = "asset_id='57fab05b689a2b8b6738cffb5cf6cffcd0bf6156a04b7d9ba0173e384fe38c8c' AND amount_lower_limit = 50 AND amount_upper_limit = 100";
        String alias = "test2";

        Transaction.Feed.update(client, alias, filter);
    }

    @Test
    public void testTXFeedList() throws Exception {
        List<Transaction.Feed> txFeedList = Transaction.Feed.list(client);
        Assert.assertNotNull(txFeedList);
    }

    @Test
    public void testTXFeedDelete() throws Exception {
        String alias = "test2";
        Transaction.Feed.deleteByAlias(client, alias);
    }

}
