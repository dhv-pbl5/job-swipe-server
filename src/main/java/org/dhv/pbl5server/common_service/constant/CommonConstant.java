package org.dhv.pbl5server.common_service.constant;

public class CommonConstant {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String PROD_PROFILE = "production";
    public static final String DEV_PROFILE = "development";
    public static final String JWT_TYPE = "Bearer";
    public static final long DAY_IN_MILLIS = 86400000L;

    /*
     * Regex pattern
     */
    public static final String UUID_REGEX_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String PASSWORD_REGEXP_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,32}$";
    public static final String EMAIL_REGEXP_PATTERN = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
        + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";

    /*
     * Constant
     */
    public static final int CONSTANT_LENGTH = 7;
    public static final String NOTIFICATION_SENDER_PATTERN = "{sender}";
    public static final String NOTIFICATION_RECEIVER_PATTERN = "{receiver}";
    public static final String NOTIFICATION_DATA_END_SYMBOL = "$";

    /*
     * Template
     */
    public static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
    public static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    public static final String MAIL_TEMPLATE_SUFFIX = ".html";
    public static final String UTF_8 = "UTF-8";

    private CommonConstant() {
    }
}
