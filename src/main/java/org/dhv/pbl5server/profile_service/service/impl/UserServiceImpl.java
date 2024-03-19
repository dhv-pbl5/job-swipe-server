package org.dhv.pbl5server.profile_service.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.mapper.UserMapper;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final EntityManager em;

    @Override
    public UserProfileResponse getUserProfile(Account account) {
        User user = getAllDataByAccountId(account.getAccountId());
        return userMapper.toUserProfileResponse(account, user);
    }

    @Override
    public User getAllDataByAccountId(UUID accountId) {
        // Education
        User user = repository.fetchAllDataEducationByAccountId(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        // Experience
        user.setExperiences(repository.fetchAllDataExperienceByAccountId(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND))
            .getExperiences());
        // Award
        user.setAwards(repository.fetchAllDataAwardByAccountId(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND))
            .getAwards());
        return user;
    }


}
