package io.bytom.types;

public class ValueDestination {

    public Hash ref;

    public AssetAmount value;

    public long position;

    public ValueDestination() {
    }

    public ValueDestination(Hash ref, AssetAmount value, long position) {
        this.ref = ref;
        this.value = value;
        this.position = position;
    }
}
