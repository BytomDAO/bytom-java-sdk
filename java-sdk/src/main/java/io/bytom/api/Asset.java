package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.offline.common.ParameterizedTypeImpl;
import io.bytom.offline.common.Utils;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <h1>Asset Class</h1>
 * <br>
 * String - id, asset id.<br>
 * String - alias, name of the asset.<br>
 * String - issuance_program, control program of the issuance of asset.<br>
 * Array of Object - keys, information of asset pubkey.<br>
 * String - definition, definition of asset.<br>
 * Integer - quorum, threshold of keys that must sign a transaction to spend asset units controlled by the account.<br>
 */
public class Asset {

    /**
     * Globally unique identifier of the asset.<br>
     * Asset version 1 specifies the asset id as the hash of:<br>
     * - the asset version<br>
     * - the asset's issuance program<br>
     * - the core's VM version<br>
     * - the hash of the network's initial block
     */
    public String id;

    /**
     * User specified, unique identifier.
     */
    public String alias;

    /**
     * A program specifying a predicate to be satisfied when issuing the asset.
     */
    @SerializedName(value = "issuance_program", alternate = {"issue_program"})
    public String issuanceProgram;

    /**
     * The list of keys used to create the issuance program for the asset.<br>
     * Signatures from these keys are required for issuing units of the asset.
     */
    public Key[] keys;

    @SerializedName("key_index")
    public Integer keyIndex;

    @SerializedName("xpubs")
    public List<String> xpubs;

    /**
     * The number of keys required to sign an issuance of the asset.
     */
    @SerializedName("quorum")
    public int quorum;

    /**
     * User-specified, arbitrary/unstructured data visible across blockchain networks.<br>
     * Version 1 assets specify the definition in their issuance programs, rendering the
     * definition immutable.
     */
    @SerializedName("definition")
    public Map<String, Object> definition;

    /**
     * version of VM.
     */
    @SerializedName("vm_version")
    public int vmVersion;

    /**
     * type of asset.
     */
    @SerializedName("type")
    public String type;

    /**
     * byte of asset definition.
     */
    @SerializedName("raw_definition_byte")
    public String rawDefinitionByte;

