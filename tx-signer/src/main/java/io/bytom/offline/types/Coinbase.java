package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Coinbase extends InputEntry{
    private Hash coinbaseOutputID;

    private int ordinal;

    private ValueDestination witnessDestination;

    private String arbitrary;

    public Coinbase(Hash coinbaseOutputID, int ordinal) {
        this.coinbaseOutputID = coinbaseOutputID;
        this.ordinal = ordinal;
    }

    public Coinbase(String arbitrary) {
        this.arbitrary = arbitrary;
    }


    @Override
    public String typ() {
        return "coinbase1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        System.out.println("write for hash");
        System.out.println(this.arbitrary);
          mustWriteForHash(out,this.arbitrary);
    }

    @Override
    public void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap) {
//        OutputEntry spendOutput = (OutputEntry) entryMap.get(this.coinbaseOutputID);
//        this.witnessDestination = new ValueDestination(id, spendOutput.getSource().getValue(), pos);
    }

    public String getArbitrary() {
        return arbitrary;
    }

    public void setArbitrary(String arbitrary) {
        this.arbitrary = arbitrary;
    }
}
