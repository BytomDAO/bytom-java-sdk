package io.bytom.types;

import java.io.ByteArrayOutputStream;

public class Retirement extends Entry {
    public ValueSource valueSource;
    public int ordinal;

    public Retirement(ValueSource valueSource, int ordinal) {
        this.valueSource = valueSource;
        this.ordinal = ordinal;
    }


    @Override
    public String typ() {
        return "retirement1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, valueSource);
    }
}
