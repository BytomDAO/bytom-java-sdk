## Creating transactions

Creating a transaction consists of three steps:

1. **Build transaction**: Define what the transaction is supposed to do: issue new units of an asset, spend assets held in an account, control assets with an account, etc.
2. **Sign transaction**: Authorize the spending of assets or the issuance of new asset units using private keys.
3. **Submit transaction**: Submit a complete, signed transaction to the blockchain, and propagate it to other cores on the network.

### Build transaction

Rather than forcing you to manipulate inputs, outputs and change directly, the Bytom Core API allows you to build transactions using a list of high-level **actions**.

There are five types of actions:

| ACTION                                  | DESCRIPTION                                                  |
| --------------------------------------- | ------------------------------------------------------------ |
| Issue                                   | Issues new units of a specified asset.                       |
| Spend from account                      | Spends units of a specified asset from a specified account. Automatically handles locating outputs with enough units, and the creation of change outputs. |
| Spend an unspent output from an account | Spends an entire, specific unspent output in an account. Change must be handled manually, using other actions. |
| Control with receiver                   | Receives units of an asset into a receiver, which contains a control program and supplementary payment information, such as an expiration date. Used when making a payment to an external party/account in another Bytom Core. |
| Retire                                  | Retires units of a specified asset.                          |

### Sign transaction

In order for a transaction to be accepted into the blockchain, its inputs must contain valid signatures. For issuance inputs, the signature must correspond to public keys named in the issuance program. For spending inputs, the signature must correspond to the public keys named in the control programs of the outputs being spent.

Transaction signing provides the blockchain with its security. Strong cryptography prevents everyone–even the operators of the blockchain network–from producing valid transaction signatures without the relevant private keys.

### Submit transaction

Once a transaction is balanced and all inputs are signed, it is considered valid and can be submitted to the blockchain. The local core will forward the transaction to the generator, which adds it to the blockchain and propagates it to other cores on the network. 

The Chain Core API does not return a response until either the transaction has been added to the blockchain and indexed by the local core, or there was an error. This allows you to write your applications in a linear fashion. In general, if a submission responds with success, the rest of your application may proceed with the guarantee that the transaction has been committed to the blockchain.

## Examples

### Asset issuance

Issue 1000 units of gold to Alice.

#### Within a Chain Core

```java
Transaction.Template issuance = new Transaction.Builder()
  .addAction(new Transaction.Action.Issue()
    .setAssetAlias("gold")
    .setAmount(1000)
  ).addAction(new Transaction.Action.ControlWithAccount()
    .setAccountAlias("alice")
    .setAssetAlias("gold")
    .setAmount(1000)
  ).build(client);

Transaction.Template signedIssuance = new Transaction.SignerBuilder().sign(client,
                issuance, "123456");

Transaction.submit(client, signedIssuance);
```

#### Between two Chain Cores

First, Bob creates a receiver in his account, which he can serialize and send to the issuer of gold.

```java
Receiver bobIssuanceReceiver = new Account.ReceiverBuilder()
  .setAccountAlias("bob")
  .create(otherCoreClient);
String bobIssuanceReceiverSerialized = bobIssuanceReceiver.toJson();
```

The issuer then builds, signs, and submits a transaction, sending gold to Bob’s receiver.

```java
Transaction.Template issuanceToReceiver = new Transaction.Builder()
  .addAction(new Transaction.Action.Issue()
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.ControlWithReceiver()
    .setReceiver(Receiver.fromJson(bobIssuanceReceiverSerialized))
    .setAssetAlias("gold")
    .setAmount(10)
  ).build(client);

Transaction.Template signedIssuanceToReceiver = new Transaction.SignerBuilder().sign(client,
                issuanceToReceiver, "123456");

Transaction.submit(client, signedIssuanceToReceiver);
```

### Simple payment

Alice pays 10 units of gold to Bob.

#### Within a Chain Core

