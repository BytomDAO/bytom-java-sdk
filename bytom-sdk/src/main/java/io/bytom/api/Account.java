package io.bytom.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import io.bytom.common.ParameterizedTypeImpl;
import io.bytom.common.Utils;
import io.bytom.exception.*;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <h1>Account Class</h1>
 */
public class Account {

    @SerializedName("id")
    public String id;

    @SerializedName("alias")
    public String alias;

    @SerializedName("key_index")
    public Integer key_index;

    @SerializedName("quorum")
    public Integer quorum;

    @SerializedName("xpubs")
    public List<String> xpubs;

    private static Logger logger = Logger.getLogger(Account.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * create-account
     *
     * @param client client object that makes requests to the core
     * @param builder Account.Builder to make parameters
     * @return Account return a account object
     * @throws BytomException BytomException
     */
    public static Account create(Client client, Builder builder) throws BytomException {
        Account account = client.request("create-account", builder, Account.class);
        logger.info("create-account");
        logger.info(account.toString());
        return account;
    }

    /**
     * list-accounts
     *
     * @param client client object that makes requests to the core
     * @return return a list of account object
     * @throws BytomException BytomException
     */
    public static List<Account> list(Client client) throws BytomException {
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Account.class});
        List<Account> accountList = client.request("list-accounts", null, listType);
        logger.info("list-accounts:");
        logger.info("size of accountList:"+accountList.size());
        logger.info(accountList);
        return accountList;
    }

    /**
     * delete-account
     * @param client client object that makes requests to the core
     * @param account_info account_info
     * @throws BytomException BytomException
     */
    public static void delete(Client client, String account_info) throws BytomException {
        Map<String, String> req = new HashMap<>();
        req.put("account_info", account_info);
        client.request("delete-account", req);
    }

    public static class Builder {

        public List<String> root_xpubs;

        public String alias;

        public Integer quorum;

        /**
         * add a xpub to root_xpubs
         *
         * @param xpub xpub
         * @return this Builder object
         */
        public Builder addRootXpub(String xpub) {
            this.root_xpubs.add(xpub);
            return this;
        }

        /**
         * set xpubs to root_xpubs
         *
         * @param xpubs xpubs
         * @return this Builder object
         */
        public Builder setRootXpub(List<String> xpubs) {
            this.root_xpubs = new ArrayList<>(xpubs);
            return this;
        }

        /**
         * set alias to alias
         * @param alias alias
         * @return this Builder object
         */
        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        /**
         * set quorum to quorum
         *
         * @param quorum quorum
         * @return this Builder object
         */
        public Builder setQuorum(Integer quorum) {
            this.quorum = quorum;
            return this;
        }

    }

    /**
     * Use this class to create a {@link Receiver} under an account.
     */
    public static class ReceiverBuilder {

        @SerializedName("account_alias")
        public String accountAlias;

        @SerializedName("account_id")
        public String accountId;

        /**
         * Specifies the account under which the receiver is created. You must use
         * this method or @{link ReceiverBuilder#setAccountId}, but not both.
         *
         * @param alias the unique alias of the account
         * @return this ReceiverBuilder object
         */
        public ReceiverBuilder setAccountAlias(String alias) {
            this.accountAlias = alias;
            return this;
        }

        /**
         * Specifies the account under which the receiver is created. You must use
         * this method or @{link ReceiverBuilder#setAccountAlias}, but not both.
         *
         * @param id the unique ID of the account
         * @return this ReceiverBuilder object
         */
        public ReceiverBuilder setAccountId(String id) {
            this.accountId = id;
            return this;
        }

        /**
         * Creates a single Receiver object under an account.
         *
         * @param client the client object providing access to an instance of Chain Core
         * @return a new Receiver object
         * @throws APIException          This exception is raised if the api returns errors while creating the control programs.
         * @throws BadURLException       This exception wraps java.net.MalformedURLException.
         * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
         * @throws HTTPException         This exception is raised when errors occur making http requests.
         * @throws JSONException         This exception is raised due to malformed json requests or responses.
         */
        public Receiver create(Client client) throws BytomException {
            Gson gson = new Gson();
            Receiver receiver = client.request(
                    "create-account-receiver", this, Receiver.class);
            logger.info("create-account-receiver:");
            logger.info(receiver.toJson());
            return receiver;
        }


        @Override
        public String toString() {
            return "ReceiverBuilder{" +
                    "accountAlias='" + accountAlias + '\'' +
                    ", accountId='" + accountId + '\'' +
                    '}';
        }
    }

    /**
     * Address Class
     */
    public static class Address {
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

    /**
     * Use this class to create a {@link Address} under an account.
     */
    public static class AddressBuilder {

        @SerializedName("account_alias")
        public String accountAlias;

        @SerializedName("account_id")
        public String accountId;

        /**
         * Specifies the account under which the address is created. You must use
         * this method or @{link AddressBuilder#setAccountId}, but not both.
         *
         * @param alias the unique alias of the account
         * @return this AddressBuilder object
         */
        public AddressBuilder setAccountAlias(String alias) {
            this.accountAlias = alias;
            return this;
        }

        /**
         * Specifies the account under which the address is created. You must use
         * this method or @{link AddressBuilder#setAccountAlias}, but not both.
         *
         * @param id the unique ID of the account
         * @return this AddressBuilder object
         */
        public AddressBuilder setAccountId(String id) {
            this.accountId = id;
            return this;
        }

        /**
         * list-addresses
         * @param client client object that makes requests to the core
         * @return list of address object
         * @throws BytomException BytomException
         */
        public List<Address> list(Client client) throws BytomException {
            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Address.class});
            List<Address> addressList = client.request("list-addresses", this, listType);
            logger.info("list-addresses:");
            logger.info("size of addressList:" + addressList.size());
            logger.info(addressList.get(0).toJson());

            return addressList;
        }

        /**
         * validate-address
         * @param client client object that makes requests to the core
         * @param address an address string
         * @return an address object
         * @throws BytomException BytomException
         */
        public Address validate(Client client, String address) throws BytomException {
            Map<String, Object> req = new HashMap<>();
            req.put("address", address);
            Address addressResult = client.request("validate-address", req, Address.class);
            logger.info("validate-address:");
            logger.info(addressResult.toJson());

            return addressResult;
        }

        @Override
        public String toString() {
            return "AddressBuilder{" +
                    "accountAlias='" + accountAlias + '\'' +
                    ", accountId='" + accountId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", key_index=" + key_index +
                ", quorum=" + quorum +
                ", xpubs=" + xpubs +
                '}';
    }
}
