package io.bytom.api;

import io.bytom.common.Utils;
import io.bytom.types.*;
import java.io.IOException;
import java.util.Map;

public abstract class BaseInput {

    public static final int ISSUANCE_INPUT_TYPE = 0;
    public static final int SPEND_INPUT_TYPE = 1;

    private String inputID;

    /**
     * The number of units of the asset being issued or spent.
     */
    private long amount;

    /**
     * The id of the asset being issued or spent.
     */
    private String assetId;

    /**
     * The program which must be satisfied to transfer this output.
     */
    private String program;

    private int vmVersion;

    private int keyIndex;

    private WitnessComponent witnessComponent = new WitnessComponent();

    public BaseInput() {
        this.vmVersion = 1;
    }

    public abstract InputEntry convertInputEntry(Map<Hash, Entry> entryMap, int index);

    public abstract byte[] serializeInput() throws IOException;

    public abstract void buildWitness(String txID) throws Exception;

    @Override
    public String toString() {
        return Utils.serializer.toJson(this);
    }


    public AssetAmount getAssetAmount() {
        return new AssetAmount(new AssetID(this.assetId), this.amount);
    }

    public void setRootPrivateKey(String prvKey) {
        witnessComponent.setRootPrivateKey(prvKey);
    }

    public String getInputID() {
        return inputID;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public int getVmVersion() {
        return vmVersion;
    }

    public String getProgram() {
        return program;
    }

    public String getAssetId() {
        return assetId;
    }

    public long getAmount() {
        return amount;
    }

    public void setInputID(String inputID) {
        this.inputID = inputID;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    public WitnessComponent getWitnessComponent() {
        return witnessComponent;
    }
}
