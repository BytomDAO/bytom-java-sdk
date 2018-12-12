package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;

import java.util.List;

public class Template {
    /**
     * A hex-encoded representation of a transaction template.
     */
    @SerializedName("raw_transaction")
    public String rawTransaction;

    /**
     * The list of signing instructions for inputs in the transaction.
     */
    @SerializedName("signing_instructions")
    public List<SigningInstruction> signingInstructions;

    /**
     * For core use only.
     */
    @SerializedName("local")
    private boolean local;

    /**
     * False (the default) makes the transaction "final" when signing, preventing
     * further changes - the signature program commits to the transaction's signature
     * hash. True makes the transaction extensible, committing only to the elements in
     * the transaction so far, permitting the addition of new elements.
     */
    @SerializedName("allow_additional_actions")
    private boolean allowAdditionalActions;

    /**
     * allowAdditionalActions causes the transaction to be signed so that it can be
     * used as a base transaction in a multiparty trade flow. To enable this setting,
     * call this method after building the transaction, but before sending it to the
     * signer.
     * <p>
     * All participants in a multiparty trade flow should call this method except for
     * the last signer. Do not call this option if the transaction is complete, i.e.
     * if it will not be used as a base transaction.
     *
     * @return updated transaction template
     */
    public Template allowAdditionalActions() {
        this.allowAdditionalActions = true;
        return this;
    }

    /**
     * A single signing instruction included in a transaction template.
     */
    public static class SigningInstruction {
        /**
         * The input's position in a transaction's list of inputs.
         */
        public int position;

        /**
         * A list of components used to coordinate the signing of an input.
         */
        @SerializedName("witness_components")
        public WitnessComponent[] witnessComponents;
    }

    /**
     * A single witness component, holding information that will become the input
     * witness.
     */
    public static class WitnessComponent {
        /**
         * The type of witness component.<br>
         * Possible types are "data" and "raw_tx_signature".
         */
        public String type;

        /**
         * Data to be included in the input witness (null unless type is "data").
         */
        public String value;

        /**
         * The number of signatures required for an input (null unless type is
         * "signature").
         */
        public int quorum;

        /**
         * The list of keys to sign with (null unless type is "signature").
         */
        public KeyID[] keys;

        /**
         * The list of signatures made with the specified keys (null unless type is
         * "signature").
         */
        public String[] signatures;
    }

    /**
     * A class representing a derived signing key.
     */
    public static class KeyID {
        /**
         * The extended public key associated with the private key used to sign.
         */
        public String xpub;

        /**
         * The derivation path of the extended public key.
         */
        @SerializedName("derivation_path")
        public String[] derivationPath;
    }

    /**
     * Serializes the Address into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static Template fromJson(String json) {
        return Utils.serializer.fromJson(json, Template.class);
    }

}
