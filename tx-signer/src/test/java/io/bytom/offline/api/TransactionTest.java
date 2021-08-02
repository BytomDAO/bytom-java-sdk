package io.bytom.offline.api;

import io.bytom.offline.util.AssetIdUtil;
import org.junit.Test;

public class TransactionTest {
    String btmAssetID = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

    @Test
    public void testIssuance() {
        String rawAssetDefinition = "7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022676f6c64222c0a20202271756f72756d223a20312c0a20202272656973737565223a202266616c7365222c0a20202273796d626f6c223a2022474f4c44220a7d";
        String issuanceProgram = "0204bfcda069ae2000cf50143dac994b9cfc1a2c45a4332ecefbbb15824b9f1a537f0b49fd9f44b45151ad";
        System.out.println(AssetIdUtil.computeAssetID(rawAssetDefinition,issuanceProgram));
        IssuanceInput issuanceInput = new IssuanceInput("0541db69c21dc827092ddbc5673f5c1f0a09d3112da2a67c6644ec1be3fa38b3",2543541111111L,"0204bfcda069ae2000cf50143dac994b9cfc1a2c45a4332ecefbbb15824b9f1a537f0b49fd9f44b45151ad");
        issuanceInput.setVmVersion(1);
        issuanceInput.setRootPrivateKey("b0cc39f8a4b9539fcc8f05c0df21563155767bfc6f2c4b801738eb831589d84afe549d3a89e8223762445a0f3bea5c192675636846f75f1455c6a23d30a37023");
        issuanceInput.setKeyIndex(3);
        issuanceInput.setRawAssetDefinition("7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022676f6c64222c0a20202271756f72756d223a20312c0a20202272656973737565223a202266616c7365222c0a20202273796d626f6c223a2022474f4c44220a7d");
        issuanceInput.setNonce("");

        SpendInput spendInput = new SpendInput();
        spendInput.setAssetId(btmAssetID);
        spendInput.setAmount(2853881270l);
        spendInput.setSourceID("0b75ee155f60160ac14e038eaf7c67c820d77096a615ab00c1a3d9b52f9f246c");
        spendInput.setSourcePosition(0);
        spendInput.setChange(false);
        spendInput.setVmVersion(1);
        spendInput.setProgram("0014e933e5a0545c63ff2c28eaedb47c831cac3b16cf");
        spendInput.setControlProgramIndex(2);
        spendInput.setRootPrivateKey("b0cc39f8a4b9539fcc8f05c0df21563155767bfc6f2c4b801738eb831589d84afe549d3a89e8223762445a0f3bea5c192675636846f75f1455c6a23d30a37023");
        spendInput.setKeyIndex(1);

        OriginalOutput issuanceOutput = new OriginalOutput("0541db69c21dc827092ddbc5673f5c1f0a09d3112da2a67c6644ec1be3fa38b3",2543541111111L,"00145894d753c19d8fccce04db88f54751340ad8ca4f");
        OriginalOutput output = new OriginalOutput(btmAssetID,2753881270L,"0014b0b9455c1f77476b96858927d98e823d680ce889");

        Transaction tx = new Transaction.Builder()
                .addInput(issuanceInput)
                .addInput(spendInput)
                .addOutput(issuanceOutput)
                .addOutput(output)
                .setTimeRange(0)
                .setSize(0)
                .build();

        String rawTransaction = tx.rawTransaction();
        System.out.println(rawTransaction);
    }

    @Test
    public void testSpendBIP44() {
        SpendInput spendInput = new SpendInput();
        spendInput.setAssetId(btmAssetID);
        spendInput.setAmount(2873881270l);
        spendInput.setSourceID("6dd143552ff8fde887e65eb7c387ff77265d270dfc76d6488a0185476f66c601");
        spendInput.setSourcePosition(0);
        spendInput.setChange(false);
        spendInput.setVmVersion(1);
        spendInput.setProgram("0014e933e5a0545c63ff2c28eaedb47c831cac3b16cf");
        spendInput.setControlProgramIndex(2);
        spendInput.setRootPrivateKey("b0cc39f8a4b9539fcc8f05c0df21563155767bfc6f2c4b801738eb831589d84afe549d3a89e8223762445a0f3bea5c192675636846f75f1455c6a23d30a37023");
        spendInput.setKeyIndex(1);


        OriginalOutput output = new OriginalOutput(btmAssetID,2773881270l,"0014b0b9455c1f77476b96858927d98e823d680ce889");
        Transaction tx = new Transaction.Builder()
                .addInput(spendInput)
                .addOutput(output)
                .setTimeRange(0)
                .setSize(0)
                .build();
        System.out.println(tx.rawTransaction());
        System.out.println(tx.getTxID());
    }
}
