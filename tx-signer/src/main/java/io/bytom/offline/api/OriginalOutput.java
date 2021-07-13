package io.bytom.offline.api;

import io.bytom.offline.common.Utils;
import io.bytom.offline.common.VMUtil;
import io.bytom.offline.util.AssetIdUtil;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OriginalOutput {
    /**
     * The number of units of the asset being controlled.
     */
    private Long amount;

    /**
     * The id of the asset being controlled.
     */
    private String assetId;

    /**
     * The control program which must be satisfied to transfer this output.
     */
    private String controlProgram;

    /**
     * The id of the output.
     */
    private String id;

    StateData stateData = new StateData();

    public OriginalOutput() {
    }

    public OriginalOutput(String assetID, long amount, String controlProgram) {
        this.assetId = assetID;
        this.amount = amount;
        this.controlProgram = controlProgram;
    }

    public OriginalOutput(String rawAssetDefinition,String program) {
        this.assetId = AssetIdUtil.computeAssetID(rawAssetDefinition,program);
        this.controlProgram = program;
    }

    public byte[] serializeOutput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // asset version
        Utils.writeVarint(1, stream);
        //outputType
        Utils.writeVarint(0, stream); //outputType
        //outputCommit
        ByteArrayOutputStream outputCommitSteam = new ByteArrayOutputStream();
        //assetId
        outputCommitSteam.write(Hex.decode(assetId));
        //amount
        Utils.writeVarint(amount, outputCommitSteam);
        //vmVersion
        Utils.writeVarint(1, outputCommitSteam); //db中获取vm_version
        //controlProgram
        Utils.writeVarStr(Hex.decode(controlProgram), outputCommitSteam);
        //stateData
        Utils.writeVarList(this.stateData.toByteArray(), outputCommitSteam);

        Utils.writeExtensibleString(outputCommitSteam.toByteArray(), stream);

        //outputWitness
        Utils.writeVarint(0, stream);
        return stream.toByteArray();
    }


    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getControlProgram() {
        return controlProgram;
    }

    public void setControlProgram(String controlProgram) {
        this.controlProgram = controlProgram;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void appendStateData(String stateDataStr){
        stateData.appendStateData(stateDataStr);
    }
}
