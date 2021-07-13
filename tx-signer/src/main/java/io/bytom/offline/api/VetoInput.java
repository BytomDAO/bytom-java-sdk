package io.bytom.offline.api;

import io.bytom.offline.common.Utils;
import io.bytom.offline.types.*;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VetoInput extends BaseInput{
    private String sourceID;

    private Integer sourcePosition;

    private Boolean change;

    private Integer controlProgramIndex;

    private byte[] vote;

    StateData stateData = new StateData();

    @Override
    public InputEntry toInputEntry(Map<Hash, Entry> entryMap, int index) {
        Program pro = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        AssetAmount assetAmount = this.getAssetAmount();
        Hash sourceID = new Hash(this.sourceID);
        ValueSource src = new ValueSource(sourceID, assetAmount, this.sourcePosition);

        Vote  prevOut = new Vote(src, pro, 0,this.vote,stateData.toByteArray());
        Hash prevOutID = prevOut.entryID();
        entryMap.put(prevOutID, prevOut);
        return new Veto(prevOutID, index);
    }

    @Override
    public void buildWitness(String txID) throws Exception {

    }

    @Override
    public byte[] serializeInputCommitment() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarint(Veto_INPUT_TYPE, stream);

        ByteArrayOutputStream spendCommitSteam = new ByteArrayOutputStream();
        spendCommitSteam.write(Hex.decode(sourceID));
        spendCommitSteam.write(Hex.decode(getAssetId()));
        Utils.writeVarint(getAmount(), spendCommitSteam);
        Utils.writeVarint(sourcePosition, spendCommitSteam);
        // vm version
        Utils.writeVarint(1, spendCommitSteam);
        Utils.writeVarStr(Hex.decode(getProgram()), spendCommitSteam);
        Utils.writeVarList(stateData.toByteArray(), spendCommitSteam);
        Utils.writeExtensibleString(spendCommitSteam.toByteArray(), stream);
        Utils.writeVarStr(this.vote,stream);
        return stream.toByteArray();
    }

    @Override
    public byte[] serializeInputWitness() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarList(witnessComponent.toByteArray(), stream);
        return stream.toByteArray();
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(Integer sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public Boolean getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }

    public Integer getControlProgramIndex() {
        return controlProgramIndex;
    }

    public void setControlProgramIndex(Integer controlProgramIndex) {
        this.controlProgramIndex = controlProgramIndex;
    }

    public byte[] getVote() {
        return vote;
    }

    public void setVote(byte[] vote) {
        this.vote = vote;
    }

}
