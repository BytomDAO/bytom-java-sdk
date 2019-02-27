package io.bytom.api;

import io.bytom.common.DerivePrivateKey;
import io.bytom.common.DeriveXpub;
import io.bytom.common.ExpandedPrivateKey;
import io.bytom.common.Utils;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


/**
 * Created by liqiang on 2018/10/24.
 */
public class SignTransaction {
    public String serializeTransaction(Transaction tx) {
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
                for (Transaction.AnnotatedInput input : tx.inputs) {
                    if (input.type == 1) {
                        //assertVersion
                        Utils.writeVarint(tx.version, stream); //AssetVersion是否默认为1

                        //inputCommitment
                        ByteArrayOutputStream inputCommitStream = new ByteArrayOutputStream();
                        //spend type flag
                        Utils.writeVarint(input.type, inputCommitStream);
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
                        if (null != input.witnessComponent) {
                            ByteArrayOutputStream witnessStream = new ByteArrayOutputStream();
                            //arguments
                            int lenSigs = input.witnessComponent.signatures.length;
                            //arguments的length
                            Utils.writeVarint(lenSigs, witnessStream);
                            for (int i = 0; i < lenSigs; i++) {
                                String sig = input.witnessComponent.signatures[i];
                                Utils.writeVarStr(Hex.decode(sig), witnessStream);
                            }
                            byte[] dataWitnessComponets = witnessStream.toByteArray();
                            //witness的length
                            Utils.writeVarint(dataWitnessComponets.length, stream);
                            stream.write(dataWitnessComponets);
                        }
                    }
                    if (input.type == 0) {
                        //assetVersion
                        Utils.writeVarint(01, stream);
                        //写入 type nonce assetId amount
                        ByteArrayOutputStream issueInfo = new ByteArrayOutputStream();
                        //写入 input.type==00 issue
                        Utils.writeVarint(input.type, issueInfo);
                        //写入 8个字节的随机数
                        Utils.writeVarStr(Hex.decode(input.nonce), issueInfo);
                        issueInfo.write(Hex.decode(input.assetId));
                        Utils.writeVarint(input.amount, issueInfo);
                        stream.write(issueInfo.toByteArray().length);
                        stream.write(issueInfo.toByteArray());

                        ByteArrayOutputStream issueInfo1 = new ByteArrayOutputStream();
                        //未知
                        Utils.writeVarint(1, issueInfo1);
                        //写入assetDefine
                        Utils.writeVarStr(Hex.decode(input.assetDefinition), issueInfo1);
                        //vm.version
                        Utils.writeVarint(1, issueInfo1);
                        //controlProgram
                        Utils.writeVarStr(Hex.decode(input.controlProgram), issueInfo1);

                        //inputWitness
                        if (null != input.witnessComponent) {
                            ByteArrayOutputStream witnessStream = new ByteArrayOutputStream();
                            //arguments
                            int lenSigs = input.witnessComponent.signatures.length;
                            //arguments的length
                            Utils.writeVarint(lenSigs, witnessStream);
                            for (int i = 0; i < lenSigs; i++) {
                                String sig = input.witnessComponent.signatures[i];
                                Utils.writeVarStr(Hex.decode(sig), witnessStream);
                            }
//                            byte[] dataWitnessComponets = witnessStream.toByteArray();
                            //witness的length
//                            Utils.writeVarint(dataWitnessComponets.length, issueInfo1);

                            issueInfo1.write(witnessStream.toByteArray());
                        }
                        stream.write(issueInfo1.toByteArray().length - 1);
                        stream.write(issueInfo1.toByteArray());

                    }
                }
            }

