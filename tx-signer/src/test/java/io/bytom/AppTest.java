package io.bytom;

import io.bytom.api.*;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class AppTest {
    String rootKey = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
    String btmAssetID = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";


    @Test
    public void testSpend() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Transaction.AnnotatedInput input = btmUtxoToInput();
        Transaction Transaction = new Transaction.Builder()
//                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
//                        setChange(false).setControlProgramIndex(2).setSourceId("fc43933d1c601b2503b033e31d3bacfa5c40ccb2ff0be6e94d8332462e0928a3").setSourcePosition(0))
                .addInput(input.setType(1))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(49100000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(10000000).setControlProgram("0020fa56ca7d47f8528e68e120d0e052885faeb9d090d238fa4266bdde21b137513c"))
                .build(200000);
        io.bytom.api.Transaction transaction = MapTransaction.mapTx(Transaction);
        SignTransaction signTransaction = new SignTransaction();
        String rawTransaction = signTransaction.rawTransaction(rootKey, transaction);
        System.out.println(rawTransaction);
    }

    //issue asset
    @Test
    public void testIssue() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        String issueAssetId = "a680606d49daae62ef9cb03263ca82a0b1e3184bb6311ea52a5189207f718789";
        String program = "ae204cae24c2cec15491e70fc554026469496e373df9b9970b23acac8b782da0822d5151ad";
        String assetDefine = "7b0a202022646563696d616c73223a20382c0a2020226465736372697074696f6e223a207b7d2c0a2020226e616d65223a2022222c0a20202273796d626f6c223a2022220a7d";
        Transaction.AnnotatedInput input = btmUtxoToInput();
        Transaction Transaction = new Transaction.Builder()
                .addInput(new Transaction.AnnotatedInput().setType(0).setAssetId(issueAssetId).setControlProgram(program).setAmount(100000000).setAssetDefinition(assetDefine).setChange(false).setKeyIndex(13))
