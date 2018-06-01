## Creating transactions

Creating a transaction consists of three steps:

1. **Build transaction**: Define what the transaction is supposed to do: issue new units of an asset, spend assets held in an account, control assets with an account, etc.
2. **Sign transaction**: Authorize the spending of assets or the issuance of new asset units using private keys.
3. **Submit transaction**: Submit a complete, signed transaction to the blockchain, and propagate it to other cores on the network.

### Build transaction

Rather than forcing you to manipulate inputs, outputs and change directly, the Bytom Core API allows you to build transactions using a list of high-level **actions**.

There are seven types of actions:

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

Transaction.Template singer = new Transaction.SignerBuilder().sign(client, controlAddress, "123456");

Transaction.SubmitResponse txs = Transaction.submit(client, singer);
```

