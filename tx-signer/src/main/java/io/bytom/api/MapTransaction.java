package io.bytom.api;

import io.bytom.types.*;
import io.bytom.util.SHA3Util;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTransaction {

    public static Transaction mapTx(Transaction tx) {
        Map<Hash, Entry> entryMap = new HashMap<>();
        ValueSource[] muxSources = new ValueSource[tx.inputs.size()];
        List<Spend> spends = new ArrayList<>();
        List<Issue> issuances = new ArrayList<>();
        try {
            for (int i = 0; i < tx.inputs.size(); i++) {
                Transaction.AnnotatedInput input = tx.inputs.get(i);
                switch (input.type) {
                    case 1: {
                        Program pro = new Program(input.type, Hex.decode(input.controlProgram));
                        AssetID assetID = new AssetID(input.assetId);
                        AssetAmount assetAmount = new AssetAmount(assetID, input.amount);
                        Hash sourceID = new Hash(input.sourceId);
                        ValueSource src = new ValueSource(sourceID, assetAmount, input.sourcePosition);

                        Output prevout = new Output(src, pro, 0);
                        Hash prevoutID = addEntry(entryMap, prevout);
                        Spend spend = new Spend(prevoutID, i);
                        Hash spendID = addEntry(entryMap, spend);
                        input.inputID = spendID.toString();

                        muxSources[i] = new ValueSource(spendID, assetAmount, 0);
                        spends.add(spend);
                        break;
                    }
                    case 0: {
                        if (input.nonce == null) {
                            SecureRandom sr = new SecureRandom();
                            byte[] randBytes = new byte[8];
                            sr.nextBytes(randBytes);
                            input.nonce = Hex.toHexString(randBytes);
                        }
                        Hash nonceHash = new Hash(SHA3Util.hashSha256(Hex.decode(input.nonce)));
                        Hash assetDefHash = new Hash(input.assetDefinition);
                        AssetID assetID = new AssetID(input.assetId);
                        AssetAmount value = new AssetAmount(assetID, input.amount);

                        Issue issuance = new Issue(nonceHash, value, i);
                        Program pro = new Program(input.type, Hex.decode(input.controlProgram));
                        issuance.assetDefinition = new AssetDefinition(assetDefHash, pro);
                        Hash issuanceID = addEntry(entryMap, issuance);
                        input.inputID = issuanceID.toString();
                        muxSources[i] = new ValueSource(issuanceID, value, 0);
                        issuances.add(issuance);
                        break;
                    }
                }
            }
            Mux mux = new Mux(muxSources, new Program(1, new byte[]{0x51}));
            Hash muxID = addEntry(entryMap, mux);
            for (Spend spend : spends) {
                Output spendOutput = (Output) entryMap.get(spend.spentOutputID);
                spend.setDestination(muxID, spendOutput.source.value, spend.ordinal);
            }
            for (Issue issue : issuances) {
                issue.setDestination(muxID, issue.assetAmount, issue.ordinal);
            }

            List<Hash> resultIDList = new ArrayList<>();
            for (int i = 0; i < tx.outputs.size(); i++) {
                Transaction.AnnotatedOutput output = tx.outputs.get(i);

                AssetAmount amount = new AssetAmount(new AssetID(output.assetId), output.amount);
                ValueSource src = new ValueSource(muxID, amount, i);
                Hash resultID;
                if (output.controlProgram.startsWith("6a")) {
                    Retirement retirement = new Retirement(src, i);
                    resultID = addEntry(entryMap, retirement);
                } else {
                    Program prog = new Program(1, Hex.decode(output.controlProgram));
                    Output oup = new Output(src, prog, i);
                    resultID = addEntry(entryMap, oup);
                }
                resultIDList.add(resultID);
                output.id = resultID.toString();

                ValueDestination destination = new ValueDestination(resultID, src.value, 0);
                mux.witnessDestinations.add(destination);
            }

            TxHeader txHeader = new TxHeader(tx.version, tx.size, tx.timeRange, resultIDList.toArray(new Hash[]{}));
            Hash txID = addEntry(entryMap, txHeader);
            tx.txID = txID.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tx;
    }

    private static Hash addEntry(Map<Hash, Entry> entryMap, Entry entry) {
        Hash id = entry.entryID();
        entryMap.put(id, entry);
        return id;
    }
}
