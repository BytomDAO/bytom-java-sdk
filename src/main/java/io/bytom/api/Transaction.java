package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <h1>Transaction Class</h1>
 */
public class Transaction {
    /**
     * Unique identifier, or transaction hash, of a transaction.
     */
    @SerializedName("tx_id")
    public String txId;

    /**
     * Time of transaction.
     */
    @SerializedName("block_time")
    public String blockTime;

    /**
     * Unique identifier, or block hash, of the block containing a transaction.
     */
    @SerializedName("block_hash")
    public String blockHash;

    /**
     * Index of a transaction within the block.
     */
    @SerializedName("block_index")
    public String blockIndex;

    @SerializedName("block_transactions_count")
    public String blockTransactionsCount;

    /**
     * Height of the block containing a transaction.
     */
    @SerializedName("block_height")
    public int blockHeight;

    /**
     * whether the state of the request has failed.
     */
    @SerializedName("status_fail")
    public boolean statusFail;

    /**
     * List of specified inputs for a transaction.
     */
    public List<Input> inputs;

    /**
     * List of specified outputs for a transaction.
     */
    public List<Output> outputs;

    private static Logger logger = Logger.getLogger(Transaction.class);

    /**
     * Serializes the Address into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public static class Builder {
        /**
         * Hex-encoded serialization of a transaction to add to the current template.
         */
        @SerializedName("base_transaction")
        protected String baseTransaction;

        /**
         * List of actions in a transaction.
         */
        protected List<Action> actions;

        /**
         * A time duration in milliseconds. If the transaction is not fully signed and
         * submitted within this time, it will be rejected by the blockchain.
         * Additionally, any outputs reserved when building this transaction will remain
         * reserved for this duration.
         */
        protected long ttl;

        /**
         * Call build-transaction api.<br>
         *
         * Builds a single transaction template.
         *
         * @param client client object which makes requests to the server
         * @return a transaction template
         */
        public Template build(Client client) throws BytomException {
            return client.request("build-transaction", this, Template.class);
        }

        /**
         * Default constructor initializes actions list.
         */
        public Builder() {
            this.actions = new ArrayList<>();
        }

        /**
         * Sets the baseTransaction field and initializes the actions lists.<br>
         * This constructor can be used when executing an atomic swap and the counter
         * party has sent an initialized tx template.
         */
        public Builder(String baseTransaction) {
            this.setBaseTransaction(baseTransaction);
            this.actions = new ArrayList<>();
        }

        /**
         * Sets the base transaction that will be added to the current template.
         */
        public Builder setBaseTransaction(String baseTransaction) {
            this.baseTransaction = baseTransaction;
            return this;
        }

        /**
         * Adds an action to a transaction builder.
         * @param action action to add
         * @return updated builder object
         */
        public Builder addAction(Action action) {
            this.actions.add(action);
            return this;
        }

