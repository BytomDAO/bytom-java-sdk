package io.bytom.offline.api;

import io.bytom.offline.common.Utils;
import io.bytom.offline.types.*;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class CoinbaseInput extends BaseInput{
    private String arbitrary;

    @Override
    public InputEntry toInputEntry(Map<Hash, Entry> entryMap, int index) {
        Coinbase coinbase =  new Coinbase(arbitrary);

        Hash prevOutID = coinbase.entryID();
        entryMap.put(prevOutID, coinbase);
        return new Coinbase(prevOutID,index);
    }

    @Override
    public void buildWitness(String txID) throws Exception {

    }

    @Override
    public byte[] serializeInputCommitment() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Utils.writeVarint(Coinbase_INPUT_TYPE, stream);
        Utils.writeVarStr(Hex.decode(this.arbitrary),stream);
        return stream.toByteArray();
    }

    @Override
    public byte[] serializeInputWitness() throws IOException {
        return new byte[0];
    }

    public String getArbitrary() {
        return arbitrary;
    }

    public void setArbitrary(String arbitrary) {
        this.arbitrary = arbitrary;
    }
}
