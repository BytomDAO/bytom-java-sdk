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
        byte[] sig = Signer.Ed25519InnerSign(Hex.decode(expandedXprv), Hex.decode(hashedMessage));
        System.out.println("sig:" + Hex.toHexString(sig));
        //expected: 38b11090e8dd5372018acc24ea4db2c3d82cf01ed5c69a0fae95bff2379c1630f8c8f96937b22685142b4181e6ef5072e7945c101eb81814a20d90cb1d1f0c08
        //          38b11090e8dd5372018acc24ea4db2c3d82cf01ed5c69a0fae95bff2379c1630f8c8f96937b22685142b4181e6ef5072e7945c101eb81814a20d90cb1d1f0c08
    }

}