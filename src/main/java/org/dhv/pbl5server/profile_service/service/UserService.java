package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserAwardResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserEducationResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserExperienceResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-559 user experience"
// git commit -m "PBL-561 delete user experience"
// git commit -m "PBL-522 update user profile"
// git commit -m "PBL-557 update user experience"

public interface UserService {
    UserProfileResponse getUserProfile(Account account);

    UserProfileResponse getUserProfileById(String id);

    ApiDataResponse getListAwardByUserId(String userId, Pageable pageable);

    UserAwardResponse getUserAwardById(String id);

    ApiDataResponse getListEducationByUserId(String userId, Pageable pageable);

    UserEducationResponse getUserEducationById(String id);

    ApiDataResponse getListExperienceByUserId(String userId, Pageable pageable);

    UserExperienceResponse getUserExperienceById(String id);

    OtherDescription getUserOtherDescriptionById(String userId, String id);

    ApiDataResponse getListOtherDescriptionByUserId(String userId);

    UserProfileResponse insertEducations(Account account, List<UserEducationRequest> request);

    UserProfileResponse insertExperiences(Account account, List<UserExperienceRequest> request);

    UserProfileResponse insertOtherDescriptions(Account account, List<OtherDescription> request);

    UserProfileResponse insertAwards(Account account, List<UserAwardRequest> request);

    UserProfileResponse updateBasicInfo(Account account, UserBasicInfoRequest request);

    UserProfileResponse updateEducations(Account account, List<UserEducationRequest> request);

    UserProfileResponse updateExperiences(Account account, List<UserExperienceRequest> request);

    UserProfileResponse updateOtherDescriptions(Account account, List<OtherDescription> request);

    UserProfileResponse updateAwards(Account account, List<UserAwardRequest> request);

    String updateAvatar(Account account, MultipartFile file);

    void deleteEducations(Account account, List<String> ids);

    void deleteAwards(Account account, List<String> ids);

    void deleteExperiences(Account account, List<String> ids);

    void deleteOtherDescriptions(Account account, List<String> ids);

    User getAllDataByAccountId(UUID accountId);

    User getAllDataByAccountId(User user, UUID accountId);

    ApiDataResponse getAllData(Pageable pageable);
}
