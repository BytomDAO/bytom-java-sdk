package io.bytom.types;


public class ValueSource {

    public Hash ref;

    public AssetAmount value;

    public long position;

    public ValueSource() {}

    public ValueSource(Hash ref, AssetAmount value, long position) {
        this.ref = ref;
        this.value = value;
        this.position = position;
    }

}
