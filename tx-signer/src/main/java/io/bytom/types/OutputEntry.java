package io.bytom.types;

import java.io.ByteArrayOutputStream;

public class OutputEntry extends Entry {

    public ValueSource source;

    public Program controlProgram;

    public Integer ordinal;

    public OutputEntry() {
        this.source = new ValueSource();
        this.controlProgram = new Program();
    }


    public OutputEntry(ValueSource source, Program controlProgram, Integer ordinal) {
        this.source = source;
        this.controlProgram = controlProgram;
        this.ordinal = ordinal;
    }

    @Override
    public String typ() {
        return "output1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.source);
        mustWriteForHash(out, this.controlProgram);
    }
}
