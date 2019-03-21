package io.bytom.api;

import io.bytom.common.DerivePrivateKey;
import io.bytom.common.ExpandedPrivateKey;
import io.bytom.common.Signer;
import io.bytom.common.Utils;
import io.bytom.types.*;
import io.bytom.util.SHA3Util;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;

public class IssuanceInput extends BaseInput {

    private String nonce;

    private String rawAssetDefinition;

    public IssuanceInput() {}

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
    public InputEntry convertInputEntry(Map<Hash, Entry> entryMap, int index) {
        if (this.nonce == null) {
            SecureRandom sr = new SecureRandom();
            byte[] randBytes = new byte[8];
            sr.nextBytes(randBytes);
            this.nonce = Hex.toHexString(randBytes);
        }

        Hash nonceHash = new Hash(SHA3Util.hashSha256(Hex.decode(this.nonce)));
        Hash assetDefHash = new Hash(this.rawAssetDefinition);
        AssetAmount value = this.getAssetAmount();

        Program pro = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        return new Issue(nonceHash, value, index, new AssetDefinition(assetDefHash, pro));
    }

    @Override
    public byte[] serializeInput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //assetVersion
        Utils.writeVarint(1, stream);

        ByteArrayOutputStream issueInfo = new ByteArrayOutputStream();
        Utils.writeVarint(ISSUANCE_INPUT_TYPE, issueInfo);
        Utils.writeVarStr(Hex.decode(nonce), issueInfo);
        issueInfo.write(Hex.decode(getAssetId()));
        Utils.writeVarint(getAmount(), issueInfo);
        stream.write(issueInfo.toByteArray().length);
        stream.write(issueInfo.toByteArray());

        ByteArrayOutputStream issueInfo1 = new ByteArrayOutputStream();
        Utils.writeVarint(1, issueInfo1);
        Utils.writeVarStr(Hex.decode(rawAssetDefinition), issueInfo1);
        // vm version
        Utils.writeVarint(1, issueInfo1);
        Utils.writeVarStr(Hex.decode(getProgram()), issueInfo1);

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
        issueInfo1.write(witnessStream.toByteArray());
        stream.write(issueInfo1.toByteArray().length - 1);
        stream.write(issueInfo1.toByteArray());
        return stream.toByteArray();
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
    public void validate() {
        super.validate();
        if (nonce == null) {
            throw new IllegalArgumentException("the nonce of issuance input must be specified.");
        }
        if (rawAssetDefinition == null) {
            throw new IllegalArgumentException("the nonce of issuance input must be specified.");
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
