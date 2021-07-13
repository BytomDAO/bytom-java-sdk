package io.bytom.offline.util;

import io.bytom.offline.types.AssetDefinition;
import io.bytom.offline.types.Hash;
import io.bytom.offline.types.Program;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;

public class AssetIdUtil {
    public static String computeAssetID(String rawAssetDefinition,String controlProgram){
        byte[] hashBytes = SHA3Util.hashSha256(Hex.decode(rawAssetDefinition));
        Hash assetDefHash = new Hash(hashBytes);
        Program program = new Program(1,Hex.decode(controlProgram));
        AssetDefinition assetDefinition = new AssetDefinition(assetDefHash, program);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assetDefinition.writeForHash(out);
        return Hex.toHexString(SHA3Util.hashSha256(out.toByteArray()));
    }
}
