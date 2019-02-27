# tx_signer

Java implementation of signing transaction offline to bytomd.

## Pre

#### Get the source code

```
$ git clone https://github.com/Bytom/bytom.git $GOPATH/src/github.com/bytom
```

#### git checkout

```
$ git checkout dev
```

**Why need dev branch? Because you could call decode transaction api from dev branch and obtain tx_id and some inputs ids.**

#### Build

```
$ cd $GOPATH/src/github.com/bytom
$ make bytomd    # build bytomd
$ make bytomcli  # build bytomcli
```

When successfully building the project, the `bytom` and `bytomcli` binary should be present in `cmd/bytomd` and `cmd/bytomcli` directory, respectively.

#### Initialize

First of all, initialize the node:

```
$ cd ./cmd/bytomd
$ ./bytomd init --chain_id solonet
```

#### launch

```
$ ./bytomd node --mining
```

## Usage

#### Build jar

1. first get source code

   ```
   git clone https://github.com/successli/tx_signer.git
   ```

2. get jar package

   ```
   $ mvn assembly:assembly -Dmaven.test.skip=true
   ```

   You can get a jar with dependencies, and you can use it in your project.

#### Test cases

Need 3 Parameters:

- Private Keys Array
- Template Object
  - After call build transaction api return a Template json object. [build transaction api](https://github.com/Bytom/bytom/wiki/API-Reference#build-transaction)
  - use bytom java sdk return a Template object.
- Raw Transaction
  - call decode raw-transaction api from dev branch. [decode raw-transaction api](https://github.com/Bytom/bytom/wiki/API-Reference#decode-raw-transaction)

Call method:

```java
// return a Template object signed offline basically.
Template result = signatures.generateSignatures(privates, template, rawTransaction);
// use result's raw_transaction call sign transaction api to build another data but not need password or private key.
```

Single-key Example:

```java
@Test
// 使用 SDK 来构造 Template 对象参数, 单签
public void testSignSingleKey() throws BytomException {
    Client client = Client.generateClient();

    String asset_id = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    String address = "sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em";
    // build transaction obtain a Template object
    Template template = new Transaction.Builder()
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G0NLBNU00A02")
        .setAssetId(asset_id)
        .setAmount(40000000)
    )
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G0NLBNU00A02")
        .setAssetId(asset_id)
        .setAmount(300000000)
    )
        .addAction(
        new Transaction.Action.ControlWithAddress()
        .setAddress(address)
        .setAssetId(asset_id)
        .setAmount(30000000)
    ).build(client);
    logger.info("template: " + template.toJson());
    // use Template object's raw_transaction id to decode raw_transaction obtain a RawTransaction object
    RawTransaction decodedTx = RawTransaction.decode(client, template.rawTransaction);
    logger.info("decodeTx: " + decodedTx.toJson());
    // need a private key array
    String[] privateKeys = new String[]{"10fdbc41a4d3b8e5a0f50dd3905c1660e7476d4db3dbd9454fa4347500a633531c487e8174ffc0cfa76c3be6833111a9b8cd94446e37a76ee18bb21a7d6ea66b"};
    logger.info("private key:" + privateKeys[0]);
    // call offline sign method to obtain a basic offline signed template
    Signatures signatures = new SignaturesImpl();
    Template basicSigned = signatures.generateSignatures(privateKeys, template, decodedTx);
    logger.info("basic signed raw: " + basicSigned.toJson());
    // call sign transaction api to calculate whole raw_transaction id
    // sign password is None or another random String
    Template result = new Transaction.SignerBuilder().sign(client,
                                                           basicSigned, "");
    logger.info("result raw_transaction: " + result.toJson());
    // success to submit transaction
}
```

Multi-keys Example:

> Need an account has two or more keys.

```java
@Test
// 使用 SDK 来构造 Template 对象参数, 多签
public void testSignMultiKeys() throws BytomException {
    Client client = Client.generateClient();

    String asset_id = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    String address = "sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em";
    // build transaction obtain a Template object
    // account 0G1RPP6OG0A06 has two keys
    Template template = new Transaction.Builder()
        .setTtl(10)
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G1RPP6OG0A06")
        .setAssetId(asset_id)
        .setAmount(40000000)
    )
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G1RPP6OG0A06")
        .setAssetId(asset_id)
        .setAmount(300000000)
    )
        .addAction(
        new Transaction.Action.ControlWithAddress()
        .setAddress(address)
        .setAssetId(asset_id)
        .setAmount(30000000)
    ).build(client);
    logger.info("template: " + template.toJson());
    // use Template object's raw_transaction id to decode raw_transaction obtain a RawTransaction object
    RawTransaction decodedTx = RawTransaction.decode(client, template.rawTransaction);
    logger.info("decodeTx: " + decodedTx.toJson());
    // need a private key array
    String[] privateKeys = new String[]{"08bdbd6c22856c5747c930f64d0e5d58ded17c4473910c6c0c3f94e485833a436247976253c8e29e961041ad8dfad9309744255364323163837cbef2483b4f67",
                                        "40c821f736f60805ad59b1fea158762fa6355e258601dfb49dda6f672092ae5adf072d5cab2ceaaa0d68dd3fe7fa04869d95afed8c20069f446a338576901e1b"};
    logger.info("private key 1:" + privateKeys[0]);
    logger.info("private key 2:" + privateKeys[1]);
    // call offline sign method to obtain a basic offline signed template
    Signatures signatures = new SignaturesImpl();
    Template basicSigned = signatures.generateSignatures(privateKeys, template, decodedTx);
    logger.info("basic signed raw: " + basicSigned.toJson());
    // call sign transaction api to calculate whole raw_transaction id
    // sign password is None or another random String
    Template result = new Transaction.SignerBuilder().sign(client,
                                                           basicSigned, "");
    logger.info("result raw_transaction: " + result.toJson());
    // success to submit transaction
}
```
Multi-keys and Multi-inputs Example:

```java
@Test
// 使用 SDK 来构造 Template 对象参数, 多签, 多输入
public void testSignMultiKeysMultiInputs() throws BytomException {
    Client client = Client.generateClient();

    String asset_id = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
    String address = "sm1qvyus3s5d7jv782syuqe3qrh65fx23lgpzf33em";
    // build transaction obtain a Template object
    Template template = new Transaction.Builder()
        .setTtl(10)
        // 1 input
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G1RPP6OG0A06") // Multi-keys account
        .setAssetId(asset_id)
        .setAmount(40000000)
    )
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G1RPP6OG0A06")
        .setAssetId(asset_id)
        .setAmount(300000000)
    )	// 2 input
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G1Q6V1P00A02") // Multi-keys account
        .setAssetId(asset_id)
        .setAmount(40000000)
    )
        .addAction(
        new Transaction.Action.SpendFromAccount()
        .setAccountId("0G1Q6V1P00A02")
        .setAssetId(asset_id)
        .setAmount(300000000)
    )
        .addAction(
        new Transaction.Action.ControlWithAddress()
        .setAddress(address)
        .setAssetId(asset_id)
        .setAmount(60000000)
    ).build(client);
    logger.info("template: " + template.toJson());
    // use Template object's raw_transaction id to decode raw_transaction obtain a RawTransaction object
    RawTransaction decodedTx = RawTransaction.decode(client, template.rawTransaction);
    logger.info("decodeTx: " + decodedTx.toJson());
    // need a private key array
    String[] privateKeys = new String[]{"08bdbd6c22856c5747c930f64d0e5d58ded17c4473910c6c0c3f94e485833a436247976253c8e29e961041ad8dfad9309744255364323163837cbef2483b4f67",
                                        "40c821f736f60805ad59b1fea158762fa6355e258601dfb49dda6f672092ae5adf072d5cab2ceaaa0d68dd3fe7fa04869d95afed8c20069f446a338576901e1b",
                                        "08bdbd6c22856c5747c930f64d0e5d58ded17c4473910c6c0c3f94e485833a436247976253c8e29e961041ad8dfad9309744255364323163837cbef2483b4f67"};
    logger.info("private key 1:" + privateKeys[0]);
    logger.info("private key 2:" + privateKeys[1]);
    // call offline sign method to obtain a basic offline signed template
    Signatures signatures = new SignaturesImpl();
    Template basicSigned = signatures.generateSignatures(privateKeys, template, decodedTx);
    logger.info("basic signed raw: " + basicSigned.toJson());
    // call sign transaction api to calculate whole raw_transaction id
    // sign password is None or another random String
    Template result = new Transaction.SignerBuilder().sign(client,
                                                           basicSigned, "");
    logger.info("result raw_transaction: " + result.toJson());
    // success to submit transaction
}
```

