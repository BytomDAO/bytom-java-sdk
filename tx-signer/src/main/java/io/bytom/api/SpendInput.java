package io.bytom.api;

import io.bytom.common.*;
import io.bytom.types.*;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class SpendInput extends BaseInput {

    private String sourceId;

    private long sourcePosition;

    private boolean change;

    private int controlProgramIndex;

    private BIPProtocol bipProtocol;

    public SpendInput() {}

    public SpendInput(String assetID, long amount, String controlProgram) {
        this.setAssetId(assetID);
        this.setAmount(amount);
        this.setProgram(controlProgram);
        this.bipProtocol = BIPProtocol.BIP44;
    }

    @Override
    public InputEntry convertInputEntry(Map<Hash, Entry> entryMap, int index) {
        Program pro = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        AssetAmount assetAmount = this.getAssetAmount();
        Hash sourceID = new Hash(this.sourceId);
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
        spendCommitSteam.write(Hex.decode(sourceId)); //计算muxID
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
        if (null != getWitnessComponent()) {
            ByteArrayOutputStream witnessStream = new ByteArrayOutputStream();
            //arguments
            int witnessSize = getWitnessComponent().size();
            //arguments的length
            Utils.writeVarint(witnessSize, witnessStream);
            for (int i = 0; i < witnessSize; i++) {
                String witness = getWitnessComponent().getWitness(i);
                Utils.writeVarStr(Hex.decode(witness), witnessStream);
            }
            byte[] dataWitnessComponents = witnessStream.toByteArray();
            //witness的length
            Utils.writeVarint(dataWitnessComponents.length, stream);
            stream.write(dataWitnessComponents);
        }
        return stream.toByteArray();
    }

    @Override
    public void buildWitness(String txID) throws Exception {
        String rootPrvKey = getWitnessComponent().getRootPrivateKey();

        byte[] privateChild;
        if (bipProtocol == BIPProtocol.BIP44) {
            privateChild = DerivePrivateKey.bip44derivePrvKey(rootPrvKey, TransactionSigner.AccountKeySpace, change, controlProgramIndex);
        } else {
            privateChild = DerivePrivateKey.bip32derivePrvKey(rootPrvKey, getKeyIndex(), TransactionSigner.AccountKeySpace, controlProgramIndex);
        }

        byte[] message = Utils.hashFn(Hex.decode(getInputID()), Hex.decode(txID));
        byte[] expandedPrivateKey = ExpandedPrivateKey.expandedPrivateKey(privateChild);
        byte[] sig = Signer.ed25519InnerSign(expandedPrivateKey, message);

        getWitnessComponent().appendWitness(Hex.toHexString(sig));

        byte[] deriveXpub = DeriveXpub.deriveXpub(privateChild);
        String pubKey = Hex.toHexString(deriveXpub).substring(0, 64);

        getWitnessComponent().appendWitness(pubKey);
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setSourcePosition(long sourcePosition) {
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