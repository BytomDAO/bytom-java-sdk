package io.bytom.common;


import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class DeriveXpubTest {

    @Test
    public void testDeriveXpub() {
        String hxprv = "10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b";
        byte[] xpub = DeriveXpub.deriveXpub(Hex.decode(hxprv));
        System.out.println("hxpub: "+Hex.toHexString(xpub));
        //expected: d9c7b41f030a398dada343096040c675be48278046623849977cb0fd01d395a51c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b
        //          d9c7b41f030a398dada343096040c675be48278046623849977cb0fd01d395a51c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b
    }
}