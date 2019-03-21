package io.bytom.offline.common;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class DerivePrivateKeyTest {
    @Test
    public void testBip44Key() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String rootKey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
        byte[] derivePrivateKey = DerivePrivateKey.bip44derivePrvKey(rootKey, 1, false, 2);
        System.out.println(Hex.toHexString(derivePrivateKey));
        //expected 48c65f40d860723e71b03988a22edc9ad00ae0deae992e79fb3b812edb5c3e43e78065bf46d0e8ad922cdae600fd2c2a6239b8f1f504f8f255460c6fcce023ff
    }

    @Test
    public void testBip32Key() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String rootKey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
        byte[] derivePrivateKey = DerivePrivateKey.bip32derivePrvKey(rootKey, 2, (byte) 0);
        System.out.println(Hex.toHexString(derivePrivateKey));
        //expected 2806f655e862ffc550c2dcaa5057e02e0a1c7c714a4c5a1823bd83f060593e43f5fef4fced766de36cb7ea8ca51cebac7830d54dca1929e669a4a7c3dc7b9dcc
    }

    @Test
    public void testBip32PublicKey() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String rootKey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
        byte[] derivePrivateKey = DerivePrivateKey.bip32derivePrvKey(rootKey, 14, (byte) 0);
        byte[] deriveXpub = DeriveXpub.deriveXpub(derivePrivateKey);
        System.out.println(Hex.toHexString(deriveXpub).substring(0, 64));
        //expected 2806f655e862ffc550c2dcaa5057e02e0a1c7c714a4c5a1823bd83f060593e43f5fef4fced766de36cb7ea8ca51cebac7830d54dca1929e669a4a7c3dc7b9dcc
    }
}
