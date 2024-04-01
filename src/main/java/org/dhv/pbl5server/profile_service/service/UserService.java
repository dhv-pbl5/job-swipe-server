package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserProfileResponse getUserProfile(Account account);

    UserProfileResponse getUserProfileById(String id);

    UserAwardResponse getUserAwardById(String id);

    UserEducationResponse getUserEducationById(String id);

    UserExperienceResponse getUserExperienceById(String id);

    OtherDescription getUserOtherDescriptionById(String userId, String id);

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

    List<UserProfileResponse> getAllData();
}
