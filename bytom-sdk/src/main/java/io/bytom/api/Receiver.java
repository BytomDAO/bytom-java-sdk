package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.JSONException;

/**
 * Receivers are used to facilitate payments between accounts on different
 * cores. They contain a control program and an expiration date. In the future,
 * more payment-related metadata may be placed here.
 * <p>
 * Receivers are typically created under accounts via the
 * {@link Account.ReceiverBuilder} class.
 */
public class Receiver {

    @SerializedName("address")
    public String address;
    /**
     * Hex-encoded string representation of the control program.
     */
    @SerializedName("control_program")
    public String controlProgram;


    /**
     * Serializes the receiver into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * Deserializes a Receiver from JSON.
     *
     * @param json a JSON-serialized Receiver object
     * @return the deserialized Receiver object
     * @throws JSONException Raised if the provided string is not valid JSON.
     */
    public static Receiver fromJson(String json) throws JSONException {
        try {
            return Utils.serializer.fromJson(json, Receiver.class);
        } catch (IllegalStateException e) {
            throw new JSONException("Unable to parse serialized receiver: " + e.getMessage());
        }
    }
}
