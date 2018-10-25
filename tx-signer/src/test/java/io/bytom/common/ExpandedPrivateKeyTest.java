package io.bytom.common;


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
}