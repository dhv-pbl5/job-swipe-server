package org.dhv.pbl5server.common_service.constant;

public final class ErrorMessageConstant {

    public static final String INTERNAL_SERVER_ERROR_CODE = "ERR_SER0101";
    public static final String BAD_REQUEST_ERROR_CODE = "ERR_SER0102";

    /*
        Common
    */
    public static final String INTERNAL_SERVER_ERROR = "internal_server_error";
    public static final String PAGE_NOT_FOUND = "page_not_found";
    public static final String FORBIDDEN = "forbidden";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String REQUIRED_BODY_IN_REQUEST = "required_body_in_request";
    public static final String INVALID_UUID = "invalid_uuid";
    public static final String INVALID_ENUM = "invalid_enum";


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
        Account
    */
    public static final String ACCOUNT_NOT_FOUND = "account_not_found";
    public static final String INCORRECT_EMAIL_OR_PASSWORD = "incorrect_email_or_password";
    public static final String ACCOUNT_IS_DISABLED = "account_is_disabled";
    public static final String ACCOUNT_ID_IS_REQUIRED = "account_id_is_required";

    /*
        Constant
    */
    public static final String CONSTANT_NOT_FOUND = "constant_not_found";
    public static final String SYSTEM_ROLE_NOT_FOUND = "system_role_not_found";
    public static final String CONSTANT_TYPE_MUST_BE_NUMBER = "constant_type_must_be_number";
    public static final String CONSTANT_TYPE_MUST_BE_SYSTEM_ROLE = "constant_type_must_be_system_role";
    public static final String CONSTANT_TYPE_MUST_BE_APPLY_POSITION = "constant_type_must_be_apply_position";
    public static final String CONSTANT_TYPE_MUST_BE_APPLY_SKILL = "constant_type_must_be_apply_skill";
    public static final String CONSTANT_TYPE_MUST_BE_EXPERIENCE = "constant_type_must_be_experience";
    public static final String CONSTANT_TYPE_MUST_BE_NOTIFICATION = "constant_type_must_be_notification";

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

    /*
        User
    */
    public static final String USER_NOT_FOUND = "user_not_found";
    public static final String BASIC_INFO_REQUEST_MUST_BE_OBJECT = "basic_info_request_must_be_object";
    public static final String BASIC_INFO_REQUEST_INVALID = "basic_info_request_invalid";
    public static final String COMPONENT_ID_IS_REQUIRED = "component_id_is_required";

    /*
        Education
    */
    public static final String EDUCATION_TIME_INVALID = "study_end_time_must_be_greater_than_start_time";
    public static final String EDUCATION_REQUEST_INVALID = "education_request_invalid";
    public static final String EDUCATION_REQUEST_MUST_BE_LIST = "education_request_must_be_list";
    public static final String EDUCATION_NOT_FOUND = "education_not_found";
    public static final String EDUCATION_ID_IS_REQUIRED = "education_id_is_required";

    /*
       Award
     */
    public static final String AWARD_REQUEST_MUST_BE_LIST = "award_request_must_be_list";
    public static final String AWARD_REQUEST_INVALID = "award_request_invalid";
    public static final String AWARD_NOT_FOUND = "award_not_found";
    public static final String AWARD_ID_IS_REQUIRED = "award_id_is_required";

    /*
       Experience
    */
    public static final String EXPERIENCE_REQUEST_MUST_BE_LIST = "experience_request_must_be_list";
    public static final String EXPERIENCE_REQUEST_INVALID = "experience_request_invalid";
    public static final String EXPERIENCE_TIME_INVALID = "experience_end_time_must_be_greater_than_start_time";
    public static final String EXPERIENCE_NOT_FOUND = "experience_not_found";
    public static final String EXPERIENCE_ID_IS_REQUIRED = "experience_id_is_required";

    /*
       Other Description
    */
    public static final String OTHER_DESCRIPTION_REQUEST_MUST_BE_LIST = "other_description_request_must_be_list";
    public static final String OTHER_DESCRIPTION_REQUEST_INVALID = "other_description_request_invalid";
    public static final String OTHER_DESCRIPTION_NOT_FOUND = "other_description_not_found";
    public static final String OTHER_DESCRIPTION_USER_ID_IS_REQUIRED = "other_description_user_id_is_required";
    public static final String OTHER_DESCRIPTION_ID_IS_REQUIRED = "other_description_id_is_required";
    public static final String DELETE_IDS_REQUEST_HAVE_ONE_NOT_FOUND = "delete_ids_request_have_one_not_found";

    /*
       Application Position
    */
    public static final String APPLICATION_POSITION_NOT_FOUND = "application_position_not_found";
    public static final String APPLICATION_POSITION_ID_REQUIRED = "application_position_id_is_required";

    /*
       Application Position
    */
    public static final String APPLICATION_SKILL_NOT_FOUND = "application_skill_not_found";
    public static final String APPLICATION_SKILL_ID_REQUIRED = "application_skill_id_is_required";

    /*
       Company profile
    */
    public static final String COMPANY_PROFILE_NOT_FOUND = "company_profile_not_found";

    /*
       Conversation
    */
    public static final String CONVERSATION_NOT_FOUND = "conversation_not_found";
    public static final String REQUIRED_USER_AND_COMPANY = "user_and_company_are_required";

    /*
       Message
    */
    public static final String MESSAGE_NOT_FOUND = "message_not_found";
    public static final String MESSAGE_MUST_HAVE_CONTENT_OR_FILE = "message_must_have_content_or_file";

    /*
       Notification
    */
    public static final String NOTIFICATION_NOT_FOUND = "notification_not_found";
    public static final String REQUIRED_SENDER_AND_RECEIVER = "required_sender_and_receiver";
    public static final String REQUIRED_NOTIFICATION_TYPE = "required_notification_type";

    /*
       Match
    */
    public static final String MATCH_NOT_FOUND = "match_not_found";
    public static final String MATCH_ID_INVALID = "match_id_is_invalid";
    public static final String REQUESTED_ACCOUNT_ID_INVALID = "requested_account_id_is_invalid";
    public static final String REQUESTED_ACCOUNT_NOT_FOUND = "requested_account_not_found";
    public static final String REQUESTED_ACCOUNT_SAME_ROLE = "requested_account_and_your_account_must_not_same_role";
    public static final String REQUESTED_ACCOUNT_BANNED = "requested_account_is_banned";
    public static final String MATCH_ALREADY_ACCEPTED = "already_matched";
    public static final String MATCH_NOT_ACCEPTED_YOURSELF = "match_not_accepted_by_yourself";
    public static final String REJECT_NOT_ACCEPTED_MATCH = "reject_not_accepted_match";
    public static final String MATCH_FEATURE_NOT_FOR_ADMIN = "match_feature_not_for_admin";
    public static final String MATCH_ALREADY_REQUESTED = "match_already_requested";
    public static final String MATCH_ALREADY_REJECTED = "match_already_rejected";

    private ErrorMessageConstant() {
    }
}
