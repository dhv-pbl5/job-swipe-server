package org.dhv.pbl5server.profile_service.controller;

import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.CurrentAccount;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.ErrorUtils;
import org.dhv.pbl5server.profile_service.enums.UpdateUserProfileType;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getUserProfile(@CurrentAccount Account currentAccount) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getUserProfile(currentAccount)));
    }

    @PreAuthorize("hasAuthority('USER')")
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
        }
        return ResponseEntity.badRequest().body(ApiDataResponse.builder().status(CommonConstant.FAILURE).build());
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
        }
        return object;
    }
}
