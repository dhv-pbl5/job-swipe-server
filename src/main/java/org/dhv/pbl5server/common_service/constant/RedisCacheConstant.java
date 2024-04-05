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
            return STR."revoke_access_token:\{uuid}";
        return STR."revoke_access_token:\{uuid}:\{CommonUtils.getCurrentTimestamp()}"
            .replaceAll(" ", "_");
    }

    public static String REVOKE_REFRESH_TOKEN_HASH(String uuid, boolean isGenerate) {
        if (!isGenerate)
            return STR."revoke_refresh_token:\{uuid}";
        return STR."revoke_refresh_token:\{uuid}:\{CommonUtils.getCurrentTimestamp()}"
            .replaceAll(" ", "_");
    }

    private RedisCacheConstant() {
    }
}
