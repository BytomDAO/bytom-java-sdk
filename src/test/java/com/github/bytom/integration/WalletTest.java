package com.github.bytom.integration;

import com.github.bytom.TestUtils;
import com.github.bytom.api.Wallet;
import com.github.bytom.exception.BytomException;
import com.github.bytom.http.Client;
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
