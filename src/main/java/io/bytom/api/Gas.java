package io.bytom.api;

import com.google.gson.annotations.SerializedName;
import io.bytom.common.Utils;
import io.bytom.exception.BytomException;
import io.bytom.http.Client;
import org.apache.log4j.Logger;

/**
 * <h1>Gas Class</h1>
 */
public class Gas {

    @SerializedName("gas_rate")
    public Integer gasRate;

    private static Logger logger = Logger.getLogger(Gas.class);

    public String toJson() {
        return Utils.serializer.toJson(this);
    }


    /**
     * Call gas-rate api
     *
     * @param client
     * @return
     * @throws BytomException
     */
    public static Gas gasRate(Client client) throws BytomException {
        Gas gas = client.request("gas-rate", null, Gas.class);

        logger.info("gas-rate:");
        logger.info(gas.toJson());

        return gas;
    }
}
