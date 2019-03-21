package io.bytom.offline.api;

import io.bytom.offline.common.*;
import io.bytom.offline.types.*;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class SpendInput extends BaseInput {

    private String sourceID;

    private Integer sourcePosition;

    private Boolean change;

    private Integer controlProgramIndex;

    private BIPProtocol bipProtocol = BIPProtocol.BIP44;

    public SpendInput() {}

    public SpendInput(String assetID, long amount, String controlProgram) {
        this.setAssetId(assetID);
        this.setAmount(amount);
        this.setProgram(controlProgram);
    }

    public SpendInput(String assetID, long amount, String controlProgram, String sourceID, Integer sourcePosition) {
        this(assetID, amount, controlProgram);
        this.sourceID = sourceID;
        this.sourcePosition = sourcePosition;
    }

    @Override
    public InputEntry toInputEntry(Map<Hash, Entry> entryMap, int index) {
        Program pro = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        AssetAmount assetAmount = this.getAssetAmount();
        Hash sourceID = new Hash(this.sourceID);
        ValueSource src = new ValueSource(sourceID, assetAmount, this.sourcePosition);

        OutputEntry prevOut = new OutputEntry(src, pro, 0);
        Hash prevOutID = prevOut.entryID();
        entryMap.put(prevOutID, prevOut);
        return new Spend(prevOutID, index);
    }

    @Override
    public void buildWitness(String txID) throws Exception {
        String rootPrvKey = witnessComponent.getRootPrivateKey();

        byte[] privateChild;
        if (bipProtocol == BIPProtocol.BIP44) {
            privateChild = DerivePrivateKey.bip44derivePrvKey(rootPrvKey, AccountKeySpace, change, controlProgramIndex);
        } else {
            privateChild = DerivePrivateKey.bip32derivePrvKey(rootPrvKey, getKeyIndex(), AccountKeySpace, controlProgramIndex);
        }

        byte[] message = Utils.hashFn(Hex.decode(getInputID()), Hex.decode(txID));
        byte[] expandedPrivateKey = ExpandedPrivateKey.expandedPrivateKey(privateChild);
        byte[] sig = Signer.ed25519InnerSign(expandedPrivateKey, message);

        witnessComponent.appendWitness(Hex.toHexString(sig));

        byte[] deriveXpub = DeriveXpub.deriveXpub(privateChild);
        String pubKey = Hex.toHexString(deriveXpub).substring(0, 64);

        witnessComponent.appendWitness(pubKey);
    }

    @Override
    public byte[] serializeInputCommitment() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarint(SPEND_INPUT_TYPE, stream);

        ByteArrayOutputStream spendCommitSteam = new ByteArrayOutputStream();
        spendCommitSteam.write(Hex.decode(sourceID));
        spendCommitSteam.write(Hex.decode(getAssetId()));
        Utils.writeVarint(getAmount(), spendCommitSteam);
        Utils.writeVarint(sourcePosition, spendCommitSteam);
        // vm version
        Utils.writeVarint(1, spendCommitSteam);
        Utils.writeVarStr(Hex.decode(getProgram()), spendCommitSteam);
        Utils.writeExtensibleString(spendCommitSteam.toByteArray(), stream);
        return stream.toByteArray();
    }

    @Override
    public byte[] serializeInputWitness() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarList(witnessComponent.toByteArray(), stream);
        return stream.toByteArray();
    }

    @Override
    public void validate() {
        super.validate();
        if (sourceID == null) {
            throw new IllegalArgumentException("the source id of spend input must be specified.");
        }
        if (sourcePosition == null) {
            throw new IllegalArgumentException("the source position of spend input must be specified.");
        }
        if (change == null) {
            throw new IllegalArgumentException("the change of spend input must be specified.");
        }
        if (controlProgramIndex == null) {
            throw new IllegalArgumentException("the control program index of spend input must be specified.");
        }
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public void setSourcePosition(int sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public void setControlProgramIndex(int controlProgramIndex) {
        this.controlProgramIndex = controlProgramIndex;
    }

    public void setBipProtocol(BIPProtocol bipProtocol) {
        this.bipProtocol = bipProtocol;
    }
}