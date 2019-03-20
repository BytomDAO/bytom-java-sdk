package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;

public class UTXO {
    /**
     * id : fda38648c553386c56b2f1276b908061b5d812341f0a96921abad8b2b2f28044
     * amount : 1700000
     * address : tm1qhw9q89exmudkf9ecaxtnmv22fd8af0k07jq7u5
     * program : 0014bb8a039726df1b649738e9973db14a4b4fd4becf
     * change : true
     * highest : 139744
     * account_alias : wyjbtm
     * asset_id : ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
     * asset_alias : BTM
     * account_id : 0NNSS39M00A02
     * control_program_index : 26
     * source_id : 34bc595dff3d40c2bd644e0ea0234e843ef8e3aa0720013a2cb712362cc5933f
     * source_pos : 0
     * valid_height : 0
     * derive_rule : 0
     */

    @SerializedName("id")
    public String id;
    @SerializedName("amount")
    public long amount;
    @SerializedName("address")
    public String address;
    @SerializedName("program")
    public String program;
    @SerializedName("change")
    public boolean change;
    @SerializedName("highest")
    public int highest;
    @SerializedName("account_alias")
    public String accountAlias;
    @SerializedName("asset_id")
    public String assetId;
    @SerializedName("asset_alias")
    public String assetAlias;
    @SerializedName("account_id")
    public String accountId;
    @SerializedName("control_program_index")
    public int controlProgramIndex;
    @SerializedName("source_id")
    public String sourceId;
    @SerializedName("source_pos")
    public int sourcePos;
    @SerializedName("valid_height")
    public int validHeight;
    @SerializedName("derive_rule")
    public int deriveRule;

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static UTXO fromJson(String json) {
        return Utils.serializer.fromJson(json, UTXO.class);
    }

    public SpendInput toSpendAnnotatedInput() {
        SpendInput spendInput = new SpendInput();
        spendInput.setAmount(amount);
        spendInput.setProgram(program);
        spendInput.setChange(change);
        spendInput.setAssetId(assetId);
        spendInput.setControlProgramIndex(controlProgramIndex);
        spendInput.setSourceId(sourceId);
        spendInput.setSourcePosition(sourcePos);
        return spendInput;
    }
}
