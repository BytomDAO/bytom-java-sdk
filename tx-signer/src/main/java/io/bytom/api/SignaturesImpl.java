package io.bytom.api;

import io.bytom.common.DeriveXpub;
import io.bytom.common.ExpandedPrivateKey;
import io.bytom.common.FindDst;
import io.bytom.common.NonHardenedChild;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

public class SignaturesImpl implements Signatures {

    private static Logger logger = Logger.getLogger(SignaturesImpl.class);

    @Override
    public Template generateSignatures(String[] privateKeys, Template template, RawTransaction decodedTx) {
        Template result = template;
        for (int i = 0; i < template.signingInstructions.size(); i++) {
            Template.SigningInstruction sigIns = template.signingInstructions.get(i);
            for (Template.WitnessComponent wc : sigIns.witnessComponents) {

                // Have two cases
                switch (wc.type) {
                    case "raw_tx_signature":
                        logger.info("=====raw_tx_signature");
                        logger.info("keys.length: "+wc.keys.length);
                        if (wc.signatures==null || wc.signatures.length < wc.keys.length) {
                            wc.signatures = new String[wc.keys.length];
                        }
                        // 一个input对应一个Template.WitnessComponent
                        String input = decodedTx.inputs.get(sigIns.position).inputID;
                        String tx_id = decodedTx.txID;
                        byte[] message = decodedTx.hashFn(Hex.decode(input), Hex.decode(tx_id));
                        for (int j = 0; j < wc.keys.length; j++) {
                            if (wc.signatures[j] == null || wc.signatures[j].isEmpty()) {

                                byte[] sig = new byte[64];
                                try {
                                    String publicKey = wc.keys[j].xpub;
                                    // 多签情况下，找到xpub对应的private key的下标 dst
                                    int dst = FindDst.find(privateKeys, publicKey);
                                    //一级私钥
                                    byte[] privateKey = Hex.decode(privateKeys[dst]);
                                    // 一级私钥推出二级私钥
                                    String[] hpaths = wc.keys[j].derivationPath;
                                    byte[] childXprv = NonHardenedChild.child(privateKey, hpaths);
                                    // 一级私钥推出公钥
                                    byte[] xpub = DeriveXpub.deriveXpub(privateKey);
                                    // 二级私钥得到扩展私钥
                                    byte[] expandedPrv = ExpandedPrivateKey.ExpandedPrivateKey(childXprv);
                                    logger.info("privateKey: "+Hex.toHexString(privateKey));
                                    logger.info("childXpriv: "+Hex.toHexString(childXprv));
                                    logger.info("xpub: "+Hex.toHexString(xpub));
                                    logger.info("message: "+Hex.toHexString(message));
//                                    sig = com.google.crypto.tink.subtle.Ed25519.sign(message, xpub, expandedPrv);
                                    sig = Signer.Ed25519InnerSign(expandedPrv, message);
                                    logger.info("sig google: "+Hex.toHexString(sig));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                logger.info("sig:"+Hex.toHexString(sig));
                                wc.signatures[j] = Hex.toHexString(sig);
                                result.signingInstructions.get(i).witnessComponents[j].signatures = wc.signatures;
                            }

                        }
                        break;
                    case "":

                        break;
                    default:

                }
            }
        }
        return result;
    }



}
