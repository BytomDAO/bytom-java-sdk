package io.bytom.types;


public class ValueSource {

    private Hash ref;

    private AssetAmount value;

    private long position;

    public ValueSource() {}

    public ValueSource(Hash ref, AssetAmount value, long position) {
        this.ref = ref;
        this.value = value;
        this.position = position;
    }

    public Hash getRef() {
        return ref;
    }

    public void setRef(Hash ref) {
        this.ref = ref;
    }

    public AssetAmount getValue() {
        return value;
    }

    public void setValue(AssetAmount value) {
        this.value = value;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
