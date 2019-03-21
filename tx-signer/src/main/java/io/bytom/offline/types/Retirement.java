package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public class Retirement extends Entry {

    private ValueSource valueSource;
    private int ordinal;

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

    public ValueSource getValueSource() {
        return valueSource;
    }

    public void setValueSource(ValueSource valueSource) {
        this.valueSource = valueSource;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
