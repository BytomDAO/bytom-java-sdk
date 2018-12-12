package io.bytom.api;

import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;
import org.junit.Test;

public class SignaturesImplTest {

    public static Logger logger = Logger.getLogger(SignaturesImplTest.class);

    @Test
    // 使用build transaction 得到的 template json 来构造 Template 对象参数
    public void testSignUseTemplateJson() throws BytomException {
        String[] privates = new String[1];
        privates[0] = "10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b";

        String raw_tx = "0701dfd5c8d505010161015f0434bc790dbb3746c88fd301b9839a0f7c990bb8bdc96881d17bc2fb47525ad8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80d0dbc3f4020101160014f54622eeb837e39d359f7530b6fbbd7256c9e73d010002013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8c98d2b0f402011600144453a011caf735428d0291d82b186e976e286fc100013afffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff40301160014613908c28df499e3aa04e033100efaa24ca8fd0100";
        Client client = Client.generateClient();
        RawTransaction rawTransaction = RawTransaction.decode(client, raw_tx);

        String json = "{\n" +
                "        \"raw_transaction\": \"0701dfd5c8d505010161015f0434bc790dbb3746c88fd301b9839a0f7c990bb8bdc96881d17bc2fb47525ad8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80d0dbc3f4020101160014f54622eeb837e39d359f7530b6fbbd7256c9e73d010002013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8c98d2b0f402011600144453a011caf735428d0291d82b186e976e286fc100013afffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff40301160014613908c28df499e3aa04e033100efaa24ca8fd0100\",\n" +
                "        \"signing_instructions\": [\n" +
                "            {\n" +
                "                \"position\": 0,\n" +
                "                \"witness_components\": [\n" +
                "                    {\n" +
                "                        \"type\": \"raw_tx_signature\",\n" +
                "                        \"quorum\": 1,\n" +
                "                        \"keys\": [\n" +
                "                            {\n" +
                "                                \"xpub\": \"d9c7b41f030a398dada343096040c675be48278046623849977cb0fd01d395a51c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b\",\n" +
                "                                \"derivation_path\": [\n" +
                "                                    \"010400000000000000\",\n" +
                "                                    \"0100000000000000\"\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"signatures\": null\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"type\": \"data\",\n" +
                "                        \"value\": \"5024b9d7cdfe9b3ece98bc06111e06dd79d425411614bfbb473d07ca44795612\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"allow_additional_actions\": false\n" +
                "    }";
        Template template = Template.fromJson(json);
        Signatures signatures = new SignaturesImpl();
        Template result = signatures.generateSignatures(privates, template, rawTransaction);
        System.out.println(result.toJson());
        // use result's raw_transaction call sign transaction api to build another data but not need password or private key.
    }

