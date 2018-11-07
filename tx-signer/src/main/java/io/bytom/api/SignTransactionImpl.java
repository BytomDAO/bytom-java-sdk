package io.bytom.api;

import com.google.common.base.Preconditions;
import io.bytom.common.Constants;
import io.bytom.common.ExpandedPrivateKey;
import io.bytom.types.*;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.*;


/**
 * Created by liqiang on 2018/10/24.
 */
public class SignTransactionImpl {

    public boolean signTransaction(SignTransaction tx, String priKey) {

        //组装计算program、inputID、sourceID(muxID)、txID, json数据中这些字段的值为测试值,需重新计算
        mapTransaction(tx);

        //签名得到signatures
        generateSignature(tx,priKey);

        return true;
    }

    public String serializeTransaction(SignTransaction tx) {

        String txSign = null;

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

                    System.out.println("serialize1: " + Hex.toHexString(stream.toByteArray()));

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

                    System.out.println("witnessStream: " + Hex.toHexString(dataWitnessComponets));

                    //witness的length
                    writeVarint(dataWitnessComponets.length, stream);

                    System.out.println("serialize witnessLen: " + dataWitnessComponets.length);

                    stream.write(dataWitnessComponets);

                    System.out.println("serialize2: " + Hex.toHexString(stream.toByteArray()));
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

    public void mapTransaction(SignTransaction signTransaction) {
        Map<Hash, Entry> entryMap = new HashMap<>();
        ValueSource[] muxSources = new ValueSource[signTransaction.inputs.size()];
        List<Spend> spends = new ArrayList<>();

        try {
            for (int i = 0; i < signTransaction.inputs.size(); i++) {
                SignTransaction.AnnotatedInput input = signTransaction.inputs.get(i);
                Program proc = new Program(1, Hex.decode(input.controlProgram));

                AssetID assetID = new AssetID(input.assetId);
                AssetAmount assetAmount = new AssetAmount(assetID, input.amount);

                Hash sourceID = new Hash(input.sourceId);
                ValueSource src = new ValueSource(sourceID, assetAmount, input.sourcePosition);
                Output prevout = new Output(src, proc, 0);
                Hash prevoutID = addEntry(entryMap, prevout);

                input.spentOutputId = prevoutID.toString();

                Spend spend = new Spend(prevoutID, i);
                Hash spendID = addEntry(entryMap, spend);

                input.inputID = spendID.toString();

                muxSources[i] = new ValueSource(spendID, assetAmount, 0);
                spends.add(spend);
            }

            Mux mux = new Mux(muxSources, new Program(1, new byte[]{0x51}));
            Hash muxID = addEntry(entryMap, mux);

            for (Spend spend : spends) {
                Output spendOutput =  (Output) entryMap.get(spend.spentOutputID);
                spend.setDestination(muxID, spendOutput.source.value, spend.ordinal);
            }

            List<Hash> resultIDList = new ArrayList<>();
            for (int i = 0; i < signTransaction.outputs.size(); i++) {
                SignTransaction.AnnotatedOutput output = signTransaction.outputs.get(i);

                AssetAmount amount = new AssetAmount(new AssetID(output.assetId), output.amount);
                ValueSource src = new ValueSource(muxID, amount, i);
                Program prog = new Program(1, Hex.decode(output.controlProgram));
                Output oup = new Output(src, prog, i);

                Hash resultID = addEntry(entryMap, oup);
                resultIDList.add(resultID);

                output.id = resultID.toString();

                ValueDestination destination = new ValueDestination(resultID, src.value, 0);
                mux.witnessDestinations.add(destination);
            }

            TxHeader txHeader = new TxHeader(signTransaction.version, signTransaction.size, signTransaction.timeRange, resultIDList.toArray(new Hash[]{}));
            Hash txID = addEntry(entryMap, txHeader);
            signTransaction.txID = txID.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Hash addEntry(Map<Hash, Entry> entryMap, Entry entry) {
        Hash id = entry.entryID();
        entryMap.put(id, entry);
        return id;
    }


    public SignTransaction generateSignatures(SignTransaction signTransaction, BigInteger[] keys) {

        String[] keyArray = {"819e1f2b7e01ce55594fa1ef9f676517099e5a8b1b4b1628abc34b74e0e8f7e9","58e3bd92f34faac04bc0b00190841180f76ded55105056ad24b36566d4be253e"};
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

    public SignTransaction generateSignature(SignTransaction signTransaction, String priKey) {

        SignTransaction.AnnotatedInput input = signTransaction.inputs.get(0);
        input.witnessComponent.signatures = new String[2];
            if (null != input) {
                try {
                    byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(signTransaction.txID));
                    System.out.println(Hex.toHexString(message));
                    byte[] key = Hex.decode(priKey);
                    byte[] sig = Signer.Ed25519InnerSign(key, message);
                    input.witnessComponent.signatures[0] = Hex.toHexString(sig);
                    input.witnessComponent.signatures[1] = "5b9adf91a5d20f4d45cabe72f21077335152b3f1acf6f7ffee9e1ed975550ed7";
                    System.out.println("sig: " + Hex.toHexString(sig));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("generate signatures failed.");
            }
        return signTransaction;
    }

    //分多次签名
    public boolean generateSignature(SignTransaction signTransaction, BigInteger key, int index) {
        boolean result = false;
        SignTransaction.AnnotatedInput input = signTransaction.inputs.get(index);
        if (null != input) {
            try {
                byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(signTransaction.txID));
                byte[] expandedPrv = BigIntegerToBytes(key);
                byte[] priKey = ExpandedPrivateKey.ExpandedPrivateKey(expandedPrv);
                byte[] sig = Signer.Ed25519InnerSign(priKey, message);
                input.witnessComponent.signatures[index] = Hex.toHexString(sig);
                System.out.println("sig: " + Hex.toHexString(sig));
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            System.out.println("generate signatures failed.");
            result = false;
        }
        return result;
    }
//
//    public void writeVarint(long data, OutputStream out) throws IOException {
//        byte[] buf = new byte[9];
//        int n = putUvarint(buf, data);
//        out.write(buf, 0, n);
//    }
//
//    public void writeVarStr(byte[] str, OutputStream out) throws IOException {
//        writeVarint(str.length, out);
//        out.write(str);
//    }

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

    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        Preconditions.checkArgument(b.signum() >= 0, "b must be positive or zero");
        Preconditions.checkArgument(numBytes > 0, "numBytes must be positive");
        byte[] src = b.toByteArray();
        byte[] dest = new byte[numBytes];
        boolean isFirstByteOnlyForSign = src[0] == 0;
        int length = isFirstByteOnlyForSign ? src.length - 1 : src.length;
        Preconditions.checkArgument(length <= numBytes, "The given number does not fit in " + numBytes);
        int srcPos = isFirstByteOnlyForSign ? 1 : 0;
        int destPos = numBytes - length;
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    public static byte[] pruneIntermediateScalar(byte[] f) {
        f[0] &= 248;
        f[31] &= 31; // clear top 3 bits
        f[31] |= 64; // set second highest bit
        return f;
    }
}
