package io.bytom.offline.api;

import io.bytom.offline.common.Utils;
import io.bytom.offline.types.*;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class BaseOutput {
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

    public BaseOutput() {
    }

    public BaseOutput(String assetID, long amount, String controlProgram) {
        this.assetId = assetID;
        this.amount = amount;
        this.controlProgram = controlProgram;
    }


    public abstract byte[] serializeOutputCommitment() throws IOException;


    public byte[] serializeOutput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // asset version
        Utils.writeVarint(1, stream);
        Utils.writeExtensibleString(serializeOutputCommitment(), stream);
        return stream.toByteArray();
    }

}