package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public abstract class OutputEntry extends Entry {
    protected ValueSource source;

    protected Program controlProgram;

    protected Integer ordinal;

    protected byte[][] stateData;


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
