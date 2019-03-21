package io.bytom.offline.common;

import io.bytom.offline.util.PathUtil;
import org.bouncycastle.util.encoders.Hex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class DerivePrivateKey {
    //bip44 派生子秘钥
    //accountIndex 默认为1
    public static byte[] bip44derivePrvKey(String rootPriv, int accountIndex, boolean change, int programIndex) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[][] paths = PathUtil.getBip44Path(accountIndex, change, programIndex);
        byte[] res = Hex.decode(rootPriv);
        for (int i = 0; i < paths.length; i++) {
            byte[] xpub = DeriveXpub.deriveXpub(res);
            res = NonHardenedChild.nhChild(paths[i], res, xpub);
        }
        return res;
    }

    //BIP32 派生子秘钥 issue 需要用到BIP32派生规则
    public static byte[] bip32derivePrvKey(String rootPrivateKey, int accountIndex, byte keySpace, long ...programIndex) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] res = Hex.decode(rootPrivateKey);
        byte[][] paths = PathUtil.getBip32Path(keySpace, accountIndex, programIndex);
        for (int i = 0; i < paths.length; i++) {
            byte[] xpub = DeriveXpub.deriveXpub(res);
            res = NonHardenedChild.nhChild(paths[i], res, xpub);
        }
        return res;
    }
}
