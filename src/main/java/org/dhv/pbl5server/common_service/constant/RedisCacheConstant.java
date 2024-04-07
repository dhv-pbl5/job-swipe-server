package org.dhv.pbl5server.common_service.constant;

import org.dhv.pbl5server.common_service.utils.CommonUtils;

public class RedisCacheConstant {

    /*
     * Key storing in Redis
     */
    public static final String AUTH_KEY = "Authentication";

    /*
     * Hash key storing in Redis
     */
    public static String REVOKE_ACCESS_TOKEN_HASH(String uuid, boolean isGenerate) {
        if (!isGenerate)
            return "revoke_access_token:%s".formatted(uuid);
        return "revoke_access_token:%s:%s".formatted(uuid, getCurrentTimestamp());
    }

    public static String REVOKE_REFRESH_TOKEN_HASH(String uuid, boolean isGenerate) {
        if (!isGenerate)
            return "revoke_refresh_token:%s".formatted(uuid);
        return "revoke_refresh_token:%s:%s".formatted(uuid, getCurrentTimestamp());
    }

    private static String getCurrentTimestamp() {
        return CommonUtils.getCurrentTimestamp().toString().replaceAll(" ", "_");
    }

    private RedisCacheConstant() {
    }
}
