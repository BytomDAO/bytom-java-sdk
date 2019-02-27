package io.bytom.util;

import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.io.ByteArrayOutputStream;

public class SHA3Util {
    public static byte[] hashSha256(byte[] hash) {
        SHA3.Digest256 digest256 = new SHA3.Digest256();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(hash, 0, hash.length);
        byte[] data = out.toByteArray();
        return digest256.digest(data);
    }
}
