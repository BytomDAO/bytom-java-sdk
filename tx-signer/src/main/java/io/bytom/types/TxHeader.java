package io.bytom.types;

import java.io.ByteArrayOutputStream;

public class TxHeader extends Entry {

    public long version;

    public long serializedSize;

    public long timeRange;

    public Hash[] resultIDs;

    public TxHeader() {}

    public TxHeader(long version, long serializedSize, long timeRange, Hash[] resultIDs) {
        this.version = version;
        this.serializedSize = serializedSize;
        this.timeRange = timeRange;
        this.resultIDs = resultIDs;
    }

    @Override
    public String typ() {
        return "txheader";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out, this.version);
        mustWriteForHash(out, this.timeRange);
        mustWriteForHash(out, this.resultIDs);
    }
}
