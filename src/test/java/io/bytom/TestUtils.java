package io.bytom;

import io.bytom.common.Configuration;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;

/**
 * TestUtils provides a simplified api for testing.
 */
public class TestUtils {
    public static Client generateClient_old() throws BytomException {

        String coreURL = "http://127.0.0.1:9888";

        return new Client(coreURL);
    }

    public static Client generateClient() throws BytomException {

        String coreURL = Configuration.getValue("bytom.api.url");
        String accessToken = Configuration.getValue("client.access.token");

        if (coreURL == null || coreURL.isEmpty()) {
            coreURL = "http://127.0.0.1:9888/";
        }

        return new Client(coreURL, accessToken);
    }
}
