package io.bytom.api;

import io.bytom.common.ExpandedPrivateKey;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.bouncycastle.util.encoders.Hex;

/**
 * Created by liqiang on 2018/10/24.
 */
public class SignTransactionImpl {

    public byte[] hashFn(byte[] hashedInputHex, byte[] txID) {

        SHA3.Digest256 digest256 = new SHA3.Digest256();

        // data = hashedInputHex + txID
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(hashedInputHex, 0, hashedInputHex.length);
        out.write(txID, 0, txID.length);
        byte[] data = out.toByteArray();

        return digest256.digest(data);
    }

    public SignTransaction buildData(SignTransaction signTransaction) {
        //build program by address

        //build sourceId(muxId), inputId, txId

        return signTransaction;
    }

    public SignTransaction generateSignatures(SignTransaction signTransaction, BigInteger[] keys) {
        SignTransaction.AnnotatedInput input = signTransaction.inputs.get(0);
        input.witnessComponent.signatures = new String[keys.length];
        for (int i=0; i<keys.length; i++) {
            if (null != input) {
                try {
                    byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(signTransaction.txID));
                    byte[] expandedPrv = BigIntegerToBytes(keys[i]);
                    byte[] priKey = ExpandedPrivateKey.ExpandedPrivateKey(expandedPrv);
                    byte[] sig = Signer.Ed25519InnerSign(priKey, message);
                    input.witnessComponent.signatures[i] = Hex.toHexString(sig);
                    System.out.println("sig: " + Hex.toHexString(sig));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("generate signatures failed.");
            }
        }
        return signTransaction;
    }

    public int writeVarint(long value, ByteArrayOutputStream stream) throws IOException {
        byte[] varint = new byte[9];
        int n = putUvarint(varint, value);
        byte[] varintTime = Arrays.copyOf(varint, n);
        stream.write(varintTime);
        return n;
    }

    public int writeVarStr(byte[] buf, ByteArrayOutputStream stream) throws IOException {
        int n = writeVarint(buf.length, stream);
        stream.write(buf);
        return n+(buf.length);
    }

    public int getLengthVarInt(long x) {
        byte[] varint = new byte[9];
        int n = putUvarint(varint, x);
        byte[] varintTime = Arrays.copyOf(varint, n);
        return varintTime.length;
    }

    private static int putUvarint(byte[] buf, long x) {
        int i = 0;
        while (x >= 0x80) {
            buf[i] = (byte)(x | 0x80);
            x >>= 7;
            i++;
        }
        buf[i] = (byte)x;
        return i + 1;
    }

    private byte[] BigIntegerToBytes(BigInteger value) {
        if(value == null) {
            return null;
        } else {
            byte[] data = value.toByteArray();
            if(data.length != 1 && data[0] == 0) {
                byte[] tmp = new byte[data.length - 1];
                System.arraycopy(data, 1, tmp, 0, tmp.length);
                data = tmp;
            }

            return data;
        }
    }
}
