package org.dhv.pbl5server.common_service.constant;

public class CommonConstant {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String JWT_TYPE = "Bearer";
    public static final String PASSWORD_REGEXP_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32})"; // (?=.*[@#$%])
    public static final String EMAIL_REGEXP_PATTERN = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
        + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$"; // (?=.*[@#$%])

    private CommonConstant() {
    }
}
