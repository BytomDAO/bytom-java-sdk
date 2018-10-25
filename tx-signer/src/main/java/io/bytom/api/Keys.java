package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;

public class Keys {

    @SerializedName("alias")
    public String alias;

    @SerializedName("xpub")
    public String xpub;

    @SerializedName("file")
    public String file;

    public String toJson() {
        return Utils.serializer.toJson(this);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getXpub() {
        return xpub;
    }

    public void setXpub(String xpub) {
        this.xpub = xpub;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
