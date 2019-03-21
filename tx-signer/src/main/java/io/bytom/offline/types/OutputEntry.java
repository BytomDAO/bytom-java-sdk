package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public class OutputEntry extends Entry {

    private ValueSource source;

    private Program controlProgram;

    private Integer ordinal;

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

    public ValueSource getSource() {
        return source;
    }

    public void setSource(ValueSource source) {
        this.source = source;
    }

    public Program getControlProgram() {
        return controlProgram;
    }

    public void setControlProgram(Program controlProgram) {
        this.controlProgram = controlProgram;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
}
