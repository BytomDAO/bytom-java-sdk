package io.bytom.types;

import java.io.ByteArrayOutputStream;

public class Issue extends Entry {
    public Hash nonceHash;
    public AssetAmount assetAmount;
    public int ordinal;
    public AssetDefinition assetDefinition;
    public ValueDestination witnessDestination;

    public Issue(Hash nonceHash, AssetAmount assetAmount, int ordinal) {
        this.nonceHash = nonceHash;
        this.assetAmount = assetAmount;
        this.ordinal = ordinal;
    }

    public void setDestination(Hash id, AssetAmount val, long pos) {
        this.witnessDestination = new ValueDestination(id, val, pos);
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
}
