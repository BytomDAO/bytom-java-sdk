package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public class TxHeader extends Entry {

    private long version;

    private long serializedSize;

    private long timeRange;

    private Hash[] resultIDs;

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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getSerializedSize() {
        return serializedSize;
    }

    public void setSerializedSize(long serializedSize) {
        this.serializedSize = serializedSize;
    }

    public long getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(long timeRange) {
        this.timeRange = timeRange;
    }

    public Hash[] getResultIDs() {
        return resultIDs;
    }

    public void setResultIDs(Hash[] resultIDs) {
        this.resultIDs = resultIDs;
    }
}
