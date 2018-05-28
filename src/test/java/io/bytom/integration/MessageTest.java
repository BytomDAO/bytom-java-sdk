package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Account;
import io.bytom.api.Address;
import io.bytom.api.Message;
import io.bytom.api.Wallet;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Test;

import java.util.List;

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

        String id = "0E6K7AFF00A02";
        String alias = "test";
        Account.AddressBuilder addressBuilder = new Account.AddressBuilder()
                                                            .setAccountId(id)
                                                            .setAccountAlias(alias);
        List<Address> addressList = addressBuilder.list(client);

        String address = addressList.get(0).address;

        message = new Message.SignBuilder()
                    .setAddress("sm1qz2j8k5anh0d0nu63pcwccxwkn7qu4y2zjwaj5h")
                    .setMessage("this is a test message")
                    .setPassword("123456")
                    .sign(client);
    }


}
