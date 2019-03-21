
package io.bytom.api;

import io.bytom.types.*;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * Created by liqiang on 2018/10/24.
 */
public class SignTransactionTest {

    //以下为测试用的区块上的交易utxo，即output中第二个输出
    //新交易接收地址为bm1qdpc5sejdkm22uv23jwd8pg6lyqz2trz4trgxh0，需要找零
    /*{
        "id": "3b36453f7dc03b13523d6431afd7e544f60339daed52ba8fca7ebf88cd5e5939",
            "version": 1,
            "size": 330,
            "time_range": 0,
            "inputs": [
        {
            "type": "spend",
                "asset_id": "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "asset_definition": {},
            "amount": 482000000,
                "control_program": "00148da6ccbc216f9019cf80d23fd2083c80e29fcba2",
                "address": "bm1q3knve0ppd7gpnnuq6glayzpusr3fljazzcq0eh",
                "spent_output_id": "d11967ce15741217c650bc0b9dd7a390aaedd8ea5c645266920a7d19d8be681a",
                "input_id": "caae7c37f6cecce6854e6488cc389379e312acd2f7495337633501fc7f72b5f3"
        }
        ],
        "outputs": [
        {
            "type": "control",
                "id": "3110bc8e7d713c17fb3dc3c9deadbfc419a25c25252c8e613d1fa54cc4d05dbd",
                "position": 0,
                "asset_id": "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "asset_definition": {},
            "amount": 281500000,
                "control_program": "00145d6ba5bf0cfdb2487abd594429cd04c2ba566f9f",
                "address": "bm1qt446t0cvlkeys74at9zznngyc2a9vmulcr2xy6"
        },
        {
            "type": "control",
                "id": "db5afebb5b33aec2c46fcebb20b98fffa8c065a101f4c1789fe5491b34dc1b8f",
                "position": 1,
                "asset_id": "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                "asset_definition": {},
            "amount": 200000000,
                "control_program": "00140d074bc86bd388a45f1c8911a41b8f0705d9058b",
                "address": "bm1qp5r5hjrt6wy2ghcu3yg6gxu0quzajpvtsm2gnc"
        }
        ],
        "status_fail": false,
            "mux_id": "0e97230a7347967764fd77c8cfa96b38ec6ff08465300a01900c645dfb694f24"
    }*/


    @Test
    public void testMustWriteForHash() throws Exception {
        Entry entry = new Entry() {
            @Override
            public String typ() {
                return null;
            }

            @Override
            public void writeForHash(ByteArrayOutputStream out) {

            }
        };

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entry.mustWriteForHash(out, (byte) 2);
        assert Hex.toHexString(out.toByteArray()).equals("02");

        out.reset();
        entry.mustWriteForHash(out, 1L);
        assert Hex.toHexString(out.toByteArray()).equals("0100000000000000");

        out.reset();
        entry.mustWriteForHash(out, 0x3456584738473837L);
        assert Hex.toHexString(out.toByteArray()).equals("3738473847585634");

        out.reset();
        entry.mustWriteForHash(out, new byte[]{0x12, 0x34, (byte) 0x85});
        assert Hex.toHexString(out.toByteArray()).equals("03123485");

        out.reset();
        entry.mustWriteForHash(out, new byte[][]{{0x12, 0x34, (byte) 0x85}, {(byte) 0x86, 0x17, 0x40}});
        assert Hex.toHexString(out.toByteArray()).equals("020312348503861740");

        out.reset();
        entry.mustWriteForHash(out, "hello, 世界");
        assert Hex.toHexString(out.toByteArray()).equals("0d68656c6c6f2c20e4b896e7958c");

        out.reset();
        entry.mustWriteForHash(out, new String[]{"hi", "你好", "hello"});
        assert Hex.toHexString(out.toByteArray()).equals("0302686906e4bda0e5a5bd0568656c6c6f");

        out.reset();
        String hash = "d8ab56a5c9296f591db071a8b63f395e1485b12d4b105b49fee287c03888331f";
        entry.mustWriteForHash(out, new Hash(hash));
        assert Hex.toHexString(out.toByteArray()).equals(hash);

        out.reset();
        ValueSource valueSource = new ValueSource(new Hash(hash), null, 1);
        Program program = new Program(1, new byte[]{1});
        Mux mux = new Mux(new ValueSource[]{valueSource}, program);
        entry.mustWriteForHash(out, mux);
        assert Hex.toHexString(out.toByteArray()).equals("01d8ab56a5c9296f591db071a8b63f395e1485b12d4b105b49fee287c03888331f010000000000000001000000000000000101");
    }

    @Test
    public void testEntryID() throws Exception {
        String hash = "d8ab56a5c9296f591db071a8b63f395e1485b12d4b105b49fee287c03888331f";
        ValueSource valueSource = new ValueSource(new Hash(hash), null, 1);
        Program program = new Program(1, new byte[]{1});
        Mux mux = new Mux(new ValueSource[]{valueSource}, program);
        String entryID = mux.entryID().toString();
        assert entryID.equals("ebd967df33a3373ab85521fba24c22bf993c73f46fa96254b0c86646093184e9");
    }

}


