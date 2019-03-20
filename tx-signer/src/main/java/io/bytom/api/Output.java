package io.bytom.api;

import io.bytom.common.Utils;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class Output {

    /**
     * address
     */
    public String address;

    /**
     * The number of units of the asset being controlled.
     */
    public long amount;

    /**
     * The definition of the asset being controlled (possibly null).
     */
    public Map<String, Object> assetDefinition;

    /**
     * The id of the asset being controlled.
     */
    public String assetId;

    /**
     * The control program which must be satisfied to transfer this output.
     */
    public String controlProgram;

    /**
     * The id of the output.
     */
    public String id;

    /**
     * The output's position in a transaction's list of outputs.
     */
    public Integer position;

    public Output(String assetId, long amount, String controlProgram) {
        this.assetId = assetId;
        this.amount = amount;
        this.controlProgram = controlProgram;
    }

    public byte[] serializeOutput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //assetVersion
        Utils.writeVarint(1, stream); //AssetVersion是否默认为1
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

        byte[] dataOutputCommit = outputCommitSteam.toByteArray();
        //outputCommit的length
        Utils.writeVarint(dataOutputCommit.length, stream);
        stream.write(dataOutputCommit);

        //outputWitness
        Utils.writeVarint(0, stream);
        return stream.toByteArray();
    }

    /**
     * The type the output.<br>
     * Possible values are "control" and "retire".
     */
    public String type;

    public Output setAddress(String address) {
        this.address = address;
        return this;
    }

    public Output setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    public Output setAssetId(String assetId) {
        this.assetId = assetId;
        return this;
    }

    public Output setControlProgram(String controlProgram) {
        this.controlProgram = controlProgram;
        return this;
    }

    public Output setPosition(Integer position) {
        this.position = position;
        return this;
    }
}
