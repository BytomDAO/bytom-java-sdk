package io.bytom.types;

public class AssetAmount {

    public AssetID assetID;

    public long amount;

    public AssetAmount() {
    }

    public AssetAmount(AssetID assetID, long amount) {
        this.assetID = assetID;
        this.amount = amount;
    }
}
