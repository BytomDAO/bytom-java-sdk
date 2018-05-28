package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

public class Message {

    @SerializedName("derived_xpub")
    public String derivedXpub;
    @SerializedName("signature")
    public String signature;

    private static Logger logger = Logger.getLogger(Message.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static class SignBuilder {

        public String address;
        public String message;
        public String password;

        public SignBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public SignBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SignBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Call sign-message api
         *
         * @param client
         * @return
         * @throws BytomException
         */
        public Message sign(Client client) throws BytomException {
            Message message = client.request("sign-message", this, Message.class);

            logger.info("sign-message:");
            logger.info(message.toJson());

            return message;
        }

    }

    public static class VerifyBuilder {

        /**
         * address, address for account.
         */
        public String address;

        /**
         * derived_xpub, derived xpub.
         */
        @SerializedName("derived_xpub")
        public String derivedXpub;

        /**
         * message, message for signature by derived_xpub.
         */
        public String message;

        /**
         * signature, signature for message.
         */
        public String signature;


        public VerifyBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public VerifyBuilder setDerivedXpub(String derivedXpub) {
            this.derivedXpub = derivedXpub;
            return this;
        }

        public VerifyBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public VerifyBuilder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        /**
         * Call verify-message api
         * @param client
         * @return
         * @throws BytomException
         */
        public Boolean verifyMessage(Client client) throws BytomException {
            Boolean result = client.requestGet("verify-message", this, "result", Boolean.class);

            logger.info("verify-message:");
            logger.info(result);

            return result;
        }

    }


}
