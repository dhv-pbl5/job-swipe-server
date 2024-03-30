package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.mapper.UserMapper;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserProfileResponse getUserProfile(Account account) {
        User user = getAllDataByAccountId(account.getAccountId());
        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateBasicInfo(Account account, UserBasicInfoRequest request) {
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUser(user, request);
        var updatedAccount = userMapper.toAccount(account, request);
        updatedUser.setAccount(updatedAccount);
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    public UserProfileResponse updateEducations(Account account, List<UserEducationRequest> request) {
        var user = repository.fetchAllDataEducationById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListEducations(user, request);
        for (var education : updatedUser.getEducations()) {
            if (education.getStudyEndTime().before(education.getStudyStartTime()))
                throw new NotFoundObjectException(ErrorMessageConstant.EDUCATION_TIME_INVALID);
            education.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    public UserProfileResponse updateExperiences(Account account, List<UserExperienceRequest> request) {
        var user = repository.fetchAllDataEducationById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListExperiences(user, request);
        for (var experience : updatedUser.getExperiences()) {
            if (experience.getExperienceEndTime().before(experience.getExperienceStartTime()))
                throw new NotFoundObjectException(ErrorMessageConstant.EXPERIENCE_TIME_INVALID);
            experience.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    public UserProfileResponse updateAwards(Account account, List<UserAwardRequest> request) {
        var user = repository.fetchAllDataEducationById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListAwards(user, request);
        for (var award : updatedUser.getAwards()) {
            award.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }


    @Override
    public User getAllDataByAccountId(UUID accountId) {
        // Education
        User user = repository.fetchAllDataEducationById(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        // Experience
        user.setExperiences(repository.fetchAllDataExperienceById(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND))
            .getExperiences());
        // Award
        user.setAwards(repository.fetchAllDataAwardById(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND))
            .getAwards());
        return user;
    }
}
