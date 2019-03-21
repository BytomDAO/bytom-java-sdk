package io.bytom.types;

import io.bytom.exception.WriteForHashException;
import io.bytom.util.OutputUtil;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class Entry {

    public abstract String typ();

    public abstract void writeForHash(ByteArrayOutputStream out);

    public void mustWriteForHash(ByteArrayOutputStream out, Object data) {
        try {
            if (data == null) {
                throw new WriteForHashException("The field needs to be written to hash is null");
            }
            if (data instanceof Byte) {
                OutputUtil.writeByte(out, (byte) data);
            } else if (data instanceof Long) {
                OutputUtil.writeLong(out, (long) data);
            } else if (data instanceof byte[]) {
                OutputUtil.writeVarstr(out, (byte[]) data);
            } else if (data instanceof byte[][]) {
                OutputUtil.writeVarstrList(out, (byte[][]) data);
            } else if (data instanceof String) {
                OutputUtil.writeVarstr(out, ((String) data).getBytes());
            } else if (data instanceof Hash) {
                out.write(((Hash) data).toByteArray());
            } else if (data instanceof AssetID) {
                out.write(((AssetID) data).toByteArray());
            } else if (data.getClass().isArray()) {
                Object[] array = (Object[]) data;
                OutputUtil.writeVarint(out, array.length);
                for (Object obj : array) {
                    mustWriteForHash(out, obj);
                }
            } else {
                Class<?> cls = data.getClass();
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), cls);
                    Method readMethod = descriptor.getReadMethod();
                    mustWriteForHash(out, readMethod.invoke(data));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WriteForHashException(e);
        }
    }

    public Hash entryID() {
        SHA3.Digest256 digest256 = new SHA3.Digest256();
        ByteArrayOutputStream hasher = new ByteArrayOutputStream();
        ByteArrayOutputStream bh = new ByteArrayOutputStream();
        try {
            hasher.write("entryid:".getBytes());
            hasher.write(this.typ().getBytes());
            hasher.write(":".getBytes());

            this.writeForHash(bh);
            hasher.write(digest256.digest(bh.toByteArray()));
            return new Hash(digest256.digest(hasher.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                bh.close();
                hasher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
