package io.bytom.common;

import com.google.crypto.tink.subtle.Ed25519;

public class DeriveXpub {
    public static byte[] deriveXpub(byte[] xprv) {
        byte[] xpub = new byte[xprv.length];
        byte[] scalar = new byte[xprv.length / 2];

        System.arraycopy(xprv, 0, scalar, 0, xprv.length / 2);
        byte[] buf = Ed25519.scalarMultWithBaseToBytes(scalar);

        System.arraycopy(buf, 0, xpub, 0, buf.length);
        System.arraycopy(xprv, xprv.length / 2, xpub, xprv.length / 2, xprv.length / 2);
        return xpub;
    }
}
