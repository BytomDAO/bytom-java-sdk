package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.offline.common.ParameterizedTypeImpl;
import io.bytom.offline.common.Utils;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>AccessToken Class</h1>
 */
public class AccessToken {
    /**
     * Token id
     */
    public String id;
    /**
     * Token token
     */
    public String token;
    /**
     * Token type
     */
    public String type;
    /**
     * create time of token
     */
    @SerializedName(value = "created_at", alternate = {"create"})
    public String createTime;

    private static Logger logger = Logger.getLogger(AccessToken.class);

    /**
     * Serializes the AccessToken into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the AccessToken object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static class Builder {
        /**
         * id of Token
         */
        public String id;
        /**
         * type of Token
         */
        public String type;

        public Builder() {
        }

        /**
         * @param id the id to set
         * @return Builder
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * @param type the type to set, possibly null
         * @return Builder
         */
        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Call create-access-token api
         *
         * @param client client object that makes requests to the core
         * @return AccessToken object
         * @throws BytomException
         */
        public AccessToken create(Client client) throws BytomException {
            AccessToken accessToken = client.request("create-access-token", this, AccessToken.class);

            logger.info("create-access-token:");
            logger.info(accessToken.toJson());

            return accessToken;
        }
    }

    /**
     * Call check-access-token api
     *
     * @param client client object that makes requests to the core
     * @param id     id
     * @param secret secret
     * @throws BytomException
     */
    public static void check(Client client, String id, String secret) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("id", id);
        req.put("secret", secret);
        // add a native control
        if (client.getUrl().equals("http://127.0.0.1:9888") ||
                client.getUrl().equals("http://127.0.0.1:9888/")) {
            client.request("check-access-token", req);
            logger.info("check-access-token successfully.");
        } else {
            logger.info("this is a native method.");
        }
    }

    /**
     * Call delete-access-token api
     * native method, can't rpc
     *
     * @param client client object that makes requests to the core
     * @param id     id
     * @throws BytomException
     */
    public static void delete(Client client, String id) throws BytomException {
        Map<String, Object> req = new HashMap<String, Object>();
        req.put("id", id);
        // add a native control
        if (client.getUrl().equals("http://127.0.0.1:9888") ||
                client.getUrl().equals("http://127.0.0.1:9888/")) {
            client.request("delete-access-token", req);
            logger.info("delete-access-token.");
        } else {
            logger.info("this is a native method.");
        }
    }

    /**
     * Call list-access-tokens api.<br>
     * native method, can't rpc
     *
     * @param client client object that makes requests to the core
     * @return list of AccessToken objects
     * @throws BytomException
     */
    public static List<AccessToken> list(Client client) throws BytomException {
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{AccessToken.class});
        List<AccessToken> accessTokenList = null;
        if (client.getUrl().equals("http://127.0.0.1:9888") ||
                client.getUrl().equals("http://127.0.0.1:9888/")) {
            accessTokenList = client.request("list-access-tokens", null, listType);

            logger.info("list-access-tokens:");
            logger.info("size of accessTokenList:" + accessTokenList.size());
            logger.info(accessTokenList.get(0).toJson());
        } else {
            logger.info("this is a native method.");
        }

        return accessTokenList;
    }

}
