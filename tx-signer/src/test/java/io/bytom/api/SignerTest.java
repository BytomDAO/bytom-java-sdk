package io.bytom.api;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class SignerTest {

    @Test
    public void testEd25519InnerSign() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String rootXprv = "10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b";
        String childXprv = "e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a6335398a3720b3f96077fa187fdde48fe7dc293984b196f5e292ef8ed78fdbd8ed954";
        String expandedXprv = "20849f14bbe212d1b8917da2e1eda9afc4c21e9dd0a47f1e169a3326d45ae443236f54b987369e86ed78eb2b0a2def89a69ec69ca1059e2efe045796dc583d91";
        String hashedMessage = "99ab9ebdba106466371467b036d56a0e54ad2a6035e365a6103ba97ab553fd52";
        byte[] sig = Signer.ed25519InnerSign(Hex.decode(expandedXprv), Hex.decode(hashedMessage));
        System.out.println("sig:" + Hex.toHexString(sig));
        //expected: e628e980c690d9ef4ca8a2edee1654a6b401edc4f1af7bda3ffd97fe412522c3bab671dd4e51d0aeeb64f8d761fbdb03e296ab0c1dcbed4eafa504f412a98100
    }
}