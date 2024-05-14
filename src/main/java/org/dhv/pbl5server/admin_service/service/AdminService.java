package org.dhv.pbl5server.admin_service.service;

import java.util.List;

import org.dhv.pbl5server.authentication_service.entity.Account;

public interface AdminService {

    void activateAccount(Account admin, List<String> accountIds);

    void deactivateAccount(Account admin, List<String> accountIds);

    void initialDefaultAccount();
}
