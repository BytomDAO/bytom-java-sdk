package io.bytom.api;

import io.bytom.common.Constants;
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

    public String signTransaction(SignTransaction tx, BigInteger[] keys) {

        String txSign = null;
        //组装计算program、inputID、sourceID(muxID)、txID, json数据中这些字段的值为测试值,需重新计算
        buildData(tx);

        //签名得到signatures
        generateSignatures(tx,keys);

        //开始序列化
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(7);
            // version
            if (null != tx.version)
                writeVarint(tx.version, stream);
            if (null != tx.timeRange)
                writeVarint(tx.timeRange, stream);
            //inputs
            if (null != tx.inputs && tx.inputs.size() > 0) {
                writeVarint(tx.inputs.size(), stream);
                for (SignTransaction.AnnotatedInput input:tx.inputs) {
                    //assertVersion
                    writeVarint(tx.version, stream); //AssetVersion是否默认为1

                    //inputCommitment
                    ByteArrayOutputStream inputCommitStream = new ByteArrayOutputStream();
                    //spend type flag
                    writeVarint(Constants.INPUT_TYPE_SPEND, inputCommitStream);
                    //spendCommitment
                    ByteArrayOutputStream spendCommitSteam = new ByteArrayOutputStream();
                    spendCommitSteam.write(Hex.decode(input.sourceId)); //计算muxID
                    spendCommitSteam.write(Hex.decode(input.assetId));
                    writeVarint(input.amount, spendCommitSteam);
                    //sourcePosition
                    writeVarint(input.sourcePosition, spendCommitSteam); //db中获取position
                    //vmVersion
                    writeVarint(1, spendCommitSteam); //db中获取vm_version
                    //controlProgram
                    writeVarStr(Hex.decode(input.controlProgram), spendCommitSteam);

                    byte[] dataSpendCommit = spendCommitSteam.toByteArray();
                    writeVarint(dataSpendCommit.length, inputCommitStream);
                    inputCommitStream.write(dataSpendCommit);
                    byte[] dataInputCommit = inputCommitStream.toByteArray();
                    //inputCommit的length
                    writeVarint(dataInputCommit.length, stream);
                    stream.write(dataInputCommit);


                    //inputWitness
                    ByteArrayOutputStream witnessStream = new ByteArrayOutputStream();
                    //arguments
                    int lenSigs = input.witnessComponent.signatures.length;
                    //arguments的length: 应是qorum和sigs
                    writeVarint(lenSigs, witnessStream);
                    for (int i =0; i<lenSigs; i++) {
                        String sig = input.witnessComponent.signatures[i];
                       writeVarStr(Hex.decode(sig), witnessStream); //实际环境中注意替换为HEX.decode的方式
                    }
                    byte[] dataWitnessComponets = witnessStream.toByteArray();
                    //witness的length
                    writeVarint(dataWitnessComponets.length, stream);
                    stream.write(dataWitnessComponets);
                }
            }

            //outputs
            if (null != tx.outputs && tx.outputs.size() > 0) {
                writeVarint(tx.outputs.size(), stream);
                for (SignTransaction.AnnotatedOutput output:tx.outputs) {
                    //assertVersion
                    writeVarint(tx.version, stream); //AssetVersion是否默认为1
                    //outputCommit
                    ByteArrayOutputStream outputCommitSteam = new ByteArrayOutputStream();
                    //assetId
                    outputCommitSteam.write(Hex.decode(output.assetId));
                    //amount
                    writeVarint(output.amount, outputCommitSteam);
                    //vmVersion
                    writeVarint(1, outputCommitSteam); //db中获取vm_version
                    //controlProgram
                    writeVarStr(Hex.decode(output.controlProgram), outputCommitSteam);

                    byte[] dataOutputCommit = outputCommitSteam.toByteArray();
                    //outputCommit的length
                    writeVarint(dataOutputCommit.length, stream);
                    stream.write(dataOutputCommit);

                    //outputWitness
                    writeVarint(0, stream);
                }
            }
            //02013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8c98d2b0f402011600144453a011caf735428d0291d82b186e976e286fc100013afffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff40301160014613908c28df499e3aa04e033100efaa24ca8fd0100
            //02013effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8c98d2b0f402011600144453a011caf735428d0291d82b186e976e286fc100013afffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff40301160014613908c28df499e3aa04e033100efaa24ca8fd0100
            byte[] data = stream.toByteArray();
            txSign = Hex.toHexString(data);
            System.out.println(txSign);

            System.out.println("sign Transaction success.");
        } catch (IOException e) {
            System.out.println("sign Transaction failed.");
        }
        return txSign;
    }

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
