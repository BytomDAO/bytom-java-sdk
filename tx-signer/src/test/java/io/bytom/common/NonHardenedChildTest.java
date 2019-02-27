package io.bytom.common;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


public class NonHardenedChildTest {

    @Test
    public void testNHChild() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String hxprv = "10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b";
        byte[] xprv = Hex.decode(hxprv);
        //expected: d9c7b41f030a398dada343096040c675be48278046623849977cb0fd01d395a51c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b
        String[] hpaths = {"010400000000000000", "0100000000000000"};
        byte[][] paths = new byte[][]{
                Hex.decode(hpaths[0]),
                Hex.decode(hpaths[1])
        };
        byte[] res = xprv;
        for (int i = 0; i < hpaths.length; i++) {
            byte[] xpub = DeriveXpub.deriveXpub(res);
//            System.out.println("xpub: "+Hex.toHexString(xpub));
            res = NonHardenedChild.NHchild(paths[i], res, xpub);
        }
        System.out.println("res: " + Hex.toHexString(res));
        //expected: e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a6335398a3720b3f96077fa187fdde48fe7dc293984b196f5e292ef8ed78fdbd8ed954
        //          e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a6335398a3720b3f96077fa187fdde48fe7dc293984b196f5e292ef8ed78fdbd8ed954
    }

    @Test
    public void testChild() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String hxprv = "10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b";
        String[] hpaths = {"010400000000000000", "0100000000000000"};
        byte[] childXprv = NonHardenedChild.child(Hex.decode(hxprv), hpaths);
        System.out.println("childXprv: " + Hex.toHexString(childXprv));
        //expected: e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a6335398a3720b3f96077fa187fdde48fe7dc293984b196f5e292ef8ed78fdbd8ed954
        //          e8c0965af60563c4cabcf2e947b1cd955c4f501eb946ffc8c3447e5ec8a6335398a3720b3f96077fa187fdde48fe7dc293984b196f5e292ef8ed78fdbd8ed954
    }
}