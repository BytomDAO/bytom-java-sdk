package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.AccessToken;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Test;

import java.util.List;

public class AccessTokenTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTokenCreate() throws Exception {
        AccessToken accessToken = new AccessToken.Builder().setId("sheng").create(client);
    }


    @Test
    public void testTokenList() throws Exception {
        List<AccessToken> tokenList = AccessToken.list(client);
    }

    @Test
    public void testTokenCheck() throws Exception {
        String secret = "5e37378eb59de6b10e60f2247ebf71c4955002e75e0cd31ede3bf48813a0a799";
        AccessToken.check(client, "sheng", secret);
    }
}
