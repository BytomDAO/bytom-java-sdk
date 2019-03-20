package io.bytom.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class VMUtil {

    private static final byte OP_0 = (byte) 0x00;
    private static final byte OP_1 = (byte) 0x51;
    private static final byte OP_PUSHDATA1 = (byte) 0x4c;
    private static final byte OP_PUSHDATA2 = (byte) 0x4d;
    private static final byte OP_PUSHDATA4 = (byte) 0x43;
    private static final byte OP_TXSIGHASH = (byte) 0xae;
    private static final byte OP_CHECKMULTISIG  = (byte) 0xad;

    public static byte[] p2spMultiSigProgram(byte[][] pubKeys, int nRequired) {
        checkMultiSigParams(nRequired, pubKeys.length);
        ByteArrayOutputStream program = new ByteArrayOutputStream();
        program.write(OP_TXSIGHASH);

        for (byte[] pubKey : pubKeys) {
            try {
                program.write(pushDataBytes(pubKey));
                program.write(pushDataInt64(nRequired));
                program.write(pushDataInt64(pubKeys.length));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        program.write(OP_CHECKMULTISIG);
        return program.toByteArray();
    }

    private static void checkMultiSigParams(int nRequired, int nPubkeys) {
        if (nRequired < 0 || nPubkeys < 0 || nRequired > nPubkeys || (nRequired == 0 && nPubkeys > 0)) {
            throw new IllegalArgumentException();
        }
    }

    private static byte[] pushDataBytes(byte[] data) {
        int len = data.length;
        if (len == 0) {
            return new byte[] {OP_0};
        }
        if (len <= 75) {
            byte[] dest = new byte[1 + len];
            dest[0] = (byte) len;
            System.arraycopy(data, 0, dest, 1, len);
            return dest;
        }
        if (len < 256) {
            byte[] dest = new byte[2 + len];
            dest[0] = OP_PUSHDATA1;
            dest[1] = (byte) len;
            System.arraycopy(data, 0, dest, 2, len);
            return dest;
        }
        if (len < 65536) {
            byte[] dest = new byte[3 + len];
            dest[0] = OP_PUSHDATA2;
            dest[1] = (byte) len;
            dest[2] = (byte) (len >> 8);
            System.arraycopy(data, 0, dest, 3, len);
            return dest;
        }
        byte[] dest = new byte[5 + len];
        dest[0] = OP_PUSHDATA4;
        dest[1] = (byte) len;
        dest[2] = (byte) (len >> 8);
        dest[3] = (byte) (len >> 16);
        dest[4] = (byte) (len >> 24);
        System.arraycopy(data, 0, dest, 5, len);
        return dest;
    }

    private static byte[] pushDataInt64(long n) {
        if (n == 0) {
            return new byte[] {OP_0};
        }
        if (n >= 1 && n <= 16) {
            return new byte[] {(byte) (OP_1 + (byte) n - 1)};
        }
        return pushDataBytes(int64Bytes(n));
    }

    private static byte[] int64Bytes(long n) {
        byte[] bytes = new byte[8];
        int i = 0;
        while (n != 0) {
            bytes[i] = (byte) n;
            n >>= 8;
            i++;
        }
        byte[] res = new byte[i];
        System.arraycopy(bytes, 0, res, 0, i);
        return res;
    }

}

