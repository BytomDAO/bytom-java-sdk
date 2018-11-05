package io.bytom.types;

import org.bouncycastle.util.encoders.Hex;

import java.util.Objects;

public class AssetID {

    private String hexValue;

    public AssetID() {}

    public AssetID(String hexValue) {
        this.hexValue = hexValue;
    }

    public AssetID(byte[] byteArray) {
        this.hexValue = Hex.toHexString(byteArray);
    }

    public byte[] toByteArray() {
        return Hex.decode(this.hexValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetID assetID = (AssetID) o;
        return Objects.equals(hexValue, assetID.hexValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hexValue);
    }

    @Override
    public String toString() {
        return this.hexValue;
    }
}
