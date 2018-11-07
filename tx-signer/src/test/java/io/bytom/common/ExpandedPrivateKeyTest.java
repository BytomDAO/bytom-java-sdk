package io.bytom.common;


import io.bytom.api.SignTransactionImpl;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class ExpandedPrivateKeyTest {

    @Test
    public void testExpandedKey() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String childXprv = "e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a6335398a3720b3f96077fa187fdde48fe7dc293984b196f5e292ef8ed78fdbd8ed954";
        byte[] z = ExpandedPrivateKey.ExpandedPrivateKey(Hex.decode(childXprv));
        System.out.println(Hex.toHexString(z));
        //expect: e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a633535b899d45316cd83e027913d3ff3dc52f6a951a686fd2b750099e1f7c70cb98c3
        //        e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a633535b899d45316cd83e027913d3ff3dc52f6a951a686fd2b750099e1f7c70cb98c3
    }

    @Test
    public void testExpandedPriKey() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String childXprv = "5c0fd0d454c4e06fa9083a14d786d714e74088359f917777d6e0847ddddec01a";
        childXprv = "0466f40dab3655412dcbdc71ae98a93db2fc4c32e6f54b900ce6e1ec0b63b39e";
        String key = "Root";
        byte[] hashPriKey = ExpandedPrivateKey.HMacSha512(Hex.decode(childXprv), key.getBytes());
        //begin operate res[:32]
        byte[] f = new byte[hashPriKey.length / 2];
        System.arraycopy(hashPriKey, 0, f, 0, hashPriKey.length / 2);
        f = SignTransactionImpl.pruneIntermediateScalar(f);
        System.arraycopy(f, 0, hashPriKey, 0, hashPriKey.length / 2);
        //end operate res[:32]
        byte[] hashPubKey = DeriveXpub.deriveXpub(hashPriKey);
        System.out.println(Hex.toHexString(hashPriKey));
        System.out.println(Hex.toHexString(hashPubKey));
    }
}