```java
Transaction.Template payment = new Transaction.Builder()
  .addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.ControlWithAccount()
    .setAccountAlias("bob")
    .setAssetAlias("gold")
    .setAmount(10)
  ).build(client);

Transaction.Template signedPayment = new Transaction.SignerBuilder().sign(client,
                payment, "123456");

Transaction.submit(client, signedPayment);
```

#### Between two Chain Cores

First, Bob creates a receiver in his account, which he can serialize and send to Alice.

```java
Receiver bobPaymentReceiver = new Account.ReceiverBuilder()
  .setAccountAlias("bob")
  .create(otherCoreClient);
String bobPaymentReceiverSerialized = bobPaymentReceiver.toJson();
```

Alice then builds, signs, and submits a transaction, sending gold to Bob’s receiver.

```java
Transaction.Template paymentToReceiver = new Transaction.Builder()
  .addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.ControlWithReceiver()
    .setReceiver(Receiver.fromJson(bobPaymentReceiverSerialized))
    .setAssetAlias("gold")
    .setAmount(10)
  ).build(client);

Transaction.Template signedPaymentToReceiver = new Transaction.SignerBuilder().sign(client,
                paymentToReceiver, "123456");

Transaction.submit(client, signedPaymentToReceiver);
```

### Multi-asset payment

Alice pays 10 units of gold and 20 units of silver to Bob.

#### Within a Chain Core

```java
Transaction.Template multiAssetPayment = new Transaction.Builder()
  .addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("silver")
    .setAmount(20)
  ).addAction(new Transaction.Action.ControlWithAccount()
    .setAccountAlias("bob")
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.ControlWithAccount()
    .setAccountAlias("bob")
    .setAssetAlias("silver")
    .setAmount(20)
  ).build(client);

Transaction.Template signedMultiAssetPayment = new Transaction.SignerBuilder().sign(client,
                multiAssetPayment, "123456");

Transaction.submit(client, signedMultiAssetPayment);
```

#### Between two Chain Cores

Currently, the transaction builder API assigns each receiver to its own output, which means that a single receiver can only be used to receive a single asset type. It’s important for Bob not to re-use receivers, so he creates one for each asset payment he will receive. He serializes both and sends them to Alice.

```java
Receiver bobGoldReceiver = new Account.ReceiverBuilder()
  .setAccountAlias("bob")
  .create(otherCoreClient);
String bobGoldReceiverSerialized = bobGoldReceiver.toJson();

Receiver bobSilverReceiver = new Account.ReceiverBuilder()
  .setAccountAlias("bob")
  .create(otherCoreClient);
String bobSilverReceiverSerialized = bobSilverReceiver.toJson();
```

Alice then builds, signs, and submits a transaction, sending gold and silver to Bob’s receivers.

```java
Transaction.Template multiAssetToReceiver = new Transaction.Builder()
  .addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("silver")
    .setAmount(20)
  ).addAction(new Transaction.Action.ControlWithReceiver()
    .setReceiver(Receiver.fromJson(bobGoldReceiverSerialized))
    .setAssetAlias("gold")
    .setAmount(10)
  ).addAction(new Transaction.Action.ControlWithReceiver()
    .setReceiver(Receiver.fromJson(bobSilverReceiverSerialized))
    .setAssetAlias("silver")
    .setAmount(20)
  ).build(client);

Transaction.Template signedMultiAssetToReceiver = new Transaction.SignerBuilder().sign(client,
                multiAssetToReceiver, "123456");

Transaction.submit(client, signedMultiAssetToReceiver);
```

### Asset retirement

Alice retires 50 units of gold from her account.

```java
Transaction.Template retirement = new Transaction.Builder()
  .addAction(new Transaction.Action.SpendFromAccount()
    .setAccountAlias("alice")
    .setAssetAlias("gold")
    .setAmount(50)
  ).addAction(new Transaction.Action.Retire()
    .setAssetAlias("gold")
    .setAmount(50)
  ).build(client);

Transaction.Template signedRetirement = new Transaction.SignerBuilder().sign(client,
                retirement, "123456");

Transaction.submit(client, signedRetirement);
```

