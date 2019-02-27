package io.bytom.types;

import com.google.gson.Gson;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class AssetTest {
    String rootkey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";

    @Test
    public void testCreateAsset() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Object o = new Object();
        Asset.Definition definition = new Asset.Definition();

//        "definition": {
//            "decimals": 8,
//            "description": "Bytom Official Issue",
//            "name": "BTM",
//            "symbol": "BTM"
//        }
        definition.setDecimals(8);
        //{}
        definition.setDescription(o);
        definition.setName("");
        definition.setSymbol("");

        Asset asset = new Asset();
        asset.CreateAsset(definition, 2, rootkey);
        Gson gson = new Gson();
        String s = gson.toJson(asset);
        System.out.println(s);
    }
}
