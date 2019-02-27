package io.bytom.api;

import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;

import java.util.HashMap;

public class SubmitTransaction {

    public static SubmitResponse submitRawTransaction(Client client, String rawTransaction) throws BytomException {
        HashMap<String, Object> body = new HashMap<>();
        body.put("raw_transaction", rawTransaction);
        return client.request("submit-transaction", body, SubmitResponse.class);
    }

    public static class SubmitResponse {
        /**
         * The transaction id.
         */
        public String tx_id;

        public String toJson() {
            return Utils.serializer.toJson(this);
        }

    }
}
