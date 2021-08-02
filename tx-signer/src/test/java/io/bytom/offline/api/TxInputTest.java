package io.bytom.offline.api;

import com.amazonaws.util.StringUtils;
import com.google.crypto.tink.subtle.Hex;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TxInputTest {
    @Test
    public void testSpendInput() throws IOException {
        SpendInput input = new SpendInput();
        input.setAssetId("fe9791d71b67ee62515e08723c061b5ccb952a80d804417c8aeedf7f633c524a");
        input.setAmount(254354);
        input.setSourceID("fad5195a0c8e3b590b86a3c0a95e7529565888508aecca96e9aeda633002f409");
        input.setSourcePosition(3);
        input.setVmVersion(1);
        input.setProgram(Hex.encode("spendProgram".getBytes()));
        input.appendStateData(Hex.encode("stateData".getBytes()));
        input.appendWitnessComponent(Hex.encode("arguments1".getBytes()));
        input.appendWitnessComponent(Hex.encode("arguments2".getBytes()));

        byte[] serializeInputCommitment = input.serializeInput();
        String wantStr= StringUtils.join("",
                "01", // asset version
                "5f", // input commitment length
                "01", // spend type flag
                "5d", // spend commitment length
                "fad5195a0c8e3b590b86a3c0a95e7529565888508aecca96e9aeda633002f409", // source id
                "fe9791d71b67ee62515e08723c061b5ccb952a80d804417c8aeedf7f633c524a", // assetID
                "92c30f",                   // amount
                "03",                       // source position
                "01",                       // vm version
                "0c",                       // spend program length
                "7370656e6450726f6772616d", // spend program
                "0109",                     // state length
                "737461746544617461",       // state
                "17",                       // witness length
                "02",                       // argument array length
                "0a",                       // first argument length
                "617267756d656e747331",     // first argument data
                "0a",                       // second argument length
                "617267756d656e747332"      // second argument data
        );

        assertEquals(wantStr,Hex.encode(serializeInputCommitment));
    }

    @Test
    public void testIssuanceInput() throws IOException {
        IssuanceInput issuanceInput = new IssuanceInput();
        issuanceInput.setAmount(254354L);
        issuanceInput.setVmVersion(1);
        issuanceInput.setProgram(Hex.encode("issuanceProgram".getBytes()));
        issuanceInput.setNonce(Hex.encode("nonce".getBytes()));
        issuanceInput.setRawAssetDefinition(Hex.encode("assetDefinition".getBytes()));
        issuanceInput.appendWitnessComponent(Hex.encode("arguments1".getBytes()));
        issuanceInput.appendWitnessComponent(Hex.encode("arguments2".getBytes()));

        byte[] serializeInputCommitment = issuanceInput.serializeInput();
        String wantStr= StringUtils.join("",
                "01",         // asset version
                "2a",         // serialization length
                "00",         // issuance type flag
                "05",         // nonce length
                "6e6f6e6365", // nonce
                "a69849e11add96ac7053aad22ba2349a4abf5feb0475a0afcadff4e128be76cf", // assetID
                "92c30f",                         // amount
                "38",                             // input witness length
                "0f",                             // asset definition length
                "6173736574446566696e6974696f6e", // asset definition
                "01",                             // vm version
                "0f",                             // issuanceProgram length
                "69737375616e636550726f6772616d", // issuance program
                "02",                             // argument array length
                "0a",                             // first argument length
                "617267756d656e747331",           // first argument data
                "0a",                             // second argument length
                "617267756d656e747332"            // second argument data
        );

        assertEquals(wantStr,Hex.encode(serializeInputCommitment));
    }

    @Test
    public void testVetoInput() throws IOException {
        VetoInput vetoInput = new VetoInput();
        vetoInput.setSourceID("fad5195a0c8e3b590b86a3c0a95e7529565888508aecca96e9aeda633002f409");
        vetoInput.setAssetId("fe9791d71b67ee62515e08723c061b5ccb952a80d804417c8aeedf7f633c524a");
        vetoInput.setSourcePosition(3);
        vetoInput.setAmount(254354L);
        vetoInput.setVmVersion(1);
        vetoInput.setProgram(Hex.encode("spendProgram".getBytes()));
        vetoInput.setVote("af594006a40837d9f028daabb6d589df0b9138daefad5683e5233c2646279217294a8d532e60863bcf196625a35fb8ceeffa3c09610eb92dcfb655a947f13269".getBytes());
        vetoInput.appendWitnessComponent(Hex.encode("arguments1".getBytes()));
        vetoInput.appendWitnessComponent(Hex.encode("arguments2".getBytes()));


        byte[] serializeInputCommitment = vetoInput.serializeInput();

        String wantStr= StringUtils.join("",
                "01",   // asset version
                "d701", // input commitment length
                "03",   // veto type flag
                "53",   // veto commitment length
                "fad5195a0c8e3b590b86a3c0a95e7529565888508aecca96e9aeda633002f409", // source id
                "fe9791d71b67ee62515e08723c061b5ccb952a80d804417c8aeedf7f633c524a", // assetID
                "92c30f",                   // amount
                "03",                       // source position
                "01",                       // vm version
                "0c",                       // veto program length
                "7370656e6450726f6772616d", // veto program
                "00",                       // state length
                "8001",                     //xpub length
                "6166353934303036613430383337643966303238646161626236643538396466306239313338646165666164353638336535323333633236343632373932313732393461386435333265363038363362636631393636323561333566623863656566666133633039363130656239326463666236353561393437663133323639", //voter xpub
                "17",                   // witness length
                "02",                   // argument array length
                "0a",                   // first argument length
                "617267756d656e747331", // first argument data
                "0a",                   // second argument length
                "617267756d656e747332" // second argument data
        );

        assertEquals(wantStr,Hex.encode(serializeInputCommitment));
    }

    @Test
    public void testCoinbaseInput() throws IOException {
        CoinbaseInput coinbaseInput = new CoinbaseInput();
        coinbaseInput.setArbitrary(Hex.encode("arbitrary".getBytes()));

        byte[] serializeInputCommitment = coinbaseInput.serializeInput();

        String wantStr= StringUtils.join("",
                "01",                 // asset version
                "0b",                 // input commitment length
                "02",                 // coinbase type flag
                "09",                 // arbitrary length
                "617262697472617279", // arbitrary data
                "00"                // witness length
        );

        assertEquals(wantStr,Hex.encode(serializeInputCommitment));
    }
}
