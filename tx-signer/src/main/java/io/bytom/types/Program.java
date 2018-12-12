package io.bytom.types;


public class Program {

    public long vmVersion;

    public byte[] code;

    public Program() {}

    public Program(long vmVersion, byte[] code) {
        this.vmVersion = vmVersion;
        this.code = code;
    }
}
