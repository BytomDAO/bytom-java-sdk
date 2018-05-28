package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.JSONException;

public class Address {
    @SerializedName("account_alias")
    public String accountAlias;

    @SerializedName("account_id")
    public String accountId;

    @SerializedName("address")
    public String address;

    @SerializedName("change")
    public Boolean change;

    @SerializedName("vaild")
    public Boolean vaild;

    @SerializedName("is_local")
    public Boolean is_local;

    /**
     * Serializes the Address into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * Deserializes a Address from JSON.
     *
     * @param json a JSON-serialized Receiver object
     * @return the deserialized Receiver object
     * @throws JSONException Raised if the provided string is not valid JSON.
     */
    public static Address fromJson(String json) throws JSONException {
        try {
            return Utils.serializer.fromJson(json, Address.class);
        } catch (IllegalStateException e) {
            throw new JSONException("Unable to parse serialized receiver: " + e.getMessage());
        }
    }

}
