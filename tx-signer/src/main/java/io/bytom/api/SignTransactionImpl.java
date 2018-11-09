package io.bytom.api;

import com.google.common.base.Preconditions;
import io.bytom.common.Constants;
import io.bytom.common.DeriveXpub;
import io.bytom.common.ExpandedPrivateKey;
import io.bytom.common.Utils;
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

    public void mapTransaction(SignTransaction signTransaction) {
        //组装计算program、inputID、sourceID(muxID)、txID, json数据中这些字段的值为测试值,需重新计算
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

    public String serializeTransaction(SignTransaction tx) {

        String txSign = null;
        //开始序列化
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(7);
            // version
            if (null != tx.version)
                Utils.writeVarint(tx.version, stream);
            if (null != tx.timeRange)
                Utils.writeVarint(tx.timeRange, stream);
            //inputs
            if (null != tx.inputs && tx.inputs.size() > 0) {
                Utils.writeVarint(tx.inputs.size(), stream);
                for (SignTransaction.AnnotatedInput input:tx.inputs) {
                    //assertVersion
                    Utils.writeVarint(tx.version, stream); //AssetVersion是否默认为1

                    //inputCommitment
                    ByteArrayOutputStream inputCommitStream = new ByteArrayOutputStream();
                    //spend type flag
                    Utils.writeVarint(Constants.INPUT_TYPE_SPEND, inputCommitStream);
                    //spendCommitment
                    ByteArrayOutputStream spendCommitSteam = new ByteArrayOutputStream();
                    spendCommitSteam.write(Hex.decode(input.sourceId)); //计算muxID
                    spendCommitSteam.write(Hex.decode(input.assetId));
                    Utils.writeVarint(input.amount, spendCommitSteam);
                    //sourcePosition
                    Utils.writeVarint(input.sourcePosition, spendCommitSteam); //db中获取position
                    //vmVersion
                    Utils.writeVarint(1, spendCommitSteam); //db中获取vm_version
                    //controlProgram
                    Utils.writeVarStr(Hex.decode(input.controlProgram), spendCommitSteam);

                    byte[] dataSpendCommit = spendCommitSteam.toByteArray();
                    Utils.writeVarint(dataSpendCommit.length, inputCommitStream);
                    inputCommitStream.write(dataSpendCommit);
                    byte[] dataInputCommit = inputCommitStream.toByteArray();
                    //inputCommit的length
                    Utils.writeVarint(dataInputCommit.length, stream);
                    stream.write(dataInputCommit);

                    //inputWitness
                    ByteArrayOutputStream witnessStream = new ByteArrayOutputStream();
                    //arguments
                    int lenSigs = input.witnessComponent.signatures.length;
                    //arguments的length
                    Utils.writeVarint(lenSigs, witnessStream);
                    for (int i =0; i<lenSigs; i++) {
                        String sig = input.witnessComponent.signatures[i];
                        Utils.writeVarStr(Hex.decode(sig), witnessStream);
                    }
                    byte[] dataWitnessComponets = witnessStream.toByteArray();
                    //witness的length
                    Utils.writeVarint(dataWitnessComponets.length, stream);
                    stream.write(dataWitnessComponets);
                }
            }

            //outputs
            if (null != tx.outputs && tx.outputs.size() > 0) {
                Utils.writeVarint(tx.outputs.size(), stream);
                for (SignTransaction.AnnotatedOutput output:tx.outputs) {
                    //assertVersion
                    Utils.writeVarint(tx.version, stream); //AssetVersion是否默认为1
                    //outputCommit
                    ByteArrayOutputStream outputCommitSteam = new ByteArrayOutputStream();
                    //assetId
                    outputCommitSteam.write(Hex.decode(output.assetId));
                    //amount
                    Utils.writeVarint(output.amount, outputCommitSteam);
                    //vmVersion
                    Utils.writeVarint(1, outputCommitSteam); //db中获取vm_version
                    //controlProgram
                    Utils.writeVarStr(Hex.decode(output.controlProgram), outputCommitSteam);

                    byte[] dataOutputCommit = outputCommitSteam.toByteArray();
                    //outputCommit的length
                    Utils.writeVarint(dataOutputCommit.length, stream);
                    stream.write(dataOutputCommit);

                    //outputWitness
                    Utils.writeVarint(0, stream);
                }
            }

            byte[] data = stream.toByteArray();
            txSign = Hex.toHexString(data);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return txSign;
    }

    //签名组装witness
    public SignTransaction buildWitness(SignTransaction signTransaction, int index, String priKey, String pubKey) {

        SignTransaction.AnnotatedInput input = signTransaction.inputs.get(index);
        input.witnessComponent.signatures = new String[2];
        if (null != input) {
            try {
                byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(signTransaction.txID));
                byte[] key = Hex.decode(priKey);
                byte[] sig = Signer.Ed25519InnerSign(key, message);
                input.witnessComponent.signatures[0] = Hex.toHexString(sig);
                input.witnessComponent.signatures[1] = pubKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("build witness failed.");
        }
        return signTransaction;
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

    private Hash addEntry(Map<Hash, Entry> entryMap, Entry entry) {
        Hash id = entry.entryID();
        entryMap.put(id, entry);
        return id;
    }


    public SignTransaction generateSignatures(SignTransaction signTransaction, BigInteger[] keys) {
        SignTransaction.AnnotatedInput input = signTransaction.inputs.get(0);
        input.witnessComponent.signatures = new String[keys.length];
        for (int i=0; i<keys.length; i++) {
            if (null != input) {
                try {
                    byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(signTransaction.txID));
                    byte[] expandedPrv = Utils.BigIntegerToBytes(keys[i]);
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
}
