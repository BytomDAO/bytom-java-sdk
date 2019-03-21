package io.bytom.types;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Issue extends InputEntry {

    private Hash nonceHash;
    private AssetAmount assetAmount;
    private AssetDefinition assetDefinition;
    private ValueDestination witnessDestination;

    public Issue(Hash nonceHash, AssetAmount assetAmount, int ordinal, AssetDefinition assetDefinition) {
        this.nonceHash = nonceHash;
        this.assetAmount = assetAmount;
        this.ordinal = ordinal;
        this.assetDefinition = assetDefinition;
    }

    @Override
    public void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap) {
        this.witnessDestination = new ValueDestination(id, this.assetAmount, pos);
    }

    @Override
    public String typ() {
        return "issuance1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.nonceHash);
        mustWriteForHash(out, this.assetAmount);
    }

    public Hash getNonceHash() {
        return nonceHash;
    }

    public void setNonceHash(Hash nonceHash) {
        this.nonceHash = nonceHash;
    }

    public AssetAmount getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(AssetAmount assetAmount) {
        this.assetAmount = assetAmount;
    }

    public AssetDefinition getAssetDefinition() {
        return assetDefinition;
    }

    public void setAssetDefinition(AssetDefinition assetDefinition) {
        this.assetDefinition = assetDefinition;
    }

    public ValueDestination getWitnessDestination() {
        return witnessDestination;
    }

    public void setWitnessDestination(ValueDestination witnessDestination) {
        this.witnessDestination = witnessDestination;
    }
}
