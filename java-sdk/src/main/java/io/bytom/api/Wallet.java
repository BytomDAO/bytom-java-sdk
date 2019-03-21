package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.offline.common.Utils;
import io.bytom.offline.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>Wallet Class</h1>
 */
public class Wallet {

    @SerializedName("account_image")
    public AccountImage accountImage;

    @SerializedName("asset_image")
    public AssetImage assetImage;

    @SerializedName("key_images")
    public KeyImages keyImages;

    private static Logger logger = Logger.getLogger(Wallet.class);

    /**
     * Serializes the Address into a form that is safe to transfer over the wire.
     *
     * @return the JSON-serialized representation of the Receiver object
     */
    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    /**
     * Call backup-wallet api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static Wallet backupWallet(Client client) throws BytomException {
        Wallet wallet = client.request("backup-wallet", null, Wallet.class);

        logger.info("backup-wallet:");
        logger.info(wallet.toJson());

        return wallet;
    }

    /**
     * Call restore-wallet api
     *
     * @param client
     * @param accountImage
     * @param assetImage
     * @param keyImages
     * @throws BytomException
     */
    public static void restoreWallet(Client client ,Object accountImage, Object assetImage , Object keyImages) throws BytomException{
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("account_image", accountImage);
        body.put("asset_image", assetImage);
        body.put("key_images", keyImages);

        logger.info("restore-wallet:");
        logger.info(body.toString());

        client.request("restore-wallet", body);
    }

    public static class AccountImage {

        public Slices[] slices;

        public static class Slices {

            @SerializedName("contract_index")
            public int contractIndex;

            public Account account;

            public static class Account {

                public String type;

                public List<String> xpubs;

                public int quorum;

                @SerializedName("key_index")
                public int keyIndex;

                public String id;

                public String alias;

            }

        }
    }

    public static class AssetImage {

        public Assets[] assets;

        public static class Assets {
            public String type;
            public List<String> xpubs;
            public int quorum;
            public String id;
            public String alias;
            public Map<String, Object> definition;
            @SerializedName("key_index")
            public int keyIndex;
            @SerializedName("vm_version")
            public int vmVersion;
            @SerializedName("asset_image")
            public String issueProgram;
            @SerializedName("raw_definition_byte")
            public String rawDefinitionByte;
        }
    }

    public static class KeyImages {

        public Xkeys[] xkeys;

        public static class Xkeys {

            public Crypto crypto;
            public String id;
            public String type;
            public int version;
            public String alias;
            public String xpub;

            public static class Crypto {
                public String cipher;
                public String ciphertext;
                public Map<String, Object> cipherparams;
                public String kdf;
                public Map<String, Object> kdfparams;
                public String mac;
            }

        }
    }

}
