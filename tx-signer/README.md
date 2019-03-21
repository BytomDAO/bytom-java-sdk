

# tx_signer  
  
Java implementation of signing transaction offline.
  
## Pre  
  
#### Add dependency to your project 
  
1. first get source code  
  
   ```
   git clone https://github.com/Bytom/bytom-java-sdk.git  
   ```  
  
2. install to maven repository
  
   ```  
   $ mvn clean install -DskipTests  
   ```  
  
3. add dependency
    ```xml
    <dependency>
        <groupId>io.bytom</groupId>
        <artifactId>tx-signer</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```
  
## Example

#### build transaction with spend input
```
        String btmAssetID = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        
        // create spend input
        SpendInput input = new SpendInput();
        input.setAssetId(btmAssetID);
        input.setAmount(9800000000L);
        
        // control program of spend utxo
        input.setProgram("0014cb9f2391bafe2bc1159b2c4c8a0f17ba1b4dd94e");
        
        // source position of spend utxo
        input.setSourcePosition(2);
        
        // source id of spend utxo
        input.setSourceID("4b5cb973f5bef4eadde4c89b92ee73312b940e84164da0594149554cc8a2adea");
        
        // is the spend utxo used for change
        input.setChange(true);
        
        // BIP protocol for derived paths, default BIP44
        input.setBipProtocol(BIPProtocol.BIP32);
        
        // contorl program index of spend utxo
        input.setControlProgramIndex(457);
        
        // account index
        input.setKeyIndex(1);
        
        // provide a root private key for signing
        input.setRootPrivateKey("4864bae85cf38bfbb347684abdbc01e311a24f99e2c7fe94f3c071d9c83d8a5a349722316972e382c339b79b7e1d83a565c6b3e7cf46847733a47044ae493257");

        // build transaction with signature
        Transaction tx = new Transaction.Builder()
                .addInput(input)
                .addOutput(new Output(btmAssetID, 8800000000L, "0014a82f02bc37bc5ed87d5f9fca02f8a6a7d89cdd5c"))
                .addOutput(new Output(btmAssetID, 900000000L, "00200824e931fb806bd77fdcd291aad3bd0a4493443a4120062bd659e64a3e0bac66"))
                .setTimeRange(0)
                .build();

        String rawTransaction = tx.rawTransaction();
```

#### build transaction with issuance input
```
        IssuanceInput issuanceInput = new IssuanceInput();
        issuanceInput.setAssetId("7b38dc897329a288ea31031724f5c55bcafec80468a546955023380af2faad14");
        issuanceInput.setAmount(100000000000L);
        
        // issuance program
        issuanceInput.setProgram("ae2054a71277cc162eb3eb21b5bd9fe54402829a53b294deaed91692a2cd8a081f9c5151ad");
        issuanceInput.setNonce("ac9d5a527f5ab00a");
        
        // asset index
        issuanceInput.setKeyIndex(5);
        
        // raw asset definition
        issuanceInput.setRawAssetDefinition("7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d");
        
        // provide a root private key for signing
        issuanceInput.setRootPrivateKey("4864bae85cf38bfbb347684abdbc01e311a24f99e2c7fe94f3c071d9c83d8a5a349722316972e382c339b79b7e1d83a565c6b3e7cf46847733a47044ae493257");

        SpendInput spendInput = new SpendInput();
        spendInput.setAssetId(btmAssetID);
        spendInput.setAmount(9800000000L);
        spendInput.setProgram("0014cb9f2391bafe2bc1159b2c4c8a0f17ba1b4dd94e");
        spendInput.setKeyIndex(1);
        spendInput.setChange(true);
        spendInput.setSourceID("4b5cb973f5bef4eadde4c89b92ee73312b940e84164da0594149554cc8a2adea");
        spendInput.setSourcePosition(2);
        spendInput.setControlProgramIndex(457);
        spendInput.setRootPrivateKey("4864bae85cf38bfbb347684abdbc01e311a24f99e2c7fe94f3c071d9c83d8a5a349722316972e382c339b79b7e1d83a565c6b3e7cf46847733a47044ae493257");

        Transaction tx = new Transaction.Builder()
                .addInput(issuanceInput)
                .addInput(spendInput)
                .addOutput(new Output("7b38dc897329a288ea31031724f5c55bcafec80468a546955023380af2faad14", 100000000000L, "001437e1aec83a4e6587ca9609e4e5aa728db7007449"))
                .addOutput(new Output(btmAssetID, 9700000000L, "00148be1104e04734e5edaba5eea2e85793896b77c56"))
                .setTimeRange(0)
                .build();

        String rawTx = tx.rawTransaction();

```