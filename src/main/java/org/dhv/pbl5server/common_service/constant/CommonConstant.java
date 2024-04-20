package org.dhv.pbl5server.common_service.constant;

public class CommonConstant {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String PROD_PROFILE = "prod";
    public static final String DEV_PROFILE = "dev";
    public static final String SYSTEM_ROLE_PREFIX = "01";
    public static final String JWT_TYPE = "Bearer";
    public static final String UUID_REGEX_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String PASSWORD_REGEXP_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,32}$"; // For special character: (?=.*[@#$%^&+=])
    public static final String EMAIL_REGEXP_PATTERN = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
        + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$"; // (?=.*[@#$%])

    private CommonConstant() {
    }
}
