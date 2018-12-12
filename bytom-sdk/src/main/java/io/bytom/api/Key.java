package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.ParameterizedTypeImpl;
import io.bytom.common.Utils;
import io.bytom.exception.*;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>Key Class</h1>
 *
 * @version 1.0
 * @since   2018-05-18
 */
public class Key {

    @SerializedName("alias")
    public String alias;

    @SerializedName("xpub")
    public String xpub;

    @SerializedName("file")
    public String file;

    private static Logger logger = Logger.getLogger(Key.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * Create a key object
     *
     * @param client client object that makes requests to the core
     * @param builder  Key.Builder object that make parameters
     * @return Key a key object
     * @throws BytomException BytomException
     */
    public static Key create(Client client, Builder builder) throws BytomException {
        Key key = client.request("create-key", builder, Key.class);
        return key;
    }

    /**
     * List all key objects
     *
     * @param client client object that makes requests to the core
     * @return a list of key object
     * @throws BytomException BytomException
     */
    public static List<Key> list(Client client) throws BytomException {
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Key.class});
        List<Key> keyList = client.request("list-keys", null, listType);

        logger.info("list-key:");
        logger.info("size of key:"+keyList.size());
        logger.info(keyList);

        return keyList;
    }

    /**
     * delete a key
     *
     * @param client client object that makes requests to the core
     * @param xpub the xpub is given when creates key
     * @param password the password is given when creates key
     * @throws BytomException BytomException
     */
    public static void delete(Client client, String xpub, String password) throws BytomException {
        Map<String, String> req = new HashMap<String, String>();
        req.put("xpub", xpub);
        req.put("password", password);
        client.request("delete-key", req);
        logger.info("delete-key successfully.");
    }

    /**
     * reset password
     *
     * @param client client object that makes requests to the core
     * @param xpub the xpub is given when creates key
     * @param oldPwd the old password is given when creates key
     * @param newPwd new password used to set
     * @throws BytomException BytomException
     */
    public static void resetPassword(Client client, String xpub, String oldPwd, String newPwd) throws BytomException {
        Map<String, String> req = new HashMap<>();
        req.put("xpub", xpub);
        req.put("old_password", oldPwd);
        req.put("new_password", newPwd);
        client.request("reset-key-password", req);
    }

    /**
     * <h1>Key.Builder Class</h1>
     */
    public static class Builder {
        /**
         * User specified, unique identifier.
         */
        public String alias;

        /**
         * User specified.
         */
        public String password;

        /**
         * Sets the alias on the builder object.
         *
         * @param alias alias
         * @return updated builder object
         */
        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        /**
         * Sets the alias on the builder object.
         *
         * @param password password
         * @return updated builder object
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

    }

    @Override
    public String toString() {
        return "Key{" +
                "alias='" + alias + '\'' +
                ", xpub='" + xpub + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
