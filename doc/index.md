# Java SDK documentation

This page will document the API classes and ways to properly use the API.
Subsequent new releases also maintain backward compatibility with this class approach.

## Basic Usage

```
public static Client generateClient() throws BytomException {
    String coreURL = Configuration.getValue("bytom.api.url");
    String accessToken = Configuration.getValue("client.access.token");
    if (coreURL == null || coreURL.isEmpty()) {
        coreURL = "http://127.0.0.1:9888/";
    }
    return new Client(coreURL, accessToken);
}

Client client = TestUtils.generateClient();
```

## Usage

* [`Step 1: Create a key`](#create-a-key)
* [`Step 2: Create an account`](#create-an-account)
* [`Step 3: Create an receiver`](#create-an-receiver)
* [`Step 4: Create an asset`](#create-an-asset)
* [`Step 5: Issue asset`](#issue-asset)
    * [`Firstly build the transaction`](#firstly-build-the-transaction)
    * [`Secondly sign the transaction`](#secondly-sign-the-transaction)
    * [`Finally submit the transaction`](#finally-submit-the-transaction)

> For more details, see [`API methods`](#api-methods)

## Create a key

```java
String alias = "test";
String password = "123456";

Key.Builder builder = new Key.Builder().setAlias(alias).setPassword(password);
Key key = Key.create(client, builder);
```

## Create an account

```java
String alias = "sender-account";
Integer quorum = 1;
List<String> root_xpubs = new ArrayList<String>();
root_xpubs.add(senderKey.xpub);

Account.Builder builder = new Account.Builder().setAlias(alias).setQuorum(quorum).setRootXpub(root_xpubs);

Account account = Account.create(client, builder);
```

## Create an receiver

```java
String alias = receiverAccount.alias;
String id = receiverAccount.id;

Account.ReceiverBuilder receiverBuilder = new Account.ReceiverBuilder().setAccountAlias(alias).setAccountId(id);
Receiver receiver = receiverBuilder.create(client);
```

## Create an asset

```java
 String alias = "receiver-asset";

List<String> xpubs = receiverAccount.xpubs;

Asset.Builder builder = new Asset.Builder()
                        .setAlias(alias)
                        .setQuorum(1)
                        .setRootXpubs(xpubs);
receiverAsset = builder.create(client);
```

## Issue asset

### Firstly build the transaction

```java
Transaction.Template controlAddress = new Transaction.Builder()
        .addAction(
                new Transaction.Action.SpendFromAccount()
                        .setAccountId(senderAccount.id)
                        .setAssetId(senderAsset.id)
                        .setAmount(300000000)
        )
        .addAction(
                new Transaction.Action.ControlWithAddress()
                        .setAddress(receiverAddress.address)
                        .setAssetId(senderAsset.id)
                        .setAmount(200000000)
        ).build(client);
```

### Secondly sign the transaction

```java
Transaction.Template singer = new Transaction.SignerBuilder().sign(client,
        controlAddress, "123456");
```

### Finally submit the transaction

```java
Transaction.SubmitResponse txs = Transaction.submit(client, singer);
```

----

## API methods

* [`Key API`](#key-api)
* [`Account API`](#account-api)
* [`Asset API`](#asset-api)
* [`Transaction API`](#transaction-api)
* [`Wallet API`](#wallet-api)
* [`Access Token API`](#access-token-api)
* [`Block API`](#block-api)
* [`Mining API`](#mining-api)
* [`Other API`](#other-api)

## Key API


#### createKey

```java
Key create(Client client, Builder builder);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.
- `Key.Builder` - *builder*, Builder object that builds request parameters.

##### Returns

- `Key` - *key*, Key object.

----

#### listKeys

```java
List<Key> list(Client client);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.

##### Returns

- `List of Key`, *List<Key>*, an ArrayList object contains Key objects.

----

#### deleteKey

```java
void delete(Client client, String xpub, String password);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.
- `String` - *xpub*, pubkey of the key.
- `String` - *password*, password of the key.

##### Returns

none if the key is deleted successfully.

----

#### resetKeyPassword

```java
void resetPwd(Client client, String xpub, String oldPwd, String newPwd);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.
- `String` - *xpub*, pubkey of the key.
- `String` - *oldPwd*, old password of the key.
- `String` - *newPwd*, new password of the key.

##### Returns

none if the key password is reset successfully.


## Account API


#### createAccount

```java
Account create(Client client, Builder builder);
```
##### Parameters

- `Client` - *client*, Client object that makes requests to the core.
- `Account.Builder` - *builder*, Builder object that builds request parameters.

##### Returns

- `Account` - *account*, Account object.

----

#### listAccounts

```java
List<Account> list(Client client);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.

##### Returns

- `List of Account`, *List<Account>*, an ArrayList object contains Account objects.

----

#### deleteAccount

```java
void delete(Client client, String account_info);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.
- `String` - *account_info*, alias or ID of account.

##### Returns

none if the account is deleted successfully.

----

#### createAccountReceiver

```java
Receiver create(Client client);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.

##### Returns

- `Receiver` - *receiver*, Receiver object.


----

#### listAddresses

```java
List<Address> list(Client client);
```

##### Parameters

- `Client` - *client*, Client object that makes requests to the core.

##### Returns

- `List of Address`, *List<Address>*, an ArrayList object contains Address objects.

----

#### validateAddress

```java
Address validate(Client client, String address);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `String` - *address*, address of account.

##### Returns

- `Address` - *address*, an Address object.


## Asset API


#### createAsset

```java
Asset create(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Asset` - *asset*, an Asset object.

----

#### getAsset

```java
Asset get(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Asset` - *asset*, an Asset object.

----

#### listAssets

```java
List<Asset> list(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `List of Asset`, *List<Asset>*, an ArrayList object contains Asset objects.

----

#### updateAssetAlias

```java
void update(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

none if the asset alias is updated success.

----

#### listBalances

```java
List<Balance> list(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `List of Balance`, an ArrayList object contains Balance objects.

----

#### listUnspentOutPuts

```java
List<UnspentOutput> list(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `List of UnspentOutput`, an ArrayList object contains UnspentOutput objects.


## Transaction API


#### buildTransaction

```java
Template build(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Template` - *template*, a template object.

----

#### signTransaction

```java
Template sign(Client client, Template template, String password);
```

##### Parameters

`Object`:

- `Client` - *Client*, Client object that makes requests to the core.
- `Template` - *template*, a template object.
- `String` - *password*, signature of the password.

##### Returns

- `Template` - *template*, a template object.

----

#### submitTransaction

```java
SubmitResponse submit(Client client, Template template);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `Template` - *template*, a template object.

##### Returns

- `SubmitResponse` - *submitResponse*, a SubmitResponse object

----

#### estimateTransactionGas

```java
TransactionGas estimateGas(Client client, Template template);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `Template` - *template*, a template object.

##### Returns

- `TransactionGas` - *transactionGas*, a TransactionGas object

----

#### getTransaction

```java
Transaction get(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Transaction` - *transaction*, a Transaction object

----

#### listTransactions

```java
List<Transaction> list(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `List of Transaction`, *List<Transaction>*, an ArrayList object contains Transaction objects.

## Wallet API


#### backupWallet

```java
Wallet backupWallet(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Wallet` - *wallet*, a Wallet object

----

#### restoreWallet

```java
void restoreWallet(Client client ,Object accountImage, Object assetImage , Object keyImages);
```

##### Parameters

`Object`:
- `Client` - *Client*, Client object that makes requests to the core.
- `Object` - *account_image*, account image.
- `Object` - *asset_image*, asset image.
- `Object` - *key_images*, key image.

##### Returns

none if restore wallet success.


## Access Token API

```java
//example
AccessToken accessToken = new AccessToken.Builder().setId("sheng").create(client);

List<AccessToken> tokenList = AccessToken.list(client);

String secret = "5e37378eb59de6b10e60f2247ebf71c4955002e75e0cd31ede3bf48813a0a799";
AccessToken.check(client, "sheng", secret);
```

#### createAccessToken

```java
AccessToken create(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `AccessToken` - *accessToken*, an AccessToken object.

----

#### listAccessTokens

```java
List<AccessToken> list(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Array of Object`, access token array.

----

#### deleteAccessToken

```java
void delete(Client client, String id);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `String` - *id*, token ID.

##### Returns

none if the access token is deleted successfully.

----

#### checkAccessToken

```java
void check(Client client, String id, String secret);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `String` - *id*, token ID.
- `String` - *secret*, secret of token, the second part of the colon division for token.

##### Returns

none if the access token is checked valid.


## Block API


#### getBlockCount

```java
Integer getBlockCount(Client client);
```

##### Parameters

none

##### Returns

- `Integer` - *block_count*, recent block height of the blockchain.

----

#### getBlockHash

```java
String getBlockHash(Client client);
```

##### Parameters

none

##### Returns

- `String` - *block_hash*, recent block hash of the blockchain.

----

#### getBlock
```php
Block getBlock(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Block` - *block*, a Block object.

----

#### getBlockHeader

```java
BlockHeader getBlockHeader(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `BlockHeader` - *blockHeader*, header of block.

----

#### getDifficulty

```java
BlockDifficulty getBlockDifficulty(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `BlockDifficulty` - *blockDifficulty*, a BlockDifficulty object

----

#### getHashRate

```java
BlockHashRate getHashRate(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `BlockHashRate` - *blockHashRate*, a BlockHashRate object

## Mining API


#### isMining

```java
Boolean isMining(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Boolean` - *is_mining*, whether the node is mining.

----

#### setMining

```java
void setMining(Client client, Boolean isMining);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `Boolean` - *is_mining*, whether the node is mining.


## Other API


#### netInfo

```java
NetInfo getNetInfo(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `NetInfo` - *netInfo*, a NetInfo object.

----

#### gasRate

```java
Gas gasRate(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Gas` - *gas*, a Gas object.

----

#### verifyMessage

```java
Boolean verifyMessage(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `Boolean` - *result*, verify result.

----

#### getWork

```java
MinerWork getWork(Client client);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.

##### Returns

- `MinerWork` - *minerWork*, a MinerWork object.

----

#### submitWork

```java
void submiWork(Client client, String blockHeader);
```

##### Parameters

- `Client` - *Client*, Client object that makes requests to the core.
- `String` - *block_header*, raw block header.

##### Returns

none if the work is submitted successfully.