    @Test
    // 使用 SDK 来构造 Template 对象参数, 单签
    public void testSignSingleKey() throws BytomException {
        Client client = Client.generateClient();

        String asset_id = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String address = "sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em";
        // build transaction obtain a Template object
        Template template = new Transaction.Builder()
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G0NLBNU00A02")
                                .setAssetId(asset_id)
                                .setAmount(40000000)
                )
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G0NLBNU00A02")
                                .setAssetId(asset_id)
                                .setAmount(300000000)
                )
                .addAction(
                        new Transaction.Action.ControlWithAddress()
                                .setAddress(address)
                                .setAssetId(asset_id)
                                .setAmount(30000000)
                ).build(client);
        logger.info("template: " + template.toJson());
        // use Template object's raw_transaction id to decode raw_transaction obtain a RawTransaction object
        RawTransaction decodedTx = RawTransaction.decode(client, template.rawTransaction);
        logger.info("decodeTx: " + decodedTx.toJson());
        // need a private key array
        String[] privateKeys = new String[]{"10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b"};
        logger.info("private key:" + privateKeys[0]);
        // call offline sign method to obtain a basic offline signed template
        Signatures signatures = new SignaturesImpl();
        Template basicSigned = signatures.generateSignatures(privateKeys, template, decodedTx);
        logger.info("basic signed raw: " + basicSigned.toJson());
        // call sign transaction api to calculate whole raw_transaction id
        // sign password is None or another random String
        Template result = new Transaction.SignerBuilder().sign(client,
                basicSigned, "");
        logger.info("result raw_transaction: " + result.toJson());
        // success to submit transaction
    }

    @Test
    // 使用 SDK 来构造 Template 对象参数, 多签
    public void testSignMultiKeys() throws BytomException {
        Client client = Client.generateClient();

        String asset_id = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String address = "sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em";
        // build transaction obtain a Template object
        Template template = new Transaction.Builder()
                .setTtl(10)
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G1RPP6OG0A06")
                                .setAssetId(asset_id)
                                .setAmount(40000000)
                )
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G1RPP6OG0A06")
                                .setAssetId(asset_id)
                                .setAmount(300000000)
                )
                .addAction(
                        new Transaction.Action.ControlWithAddress()
                                .setAddress(address)
                                .setAssetId(asset_id)
                                .setAmount(30000000)
                ).build(client);
        logger.info("template: " + template.toJson());
        // use Template object's raw_transaction id to decode raw_transaction obtain a RawTransaction object
        RawTransaction decodedTx = RawTransaction.decode(client, template.rawTransaction);
        logger.info("decodeTx: " + decodedTx.toJson());
        // need a private key array
        String[] privateKeys = new String[]{"08bdbd6c22856c5747c930f64d0e5d58ded17c4473910c6c0c3f94e485833a436247976253c8e29e961041ad8dfad9309744255364323163837cbef2483b4f67",
                                            "40c821f736f60805ad59b1fea158762fa6355e258601dfb49dda6f672092ae5adf072d5cab2ceaaa0d68dd3fe7fa04869d95afed8c20069f446a338576901e1b"};
        logger.info("private key 1:" + privateKeys[0]);
        logger.info("private key 2:" + privateKeys[1]);
        // call offline sign method to obtain a basic offline signed template
        Signatures signatures = new SignaturesImpl();
        Template basicSigned = signatures.generateSignatures(privateKeys, template, decodedTx);
        logger.info("basic signed raw: " + basicSigned.toJson());
        // call sign transaction api to calculate whole raw_transaction id
        // sign password is None or another random String
        Template result = new Transaction.SignerBuilder().sign(client,
                basicSigned, "");
        logger.info("result raw_transaction: " + result.toJson());
        // success to submit transaction
    }
    @Test
    // 使用 SDK 来构造 Template 对象参数, 多签, 多输入
    public void testSignMultiKeysMultiInputs() throws BytomException {
        Client client = Client.generateClient();

        String asset_id = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String address = "sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em";
        // build transaction obtain a Template object
        Template template = new Transaction.Builder()
                .setTtl(10)
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G1RPP6OG0A06")
                                .setAssetId(asset_id)
                                .setAmount(40000000)
                )
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G1RPP6OG0A06")
                                .setAssetId(asset_id)
                                .setAmount(300000000)
                )
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G1Q6V1P00A02")
                                .setAssetId(asset_id)
                                .setAmount(40000000)
                )
                .addAction(
                        new Transaction.Action.SpendFromAccount()
                                .setAccountId("0G1Q6V1P00A02")
                                .setAssetId(asset_id)
                                .setAmount(300000000)
                )
                .addAction(
                        new Transaction.Action.ControlWithAddress()
                                .setAddress(address)
                                .setAssetId(asset_id)
                                .setAmount(60000000)
                ).build(client);
        logger.info("template: " + template.toJson());
        // use Template object's raw_transaction id to decode raw_transaction obtain a RawTransaction object
        RawTransaction decodedTx = RawTransaction.decode(client, template.rawTransaction);
        logger.info("decodeTx: " + decodedTx.toJson());
        // need a private key array
        String[] privateKeys = new String[]{"08bdbd6c22856c5747c930f64d0e5d58ded17c4473910c6c0c3f94e485833a436247976253c8e29e961041ad8dfad9309744255364323163837cbef2483b4f67",
                                            "40c821f736f60805ad59b1fea158762fa6355e258601dfb49dda6f672092ae5adf072d5cab2ceaaa0d68dd3fe7fa04869d95afed8c20069f446a338576901e1b",
                                            "08bdbd6c22856c5747c930f64d0e5d58ded17c4473910c6c0c3f94e485833a436247976253c8e29e961041ad8dfad9309744255364323163837cbef2483b4f67"};
        logger.info("private key 1:" + privateKeys[0]);
        logger.info("private key 2:" + privateKeys[1]);
        // call offline sign method to obtain a basic offline signed template
        Signatures signatures = new SignaturesImpl();
        Template basicSigned = signatures.generateSignatures(privateKeys, template, decodedTx);
        logger.info("basic signed raw: " + basicSigned.toJson());
        // call sign transaction api to calculate whole raw_transaction id
        // sign password is None or another random String
        Template result = new Transaction.SignerBuilder().sign(client,
                basicSigned, "");
        logger.info("result raw_transaction: " + result.toJson());
        // success to submit transaction
    }
}