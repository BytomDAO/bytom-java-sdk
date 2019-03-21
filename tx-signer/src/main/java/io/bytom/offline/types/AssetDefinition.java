package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public class AssetDefinition extends Entry {

    private Hash assetDefHash;
    private Program program;

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

    public Hash getAssetDefHash() {
        return assetDefHash;
    }

    public Program getProgram() {
        return program;
    }

    public void setAssetDefHash(Hash assetDefHash) {
        this.assetDefHash = assetDefHash;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
