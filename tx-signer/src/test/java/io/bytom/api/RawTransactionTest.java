package io.bytom.api;

import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class RawTransactionTest {

    @Test
    public void testHashFn() throws BytomException {
        String raw_tx = "070100010161015fc8215913a270d3d953ef431626b19a89adf38e2486bb235da732f0afed515299ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8099c4d59901000116001456ac170c7965eeac1cc34928c9f464e3f88c17d8630240b1e99a3590d7db80126b273088937a87ba1e8d2f91021a2fd2c36579f7713926e8c7b46c047a43933b008ff16ecc2eb8ee888b4ca1fe3fdf082824e0b3899b02202fb851c6ed665fcd9ebc259da1461a1e284ac3b27f5e86c84164aa518648222602013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80bbd0ec980101160014c3d320e1dc4fe787e9f13c1464e3ea5aae96a58f00013cffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8084af5f01160014bb93cdb4eca74b068321eeb84ac5d33686281b6500";
        String tx_id = "4c97d7412b04d49acc33762fc748cd0780d8b44086c229c1a6d0f2adfaaac2db";
        String input_id = "9963265eb601df48501cc240e1480780e9ed6e0c8f18fd7dd57954068c5dfd02";
        Client client = Client.generateClient();
        RawTransaction decodedTx = RawTransaction.decode(client, raw_tx);

        byte[] signedHash = decodedTx.hashFn(Hex.decode(input_id), Hex.decode(tx_id));
        System.out.println("signedHash: "+Hex.toHexString(signedHash));
        // expect: 8d2bb534c819464472a94b41cea788e97a2c9dae09a6cb3b7024a44ce5a27835
        //         8d2bb534c819464472a94b41cea788e97a2c9dae09a6cb3b7024a44ce5a27835
    }

    @Test
    public void testFromJson() {
//        String raw_tx = "0701dfd5c8d505010161015f0434bc790dbb3746c88fd301b9839a0f7c990bb8bdc96881d17bc2fb47525ad8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80d0dbc3f4020101160014f54622eeb837e39d359f7530b6fbbd7256c9e73d010002013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8c98d2b0f402011600144453a011caf735428d0291d82b186e976e286fc100013afffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff40301160014613908c28df499e3aa04e033100efaa24ca8fd0100";
        String rawTxJson = "{\n" +
                "        \"tx_id\": \"88367f8df0e3bbb0027b1133b3de36ab779e26af00fc256bde7228c9727d20ef\",\n" +
                "        \"version\": 1,\n" +
                "        \"size\": 236,\n" +
                "        \"time_range\": 1521625823,\n" +
                "        \"inputs\": [\n" +
                "            {\n" +
                "                \"type\": \"spend\",\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 100000000000,\n" +
                "                \"control_program\": \"0014f54622eeb837e39d359f7530b6fbbd7256c9e73d\",\n" +
                "                \"address\": \"sm1q74rz9m4cxl3e6dvlw5ctd7aawftvneeaqsuq3v\",\n" +
                "                \"spent_output_id\": \"34d739d5020d7e92477222b652e8fbe08467f5eb03700ce2ef57752930b05ff1\",\n" +
                "                \"input_id\": \"4ae0a25ea92e8c2749099576a234e7dfacb643597545873549c5000ba83fdd9a\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"outputs\": [\n" +
                "            {\n" +
                "                \"type\": \"control\",\n" +
                "                \"id\": \"8a511581e2fb6986abc3be3bbd842434f642db7c56a1fc5c4c7adf93c750e9a4\",\n" +
                "                \"position\": 0,\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 99959999500,\n" +
                "                \"control_program\": \"00144453a011caf735428d0291d82b186e976e286fc1\",\n" +
                "                \"address\": \"sm1qg3f6qyw27u659rgzj8vzkxrwjahzsm7pyjen5j\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"control\",\n" +
                "                \"id\": \"03b6ac529c2d1c7d422a7c063d74893e8ca2003b2b3368c27d0ede2d2f6ea3ba\",\n" +
                "                \"position\": 1,\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 500,\n" +
                "                \"control_program\": \"0014613908c28df499e3aa04e033100efaa24ca8fd01\",\n" +
                "                \"address\": \"sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"fee\": 40000000\n" +
                "    }";
        RawTransaction decodedTx = RawTransaction.fromJson(rawTxJson);
        System.out.println(decodedTx.toJson());
    }

    @Test
    public void testFromSuccessRespon() {
        String successRespon = "{\n" +
                "    \"status\": \"success\",\n" +
                "    \"data\": {\n" +
                "        \"tx_id\": \"88367f8df0e3bbb0027b1133b3de36ab779e26af00fc256bde7228c9727d20ef\",\n" +
                "        \"version\": 1,\n" +
                "        \"size\": 236,\n" +
                "        \"time_range\": 1521625823,\n" +
                "        \"inputs\": [\n" +
                "            {\n" +
                "                \"type\": \"spend\",\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 100000000000,\n" +
                "                \"control_program\": \"0014f54622eeb837e39d359f7530b6fbbd7256c9e73d\",\n" +
                "                \"address\": \"sm1q74rz9m4cxl3e6dvlw5ctd7aawftvneeaqsuq3v\",\n" +
                "                \"spent_output_id\": \"34d739d5020d7e92477222b652e8fbe08467f5eb03700ce2ef57752930b05ff1\",\n" +
                "                \"input_id\": \"4ae0a25ea92e8c2749099576a234e7dfacb643597545873549c5000ba83fdd9a\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"outputs\": [\n" +
                "            {\n" +
                "                \"type\": \"control\",\n" +
                "                \"id\": \"8a511581e2fb6986abc3be3bbd842434f642db7c56a1fc5c4c7adf93c750e9a4\",\n" +
                "                \"position\": 0,\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 99959999500,\n" +
                "                \"control_program\": \"00144453a011caf735428d0291d82b186e976e286fc1\",\n" +
                "                \"address\": \"sm1qg3f6qyw27u659rgzj8vzkxrwjahzsm7pyjen5j\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"control\",\n" +
                "                \"id\": \"03b6ac529c2d1c7d422a7c063d74893e8ca2003b2b3368c27d0ede2d2f6ea3ba\",\n" +
                "                \"position\": 1,\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 500,\n" +
                "                \"control_program\": \"0014613908c28df499e3aa04e033100efaa24ca8fd01\",\n" +
                "                \"address\": \"sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"fee\": 40000000\n" +
                "    }\n" +
                "}";
        RawTransaction decodeTx = RawTransaction.fromSuccessRespon(successRespon);
        System.out.println(decodeTx.toJson());
    }

}