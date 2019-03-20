package io.bytom.types;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Issue extends InputEntry {
    public Hash nonceHash;
    public AssetAmount assetAmount;
    public AssetDefinition assetDefinition;
    public ValueDestination witnessDestination;

    public Issue(Hash nonceHash, AssetAmount assetAmount, int ordinal) {
        this.nonceHash = nonceHash;
        this.assetAmount = assetAmount;
        this.ordinal = ordinal;
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
}
