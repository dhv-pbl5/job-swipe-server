package org.dhv.pbl5server.common_service.constant;

import org.dhv.pbl5server.common_service.utils.CommonUtils;

public class RedisCacheConstant {

    /*
     * Key storing in Redis
     */
    public static final String AUTH_KEY = "Authentication";
    public static final String OTP_KEY = "OTP";
    public static final String PROFILE = "Profile";

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

    public static String FORGOT_PASSWORD_HASH(String uuid) {
        return "forgot_password:%s".formatted(uuid);
    }

    public static String USER_PROFILE_HASH(String uuid) {
        return "user_profile:%s".formatted(uuid);
    }

    public static String COMPANY_PROFILE_HASH(String uuid) {
        return "company_profile:%s".formatted(uuid);
    }

    private static String getCurrentTimestamp() {
        return CommonUtils.getCurrentTimestamp().toString().replaceAll(" ", "_");
    }

    private RedisCacheConstant() {
    }
}