        /**
         * Sets a transaction's time-to-live, which indicates how long outputs will be
         * reserved for, and how long the transaction will remain valid. Passing zero will
         * use the default TTL, which is 300000ms (5 minutes).
         * @param ms the duration of the TTL, in milliseconds.
         * @return updated builder object
         */
        public Builder setTtl(long ms) {
            this.ttl = ms;
            return this;
        }
    }

    public static class QueryBuilder {

        public String txId;

        public String accountId;

        public QueryBuilder setTxId(String txId) {
            this.txId = txId;
            return this;
        }

        public QueryBuilder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }


        /**
         * call list-transactions api
         *
         * @param client client object that makes requests to the core
         * @return Transaction Info
         * @throws BytomException BytomException
         */
        public List<Transaction> list(Client client) throws BytomException {

            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Transaction.class});
            List<Transaction> transactionList = client.request("list-transactions", null, listType);

            logger.info("list-transactions:");
            logger.info("size of transactionList:" + transactionList.size());
            logger.info("all transactions:");
            for (int i = 0; i < transactionList.size(); i++) {
                logger.info(transactionList.get(i).toJson());
            }

            return transactionList;
        }

        public List<Transaction> listById(Client client) throws BytomException {
            Map<String, Object> req = new HashMap<String, Object>();
            req.put("tx_id", this.txId);
            req.put("detail", true);

            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Transaction.class});
            List<Transaction> transactionList = client.request("list-transactions", req, listType);

            logger.info("list-transactions:");
            logger.info("size of transactionList:" + transactionList.size());
            logger.info("all transactions:");
            for (int i = 0; i < transactionList.size(); i++) {
                logger.info(transactionList.get(i).toJson());
            }

            return transactionList;
        }

        public List<Transaction> listByAccountId(Client client) throws BytomException {
            Map<String, Object> req = new HashMap<String, Object>();
            req.put("account_id", this.accountId);

            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{Transaction.class});
            List<Transaction> transactionList = client.request("list-transactions", req, listType);

            logger.info("list-transactions:");
            logger.info("size of transactionList:" + transactionList.size());
            logger.info("all transactions:");
            for (int i = 0; i < transactionList.size(); i++) {
                logger.info(transactionList.get(i).toJson());
            }

            return transactionList;
        }

        /**
         * call get-transaction api
         *
         * @param client
         * @return
         * @throws BytomException
         */
        public Transaction get(Client client) throws BytomException {
            Map<String, Object> req = new HashMap<String, Object>();
            req.put("tx_id", this.txId);

            Transaction transaction = client.request("get-transaction", req, Transaction.class);

            logger.info("get-transaction:");
            logger.info(transaction.toJson());

            return transaction;
        }

    }

    public static class SignerBuilder {
        /**
         * call sign-transaction api
         *
         * Sends a transaction template to a remote password for signing.
         *
         * @param client
         * @param template a signed transaction template
         * @param password
         * @return
         * @throws BytomException
         */
        public Template sign(Client client, Template template,
                                         String password) throws BytomException {
            // TODO: 2018/5/23 need to test
            HashMap<String, Object> req = new HashMap<String, Object>();
            req.put("transaction", template);
            req.put("password", password);

            Template templateResult = client.requestGet("sign-transaction", req, "transaction",
                    Transaction.Template.class);

            logger.info("sign-transaction:");
            logger.info(templateResult.toJson());

            return templateResult;
        }

    }

    /**
     * A single input included in a transaction.
     */
    public static class Input {
        /**
         * The alias of the account transferring the asset (possibly null if the input is
         * an issuance or an unspent output is specified).
         */
        @SerializedName("account_alias")
        public String accountAlias;

        /**
         * The id of the account transferring the asset (possibly null if the input is an
         * issuance or an unspent output is specified).
         */
        @SerializedName("account_id")
        public String accountId;

        @SerializedName("address")
        public String address;

        /**
         * The number of units of the asset being issued or spent.
         */
        public long amount;

        /**
         * The alias of the asset being issued or spent (possibly null).
         */
        @SerializedName("asset_alias")
        public String assetAlias;

        /**
         * The definition of the asset being issued or spent (possibly null).
         */
        @SerializedName("asset_definition")
        public Map<String, Object> assetDefinition;

        /**
         * The id of the asset being issued or spent.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The id of the output consumed by this input. Null if the input is an issuance.
         */
        @SerializedName("spent_output_id")
        public String spentOutputId;

        /**
         * The type of the input.<br>
         * Possible values are "issue" and "spend".
         */
        public String type;

        public String arbitrary;

        @SerializedName("control_program")
        public String controlProgram;

    }

    /**
     * A single output included in a transaction.
     */
    public static class Output {
        /**
         * The id of the output.
         */
        @SerializedName("id")
        public String id;

        /**
         * The type the output.<br>
         * Possible values are "control" and "retire".
         */
        public String type;

        /**
         * The output's position in a transaction's list of outputs.
         */
        public int position;

        /**
         * The control program which must be satisfied to transfer this output.
         */
        @SerializedName("control_program")
        public String controlProgram;

        /**
         * The id of the asset being controlled.
         */
        @SerializedName("asset_id")
        public String assetId;

        /**
         * The alias of the asset being controlled.
         */
        @SerializedName("asset_alias")
        public String assetAlias;

        /**
         * The definition of the asset being controlled (possibly null).
         */
        @SerializedName("asset_definition")
        public Map<String, Object> assetDefinition;

        /**
         * The number of units of the asset being controlled.
         */
        public long amount;

        /**
         * The id of the account controlling this output (possibly null if a control
         * program is specified).
         */
        @SerializedName("account_id")
        public String accountId;

        /**
         * The alias of the account controlling this output (possibly null if a control
         * program is specified).
         */
        @SerializedName("account_alias")
        public String accountAlias;

        @SerializedName("address")
        public String address;

    }

    /**
     * Base class representing actions that can be taken within a transaction.
     */
    public static class Action extends HashMap<String, Object> {
        /**
         *
         */
        private static final long serialVersionUID = 7948250382060074590L;

        /**
         * Default constructor initializes list and sets the client token.
         */
        public Action() {
            // Several action types require client_token as an idempotency key.
            // It's safest to include a default value for this param.
            this.put("client_token", UUID.randomUUID().toString());
        }

        /**
         * Represents an issuance action.
         */
        public static class Issue extends Action {
            /**
             *
             */
            private static final long serialVersionUID = -6296543909434749786L;

            /**
             * Default constructor defines the action type as "issue"
             */
            public Issue() {
                this.put("type", "issue");
            }

            /**
             * Specifies the asset to be issued using its alias.<br>
             * <strong>Either this or {@link Issue#setAssetId(String)} must be
             * called.</strong>
             * @param alias alias of the asset to be issued
             * @return updated action object
             */
            public Issue setAssetAlias(String alias) {
                this.put("asset_alias", alias);
                return this;
            }

            /**
             * Specifies the asset to be issued using its id.<br>
             * <strong>Either this or {@link Issue#setAssetAlias(String)} must be
             * called.</strong>
             * @param id id of the asset to be issued
             * @return updated action object
             */
            public Issue setAssetId(String id) {
                this.put("asset_id", id);
                return this;
            }

            /**
             * Specifies the amount of the asset to be issued.<br>
             * <strong>Must be called.</strong>
             * @param amount number of units of the asset to be issued
             * @return updated action object
             */
            public Issue setAmount(long amount) {
                this.put("amount", amount);
                return this;
            }
        }

        /**
         * Represents a spend action taken on a particular account.
         */
        public static class SpendFromAccount extends Action {
            /**
             *
             */
            private static final long serialVersionUID = 6444162327409625893L;

            /**
             * Default constructor defines the action type as "spend_account"
             */
            public SpendFromAccount() {
                this.put("type", "spend_account");
            }

            /**
             * Specifies the spending account using its alias.<br>
             * <strong>Either this or {@link SpendFromAccount#setAccountId(String)} must
             * be called.</strong><br>
             * <strong>Must be used with {@link SpendFromAccount#setAssetAlias(String)}
             * .</strong>
             * @param alias alias of the spending account
             * @return updated action object
             */
            public SpendFromAccount setAccountAlias(String alias) {
                this.put("account_alias", alias);
                return this;
            }

            /**
             * Specifies the spending account using its id.<br>
             * <strong>Either this or {@link SpendFromAccount#setAccountAlias(String)}
             * must be called.</strong><br>
             * <strong>Must be used with {@link SpendFromAccount#setAssetId(String)}
             * .</strong>
             * @param id id of the spending account
             * @return updated action object
             */
            public SpendFromAccount setAccountId(String id) {
                this.put("account_id", id);
                return this;
            }

            /**
             * Specifies the asset to be spent using its alias.<br>
             * <strong>Either this or {@link SpendFromAccount#setAssetId(String)} must be
             * called.</strong><br>
             * <strong>Must be used with {@link SpendFromAccount#setAccountAlias(String)}
             * .</strong>
             * @param alias alias of the asset to be spent
             * @return updated action object
             */
            public SpendFromAccount setAssetAlias(String alias) {
                this.put("asset_alias", alias);
                return this;
            }

            /**
             * Specifies the asset to be spent using its id.<br>
             * <strong>Either this or {@link SpendFromAccount#setAssetAlias(String)} must
             * be called.</strong><br>
             * <strong>Must be used with {@link SpendFromAccount#setAccountId(String)}
             * .</strong><br>
             * @param id id of the asset to be spent
             * @return updated action object
             */
            public SpendFromAccount setAssetId(String id) {
                this.put("asset_id", id);
                return this;
            }

            /**
             * Specifies the amount of asset to be spent.<br>
             * <strong>Must be called.</strong>
             * @param amount number of units of the asset to be spent
             * @return updated action object
             */
            public SpendFromAccount setAmount(long amount) {
                this.put("amount", amount);
                return this;
            }
        }

        /**
         * Represents a control action taken on a particular account.
         */
        public static class ControlWithAccount extends Action {
            /**
             *
             */
            private static final long serialVersionUID = -1067464339402520620L;

            /**
             * Default constructor defines the action type as "control_account"
             */
            public ControlWithAccount() {
                this.put("type", "control_account");
            }

            /**
             * Specifies the controlling account using its alias.<br>
             * <strong>Either this or {@link ControlWithAccount#setAccountId(String)} must
             * be called.</strong><br>
             * <strong>Must be used with {@link ControlWithAccount#setAssetAlias(String)}
             * .</strong>
             * @param alias alias of the controlling account
             * @return updated action object
             */
            public ControlWithAccount setAccountAlias(String alias) {
                this.put("account_alias", alias);
                return this;
            }

            /**
             * Specifies the controlling account using its id.<br>
             * <strong>Either this or {@link ControlWithAccount#setAccountAlias(String)}
             * must be called.</strong><br>
             * <strong>Must be used with {@link ControlWithAccount#setAssetId(String)}
             * .</strong>
             * @param id id of the controlling account
             * @return updated action object
             */
            public ControlWithAccount setAccountId(String id) {
                this.put("account_id", id);
                return this;
            }

            /**
             * Specifies the asset to be controlled using its alias.<br>
             * <strong>Either this or {@link ControlWithAccount#setAssetId(String)} must
             * be called.</strong><br>
             * <strong>Must be used with
             * {@link ControlWithAccount#setAccountAlias(String)}.</strong>
             * @param alias alias of the asset to be controlled
             * @return updated action object
             */
            public ControlWithAccount setAssetAlias(String alias) {
                this.put("asset_alias", alias);
                return this;
            }

            /**
             * Specifies the asset to be controlled using its id.<br>
             * <strong>Either this or {@link ControlWithAccount#setAssetAlias(String)}
             * must be called.</strong><br>
             * <strong>Must be used with {@link ControlWithAccount#setAccountId(String)}
             * .</strong>
             * @param id id of the asset to be controlled
             * @return updated action object
             */
            public ControlWithAccount setAssetId(String id) {
                this.put("asset_id", id);
                return this;
            }

            /**
             * Specifies the amount of the asset to be controlled.<br>
             * <strong>Must be called.</strong>
             * @param amount number of units of the asset to be controlled
             * @return updated action object
             */
            public ControlWithAccount setAmount(long amount) {
                this.put("amount", amount);
                return this;
            }

        }

        /**
         * Represents a control action taken on a particular address.
         */
        public static class ControlWithAddress extends Action {
            /**
             *
             */
            private static final long serialVersionUID = 1292007349260961499L;

            /**
             * Default constructor defines the action type as "control_address"
             */
            public ControlWithAddress() {
                this.put("type", "control_address");
            }

            public ControlWithAddress setAddress(String address) {
                this.put("address", address);
                return this;
            }

            /**
             * Specifies the asset to be controlled using its alias.<br>
             * <strong>Either this or {@link ControlWithAddress#setAssetId(String)} must
             * be called.</strong><br>
             * @param alias alias of the asset to be controlled
             * @return updated action object
             */
            public ControlWithAddress setAssetAlias(String alias) {
                this.put("asset_alias", alias);
                return this;
            }

            /**
             * Specifies the asset to be controlled using its id.<br>
             * <strong>Either this or {@link ControlWithAccount#setAssetAlias(String)}
             * must be called.</strong><br>
             * @param id id of the asset to be controlled
             * @return updated action object
             */
            public ControlWithAddress setAssetId(String id) {
                this.put("asset_id", id);
                return this;
            }

            /**
             * Specifies the amount of the asset to be controlled.<br>
             * <strong>Must be called.</strong>
             * @param amount number of units of the asset to be controlled
             * @return updated action object
             */
            public ControlWithAddress setAmount(long amount) {
                this.put("amount", amount);
                return this;
            }

        }

        /**
         * Use this action to pay assets into a {@link Receiver}.
         */
        public static class ControlWithReceiver extends Action {
            /**
             *
             */
            private static final long serialVersionUID = 7280759134960453401L;

            /**
             * Default constructor.
             */
            public ControlWithReceiver() {
                this.put("type", "control_receiver");
            }

            /**
             * Specifies the receiver that is being paid to.
             *
             * @param receiver the receiver being paid to
             * @return this ControlWithReceiver object
             */
            public ControlWithReceiver setReceiver(Receiver receiver) {
                this.put("receiver", receiver);
                return this;
            }

            /**
             * Specifies the asset to be controlled using its alias.
             * <p>
             * <strong>Either this or {@link ControlWithReceiver#setAssetId(String)} must
             * be called.</strong>
             * @param alias unique alias of the asset to be controlled
             * @return this ControlWithReceiver object
             */
            public ControlWithReceiver setAssetAlias(String alias) {
                this.put("asset_alias", alias);
                return this;
            }

            /**
             * Specifies the asset to be controlled using its id.
             * <p>
             * <strong>Either this or {@link ControlWithReceiver#setAssetAlias(String)}
             * must be called.</strong>
             * @param id unique ID of the asset to be controlled
             * @return this ControlWithReceiver object
             */
            public ControlWithReceiver setAssetId(String id) {
                this.put("asset_id", id);
                return this;
            }

            /**
             * Specifies the amount of the asset to be controlled.
             * <p>
             * <strong>Must be called.</strong>
             * @param amount the number of units of the asset to be controlled
             * @return this ControlWithReceiver object
             */
            public ControlWithReceiver setAmount(long amount) {
                this.put("amount", amount);
                return this;
            }

        }

        /**
         * Represents a retire action.
         */
        public static class Retire extends Action {
            /**
             *
             */
            private static final long serialVersionUID = -8434272436211832706L;

            /**
             * Default constructor defines the action type as "control_program"
             */
            public Retire() {
                this.put("type", "retire");
            }

            /**
             * Specifies the amount of the asset to be retired.<br>
             * <strong>Must be called.</strong>
             * @param amount number of units of the asset to be retired
             * @return updated action object
             */
            public Retire setAmount(long amount) {
                this.put("amount", amount);
                return this;
            }

            /**
             * Specifies the asset to be retired using its alias.<br>
             * <strong>Either this or {@link Retire#setAssetId(String)} must be
             * called.</strong>
             * @param alias alias of the asset to be retired
             * @return updated action object
             */
            public Retire setAssetAlias(String alias) {
                this.put("asset_alias", alias);
                return this;
            }

            /**
             * Specifies the asset to be retired using its id.<br>
             * <strong>Either this or {@link Retire#setAssetAlias(String)} must be
             * called.</strong>
             * @param id id of the asset to be retired
             * @return updated action object
             */
            public Retire setAssetId(String id) {
                this.put("asset_id", id);
                return this;
            }

            public Retire setAccountAlias(String alias) {
                this.put("account_alias", alias);
                return this;
            }

            public Retire setAccountId(String id) {
                this.put("account_id", id);
                return this;
            }

        }

        /**
         * Sets a k,v parameter pair.
         * @param key the key on the parameter object
         * @param value the corresponding value
         * @return updated action object
         */
        public Action setParameter(String key, Object value) {
            this.put(key, value);
            return this;
        }
    }

    public static class Template {
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
         *
         * All participants in a multiparty trade flow should call this method except for
         * the last signer. Do not call this option if the transaction is complete, i.e.
         * if it will not be used as a base transaction.
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

    /**
     * Call submit-transaction api
     *
     * @param client
     * @param template
     * @return
     * @throws BytomException
     */
    public static SubmitResponse submit(Client client, Template template)
            throws BytomException {
        HashMap<String, Object> body = new HashMap<>();
        body.put("raw_transaction", template.rawTransaction);
        return client.request("submit-transaction", body, SubmitResponse.class);
    }

    public static class TransactionGas {
        /**
         * total consumed neu(1BTM = 10^8NEU) for execute transaction.
         */
        @SerializedName("total_neu")
        public int totalNeu;

        /**
         * consumed neu for storage transaction .
         */
        @SerializedName("storage_neu")
        public int storageNeu;
        /**
         * consumed neu for execute VM.
         */
        @SerializedName("vm_neu")
        public int vmNeu;
    }

    /**
     * call estimate-transaction-gas api
     *
     * @param client
     * @param template
     * @return
     * @throws BytomException
     */
    public static TransactionGas estimateGas(Client client, Template template)
            throws BytomException {
        HashMap<String, Object> body = new HashMap<>();
        body.put("transaction_template", template);
        return client.request("estimate-transaction-gas", body, TransactionGas.class);
    }
}
