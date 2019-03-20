package io.bytom.api;

import io.bytom.common.DerivePrivateKey;
import io.bytom.common.ExpandedPrivateKey;
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

    private String assetDefinition;

    public IssuanceInput() {}

    @Override
    public InputEntry convertInputEntry(Map<Hash, Entry> entryMap, int index) {
        if (this.nonce == null) {
            SecureRandom sr = new SecureRandom();
            byte[] randBytes = new byte[8];
            sr.nextBytes(randBytes);
            this.nonce = Hex.toHexString(randBytes);
        }

        Hash nonceHash = new Hash(SHA3Util.hashSha256(Hex.decode(this.nonce)));
        Hash assetDefHash = new Hash(this.assetDefinition);
        AssetAmount value = this.getAssetAmount();

        Issue issuance = new Issue(nonceHash, value, index);
        Program pro = new Program(this.getVmVersion(), Hex.decode(this.getProgram()));
        issuance.assetDefinition = new AssetDefinition(assetDefHash, pro);
        return issuance;
    }

    @Override
    public byte[] serializeInput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //assetVersion
        Utils.writeVarint(1, stream);
        //写入 type nonce assetId amount
        ByteArrayOutputStream issueInfo = new ByteArrayOutputStream();
        //写入 input.type==00 issue
        Utils.writeVarint(ISSUANCE_INPUT_TYPE, issueInfo);
        //写入 8个字节的随机数
        Utils.writeVarStr(Hex.decode(nonce), issueInfo);
        issueInfo.write(Hex.decode(getAssetId()));
        Utils.writeVarint(getAmount(), issueInfo);
        stream.write(issueInfo.toByteArray().length);
        stream.write(issueInfo.toByteArray());

        ByteArrayOutputStream issueInfo1 = new ByteArrayOutputStream();
        //未知
        Utils.writeVarint(1, issueInfo1);
        //写入assetDefine
        Utils.writeVarStr(Hex.decode(assetDefinition), issueInfo1);
        //vm.version
        Utils.writeVarint(1, issueInfo1);
        //controlProgram
        Utils.writeVarStr(Hex.decode(getProgram()), issueInfo1);

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
            issueInfo1.write(witnessStream.toByteArray());
        }
        stream.write(issueInfo1.toByteArray().length - 1);
        stream.write(issueInfo1.toByteArray());
        return stream.toByteArray();
    }

    @Override
    public void buildWitness(String txID) throws Exception {
        String rootPrivateKey = getWitnessComponent().getRootPrivateKey();
        byte[] childPrivateKey = DerivePrivateKey.bip32derivePrvKey(rootPrivateKey, getKeyIndex(), TransactionSigner.AssetKeySpace);

        byte[] message = Utils.hashFn(Hex.decode(getInputID()), Hex.decode(txID));
        byte[] expandedPrivateKey = ExpandedPrivateKey.expandedPrivateKey(childPrivateKey);
        byte[] sig = Signer.ed25519InnerSign(expandedPrivateKey, message);

        getWitnessComponent().appendWitness(Hex.toHexString(sig));
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public void setAssetDefinition(String assetDefinition) {
        this.assetDefinition = assetDefinition;
    }
}