//                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
//                        setChange(false).setControlProgramIndex(2).setSourceId("fc43933d1c601b2503b033e31d3bacfa5c40ccb2ff0be6e94d8332462e0928a3").setSourcePosition(0))
                .addInput(input.setType(1))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(issueAssetId).setAmount(100000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
//                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(870000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .build(2000000);
        MapTransaction.mapTx(Transaction);
        SignTransaction sign = new SignTransaction();
        String rawTransaction = sign.rawTransaction(rootKey, Transaction);
        System.out.println(rawTransaction);
    }


    //retire asset
    @Test
    public void testRetire() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String arbitrary = "77656c636f6d65efbc8ce6aca2e8bf8ee69da5e588b0e58e9fe5ad90e4b896e7958c";
        String retireControlProgram = "6a"+Integer.toString(Hex.decode(arbitrary).length,16)+arbitrary;
        String assetId1 = "207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf";
        Transaction transaction = new Transaction.Builder()
                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(289100000).setControlProgram("0014f1dc52048f439ac7fd74f8106a21da78f00de48f").
                        setChange(true).setControlProgramIndex(41).setSourceId("0b2cff11d1d056d95237a5f2d06059e5395e86f60e69c1e8201ea624911c0c65").setSourcePosition(0))
                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(assetId1).setAmount(70000000000l).setControlProgram("0014bb8a039726df1b649738e9973db14a4b4fd4becf").
                        setChange(true).setControlProgramIndex(26).setSourceId("be0ac837e832c34a02968e54dab4f95cbeceb9fb01cd378310f6ea32219ee29b").setSourcePosition(1))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(279100000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(assetId1).setAmount(10000000000l).setControlProgram(retireControlProgram))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(assetId1).setAmount(60000000000l).setControlProgram("0014bb8a039726df1b649738e9973db14a4b4fd4becf"))
                .build(2000000);
        MapTransaction.mapTx(transaction);
        SignTransaction sign = new SignTransaction();
        String rawTransaction = sign.rawTransaction(rootKey, transaction);
        System.out.println(rawTransaction);
    }


    //utxo
    private Transaction.AnnotatedInput btmUtxoToInput() {
        String utxoJson = "\n" +
                "{\n" +
                "  \"id\": \"687e3c3ca1ee8139e57f43697db6aaeac95b10c75b828ef2fad30abe7d047e6a\",\n" +
                "  \"amount\": 10000000,\n" +
                "  \"address\": \"tm1qznfky62w4napzrwzphk804ss6g35p72mcw53q7\",\n" +
                "  \"program\": \"001414d362694eacfa110dc20dec77d610d22340f95b\",\n" +
                "  \"change\": false,\n" +
                "  \"highest\": 141905,\n" +
                "  \"account_alias\": \"wyjbtm\",\n" +
                "  \"asset_id\": \"ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff\",\n" +
                "  \"asset_alias\": \"BTM\",\n" +
                "  \"account_id\": \"0NNSS39M00A02\",\n" +
                "  \"control_program_index\": 2,\n" +
                "  \"source_id\": \"535b88d3f6b449fdba678b00b84d4b516df1da73104d689d41f964389f5a9217\",\n" +
                "  \"source_pos\": 1,\n" +
                "  \"valid_height\": 0,\n" +
                "  \"derive_rule\": 0\n" +
                "}";
        UTXO utxo = UTXO.fromJson(utxoJson);
        Transaction.AnnotatedInput input = UTXO.utxoToAnnotatedInput(utxo);
        return input;
    }

    private Transaction.AnnotatedInput otherAssetUtxoToInput() {
        String utxoJson = "\n" +
                "{\n" +
                "  \"id\": \"a1b729714a9ad74108999b6d2f3628c6eea2bd41d7132fca93f56c3f7c12904e\",\n" +
                "  \"amount\": 89900000000,\n" +
                "  \"address\": \"tm1qstadkjjxp7nn3eegdxc38nvur82z37ek2pq2ul\",\n" +
                "  \"program\": \"001482fadb4a460fa738e72869b113cd9c19d428fb36\",\n" +
                "  \"change\": true,\n" +
                "  \"highest\": 139905,\n" +
                "  \"account_alias\": \"wyjbtm\",\n" +
                "  \"asset_id\": \"207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf\",\n" +
                "  \"asset_alias\": \"TEST3\",\n" +
                "  \"account_id\": \"0NNSS39M00A02\",\n" +
                "  \"control_program_index\": 32,\n" +
                "  \"source_id\": \"2886e635442f2f003e1d211f3264520ffb5239944d718bc834925a0cc0798980\",\n" +
                "  \"source_pos\": 1,\n" +
                "  \"valid_height\": 0,\n" +
                "  \"derive_rule\": 0\n" +
                "}";
        UTXO utxo = UTXO.fromJson(utxoJson);
        Transaction.AnnotatedInput input = UTXO.utxoToAnnotatedInput(utxo);
        return input;
    }

    // submit rawTransaction
    @Test
    public void SubmitTransaction() throws BytomException {
        Client client = TestUtil.generateClient();
        String raw = "070180897a020160015e0b2cff11d1d056d95237a5f2d06059e5395e86f60e69c1e8201ea624911c0c65ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0a1ed89010001160014f1dc52048f439ac7fd74f8106a21da78f00de48f6302401660121218ab96d9f22cce712541ca34c53f4da40450669854341ca9624ad1cf10d1bfc96449fad5406224afd253ccfbdeab683f7ec7f9ee8f45e47a0c58500f2031ecc1bdd5fb9b40016358340b87646ea39faf55c0c105205cfdfdc6184725f40161015fbe0ac837e832c34a02968e54dab4f95cbeceb9fb01cd378310f6ea32219ee29b207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf80f8cce284020101160014bb8a039726df1b649738e9973db14a4b4fd4becf630240d7b7f1c2ca1048fd6798234f2a1e895762f83e802507a008eff52605611b67390a74eaf228b76f5589ff109b2c20eaa65fad6de2e5ab8a25b54267b607df970b20a71547e1064b5edaad92cdce6b0ace832836ba28fdeaf0b83010bed247fe927c03013dffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0f48a85010116001414d362694eacfa110dc20dec77d610d22340f95b00014b207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf80c8afa02501246a2277656c636f6d65efbc8ce6aca2e8bf8ee69da5e588b0e58e9fe5ad90e4b896e7958c00013e207265909236260b30942a6b00e30ceb769e0e58156b6482bac64117619c9dcf80b09dc2df0101160014bb8a039726df1b649738e9973db14a4b4fd4becf00";
        SubmitTransaction.SubmitResponse submitResponse = SubmitTransaction.submitRawTransaction(client, raw);
        System.out.println(submitResponse.tx_id);
    }


    //单输入多签
    @Test
    public void testMutiSpend(){
        Transaction.AnnotatedInput input = btmUtxoToInput();
        Transaction Transaction = new Transaction.Builder()
//                .addInput(new Transaction.AnnotatedInput().setType(1).setAssetId(btmAssetID).setAmount(880000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b").
//                        setChange(false).setControlProgramIndex(2).setSourceId("fc43933d1c601b2503b033e31d3bacfa5c40ccb2ff0be6e94d8332462e0928a3").setSourcePosition(0))
                .addInput(input.setType(1))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(80000000).setControlProgram("00204d505f3bb98a6022fa37e387204dba3b2917cf6df4d41c5622485c55b1b17813"))
                .addOutput(new Transaction.AnnotatedOutput().setAssetId(btmAssetID).setAmount(10000000).setControlProgram("001414d362694eacfa110dc20dec77d610d22340f95b"))
                .build(200000);
        Transaction transaction = MapTransaction.mapTx(Transaction);
        SignTransaction signTransaction = new SignTransaction();
        String[] rootKeys = new String[3];
        rootKeys[0] = "38d2c44314c401b3ea7c23c54e12c36a527aee46a7f26b82443a46bf40583e439dea25de09b0018b35a741d8cd9f6ec06bc11db49762617485f5309ab72a12d4";
        rootKeys[1] = "50a23bf6200b8a98afc049a7d0296a619e2ee27fa0d6d4d271ca244b280b324347627e543cc079614642c7b88c78ce38092430b01d124663e8b84026aefefde1";
        rootKeys[2] = "00e4bf1251fb5aa37aa2a11dec6c0db5cec3f17aa312dbddb30e06957a32ae503ebcdfd4ad5e29be21ee9ec336e939eb72439cf6d99c785268c8f3d71c1be877";
        signTransaction.buildWitness(transaction, 0, rootKeys);
        String raw = signTransaction.serializeTransaction(transaction);
        System.out.println(raw);
    }
}
