package org.dhv.pbl5server.profile_service.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRoleWithoutAdmin;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeUser;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.dhv.pbl5server.profile_service.enums.UpdateUserProfileType;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/v1/profile/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final SpringValidatorAdapter validator;

    @PreAuthorizeSystemRoleWithoutAdmin
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getUserProfileComponentById(
        @Nullable @RequestParam("user_id") String userId,
        @Nullable @RequestParam("component_id") String id,
        @Nullable @RequestParam UpdateUserProfileType type,
        @CurrentAccount Account currentAccount
    ) {
        if (userId == null && id == null && type == null)
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserProfile(currentAccount)));
        if ((type == null || type == UpdateUserProfileType.BASIC_INFO) && userId == null)
            throw new BadRequestException(ErrorMessageConstant.USER_ID_IS_REQUIRED);
        if (type == null)
            return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserProfileById(userId)));
        if (type == UpdateUserProfileType.OTHER_DESCRIPTION && userId == null)
            throw new BadRequestException(ErrorMessageConstant.OTHER_DESCRIPTION_USER_ID_IS_REQUIRED);
        if (type != UpdateUserProfileType.BASIC_INFO && id == null)
            throw new BadRequestException(ErrorMessageConstant.ID_IS_REQUIRED);
        return switch (type) {
            case AWARD -> ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserAwardById(id)));
            case EXPERIENCE -> ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserExperienceById(id)));
            case EDUCATION -> ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserEducationById(id)));
            case OTHER_DESCRIPTION ->
                ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserOtherDescriptionById(userId, id)));
            default -> ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserProfileById(userId)));
        };
    }

    @PreAuthorizeUser
    @PatchMapping("")
    @SuppressWarnings("unchecked")
    public ResponseEntity<ApiDataResponse> updateBasicInfo(
        @RequestParam("type") UpdateUserProfileType type,
        @RequestBody Object body,
        @CurrentAccount Account currentAccount
    ) {
        Set<ConstraintViolation<Object>> violations = null;
        Object object = null;
        switch (type) {
            case BASIC_INFO:
                object = getObjectFromUpdateApi(body, type);
                violations = validator.validate(Objects.requireNonNull(object));
                ErrorUtils.checkConstraintViolation(violations);
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateBasicInfo(currentAccount, (UserBasicInfoRequest) object)));
            case AWARD:
                object = getObjectFromUpdateApi(body, type);
                for (var obj : (List<UserAwardRequest>) Objects.requireNonNull(object)) {
                    violations = validator.validate(Objects.requireNonNull(obj));
                    ErrorUtils.checkConstraintViolation(violations);
                }
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateAwards(currentAccount, (List<UserAwardRequest>) object)));
            case EDUCATION:
                object = getObjectFromUpdateApi(body, type);
                for (var obj : (List<UserEducationRequest>) Objects.requireNonNull(object)) {
                    violations = validator.validate(Objects.requireNonNull(obj));
                    ErrorUtils.checkConstraintViolation(violations);
                }
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateEducations(currentAccount, (List<UserEducationRequest>) object)));
            case EXPERIENCE:
                object = getObjectFromUpdateApi(body, type);
                for (var obj : (List<UserExperienceRequest>) Objects.requireNonNull(object)) {
                    violations = validator.validate(Objects.requireNonNull(obj));
                    ErrorUtils.checkConstraintViolation(violations);
                }
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateExperiences(currentAccount, (List<UserExperienceRequest>) object)));
            case OTHER_DESCRIPTION:
                object = getObjectFromUpdateApi(body, type);
                for (var obj : (List<OtherDescription>) Objects.requireNonNull(object)) {
                    violations = validator.validate(Objects.requireNonNull(obj));
                    ErrorUtils.checkConstraintViolation(violations);
                }
                return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateOtherDescriptions(currentAccount, (List<OtherDescription>) object)));

        }
        return ResponseEntity.badRequest().body(ApiDataResponse.builder().status(CommonConstant.FAILURE).build());
    }

    @PreAuthorizeUser
    @PatchMapping("/avatar")
    public ResponseEntity<ApiDataResponse> updateAvatar(MultipartFile file, @CurrentAccount Account currentAccount) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.updateAvatar(currentAccount, file)));
    }

    @PreAuthorizeUser
    @DeleteMapping("")
    public ResponseEntity<ApiDataResponse> deleteEducations(
        @RequestParam("type") UpdateUserProfileType type,
        @RequestBody List<String> ids,
        @CurrentAccount Account currentAccount
    ) {
        switch (type) {
            case EDUCATION:
                service.deleteEducations(currentAccount, ids);
                break;
            case EXPERIENCE:
                service.deleteExperiences(currentAccount, ids);
                break;
            case AWARD:
                service.deleteAwards(currentAccount, ids);
                break;
            case OTHER_DESCRIPTION:
                service.deleteOtherDescriptions(currentAccount, ids);
                break;
        }
        return ResponseEntity.ok(ApiDataResponse.successWithoutMetaAndData());
    }


    @SuppressWarnings("unchecked")
    private Object getObjectFromUpdateApi(Object body, UpdateUserProfileType type) {
        Object object = null;
        switch (type) {
            case BASIC_INFO:
                var map = CommonUtils.convertObjectToMap(body);
                if (map == null)
                    throw new BadRequestException(ErrorMessageConstant.BASIC_INFO_REQUEST_MUST_BE_OBJECT);
                object = CommonUtils.decodeJson(map, UserBasicInfoRequest.class);
                if (object == null)
                    throw new BadRequestException(ErrorMessageConstant.BASIC_INFO_REQUEST_INVALID);
                break;
            case AWARD:
                if (!CommonUtils.isList(body))
                    throw new BadRequestException(ErrorMessageConstant.AWARD_REQUEST_MUST_BE_LIST);
                object = CommonUtils.decodeJson((List<Map<String, Object>>) body, UserAwardRequest.class);
                if (object == null)
                    throw new BadRequestException(ErrorMessageConstant.AWARD_REQUEST_INVALID);
                break;
            case EDUCATION:
                if (!CommonUtils.isList(body))
                    throw new BadRequestException(ErrorMessageConstant.EDUCATION_REQUEST_MUST_BE_LIST);
                object = CommonUtils.decodeJson((List<Map<String, Object>>) body, UserEducationRequest.class);
                if (object == null)
                    throw new BadRequestException(ErrorMessageConstant.EDUCATION_REQUEST_INVALID);
                break;
            case EXPERIENCE:
                if (!CommonUtils.isList(body))
                    throw new BadRequestException(ErrorMessageConstant.EXPERIENCE_REQUEST_MUST_BE_LIST);
                object = CommonUtils.decodeJson((List<Map<String, Object>>) body, UserExperienceRequest.class);
                if (object == null)
                    throw new BadRequestException(ErrorMessageConstant.EXPERIENCE_REQUEST_INVALID);
                break;
            case OTHER_DESCRIPTION:
                if (!CommonUtils.isList(body))
                    throw new BadRequestException(ErrorMessageConstant.OTHER_DESCRIPTION_REQUEST_MUST_BE_LIST);
                object = CommonUtils.decodeJson((List<Map<String, Object>>) body, OtherDescription.class);
                if (object == null)
                    throw new BadRequestException(ErrorMessageConstant.OTHER_DESCRIPTION_REQUEST_INVALID);
                break;
        }
        return object;
    }
}
