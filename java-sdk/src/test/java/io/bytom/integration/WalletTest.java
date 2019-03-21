package io.bytom.integration;

import io.bytom.TestUtils;
import io.bytom.api.Key;
import io.bytom.api.Wallet;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WalletTest {

    static Client client;

    static {
        try {
            client = TestUtils.generateClient();
        } catch (BytomException e) {
            e.printStackTrace();
        }
    }

    static Wallet wallet;

    @Test
    public void testWalletBackUp() throws Exception {

        wallet = Wallet.backupWallet(client);
    }

    @Test
    public void testWallerRestore() throws Exception {
        // get wallet object
        wallet = Wallet.backupWallet(client);
        //restore
        Wallet.restoreWallet(client, wallet.accountImage, wallet.assetImage, wallet.keyImages);
    }



}
