package io.bytom;

import io.bytom.common.Configuration;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

public class TestUtil {
    public static Logger logger = Logger.getLogger(TestUtil.class);

    public static Client generateClient() throws BytomException {

        String coreURL = Configuration.getValue("bytom.api.url");
        String accessToken = Configuration.getValue("client.access.token");

        if (coreURL == null || coreURL.isEmpty()) {
            coreURL = "http://127.0.0.1:9888";
        }

        if (coreURL.endsWith("/")) {
            //split the last char "/"
            coreURL = coreURL.substring(0, coreURL.length() - 1);
        }

        return new Client(coreURL, accessToken);
    }
}
