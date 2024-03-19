package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;

import java.util.UUID;

public interface UserService {
    UserProfileResponse getUserProfile(Account account);

    User getAllDataByAccountId(UUID accountId);
}
