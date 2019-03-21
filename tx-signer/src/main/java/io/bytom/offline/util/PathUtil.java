package io.bytom.offline.util;

import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PathUtil {
    private static String intToPath(int index) {
        byte[] result = new byte[4];
        result[3] = (byte) (index >> 24 & 0xff);
        result[2] = (byte) (index >> 16 & 0xff);
        result[1] = (byte) (index >> 8 & 0xff);
        result[0] = (byte) (index >> 0 & 0xff);
        return Hex.toHexString(result);
    }


//  paths = {
////            "2c000000",
////            "99000000",
////            "01000000",  accountIndex
////            "00000000",  change
////            "01000000"   controlProgramIndex
////    }

    public static byte[][] getBip44Path(int accountIndex, boolean change, int programIndex) {

        String accountIndexStr = intToPath(accountIndex);
        String changeStr = "00000000";
        String programIndexStr = intToPath(programIndex);
        if (change) {
            changeStr = "01000000";
        }
        return new byte[][]{
                Hex.decode("2c000000"),
                Hex.decode("99000000"),
                Hex.decode(accountIndexStr),
                Hex.decode(changeStr),
                Hex.decode(programIndexStr),
        };
    }

    public static byte[][] getBip32Path(byte keySpace, long accountIndex, long ...itemIndexes) {
        byte[] signerPath = new byte[9];
        signerPath[0] = keySpace;
        byte[] path = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(accountIndex).array();
        System.arraycopy(path, 0, signerPath, 1, 8);

        byte[][] res = new byte[1 + itemIndexes.length][];
        res[0] = signerPath;
        for (int i = 0; i < itemIndexes.length; i++) {
            path = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(itemIndexes[i]).array();
            res[i + 1] = path;
        }
        return res;
    }
}
