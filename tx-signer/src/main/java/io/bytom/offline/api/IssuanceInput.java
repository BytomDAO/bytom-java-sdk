package io.bytom.offline.api;

import io.bytom.offline.common.DerivePrivateKey;
import io.bytom.offline.common.ExpandedPrivateKey;
import io.bytom.offline.common.Signer;
import io.bytom.offline.common.Utils;
import io.bytom.offline.types.*;
import io.bytom.offline.util.AssetIdUtil;
import io.bytom.offline.util.SHA3Util;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;

public class IssuanceInput extends BaseInput {

    private String nonce;

    private String rawAssetDefinition;

    public IssuanceInput(){

    }

    public IssuanceInput(String rawAssetDefinition,String issuanceProgram) {
        this.rawAssetDefinition = rawAssetDefinition;
        this.setProgram(issuanceProgram);
        this.setAssetId(AssetIdUtil.computeAssetID(rawAssetDefinition,issuanceProgram));
    }

    public IssuanceInput(String assetID, Long amount, String issuanceProgram) {
        this.setAssetId(assetID);
        this.setAmount(amount);
        this.setProgram(issuanceProgram);
    }

    public IssuanceInput(String assetID, Long amount, String issuanceProgram, String nonce, String rawAssetDefinition) {
        this(assetID, amount, issuanceProgram);
        this.nonce = nonce;
        this.rawAssetDefinition = rawAssetDefinition;
    }

    @Override
    public InputEntry toInputEntry(Map<Hash, Entry> entryMap, int index) {
        if (this.nonce == null) {
            SecureRandom sr = new SecureRandom();
            byte[] randBytes = new byte[8];
            sr.nextBytes(randBytes);
            this.nonce = Hex.toHexString(randBytes);
        }

        Hash nonceHash = new Hash(SHA3Util.hashSha256(Hex.decode(this.nonce)));
        Hash assetDefHash = new Hash(this.rawAssetDefinition);
        AssetAmount value = this.getAssetAmount();

        Program program = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        return new Issue(nonceHash, value, index, new AssetDefinition(assetDefHash, program));
    }

    @Override
    public void buildWitness(String txID) throws Exception {
        String rootPrivateKey = witnessComponent.getRootPrivateKey();
        byte[] childPrivateKey = DerivePrivateKey.bip32derivePrvKey(rootPrivateKey, getKeyIndex(), AssetKeySpace);

        byte[] message = Utils.hashFn(Hex.decode(getInputID()), Hex.decode(txID));
        byte[] expandedPrivateKey = ExpandedPrivateKey.expandedPrivateKey(childPrivateKey);
        byte[] sig = Signer.ed25519InnerSign(expandedPrivateKey, message);

        witnessComponent.appendWitness(Hex.toHexString(sig));
    }

    @Override
    public byte[] serializeInputCommitment() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarint(ISSUANCE_INPUT_TYPE, stream);
        Utils.writeVarStr(Hex.decode(nonce), stream);
        if (this.getAssetId()==null){
            this.setAssetId(AssetIdUtil.computeAssetID(rawAssetDefinition,this.getProgram()));
        }
        stream.write(Hex.decode(this.getAssetId()));
        Utils.writeVarint(getAmount(), stream);
        return stream.toByteArray();
    }

    @Override
    public byte[] serializeInputWitness() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarStr(Hex.decode(rawAssetDefinition), stream);
        // vm version
        Utils.writeVarint(1, stream);
        Utils.writeVarStr(Hex.decode(getProgram()), stream);
        Utils.writeVarList(witnessComponent.toByteArray(), stream);
        return stream.toByteArray();
    }

    @Override
    public void validate() {
        if (nonce == null) {
            throw new IllegalArgumentException("the nonce of issuance input must be specified.");
        }
        if (rawAssetDefinition == null) {
            throw new IllegalArgumentException("the nonce of issuance input must be specified.");
        }

        if (this.getProgram() == null) {
            throw new IllegalArgumentException("the program of issuance must be specified.");
        }
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getRawAssetDefinition() {
        return rawAssetDefinition;
    }

    public void setRawAssetDefinition(String rawAssetDefinition) {
        this.rawAssetDefinition = rawAssetDefinition;
    }
}
