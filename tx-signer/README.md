# tx_signer

Java implementation of signing transaction offline to bytomd and serializing transaction to submit.

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
   git clone https://github.com/Bytom/bytom-java-sdk.git
   ```

2. get jar package

   ```
   $ mvn assembly:assembly -Dmaven.test.skip=true
   ```

   You can get a jar with dependencies, and you can use it in your project.

#### Test cases

Need 3 Parameters:

- Private Keys Array
- UTXO list.
- Submit Transaction
  - call submit-transaction api. [submit-transaction api](https://github.com/Bytom/bytom/wiki/API-Reference#submit-transaction)



```java

//utxo json to input public Transaction.AnnotatedInput btmUtxoToInput() {
    String utxoJson = "{\n" +
            " \"id\": \"cf2f5c7340490d33d535a680dc8d95bb66fcccbf1045706484621cc067b982ae\",\n" +
            " \"amount\": 70000000,\n" +
            " \"address\": \"tm1qf4g97wae3fsz973huwrjqnd68v530nmd7n2pc43zfpw9tvd30qfsd7jl8p\",\n" +
            " \"program\": \"00204d505f3bb98a6022fa37e387204dba3b2917cf6df4d41c5622485c55b1b17813\",\n" +
            " \"change\": false,\n" +
            " \"highest\": 140925,\n" +
            " \"account_alias\": \"mutiaccout\",\n" +
            " \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
            " \"asset_alias\": \"BTM\",\n" +
            " \"account_id\": \"0PCB0S1GG0A02\",\n" +
            " \"control_program_index\": 2,\n" +
            " \"source_id\": \"9b29c72a653f986d5c5a7bf16c0fe63a9f639a0d15f3faeabeb4c14df70bbd91\",\n" +
            " \"source_pos\": 0,\n" +
            " \"valid_height\": 0,\n" +
            " \"derive_rule\": 0\n" +
            "}";
  UTXO utxo = UTXO.fromJson(utxoJson);
  Transaction.AnnotatedInput input = UTXO.utxoToAnnotatedInput(utxo);
 return input;
}

```

Single-key Example :

```java
String rootKey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
String btmAssetID = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";


  @Test
  public void testSpend() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Transaction.AnnotatedInput input = btmUtxoToInput();
  Transaction Transaction = new Transaction.Builder()
//                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
//                        setChange(false).setControlProgramIndex(2).setSourceId("fc43933d1c601b2503b033e31d3bacfa5c40ccb2ff0be6e94d8332462e0928a3").setSourcePosition(0))
  .addInput(input.setType(1))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(170000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(10000000).setControlProgram("0020fa56ca7d47f8528e68e120d0e052885faeb9d090d238fa4266bdde21b137513c"))
                .build(200000);
  Transaction transaction = MapTransaction.mapTx(Transaction);
  SignTransaction signTransaction = new SignTransaction();
  String rawTransaction = signTransaction.rawTransaction(rootKey, transaction);
  System.out.println(rawTransaction);
  }

  //issue asset
  @Test
  public void testIssue() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        String issueAssetId = "a680606d49daae62ef9cb03263ca82a0b1e3184bb6311ea52a5189207f718789";
  String program = "ae204cae24c2cec15491e70fc554026469496e373df9b9970b23acac8b782da0822d5151ad";
  String assetDefine = "7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d";
  Transaction Transaction = new Transaction.Builder()
                .addInput(new Transaction.AnnotatedInput().setType(0).setAssetId(issueAssetId).setControlProgram(program).setAmount(100000000).setAssetDefinition(assetDefine).setChange(false).setKeyIndex(13))
                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
                        setChange(false).setControlProgramIndex(2).setSourceId("fc43933d1c601b2503b033e31d3bacfa5c40ccb2ff0be6e94d8332462e0928a3").setSourcePosition(0))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(issueAssetId).setAmount(100000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(870000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .build(2000000);
  MapTransaction.mapTx(Transaction);
  SignTransaction sign = new SignTransaction();
  String rawTransaction = sign.rawTransaction(rootKey, Transaction);
  System.out.println(rawTransaction);
  }


    //retire asset
  @Test
  public void testRetire() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String assetId1 = "8e962912423d8aea3409d1754782e7910da81b66c640aece14a6dac238f38e9b";
  Transaction transaction = new Transaction.Builder()
                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(890000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
                        setChange(false).setControlProgramIndex(2).setSourceId("6026b1acb11f3cbfb5280865ea857117a76d41ba7d60509dd358648424d8496a").setSourcePosition(0))
                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(assetId1).setAmount(100000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
                        setChange(false).setControlProgramIndex(2).setSourceId("d53e7ccfa06d3b27933a44e5d6e3e288edfc7e38f5396b8552ef44cf58a20347").setSourcePosition(0))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                //arbitrary add after retire control program("6a")
  .addOutput(new Transaction.AnnotatedOutput().setAssetId(assetId1).setAmount(100000000).setControlProgram("6a"))
                .build(2000000);
  MapTransaction.mapTx(transaction);
  SignTransaction sign = new SignTransaction();
  String rawTransaction = sign.rawTransaction(rootKey, transaction);
  System.out.println(rawTransaction);
  }

