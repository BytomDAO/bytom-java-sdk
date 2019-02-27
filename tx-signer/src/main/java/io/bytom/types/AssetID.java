package io.bytom.types;

import org.bouncycastle.util.encoders.Hex;

public class AssetID {

    private String hexValue;

    public AssetID() {
    }

    public AssetID(String hexValue) {
        this.hexValue = hexValue;
    }


    public AssetID(byte[] byteArray) {
        this.hexValue = Hex.toHexString(byteArray);
    }

    public byte[] toByteArray() {
        return Hex.decode(this.hexValue);
    }


}
