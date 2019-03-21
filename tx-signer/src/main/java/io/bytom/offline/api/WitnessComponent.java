package io.bytom.offline.api;

import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

/**
 * A single witness component, holding information that will become the input
 * witness.
 */
public class WitnessComponent {

    /**
     * The list of witnesses made with the specified keys (null unless type is
     * "signature").
     */
    private List<String> witnesses;

    private String rootPrivateKey;

    public WitnessComponent() {
        witnesses = new ArrayList<>();
    }

    public byte[][] toByteArray() {
        byte[][] byteArray = new byte[witnesses.size()][];
        for (int i = 0; i < witnesses.size(); i++) {
            byteArray[i] = Hex.decode(witnesses.get(i));
        }
        return byteArray;
    }

    public String getWitness(int index) {
        return witnesses.get(index);
    }

    public void appendWitness(String witness) {
        witnesses.add(witness);
    }

    public String getRootPrivateKey() {
        return rootPrivateKey;
    }

    public void setRootPrivateKey(String rootPrivateKey) {
        this.rootPrivateKey = rootPrivateKey;
    }
}