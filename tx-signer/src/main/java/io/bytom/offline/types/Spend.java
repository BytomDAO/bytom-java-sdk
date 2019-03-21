package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Spend extends InputEntry {

    private Hash spentOutputID;

    private int ordinal;

    private ValueDestination witnessDestination;

    private byte[][] witnessArguments;

    public Spend(Hash spentOutputID, int ordinal) {
        this.spentOutputID = spentOutputID;
        this.ordinal = ordinal;
    }

    @Override
    public void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap) {
        OutputEntry spendOutput = (OutputEntry) entryMap.get(this.spentOutputID);
        this.witnessDestination = new ValueDestination(id, spendOutput.getSource().getValue(), pos);
    }

    @Override
    public String typ() {
        return "spend1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.spentOutputID);
    }

    public Hash getSpentOutputID() {
        return spentOutputID;
    }

    public void setSpentOutputID(Hash spentOutputID) {
        this.spentOutputID = spentOutputID;
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public ValueDestination getWitnessDestination() {
        return witnessDestination;
    }

    public void setWitnessDestination(ValueDestination witnessDestination) {
        this.witnessDestination = witnessDestination;
    }

    public byte[][] getWitnessArguments() {
        return witnessArguments;
    }

    public void setWitnessArguments(byte[][] witnessArguments) {
        this.witnessArguments = witnessArguments;
    }
}
