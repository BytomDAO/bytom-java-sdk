package io.bytom.offline.types;

public class AssetAmount {

    private AssetID assetID;

    private Long amount;

    public AssetAmount() {}

    public AssetAmount(AssetID assetID, long amount) {
        this.assetID = assetID;
        this.amount = amount;
    }

    public AssetID getAssetID() {
        return assetID;
    }

    public void setAssetID(AssetID assetID) {
        this.assetID = assetID;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
