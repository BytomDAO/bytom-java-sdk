package io.bytom.util;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class SHA3256Test {
    @Test
    public void testSHA3Hash() {
        String rawTransaction = "7b0a202022646563696d616c73223a20382c0a202022646573637" +
                "2697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d";
        byte[] bytes = SHA3Util.hashSha256(Hex.decode(rawTransaction));
        System.out.println(Hex.toHexString(bytes));
        //expected 69ab19b3907f40e4f264dbd3f71967654e0e93f836026918af8861932ed0409b
    }


}
