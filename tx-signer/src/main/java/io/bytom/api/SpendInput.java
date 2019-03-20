package io.bytom.api;

import io.bytom.common.*;
import io.bytom.types.*;
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
    public InputEntry convertInputEntry(Map<Hash, Entry> entryMap, int index) {
        Program pro = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        AssetAmount assetAmount = this.getAssetAmount();
        Hash sourceID = new Hash(this.sourceID);
        ValueSource src = new ValueSource(sourceID, assetAmount, this.sourcePosition);

        OutputEntry prevout = new OutputEntry(src, pro, 0);
        Hash prevOutID = prevout.entryID();
        entryMap.put(prevOutID, prevout);
        return new Spend(prevOutID, index);
    }

    @Override
    public byte[] serializeInput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //assetVersion
        Utils.writeVarint(1, stream); //AssetVersion是否默认为1

        //inputCommitment
        ByteArrayOutputStream inputCommitStream = new ByteArrayOutputStream();
        //spend type flag
        Utils.writeVarint(SPEND_INPUT_TYPE, inputCommitStream);
        //spendCommitment
        ByteArrayOutputStream spendCommitSteam = new ByteArrayOutputStream();
        spendCommitSteam.write(Hex.decode(sourceID)); //计算muxID
        spendCommitSteam.write(Hex.decode(getAssetId()));
        Utils.writeVarint(getAmount(), spendCommitSteam);
        //sourcePosition
        Utils.writeVarint(sourcePosition, spendCommitSteam); //db中获取position
        //vmVersion
        Utils.writeVarint(1, spendCommitSteam); //db中获取vm_version
        //controlProgram
        Utils.writeVarStr(Hex.decode(getProgram()), spendCommitSteam);

        byte[] dataSpendCommit = spendCommitSteam.toByteArray();

        Utils.writeVarint(dataSpendCommit.length, inputCommitStream);
        inputCommitStream.write(dataSpendCommit);
        byte[] dataInputCommit = inputCommitStream.toByteArray();
        //inputCommit的length
        Utils.writeVarint(dataInputCommit.length, stream);
        stream.write(dataInputCommit);

        //inputWitness
        ByteArrayOutputStream witnessStream = new ByteArrayOutputStream();
        //arguments
        int witnessSize = witnessComponent.size();
        //arguments的length
        Utils.writeVarint(witnessSize, witnessStream);
        for (int i = 0; i < witnessSize; i++) {
            String witness = witnessComponent.getWitness(i);
            Utils.writeVarStr(Hex.decode(witness), witnessStream);
        }
        byte[] dataWitnessComponents = witnessStream.toByteArray();
        //witness的length
        Utils.writeVarint(dataWitnessComponents.length, stream);
        stream.write(dataWitnessComponents);
        return stream.toByteArray();
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