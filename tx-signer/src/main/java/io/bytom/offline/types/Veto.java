package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Veto extends InputEntry{
    private Hash spentOutputID;
    private ValueDestination witnessDestination;


    public Veto(Hash spentOutputID, int ordinal) {
        this.spentOutputID = spentOutputID;
        this.ordinal = ordinal;
    }

    @Override
    public String typ() {
        return "vetoInput1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out,this.spentOutputID);
    }

    @Override
    public void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap) {
        OutputEntry spendOutput = (OutputEntry) entryMap.get(this.spentOutputID);
        this.witnessDestination = new ValueDestination(id, spendOutput.getSource().getValue(), pos);
    }
}
