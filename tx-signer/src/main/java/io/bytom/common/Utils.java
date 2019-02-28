package io.bytom.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.bytom.types.ExpandedKeys;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;

public class Utils {

    public static String rfc3339DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final Gson serializer = new GsonBuilder().setDateFormat(rfc3339DateFormat).create();

    public static int writeVarint(long value, ByteArrayOutputStream stream) throws IOException {
        byte[] varint = new byte[9];
        int n = putUvarint(varint, value);
        byte[] varintTime = Arrays.copyOf(varint, n);
        stream.write(varintTime);
        return n;
    }


    public static int writeVarStr(byte[] buf, ByteArrayOutputStream stream) throws IOException {
        int n = writeVarint(buf.length, stream);
        stream.write(buf);

        return n + (buf.length);
    }

    public static int getLengthVarInt(long x) {
        byte[] varint = new byte[9];
        int n = putUvarint(varint, x);
        byte[] varintTime = Arrays.copyOf(varint, n);
        return varintTime.length;
    }

    private static int putUvarint(byte[] buf, long x) {
        int i = 0;
        while (x >= 0x80) {
            buf[i] = (byte) (x | 0x80);
            x >>= 7;
            i++;
        }
        buf[i] = (byte) x;
        return i + 1;
    }

    public static byte[] BigIntegerToBytes(BigInteger value) {
        if (value == null) {
            return null;
        } else {
            byte[] data = value.toByteArray();
            if (data.length != 1 && data[0] == 0) {
                byte[] tmp = new byte[data.length - 1];
                System.arraycopy(data, 1, tmp, 0, tmp.length);
                data = tmp;
            }

            return data;
        }
    }

    public static byte[] pruneIntermediateScalar(byte[] f) {
        f[0] &= 248;
        f[31] &= 31; // clear top 3 bits
        f[31] |= 64; // set second highest bit
        return f;
    }

    public static ExpandedKeys expandedPriKey(String priKey, String key) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] hashPriKey = ExpandedPrivateKey.HMacSha512(Hex.decode(priKey), key.getBytes());
        //begin operate res[:32]
        byte[] f = new byte[hashPriKey.length / 2];
        System.arraycopy(hashPriKey, 0, f, 0, hashPriKey.length / 2);
        f = pruneIntermediateScalar(f);
        System.arraycopy(f, 0, hashPriKey, 0, hashPriKey.length / 2);
        //end operate res[:32]
        byte[] hashPubKey = DeriveXpub.deriveXpub(hashPriKey);
        ExpandedKeys expandedKeys = new ExpandedKeys();
        expandedKeys.setPriKey(Hex.toHexString(hashPriKey));
        expandedKeys.setPubKey(Hex.toHexString(hashPubKey));
        return expandedKeys;
    }

    public static String pushDataInt(int n) {
        if (n==0){
            return "00";
        }else if (n>=1&&n<=15){
            return "5"+Integer.toString(n,16);
        }else if(n==16){
            return "60";
        }
        return null;

    }
}
