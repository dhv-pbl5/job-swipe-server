package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserProfileResponse getUserProfile(Account account);

    UserProfileResponse updateBasicInfo(Account account, UserBasicInfoRequest request);

    UserProfileResponse updateEducations(Account account, List<UserEducationRequest> request);

    UserProfileResponse updateExperiences(Account account, List<UserExperienceRequest> request);

    UserProfileResponse updateAwards(Account account, List<UserAwardRequest> request);


    User getAllDataByAccountId(UUID accountId);
}
