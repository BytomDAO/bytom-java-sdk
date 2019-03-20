package io.bytom.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Signer {

    public static byte[] ed25519InnerSign(byte[] privateKey, byte[] message) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] digestData = new byte[32 + message.length];
        int digestDataIndex = 0;
        for (int i = 32; i < 64; i++) {
            digestData[digestDataIndex] = privateKey[i];
            digestDataIndex++;
        }
        for (int i = 0; i < message.length; i++) {
            digestData[digestDataIndex] = message[i];
            digestDataIndex++;
        }
        md.update(digestData);
        byte[] messageDigest = md.digest();

        com.google.crypto.tink.subtle.Ed25519.reduce(messageDigest);
        byte[] messageDigestReduced = Arrays.copyOfRange(messageDigest, 0, 32);
        byte[] encodedR = com.google.crypto.tink.subtle.Ed25519.scalarMultWithBaseToBytes(messageDigestReduced);
        byte[] publicKey = DeriveXpub.deriveXpub(privateKey);

        byte[] hramDigestData = new byte[32 + encodedR.length + message.length];
        int hramDigestIndex = 0;
        for (int i = 0; i < encodedR.length; i++) {
            hramDigestData[hramDigestIndex] = encodedR[i];
            hramDigestIndex++;
        }
        for (int i = 0; i < 32; i++) {
            hramDigestData[hramDigestIndex] = publicKey[i];
            hramDigestIndex++;
        }
        for (int i = 0; i < message.length; i++) {
            hramDigestData[hramDigestIndex] = message[i];
            hramDigestIndex++;
        }
        md.reset();
        md.update(hramDigestData);
        byte[] hramDigest = md.digest();
        com.google.crypto.tink.subtle.Ed25519.reduce(hramDigest);
        byte[] hramDigestReduced = Arrays.copyOfRange(hramDigest, 0, 32);

        byte[] sk = Arrays.copyOfRange(privateKey, 0, 32);
        byte[] s = new byte[32];
        com.google.crypto.tink.subtle.Ed25519.mulAdd(s, hramDigestReduced, sk, messageDigestReduced);

        byte[] signature = new byte[64];
        for (int i = 0; i < encodedR.length; i++) {
            signature[i] = encodedR[i];
        }
        int signatureIndex = 32;
        for (int i = 0; i < s.length; i++) {
            signature[signatureIndex] = s[i];
            signatureIndex++;
        }
        return signature;
    }

}
