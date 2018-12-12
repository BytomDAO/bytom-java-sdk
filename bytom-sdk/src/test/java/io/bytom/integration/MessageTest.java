package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Message;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static Message message;

    @Test
    public void testMessageSign() throws Exception {

        message = new Message.SignBuilder()
                    .setAddress("sm1qz2j8k5anh0d0nu63pcwccxwkn7qu4y2zjwaj5h")
                    .setMessage("test")
                    .setPassword("123456")
                    .sign(client);

        Assert.assertNotNull(message);
    }

    @Test
    public void testMessageVerify() throws Exception {
        String derived_xpub = "6e1efce70e2b29efa348aec7c148edc2beb72edc0d4422a03cfb0f40e6e4cfc6e6050b5863bbe84c44131280dff68614e0308a4d081e8b677d0f7f09fb3390c4";
        String signature = "0d840d5b6a6df028013260e94e871c1443686c446a65db4ee93005c5395c3607feb0ac5bf583a3139c8a3d0afe757448ff49fa17ffd2377831ce5f925c846b0b";

        Boolean verify = new Message.VerifyBuilder()
                    .setDerivedXpub(derived_xpub)
                    .setSignature(signature)
                    .setAddress("sm1qz2j8k5anh0d0nu63pcwccxwkn7qu4y2zjwaj5h")
                    .setMessage("test")
                    .verifyMessage(client);
    }


}
