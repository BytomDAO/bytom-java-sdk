package io.bytom.offline.api;

import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

public class StateData {
    private List<String> stateData;

    public StateData() {
        stateData = new ArrayList<>();
    }

    public byte[][] toByteArray() {
        byte[][] byteArray = new byte[stateData.size()][];
        for (int i = 0; i < stateData.size(); i++) {
            byteArray[i] = Hex.decode(stateData.get(i));
        }
        return byteArray;
    }

    public void appendStateData(String stateDataStr) {
        stateData.add(stateDataStr);
    }
}
