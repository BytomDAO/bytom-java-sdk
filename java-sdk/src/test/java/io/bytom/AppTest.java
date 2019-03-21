package io.bytom;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import io.bytom.api.Block;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();


    @Test
    public void testListAccounts() throws IOException {

        String postBody = "{}";

        Request request = new Request.Builder()
                .url("http://127.0.0.1:9888/list-accounts")
                .post(RequestBody.create(JSON, postBody))
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }


    @Test
    public void testCreateAccount() throws IOException {

        String postBody = "{\n" +
                "        \"root_xpubs\": [\n" +
                "                \"012454d25928d52d42e3ee1f2bebe0916974d958f9ec08c9a028043ffe3dd95630c1b788c947b8c07ede2a4b5e3e3bbe0e305bab4526a7bc67b21e1d051e74ef\"\n" +
                "        ], \n" +
                "        \"quorum\": 1, \n" +
                "        \"alias\": \"sheng\"\n" +
                "}";

        Request request = new Request.Builder()
                .url("http://127.0.0.1:9888/create-account")
                .post(RequestBody.create(JSON, postBody))
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }

    @Test
    public void testBlockGet() throws Exception {
        String postBody = "{\n" +
                "        \"hash\": \"37eabf4d321f43b930f52b3af2e53b2ad2dbb234949e778213a4a54e4be04ea8\",\n" +
                "        \"size\": 388,\n" +
                "        \"version\": 1,\n" +
                "        \"height\": 158,\n" +
                "        \"previous_block_hash\": \"2cfa73d6ed14a7fcb3a8ccc2d4c45b0fd4c7754ec6fafaa0964045c23e2131cd\",\n" +
                "        \"timestamp\": 1527241075,\n" +
                "        \"nonce\": 0,\n" +
                "        \"bits\": 2305843009214532812,\n" +
                "        \"difficulty\": \"5789598940468732338727519302629705571043137272394850465663635070376277442560\",\n" +
                "        \"transaction_merkle_root\": \"62755c4770374e696c4fecd94e022ccf6f2adc00f409ac694a5cb92fe02353eb\",\n" +
                "        \"transaction_status_hash\": \"c9c377e5192668bc0a367e4a4764f11e7c725ecced1d7b6a492974fab1b6d5bc\",\n" +
                "        \"transactions\": [\n" +
                "            {\n" +
                "                \"id\": \"5c38b8107d53cbfebd19adbd11f2839d914099a74e491f1cf75f8b18320c84e2\",\n" +
                "                \"version\": 1,\n" +
                "                \"size\": 77,\n" +
                "                \"time_range\": 0,\n" +
                "                \"inputs\": [\n" +
                "                    {\n" +
                "                        \"type\": \"coinbase\",\n" +
                "                        \"asset_id\": \"0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                        \"asset_definition\": {},\n" +
                "                        \"amount\": 0,\n" +
                "                        \"arbitrary\": \"c29e\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"outputs\": [\n" +
                "                    {\n" +
                "                        \"type\": \"control\",\n" +
                "                        \"id\": \"9acda4f2bd83c7114a8837b596860df773a7e61b1d6b0a774addda1473361ad4\",\n" +
                "                        \"position\": 0,\n" +
                "                        \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "                        \"asset_definition\": {},\n" +
                "                        \"amount\": 41250000000,\n" +
                "                        \"control_program\": \"001412a47b53b3bbdaf9f3510e1d8c19d69f81ca9142\",\n" +
                "                        \"address\": \"sm1qz2j8k5anh0d0nu63pcwccxwkn7qu4y2zjwaj5h\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"status_fail\": false\n" +
                "            }\n" +
                "        ]\n" +
                "    }";
        Gson gson = new Gson();
        Block block = gson.fromJson(postBody, Block.class);
    }
}

