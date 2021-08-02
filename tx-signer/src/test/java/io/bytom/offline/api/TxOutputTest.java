package io.bytom.offline.api;

import com.amazonaws.util.StringUtils;
import com.google.crypto.tink.subtle.Hex;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TxOutputTest {
    String assetID = "81756fdab39a17163b0ce582ee4ee256fb4d1e156c692b997d608a42ecb38d47";
    @Test
    public void  SerializationOriginalTxOutputTest() throws IOException {
        OriginalOutput output = new OriginalOutput(assetID,254354L,Hex.encode("TestSerializationTxOutput".getBytes()));
        output.appendStateData(Hex.encode("stateData".getBytes()));

        byte[] serializeOutputCommitment = output.serializeOutput();
        String wantStr= StringUtils.join("",
                "01", // asset version
                "00", // output type
                "49", // serialization length
                "81756fdab39a17163b0ce582ee4ee256fb4d1e156c692b997d608a42ecb38d47", // assetID
                "92c30f", // amount
                "01",     // version
                "19",     // control program length
                "5465737453657269616c697a6174696f6e54784f7574707574", // control program
                "0109",               // state data length
                "737461746544617461", // state data
                "00"                 // witness length
        );

        assertEquals(wantStr,Hex.encode(serializeOutputCommitment));
    }

    @Test
    public void  SerializationVetoOutputTest() throws IOException {
        OriginalOutput output = new OriginalOutput(assetID,254354L,Hex.encode("TestSerializationTxOutput".getBytes()));
        output.appendStateData(Hex.encode("stateData".getBytes()));

        byte[] serializeOutputCommitment = output.serializeOutput();
        String wantStr= StringUtils.join("",
                "01", // asset version
                "00", // output type
                "49", // serialization length
                "81756fdab39a17163b0ce582ee4ee256fb4d1e156c692b997d608a42ecb38d47", // assetID
                "92c30f", // amount
                "01",     // version
                "19",     // control program length
                "5465737453657269616c697a6174696f6e54784f7574707574", // control program
                "0109",               // state data length
                "737461746544617461", // state data
                "00"                 // witness length
        );

        assertEquals(wantStr,Hex.encode(serializeOutputCommitment));
    }
}
