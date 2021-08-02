package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.AccessToken;
import io.bytom.exception.BytomException;
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
        String secret = "dad13ed9f34ffd063cd34abb2320fbbb5c768db4588e319f4444109944b65218";
        AccessToken.check(client, "sheng", secret);
    }

    @Test
    public void testDeleteCheck() throws Exception {
        String secret = "dad13ed9f34ffd063cd34abb2320fbbb5c768db4588e319f4444109944b65218";
        AccessToken.delete(client, "sheng");
    }
}