            //outputs
            if (null != tx.outputs && tx.outputs.size() > 0) {
                Utils.writeVarint(tx.outputs.size(), stream);
                for (Transaction.AnnotatedOutput output : tx.outputs) {
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

    public Integer getTransactionSize(Transaction tx) {
        String result = serializeTransaction(tx);
        return Hex.decode(result).length;
    }

    //签名组装witness
    public Transaction buildWitness(Transaction transaction, int index, String priKey) {

        Transaction.AnnotatedInput input = transaction.inputs.get(index);
        if (null == input.witnessComponent)

            if (null != input) {
                try {
                    input.witnessComponent = new Transaction.InputWitnessComponent();
                    byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(transaction.txID));
                    byte[] key = Hex.decode(priKey);
                    byte[] expandedPrivateKey = ExpandedPrivateKey.ExpandedPrivateKey(key);
                    byte[] sig = Signer.Ed25519InnerSign(expandedPrivateKey, message);

                    switch (input.type) {
                        case 1: {
                            input.witnessComponent.signatures = new String[2];
                            input.witnessComponent.signatures[0] = Hex.toHexString(sig);
                            byte[] deriveXpub = DeriveXpub.deriveXpub(Hex.decode(priKey));
                            String pubKey = Hex.toHexString(deriveXpub).substring(0, 64);
                            input.witnessComponent.signatures[1] = pubKey;
                            break;
                        }
                        case 0: {
                            input.witnessComponent.signatures = new String[1];
                            input.witnessComponent.signatures[0] = Hex.toHexString(sig);
                            break;
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("build witness failed.");
            }
        return transaction;
    }

    //多签spend
    public void buildWitness(Transaction transaction, int index, String[] privateKeys) {
        Transaction.AnnotatedInput input = transaction.inputs.get(index);
        if (null == input.witnessComponent)
            input.witnessComponent = new Transaction.InputWitnessComponent();
        try {

            //TODO 多签issue
            StringBuilder sb = new StringBuilder("ae");
            input.witnessComponent.signatures = new String[privateKeys.length + 1];
            for (int i = 0; i < privateKeys.length; i++) {
                byte[] key = DerivePrivateKey.derivePrivateKey(privateKeys[i], 1, false, input.getControlProgramIndex());
                byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(transaction.txID));
                byte[] expandedPrivateKey = ExpandedPrivateKey.ExpandedPrivateKey(key);
                byte[] sig = Signer.Ed25519InnerSign(expandedPrivateKey, message);
                input.witnessComponent.signatures[i] = Hex.toHexString(sig);
                byte[] deriveXpub = DeriveXpub.deriveXpub(key);
                String publicKey = Hex.toHexString(deriveXpub).substring(0, 64);
                sb.append("20").append(publicKey);
            }
            //签名数跟公钥数相同
            //TODO 签名数跟公钥数量不同
            sb.append("5").append(privateKeys.length).append("5").append(privateKeys.length).append("ad");
            input.witnessComponent.signatures[privateKeys.length] = sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] hashFn(byte[] hashedInputHex, byte[] txID) {

        SHA3.Digest256 digest256 = new SHA3.Digest256();
        // data = hashedInputHex + txID
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(hashedInputHex, 0, hashedInputHex.length);
        out.write(txID, 0, txID.length);
        byte[] data = out.toByteArray();
        return digest256.digest(data);
    }


    public Transaction generateSignatures(Transaction Transaction, BigInteger[] keys) {
        Transaction.AnnotatedInput input = Transaction.inputs.get(0);
        input.witnessComponent.signatures = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            if (null != input) {
                try {
                    byte[] message = hashFn(Hex.decode(input.inputID), Hex.decode(Transaction.txID));
                    byte[] expandedPrv = Utils.BigIntegerToBytes(keys[i]);
                    byte[] priKey = ExpandedPrivateKey.ExpandedPrivateKey(expandedPrv);
                    byte[] sig = Signer.Ed25519InnerSign(priKey, message);
                    input.witnessComponent.signatures[i] = Hex.toHexString(sig);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("generate signatures failed.");
            }
        }
        return Transaction;
    }

    //根据主私钥对交易签名，生成序列化的rawTransaction
    public String rawTransaction(String rootPrivateKey, Transaction tx) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] privateChild;
        for (int i = 0; i < tx.inputs.size(); i++) {
            if (tx.inputs.get(i).type == 1) {
                privateChild = DerivePrivateKey.derivePrivateKey(rootPrivateKey, 1, tx.inputs.get(i).isChange(), tx.inputs.get(i).getControlProgramIndex());
            } else {
                privateChild = DerivePrivateKey.derivePrivateKey(rootPrivateKey, tx.inputs.get(i).getKeyIndex());
            }
            buildWitness(tx, i, Hex.toHexString(privateChild));
        }
        return serializeTransaction(tx);
    }

    //多签
    public String rawTransaction(String[] rootPrivateKey, Transaction tx) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String privateChild;
        for (int i = 0; i < tx.inputs.size(); i++) {
            if (tx.inputs.get(i).controlProgram.length() != 44) {
                buildWitness(tx, i, rootPrivateKey);
            }

        }
        return serializeTransaction(tx);
    }


}


