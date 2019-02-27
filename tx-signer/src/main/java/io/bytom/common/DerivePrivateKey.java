package io.bytom.common;

import io.bytom.util.PathUtil;
import org.bouncycastle.util.encoders.Hex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class DerivePrivateKey {
    //bip44 派生子秘钥
    //accountIndex 默认为1
    public static byte[] derivePrivateKey(String rootPriv, int accountIndex, boolean change, int programIndex) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] xprv = Hex.decode(rootPriv);
        byte[][] paths = PathUtil.getBip44Path(accountIndex, change, programIndex);
        byte[] res = xprv;
        for (int i = 0; i < paths.length; i++) {
            byte[] xpub = DeriveXpub.deriveXpub(res);
            res = NonHardenedChild.NHchild(paths[i], res, xpub);
        }
        return res;
    }

    //BIP32 派生子秘钥 issue 需要用到BIP32派生规则
    public static byte[] derivePrivateKey(String rootPrivateKey, int keyIndex) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] xprv = Hex.decode(rootPrivateKey);
        byte[] res = xprv;
        byte[][] paths = PathUtil.getBip32Path(keyIndex);
        for (int i = 0; i < paths.length; i++) {
            byte[] xpub = DeriveXpub.deriveXpub(res);
            res = NonHardenedChild.NHchild(paths[i], res, xpub);
        }
        return res;
    }
}
