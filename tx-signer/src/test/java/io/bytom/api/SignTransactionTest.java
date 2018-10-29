package io.bytom.api;

import io.bytom.common.Constants;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.util.encoders.Hex;
/**
 * Created by liqiang on 2018/10/24.
 */
public class SignTransactionTest {

    //以下为测试用的区块上的交易utxo，即output中第二个输出
    //新交易接收地址为bm1qdpc5sejdkm22uv23jwd8pg6lyqz2trz4trgxh0，需要找零
    /*{
        "id": "3b36453f7dc03b13523d6431afd7e544f60339daed52ba8fca7ebf88cd5e5939",
            "version": 1,
            "size": 330,
            "time_range": 0,
            "inputs": [
        {
            "type": "spend",
                "asset_id": "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "asset_definition": {},
            "amount": 482000000,
                "control_program": "00148da6ccbc216f9019cf80d23fd2083c80e29fcba2",
                "address": "bm1q3knve0ppd7gpnnuq6glayzpusr3fljazzcq0eh",
                "spent_output_id": "d11967ce15741217c650bc0b9dd7a390aaedd8ea5c645266920a7d19d8be681a",
                "input_id": "caae7c37f6cecce6854e6488cc389379e312acd2f7495337633501fc7f72b5f3"
        }
        ],
        "outputs": [
        {
            "type": "control",
                "id": "3110bc8e7d713c17fb3dc3c9deadbfc419a25c25252c8e613d1fa54cc4d05dbd",
                "position": 0,
                "asset_id": "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "asset_definition": {},
            "amount": 281500000,
                "control_program": "00145d6ba5bf0cfdb2487abd594429cd04c2ba566f9f",
                "address": "bm1qt446t0cvlkeys74at9zznngyc2a9vmulcr2xy6"
        },
        {
            "type": "control",
                "id": "db5afebb5b33aec2c46fcebb20b98fffa8c065a101f4c1789fe5491b34dc1b8f",
                "position": 1,
                "asset_id": "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "asset_definition": {},
            "amount": 200000000,
                "control_program": "00140d074bc86bd388a45f1c8911a41b8f0705d9058b",
                "address": "bm1qp5r5hjrt6wy2ghcu3yg6gxu0quzajpvtsm2gnc"
        }
        ],
        "status_fail": false,
            "mux_id": "0e97230a7347967764fd77c8cfa96b38ec6ff08465300a01900c645dfb694f24"
    }*/

    @Test
    public void testSerializeRawTransaction() {
        //组装计算program、inputID、sourceID(muxID)、txID, json数据中这些字段的值为测试值,需重新计算
        String txJson = "{\n" +
                "        \"tx_id\": \"0434bc790dbb3746c88fd301b9839a0f7c990bb8bdc96881d17bc2fb47525ad8\",\n" +
                "        \"version\": 1,\n" +
                "        \"size\": 236,\n" +
                "        \"time_range\": 1521625823,\n" +
                "        \"inputs\": [\n" +
                "            {\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 200000000,\n" +
                "                \"sourceId\": \"0e97230a7347967764fd77c8cfa96b38ec6ff08465300a01900c645dfb694f24\",\n" +
                "                \"sourcePosition\": 1,\n" +
                "                \"control_program\": \"00140d074bc86bd388a45f1c8911a41b8f0705d9058b\",\n" +
                "                \"address\": \"bm1qp5r5hjrt6wy2ghcu3yg6gxu0quzajpvtsm2gnc\",\n" +
                "                \"spent_output_id\": \"\",\n" +
                "                \"input_id\": \"3b36453f7dc03b13523d6431afd7e544f60339daed52ba8fca7ebf88cd5e5939\",\n" +
                "                \"witness_component\": \n" +
                "                    {\n" +
                "                                \"signatures\": []\n" +
                "                    }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"outputs\": [\n" +
                "            {\n" +
                "                \"id\": \"8a511581e2fb6986abc3be3bbd842434f642db7c56a1fc5c4c7adf93c750e9a4\",\n" +
                "                \"position\": 0,\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 10000000,\n" +
                "                \"control_program\": \"0014687148664db6d4ae3151939a70a35f2004a58c55\",\n" +
                "                \"address\": \"bm1qdpc5sejdkm22uv23jwd8pg6lyqz2trz4trgxh0\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"type\": \"control\",\n" +
                "                \"id\": \"03b6ac529c2d1c7d422a7c063d74893e8ca2003b2b3368c27d0ede2d2f6ea3ba\",\n" +
                "                \"position\": 1,\n" +
                "                \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                \"asset_definition\": {},\n" +
                "                \"amount\": 185000000,\n" +
                "                \"control_program\": \"00140d074bc86bd388a45f1c8911a41b8f0705d9058b\",\n" +
                "                \"address\": \"bm1qp5r5hjrt6wy2ghcu3yg6gxu0quzajpvtsm2gnc\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }";
        //0701dfd5c8d50501015f015d0e97230a7347967764fd77c8cfa96b38ec6ff08465300a01900c645dfb694f24ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8084af5f01011600140d074bc86bd388a45f1c8911a41b8f0705d9058b8301024038c7df4301e070dc888b49309fa9b20266cde899069b099fe0bb562b93518e7876ea0f2676e488dd0a4946802e0ad8447521771d7163449ed222048a1443e801406488f52223be8da187f13348dadff6e829efb807540007eeaa51feefd6cc8be238eec83aff903cfd9de7a712debb5a81c96d2cb8937a4c0384c12dbd17b6e20802013cffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80ade20401160014687148664db6d4ae3151939a70a35f2004a58c5500013cffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0c09b58011600140d074bc86bd388a45f1c8911a41b8f0705d9058b00

        //String result = "07      01      dfd5c8d505 01        01            5f  01    5d  0e97230a7347967764fd77c8cfa96b38ec6ff08465300a01900c645dfb694f24 ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff 8084af5f     01        01        16         00140d074bc86bd388a45f1c8911a41b8f0705d9058b
        //                 serflag version timeTrange input.len assertVerison len type  len sourceID                                                         assetId                                                          amount       sourcePos vmVersion spendLen

        //                 8301       02       40       38c7df4301e070dc888b49309fa9b20266cde899069b099fe0bb562b93518e7876ea0f2676e488dd0a4946802e0ad8447521771d7163449ed222048a1443e801 40       6488f52223be8da187f13348dadff6e829efb807540007eeaa51feefd6cc8be238eec83aff903cfd9de7a712debb5a81c96d2cb8937a4c0384c12dbd17b6e208
        //             witnessLen     argulen  argu1Len argu1data                                                                                                                        argu2Len argu2Data

        //                 02  01      3c       ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff 80ade20401160014687148664db6d4ae3151939a70a35f2004a58c5500 01 3c   ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff c0c09b58011600140d074bc86bd388a45f1c8911a41b8f0705d9058b00
        //           outputlen version serialen assetid

        SignTransaction tx = SignTransaction.fromJson(txJson);
        SignTransactionImpl signImpl = new SignTransactionImpl();

        BigInteger[] keys = new BigInteger[2];
        BigInteger key1 = new BigInteger("58627734430160897710546100937464200251109455274527146106473181212575120553961");//子私钥
        BigInteger key2 = new BigInteger("40205913350867552217212167676397244457827512592372060624640880098442502612286");//子私钥
        keys[0] = key1;
        keys[1] = key2;

        String txSign = signImpl.signTransaction(tx, keys);

        System.out.print(txSign);
    }
}
