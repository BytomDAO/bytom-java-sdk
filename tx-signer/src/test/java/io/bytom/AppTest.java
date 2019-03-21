package io.bytom;

import io.bytom.offline.api.*;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class AppTest {
    String rootKey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
    String btmAssetID = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";


    @Test
    public void testSpendBIP44() {
        SpendInput input = new SpendInput();
        input.setAssetId(btmAssetID);
        input.setAmount(9800000000L);
        input.setProgram("0014cb9f2391bafe2bc1159b2c4c8a0f17ba1b4dd94e");
        input.setSourcePosition(2);
        input.setSourceID("4b5cb973f5bef4eadde4c89b92ee73312b940e84164da0594149554cc8a2adea");
        input.setChange(true);
        input.setControlProgramIndex(457);
        input.setKeyIndex(1);
        input.setRootPrivateKey("4864bae85cf38bfbb347684abdbc01e311a24f99e2c7fe94f3c071d9c83d8a5a349722316972e382c339b79b7e1d83a565c6b3e7cf46847733a47044ae493257");

        Transaction tx = new Transaction.Builder()
                .addInput(input)
                .addOutput(new Output(btmAssetID, 8800000000L, "0014a82f02bc37bc5ed87d5f9fca02f8a6a7d89cdd5c"))
                .addOutput(new Output(btmAssetID, 900000000L, "00200824e931fb806bd77fdcd291aad3bd0a4493443a4120062bd659e64a3e0bac66"))
                .setTimeRange(0)
                .build();

        String rawTransaction = tx.rawTransaction();
        assert rawTransaction.equals("070100010160015e4b5cb973f5bef4eadde4c89b92ee73312b940e84164da0594149554cc8a2adeaffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80c480c1240201160014cb9f2391bafe2bc1159b2c4c8a0f17ba1b4dd94e6302401cb779288be890a28c5209036da1a27d9fe74a51c38e0a10db4817bcf4fd05f68580239eea7dcabf19f144c77bf13d3674b5139aa51a99ba58118386c190af0e20bcbe020b05e1b7d0825953d92bf47897be08cd7751a37adb95d6a2e5224f55ab02013dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80b095e42001160014a82f02bc37bc5ed87d5f9fca02f8a6a7d89cdd5c000149ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80d293ad03012200200824e931fb806bd77fdcd291aad3bd0a4493443a4120062bd659e64a3e0bac6600");
    }

    @Test
    public void testSpendBIP32() {
        SpendInput input = new SpendInput(btmAssetID, 11718900000L, "0014085a02ecdf934a56343aa59a3dec9d9feb86ee43");
        input.setSourceID("5ac79a73db78e5c9215b37cb752f0147d1157c542bb4884908ceb97abc33fe0a");
        input.setSourcePosition(0);
        input.setRootPrivateKey("4864bae85cf38bfbb347684abdbc01e311a24f99e2c7fe94f3c071d9c83d8a5a349722316972e382c339b79b7e1d83a565c6b3e7cf46847733a47044ae493257");
        input.setChange(true);
        input.setKeyIndex(1);
        input.setBipProtocol(BIPProtocol.BIP32);
        input.setControlProgramIndex(447);

        Transaction tx = new Transaction.Builder()
                .addInput(input)
                .addOutput(new Output(btmAssetID, 1718900000L, "001409a0961e9b592a944ca3ded0ef9403fdb25b3793"))
                .addOutput(new Output(btmAssetID, 9900000000L, "00145ade29df622cc68d0473aa1a20fb89690451c66e"))
                .setTimeRange(0)
                .build();

        String rawTx = tx.rawTransaction();
        assert rawTx.equals("070100010160015e5ac79a73db78e5c9215b37cb752f0147d1157c542bb4884908ceb97abc33fe0affffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffa0f280d42b0001160014085a02ecdf934a56343aa59a3dec9d9feb86ee43630240e37864ef905e943deb97e58b861c97f04f07c1d33b1ce4f1b6edddff0c8956a57d86fc922c45446cd38ea613fc3c9b7d5a2596f3dca7a7212f6da55b9515110420a29601468f08c57ca9c383d28736a9d5c7737cd483126d8db3d85490fe497b3502013dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffa0aad1b3060116001409a0961e9b592a944ca3ded0ef9403fdb25b379300013dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8086d8f024011600145ade29df622cc68d0473aa1a20fb89690451c66e00");
    }

    //issue asset
    @Test
    public void testIssue() {
        IssuanceInput issuanceInput = new IssuanceInput();
        issuanceInput.setAssetId("7b38dc897329a288ea31031724f5c55bcafec80468a546955023380af2faad14");
        issuanceInput.setAmount(100000000000L);
        issuanceInput.setProgram("ae2054a71277cc162eb3eb21b5bd9fe54402829a53b294deaed91692a2cd8a081f9c5151ad");
        issuanceInput.setNonce("ac9d5a527f5ab00a");
        issuanceInput.setKeyIndex(5);
        issuanceInput.setRawAssetDefinition("7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d");
        issuanceInput.setRootPrivateKey("4864bae85cf38bfbb347684abdbc01e311a24f99e2c7fe94f3c071d9c83d8a5a349722316972e382c339b79b7e1d83a565c6b3e7cf46847733a47044ae493257");

        SpendInput spendInput = new SpendInput(btmAssetID, 9800000000L, "0014cb9f2391bafe2bc1159b2c4c8a0f17ba1b4dd94e");
        spendInput.setBipProtocol(BIPProtocol.BIP32);
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
        assert rawTx.equals("0701000201300008ac9d5a527f5ab00a7b38dc897329a288ea31031724f5c55bcafec80468a546955023380af2faad1480d0dbc3f402b001467b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d0125ae2054a71277cc162eb3eb21b5bd9fe54402829a53b294deaed91692a2cd8a081f9c5151ad01401ba3a3f2d3e9887e20da8d43666cc1602bb5421ffea9d2b4d7df9816fc174a43b25fb00db4775b10b679a8b5f8aa28555ebb8fb49f6275d43283daf8f5ac340d0160015e4b5cb973f5bef4eadde4c89b92ee73312b940e84164da0594149554cc8a2adeaffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80c480c1240201160014cb9f2391bafe2bc1159b2c4c8a0f17ba1b4dd94e630240341c875fc815dc13ad811c9aa7c7af28a5c78765f77fd5a36dac263278fd605ae0553e34a1e645148370b7b397142ddbcd67ba33483279ab9894169b51e4f101201381d35e235813ad1e62f9a602c82abee90565639cc4573568206b55bcd2aed902013e7b38dc897329a288ea31031724f5c55bcafec80468a546955023380af2faad1480d0dbc3f4020116001437e1aec83a4e6587ca9609e4e5aa728db700744900013dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8082a99124011600148be1104e04734e5edaba5eea2e85793896b77c5600");
    }


    //retire asset
    @Test
    public void testRetire() {
        String arbitrary = "77656c636f6d65efbc8ce6aca2e8bf8ee69da5e588b0e58e9fe5ad90e4b896e7958c";
        String retireControlProgram = "6a"+Integer.toString(Hex.decode(arbitrary).length,16)+arbitrary;
        String assetId1 = "207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf";

        SpendInput input1 = new SpendInput(btmAssetID, 289100000L, "0014f1dc52048f439ac7fd74f8106a21da78f00de48f");
        input1.setRootPrivateKey(rootKey);
        input1.setChange(true);
        input1.setControlProgramIndex(41);
        input1.setSourceID("0b2cff11d1d056d95237a5f2d06059e5395e86f60e69c1e8201ea624911c0c65");
        input1.setSourcePosition(0);

        SpendInput input2 = new SpendInput(assetId1, 70000000000L, "0014bb8a039726df1b649738e9973db14a4b4fd4becf");
        input2.setRootPrivateKey(rootKey);
        input2.setChange(true);
        input2.setControlProgramIndex(26);
        input2.setSourceID("be0ac837e832c34a02968e54dab4f95cbeceb9fb01cd378310f6ea32219ee29b");
        input2.setSourcePosition(1);

        Output output1 = new Output(btmAssetID, 279100000L, "001414d362694eacfa110dc20dec77d610d22340f95b");
        Output output2 = new Output(assetId1, 10000000000L, retireControlProgram);
        Output output3 = new Output(assetId1, 60000000000L, "0014bb8a039726df1b649738e9973db14a4b4fd4becf");

        Transaction transaction = new Transaction.Builder()
                .addInput(input1)
                .addInput(input2)
                .addOutput(output1)
                .addOutput(output2)
                .addOutput(output3)
                .setTimeRange(2000000)
                .build();

        String rawTransaction = transaction.rawTransaction();
        assert  rawTransaction.equals("070180897a020160015e0b2cff11d1d056d95237a5f2d06059e5395e86f60e69c1e8201ea624911c0c65ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0a1ed89010001160014f1dc52048f439ac7fd74f8106a21da78f00de48f6302401660121218ab96d9f22cce712541ca34c53f4da40450669854341ca9624ad1cf10d1bfc96449fad5406224afd253ccfbdeab683f7ec7f9ee8f45e47a0c58500f2031ecc1bdd5fb9b40016358340b87646ea39faf55c0c105205cfdfdc6184725f40161015fbe0ac837e832c34a02968e54dab4f95cbeceb9fb01cd378310f6ea32219ee29b207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf80f8cce284020101160014bb8a039726df1b649738e9973db14a4b4fd4becf630240d7b7f1c2ca1048fd6798234f2a1e895762f83e802507a008eff52605611b67390a74eaf228b76f5589ff109b2c20eaa65fad6de2e5ab8a25b54267b607df970b20a71547e1064b5edaad92cdce6b0ace832836ba28fdeaf0b83010bed247fe927c03013dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0f48a85010116001414d362694eacfa110dc20dec77d610d22340f95b00014b207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf80c8afa02501246a2277656c636f6d65efbc8ce6aca2e8bf8ee69da5e588b0e58e9fe5ad90e4b896e7958c00013e207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf80b09dc2df0101160014bb8a039726df1b649738e9973db14a4b4fd4becf00");
    }
}
