package io.bytom.types;

public class ExpandedKeys {

    private String expandedPriKey;

    private String expandedPubKey;

    public void setPriKey(String priKey) {
        this.expandedPriKey = priKey;
    }

    public String getPriKey() {
        return this.expandedPriKey;
    }

    public void setPubKey(String pubKey) {
        this.expandedPubKey = pubKey;
    }

    public String getPubKey() {
        return this.expandedPubKey;
    }
}
