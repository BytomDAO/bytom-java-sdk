package io.bytom.types;

import java.util.Map;

public abstract class InputEntry extends Entry {

    public int ordinal;

    public abstract void setDestination(Hash id, long pos, Map<Hash, Entry> entryMap);
}
