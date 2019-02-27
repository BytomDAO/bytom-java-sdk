package io.bytom.types;

import java.io.ByteArrayOutputStream;

public class AssetDefinition extends Entry {
    public Hash assetDefHash;
    public Program program;

    public AssetDefinition(Hash assetDefHash, Program program) {
        this.assetDefHash = assetDefHash;
        this.program = program;
    }

    @Override
    public String typ() {
        return "asset";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.program);
        mustWriteForHash(out, this.assetDefHash);
    }
}
