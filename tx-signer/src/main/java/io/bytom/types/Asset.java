package io.bytom.types;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.bytom.common.DerivePrivateKey;
import io.bytom.common.DeriveXpub;
import io.bytom.util.SHA3Util;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Asset {

    /**
     * type : asset
     * xpubs : ["135022d3c218e949de69902459ba4233c8a21ecf2fde00ea592876e3138f1bf09dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4"]
     * quorum : 1
     * id : 821ab3d068a218e2ec23c28f50786c7f52412394c52bf9f9319d58bd3e459fa3
     * alias : TEST7
     * definition : {"decimals":8,"description":{},"name":"","symbol":""}
     * key_index : 14
     * derive_rule : 0
     * vm_version : 1
     * issue_program : ae202dcda0296db65a471f5e111ed1e61afec158f1ac6be8f8c23c6ccaefa27c5dfa5151ad
     * raw_definition_byte : 7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d
     */

    public String type;
    public int quorum;
    public String id;
    public String alias;
    public Definition definition;
    public int keyIndex;
    public int deriveRule;
    public int vmVersion;
    public String issueProgram;
    public String rawDefinitionByte;
    public String xpubs;

    public Asset() {

    }

    public Asset(String rawDefinitionByte, int keyIndex, String rootKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String issueProgram = computeIssueProgram(rootKey, keyIndex);
        String assetID = computeAssetID(issueProgram, 1, rawDefinitionByte);
        this.keyIndex = keyIndex;
        this.id = assetID;
        this.rawDefinitionByte = rawDefinitionByte;
        this.issueProgram = issueProgram;
    }

    public Asset CreateAsset(Definition definitionp, int keyIndex, String rootKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String rawDefinitionByte = computeRawDefinition(definitionp);
        String issueProgram = computeIssueProgram(rootKey, keyIndex);
        String assetID = computeAssetID(issueProgram, 1, rawDefinitionByte);
        this.keyIndex = keyIndex;
        this.id = assetID;
        this.issueProgram = issueProgram;
        this.rawDefinitionByte = rawDefinitionByte;
        return this;
    }

    private String computeIssueProgram(String rootKey, int keyIndex) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] derivePrivateKey = DerivePrivateKey.derivePrivateKey(rootKey, keyIndex);
        byte[] deriveXpub = DeriveXpub.deriveXpub(derivePrivateKey);

        String issueProgram = "ae20" + Hex.toHexString(deriveXpub).substring(0, 64) + "5151ad";
        return issueProgram;
    }

    private String computeAssetID(String issuanceProgram, int VMVersion, String rawDefinitionByte) {
        byte[] rawDefinitionBytes = SHA3Util.hashSha256(Hex.decode(rawDefinitionByte));
        Hash assetDefineHash = new Hash(rawDefinitionBytes);
        Program pro = new Program(1, Hex.decode(issuanceProgram));
        AssetDefinition assetDefinition = new AssetDefinition(assetDefineHash, pro);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        assetDefinition.writeForHash(bo);
        byte[] assetIDBytes = SHA3Util.hashSha256(bo.toByteArray());
        String assetID = Hex.toHexString(assetIDBytes);
        return assetID;
    }

    private String computeRawDefinition(final Definition definition) {
        Map<String, Object> map = new LinkedHashMap<String, Object>() {
            {
                put("decimals", definition.getDecimals());
                put("description", definition.getDescription());
                put("name", definition.getName());
                put("symbol", definition.getSymbol());
            }
        };
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(map, new TypeToken<Map<String, Object>>() {
        }.getType());
        String rawDefinition = Hex.toHexString(json.getBytes());
        return rawDefinition;
    }


    public static class Definition {
        private int decimals;
        private Object description;
        private String name;
        private String symbol;

        public int getDecimals() {
            return decimals;
        }

        public void setDecimals(int decimals) {
            this.decimals = decimals;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
    }

}