```

Multi-keys Example:

> Need an account has two or more keys.

```java
  //single input and multi-sign
  @Test
  public void testMutiSpend(){
        Transaction.AnnotatedInput input = btmUtxoToInput();
  Transaction Transaction = new Transaction.Builder()
//                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
//                        setChange(false).setControlProgramIndex(2).setSourceId("fc43933d1c601b2503b033e31d3bacfa5c40ccb2ff0be6e94d8332462e0928a3").setSourcePosition(0))
  .addInput(input.setType(1))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(50000000).setControlProgram("00204d505f3bb98a6022fa37e387204dba3b2917cf6df4d41c5622485c55b1b17813"))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(10000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .build(200000);
  Transaction transaction = MapTransaction.mapTx(Transaction);
  SignTransaction signTransaction = new SignTransaction();
  String[] rootKeys = new String[3];
  rootKeys[0] = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
  rootKeys[1] = "50a23bf6200b8a98afc049a7d0296a619e2ee27fa0d6d4d271ca244b280b324347627e543cc079614642c7b88c78ce38092430b01d124663e8b84026aefefde1";
  rootKeys[2] = "00e4bf1251fb5aa37aa2a11dec6c0db5cec3f17aa312dbddb30e06957a32ae503ebcdfd4ad5e29be21ee9ec336e939eb72439cf6d99c785268c8f3d71c1be877";
  signTransaction.buildWitness(transaction, 0, rootKeys);
  String raw = signTransaction.serializeTransaction(transaction);
  System.out.println(raw);
  }
```

Submit-transaction:
```java
// submit rawTransaction
@Test
public void SubmitTransaction() throws BytomException {
    Client client = TestUtil.generateClient();
  String raw = "0701c09a0c01016b01699b29c72a653f986d5c5a7bf16c0fe63a9f639a0d15f3faeabeb4c14df70bbd91ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80bbb02100012200204d505f3bb98a6022fa37e387204dba3b2917cf6df4d41c5622485c55b1b17813ac020440290bc8593d429d5c7d02f96232cf8035d3776eebf2a7e855c906cdbcf35281f98575624e326fbf1f9e8de70a7047b5af2f43c1d102fcf632d1544cff46aa970540ff237b6d2c5760bf633b640e120600d06e57ab3119d192b33894967a74293531507df473235233033f12c493bc005c8d1e2f858524a431edc7d522891dfada04406ebebe5d380e129fb212a777aca0f971cfe38a2e54cd9b822459609664303aa2e10caa10f5cb8eae4a53688f895a9cbe13e5be085dd289251c63a7c86718730f67ae204e4fcad6d69dfbad1fa83c37e6fd2031476940b44a9165fa79084e5a4d9acaed20415a96ffead835222ee96eda7c16e99a4c87e8e5e43e54d2d493855f707baf78209613d3c8a4aa730bb24e0324a920b2e3c8db7260ded4527f6d6d7d14d86b64385353ad020148ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80e1eb17012200204d505f3bb98a6022fa37e387204dba3b2917cf6df4d41c5622485c55b1b1781300013cffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80ade2040116001414d362694eacfa110dc20dec77d610d22340f95b00";
  SubmitTransaction.SubmitResponse submitResponse = SubmitTransaction.submitRawTransaction(client, raw);
  System.out.println(submitResponse.tx_id);
}