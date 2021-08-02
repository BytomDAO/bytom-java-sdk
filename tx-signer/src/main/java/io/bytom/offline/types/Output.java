package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public class Output extends OutputEntry {
    public Output() {
        this.source = new ValueSource();
        this.controlProgram = new Program();
    }


    public Output(ValueSource source, Program controlProgram, Integer ordinal, byte[][] stateData) {
        this.source = source;
        this.controlProgram = controlProgram;
        this.ordinal = ordinal;
        this.stateData = stateData;
    }

    @Override
    public String typ() {
        return "originalOutput1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.source);
        mustWriteForHash(out, this.controlProgram);
        mustWriteForHash(out, this.stateData);
    }
}
