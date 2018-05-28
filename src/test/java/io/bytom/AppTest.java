package io.bytom;

import com.squareup.okhttp.*;
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
}

