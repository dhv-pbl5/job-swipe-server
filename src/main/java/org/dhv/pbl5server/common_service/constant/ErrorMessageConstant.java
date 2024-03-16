package org.dhv.pbl5server.common_service.constant;

public final class ErrorMessageConstant {
    /*
        Common
    */
    public static final String INTERNAL_SERVER_ERROR = "internal_server_error";
    public static final String PAGE_NOT_FOUND = "page_not_found";
    public static final String FORBIDDEN = "forbidden";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String REQUIRED_BODY_IN_REQUEST = "required_body_in_request";


    /*
        Authentication
    */
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String EXPIRED_TOKEN = "expired_token";
    public static final String REVOKED_TOKEN = "revoked_token";
    public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";
    public static final String EXPIRED_REFRESH_TOKEN = "expired_refresh_token";
    public static final String REVOKED_REFRESH_TOKEN = "revoked_refresh_token";
    public static final String INVALID_RESET_PASSWORD_TOKEN = "invalid_reset_password";
    public static final String EXPIRED_RESET_PASSWORD_TOKEN = "expired_reset_password";
    public static final String ACCOUNT_IS_ACTIVE = "account_is_active";
    public static final String ACCOUNT_IS_NOT_ACTIVE = "account_is_not_active";

    /*
        Permission
    */
    public static final String PERMISSION_NOT_FOUND = "permission_not_found";
    public static final String PERMISSION_IS_REQUIRE = "permission_is_require";

    /*
        User
    */
    public static final String ACCOUNT_NOT_FOUND = "account_not_found";
    public static final String INCORRECT_EMAIL_OR_PASSWORD = "incorrect_email_or_password";
    public static final String ACCOUNT_IS_DISABLED = "account_is_disabled";

    /*
        Constant
    */
    public static final String CONSTANT_NOT_FOUND = "constant_not_found";
    public static final String SYSTEM_ROLE_NOT_FOUND = "system_role_not_found";
    public static final String CONSTANT_TYPE_MUST_BE_NUMBER = "constant_type_must_be_number";

    /*
        Register
    */
    public static final String EMAIL_ALREADY_EXISTS = "email_already_exists";
    public static final String ROLE_NOT_VALID = "role_not_valid";

    /*
        Reset password
    */
    public static final String NEW_PASSWORD_SAME_OLD_PASSWORD = "new_password_same_old_password";
    public static final String NEW_PASSWORD_CONFIRMATION_NOT_MATCH = "new_password_confirmation_not_match";
    public static final String CURRENT_PASSWORD_IS_INCORRECT = "current_password_is_incorrect";

    /*
        Reset password
    */
    public static final String UPLOAD_FILE_FAILED = "upload_file_failed";
    public static final String FILE_NOT_FOUND = "file_not_found";
    public static final String DELETE_FILE_FAILED = "delete_file_failed";


    private ErrorMessageConstant() {
    }
}
