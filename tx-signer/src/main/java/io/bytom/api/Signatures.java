package io.bytom.api;

public interface Signatures {

    /**
     * return signed transaction
     *
     * @param privateKeys
     * @param template
     * @param decodedTx
     * @return
     */
    public Template generateSignatures(String[] privateKeys, Template template, RawTransaction decodedTx);
}
