package io.bytom.offline.types;

import java.io.ByteArrayOutputStream;

public class Vote extends OutputEntry{
    private byte[] vote;

    public Vote(ValueSource source, Program controlProgram, Integer ordinal,byte[] vote, byte[][] stateData) {
        this.source = source;
        this.controlProgram = controlProgram;
        this.ordinal = ordinal;
        this.vote = vote;
        this.stateData = stateData;
    }

    @Override
    public String typ() {
        return "voteOutput1";
    }

    @Override
    public void writeForHash(ByteArrayOutputStream out) {
        mustWriteForHash(out,this.source);
        mustWriteForHash(out,this.controlProgram);
        mustWriteForHash(out,this.vote);
        mustWriteForHash(out,this.stateData);
    }

    public byte[] getVote() {
        return vote;
    }

    public void setVote(byte[] vote) {
        this.vote = vote;
    }

}
