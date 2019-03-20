package io.bytom.types;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Spend extends InputEntry {

    public Hash spentOutputID;

    public int ordinal;

    public ValueDestination witnessDestination;

    public byte[][] witnessArguments;

    public Spend(Hash spentOutputID, int ordinal) {
        this.spentOutputID = spentOutputID;
        this.ordinal = ordinal;
    }

    @Override
    public void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap) {
        OutputEntry spendOutput = (OutputEntry) entryMap.get(this.spentOutputID);
        this.witnessDestination = new ValueDestination(id, spendOutput.source.value, pos);
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
