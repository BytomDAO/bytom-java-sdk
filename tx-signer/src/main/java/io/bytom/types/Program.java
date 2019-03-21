package io.bytom.types;


public class Program {

    private long vmVersion;

    private byte[] code;

    public Program() {}

    public Program(long vmVersion, byte[] code) {
        this.vmVersion = vmVersion;
        this.code = code;
    }

    public long getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(long vmVersion) {
        this.vmVersion = vmVersion;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }
}
