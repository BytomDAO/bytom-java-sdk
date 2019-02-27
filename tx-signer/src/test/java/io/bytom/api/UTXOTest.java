package io.bytom.api;

import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class UTXOTest {
    @Test
    public void testJson() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String json = "{\n" +
                "  \"id\": \"fda38648c553386c56b2f1276b908061b5d812341f0a96921abad8b2b2f28044\",\n" +
                "  \"amount\": 1700000,\n" +
                "  \"address\": \"tm1qhw9q89exmudkf9ecaxtnmv22fd8af0k07jq7u5\",\n" +
                "  \"program\": \"0014bb8a039726df1b649738e9973db14a4b4fd4becf\",\n" +
                "  \"change\": true,\n" +
                "  \"highest\": 139744,\n" +
                "  \"account_alias\": \"wyjbtm\",\n" +
                "  \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "  \"asset_alias\": \"BTM\",\n" +
                "  \"account_id\": \"0NNSS39M00A02\",\n" +
                "  \"control_program_index\": 26,\n" +
                "  \"source_id\": \"34bc595dff3d40c2bd644e0ea0234e843ef8e3aa0720013a2cb712362cc5933f\",\n" +
                "  \"source_pos\": 0,\n" +
                "  \"valid_height\": 0,\n" +
                "  \"derive_rule\": 0\n" +
                "}";

        UTXO utxo = UTXO.fromJson(json);
        System.out.println(utxo.toJson());
    }


}
