# Bytom java-sdk

This page will document the API classes and ways to properly use the API.
Subsequent new releases also maintain backward compatibility with this class
approach. For more information, please see Bytom API reference documentation
at [Bytom wiki](https://github.com/Bytom/bytom/wiki/API-Reference)

## Installation

### Building from source code

To clone, compile, and install in your local maven repository (or copy the artifacts from the target/ directory to wherever you need them):

```shell
git clone https://github.com/Bytom/bytom-java-sdk.git
cd java-sdk
mvn package -Dmaven.test.skip=true
mvn install
```

## Basic Usage

```java
public static Client generateClient() throws BytomException {
    String coreURL = Configuration.getValue("bytom.api.url");
    String accessToken = Configuration.getValue("client.access.token");
    if (coreURL == null || coreURL.isEmpty()) {
        coreURL = "http://127.0.0.1:9888/";
    }
    return new Client(coreURL, accessToken);
}

Client client = Client.generateClient();
```
> Note: you can touch a file named ```config.properties``` in resources folder to config ```bytom.api.url``` and ```client.access.token``` by custom.

## Usage

* [`Step 1: Create a key`](#create-a-key)
* [`Step 2: Create an account`](#create-an-account)
* [`Step 3: Create an receiver`](#create-an-receiver)
* [`Step 4: Create an asset`](#create-an-asset)
* [`Step 5: Issue asset`](#issue-asset)
    * [`Firstly build the transaction`](#firstly-build-the-transaction)
    * [`Secondly sign the transaction`](#secondly-sign-the-transaction)
    * [`Finally submit the transaction`](#finally-submit-the-transaction)

> For more details, see [API methods](https://github.com/Bytom/bytom-java-sdk/blob/master/java-sdk/doc/index.md#api-methods)

### Create a key

```java
String alias = "test";
String password = "123456";

Key.Builder builder = new Key.Builder().setAlias(alias).setPassword(password);
Key key = Key.create(client, builder);
```

### Create an account

```java
String alias = "sender-account";
Integer quorum = 1;
List<String> root_xpubs = new ArrayList<String>();
root_xpubs.add(senderKey.xpub);

Account.Builder builder = new Account.Builder().setAlias(alias).setQuorum(quorum).setRootXpub(root_xpubs);

Account account = Account.create(client, builder);
```

### Create an receiver

```java
String alias = receiverAccount.alias;
String id = receiverAccount.id;

Account.ReceiverBuilder receiverBuilder = new Account.ReceiverBuilder().setAccountAlias(alias).setAccountId(id);
Receiver receiver = receiverBuilder.create(client);
```

### Create an asset

```java
 String alias = "receiver-asset";

List<String> xpubs = receiverAccount.xpubs;

Asset.Builder builder = new Asset.Builder()
                        .setAlias(alias)
                        .setQuorum(1)
                        .setRootXpubs(xpubs);
receiverAsset = builder.create(client);
```

### Issue asset

For more transaction details, see [transactions](https://github.com/Bytom/bytom-java-sdk/blob/master/java-sdk/doc/transactions.md)

#### Firstly build the transaction

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

#### Secondly sign the transaction

```java
Transaction.Template singer = new Transaction.SignerBuilder().sign(client,
        controlAddress, "123456");
```

#### Finally submit the transaction

```java
Transaction.SubmitResponse txs = Transaction.submit(client, singer);
```


### All usage examples

For more details you can see [doc](https://github.com/Bytom/bytom-java-sdk/blob/master/doc/index.md#api-methods). And you can find Test Cases at [Junit Test Cases](https://github.com/Bytom/bytom-java-sdk/tree/master/src/test/java/io/bytom/integration)

## Support and Feedback

If you find a bug, please submit the issue in Github directly by using [Issues](https://github.com/Bytom/bytom-java-sdk/issues)

## License

Bytom JAVA SDK is based on the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)  protocol.
