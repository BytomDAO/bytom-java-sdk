package io.bytom.types;

import org.bouncycastle.util.encoders.Hex;

import java.util.Objects;

public class Hash {

    private String hexValue;

    public Hash() {}

    public Hash(String hexValue) {
        this.hexValue = hexValue;
    }

    public Hash(byte[] byteArray) {
        this.hexValue = Hex.toHexString(byteArray);
    }

    public byte[] toByteArray() {
        return Hex.decode(this.hexValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hash hash = (Hash) o;
        return Objects.equals(hexValue, hash.hexValue);
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
