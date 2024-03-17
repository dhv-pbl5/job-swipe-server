package org.dhv.pbl5server.profile_service.service.impl;

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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserProfileResponse getUserProfile(Account account) {
        User user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        return userMapper.toUserProfileResponse(account, user);
    }
}
