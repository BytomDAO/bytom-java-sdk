package io.bytom.types;

import java.io.ByteArrayOutputStream;

public class Spend extends Entry {

    public Hash spentOutputID;

    public int ordinal;

    public ValueDestination witnessDestination;

    public byte[][] witnessArguments;

    public Spend(Hash spentOutputID, int ordinal) {
        this.spentOutputID = spentOutputID;
        this.ordinal = ordinal;
    }

    public void setDestination(Hash id, AssetAmount val, long pos) {
        this.witnessDestination = new ValueDestination(id, val, pos);
    }

    @Override
    public String typ() {
        return "spend1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.spentOutputID);
    }
}
