# Bytom java-sdk

This page will document the API classes and ways to properly use the API.
Subsequent new releases also maintain backward compatibility with this class
approach. For more information, please see Bytom API reference documentation
at [Bytom wiki](https://github.com/Bytom/bytom/wiki/API-Reference)

## Installation

There are various ways to install and use this sdk. We'll provide three ways to get it. Note that the bytom-sdk requires JAVA 7 or newer.

### Apache Maven

```xml
<dependency>
    <groupId>io.bytom</groupId>
    <artifactId>bytom-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle/Grails
```xml
compile 'io.bytom:bytom-sdk:1.0.0'
```

### Building from source code

To clone, compile, and install in your local maven repository (or copy the artifacts from the target/ directory to wherever you need them):

```shell
git clone https://github.com/Bytom/bytom-java-sdk.git
cd java-sdk
mvn install -Dmaven.test.skip=true
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
> Note: you can touch a file named config.properties in resources folder to config ```bytom.api.url``` and ```client.access.token``` by custom.

## Usage

* [`Step 1: Create a key`](#create-a-key)
* [`Step 2: Create an account`](#create-an-account)
* [`Step 3: Create an receiver`](#create-an-receiver)
* [`Step 4: Create an asset`](#create-an-asset)
* [`Step 5: Issue asset`](#issue-asset)
    * [`Firstly build the transaction`](#firstly-build-the-transaction)
    * [`Secondly sign the transaction`](#secondly-sign-the-transaction)
    * [`Finally submit the transaction`](#finally-submit-the-transaction)

> For more details, see [API methods](https://github.com/Bytom/java-sdk/blob/master/doc/index.md#api-methods)

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

For more transaction details, see [transactions](https://github.com/Bytom/java-sdk/blob/master/doc/transactions.md)

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

> For more details, see [API methods](https://github.com/Bytom/java-sdk/blob/master/doc/index.md#api-methods)
