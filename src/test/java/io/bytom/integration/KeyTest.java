package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Key;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class KeyTest {

    static Client client;
    static Key key;

    @Test
    public void testClientKeyCreate() throws Exception {
        client = TestUtils.generateClient();

        String alias = "KeyTest.testKeyCreate.successli004";
        String password = "123456";

        Key.Builder builder = new Key.Builder().setAlias(alias).setPassword(password);
        key = Key.create(client, builder);

        assertNotNull(key.xpub);
        assertEquals(alias.toLowerCase(), key.alias);
    }

    @Test
    public void testClientKeyList() throws Exception {
        client = TestUtils.generateClient();
        List<Key> keyList = Key.list(client);
    }

    @Test
    public void testClientKeyDelete() throws Exception {
        client = TestUtils.generateClient();
        List<Key> keyList = Key.list(client);
        String xpub = keyList.get(keyList.size()-1).xpub;
        //delete the last Key Object
        Key.delete(client, xpub, "123456");
    }

    @Test
    public void testClientKeyResetPassword() throws BytomException {
        client = TestUtils.generateClient();
        List<Key> keyList = Key.list(client);
        String xpub = keyList.get(keyList.size()-1).xpub;
        Key.resetPassword(client, xpub, "123456", "123456789");
        Key.delete(client, xpub, "123456789");
    }

}