    public static Logger logger = Logger.getLogger(Asset.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static class Key {
        /**
         * Hex-encoded representation of the root extended public key
         */
        @SerializedName("root_xpub")
        public String rootXpub;

        /**
         * The derived public key, used in the asset's issuance program.
         */
        @SerializedName("asset_pubkey")
        public String assetPubkey;

        /**
         * The derivation path of the derived key.
         */
        @SerializedName("asset_derivation_path")
        public String[] assetDerivationPath;

        @Override
        public String toString() {
            return "Key{" +
                    "rootXpub='" + rootXpub + '\'' +
                    ", assetPubkey='" + assetPubkey + '\'' +
                    ", assetDerivationPath=" + Arrays.toString(assetDerivationPath) +
                    '}';
        }
    }

    /**
     * <h2>Builder Class</h2>
     */
    public static class Builder {
        /**
         * User specified, unique identifier.
         */
        public String alias;

        /**
         * User-specified, arbitrary/unstructured data visible across blockchain networks.<br>
         * Version 1 assets specify the definition in their issuance programs, rendering
         * the definition immutable.
         */
        public Map<String, Object> definition;

        /**
         * The list of keys used to create the issuance program for the asset.<br>
         * Signatures from these keys are required for issuing units of the asset.<br>
         * <strong>Must set with {@link #addRootXpub(String)} or
         * {@link #setRootXpubs(List)} before calling {@link #create(Client)}.</strong>
         */
        @SerializedName("root_xpubs")
        public List<String> rootXpubs;

        /**
         * The number of keys required to sign an issuance of the asset.<br>
         * <strong>Must set with {@link #setQuorum(int)} before calling
         * {@link #create(Client)}.</strong>
         */
        public int quorum;

        /**
         * Unique identifier used for request idempotence.
         */
        @SerializedName("access_token")
        private String access_token;

        /**
         * Default constructor initializes the list of keys.
         */
        public Builder() {
            this.rootXpubs = new ArrayList<>();
        }

        /**
         * Creates an asset object.
         *
         * @param client client object that makes request to the core
         * @return an asset object
         * @throws BytomException BytomException
         */
        public Asset create(Client client) throws BytomException {
            Asset asset = client.request("create-asset", this, Asset.class);
            logger.info("create-asset:");
            logger.info(asset.toString());
            return asset;
        }

        /**
         * Sets the alias on the builder object.
         * @param alias alias
         * @return updated builder object
         */
        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        /**
         * Adds a field to the existing definition object (initializing the object if it
         * doesn't exist).
         * @param key key of the definition field
         * @param value value of the definition field
         * @return updated builder object
         */
        public Builder addDefinitionField(String key, Object value) {
            if (this.definition == null) {
                this.definition = new HashMap<>();
            }
            this.definition.put(key, value);
            return this;
        }

        /**
         * Sets the asset definition object.<br>
         * <strong>Note:</strong> any existing asset definition fields will be replaced.
         * @param definition asset definition object
         * @return updated builder object
         */
        public Builder setDefinition(Map<String, Object> definition) {
            this.definition = definition;
            return this;
        }

        /**
         * Sets the quorum of the issuance program. <strong>Must be called before
         * {@link #create(Client)}.</strong>
         * @param quorum proposed quorum
         * @return updated builder object
         */
        public Builder setQuorum(int quorum) {
            this.quorum = quorum;
            return this;
        }

        /**
         * Adds a key to the builder's list.<br>
         * <strong>Either this or {@link #setRootXpubs(List)} must be called before
         * {@link #create(Client)}.</strong>
         * @param xpub key
         * @return updated builder object.
         */
        public Builder addRootXpub(String xpub) {
            this.rootXpubs.add(xpub);
            return this;
        }

        /**
         * Sets the builder's list of keys.<br>
         * <strong>Note:</strong> any existing keys will be replaced.<br>
         * <strong>Either this or {@link #addRootXpub(String)} must be called before
         * {@link #create(Client)}.</strong>
         * @param xpubs list of xpubs
         * @return updated builder object
         */
        public Builder setRootXpubs(List<String> xpubs) {
            this.rootXpubs = new ArrayList<>(xpubs);
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "alias='" + alias + '\'' +
                    ", definition=" + definition +
                    ", rootXpubs=" + rootXpubs +
                    ", quorum=" + quorum +
                    ", access_token='" + access_token + '\'' +
                    '}';
        }
    }

    /**
     * <h2>QueryBuilder Class</h2>
     */
    public static class QueryBuilder {

        @SerializedName("id")
        public String id;

        public QueryBuilder setId(String assetId) {
            this.id = assetId;
            return this;
        }

        /**
         * get-asset from bytomd
         *
         * @param client client object that makes requests to the core
         * @return The Asset Object
         * @throws BytomException BytomException
         */
        public Asset get(Client client) throws BytomException {
            Asset asset = client.request("get-asset", this, Asset.class);
            logger.info("get-asset:");
            logger.info(asset.toJson());
            return asset;
        }

        /**
         * get all assets from bytomd
         *
         * @param client client object that makes requests to the core
         * @return return list of asset object
         * @throws BytomException BytomException
         */
        public List<Asset> list(Client client) throws BytomException {
            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Asset.class});
            List<Asset> assetList = client.request("list-assets", null, listType);
            logger.info("list-assets:");
            logger.info("size of assetList:"+assetList.size());
            logger.info(assetList);
            return assetList;
        }

    }

    /**
     * <h2>AliasUpdateBuilder Class</h2>
     */
    public static class AliasUpdateBuilder {
        /**
         * id of asset.
         */
        @SerializedName("id")
        public String id;
        /**
         * new alias of asset
         */
        @SerializedName("alias")
        public String alias;

        public AliasUpdateBuilder setAssetId(String assetId) {
            this.id = assetId;
            return this;
        }

        public AliasUpdateBuilder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        /**
         * update-asset-alias
         *
         * @param client client object that makes requests to the core
         * @throws BytomException BytomException
         */
        public void update(Client client) throws BytomException {
            client.request("update-asset-alias", this);
            logger.info("update-asset-alias:");
            logger.info("id:"+id);
            logger.info("alias:"+alias);
        }

    }

    @Override
    public String toString() {
        return "Asset{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", issuanceProgram='" + issuanceProgram + '\'' +
                ", keys=" + Arrays.toString(keys) +
                ", keyIndex=" + keyIndex +
                ", xpubs=" + xpubs +
                ", quorum=" + quorum +
                ", definition=" + definition +
                ", vmVersion=" + vmVersion +
                ", type='" + type + '\'' +
                ", rawDefinitionByte='" + rawDefinitionByte + '\'' +
                '}';
    }
}
