package io.bytom.offline.types;

import java.util.Map;

public abstract class InputEntry extends Entry {

    protected int ordinal;

    public abstract void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap);

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
