package org.dhv.pbl5server.admin_service.service;

import java.util.List;

// git commit -m "PBL-510 login for admin"

public interface AdminService {

    void activateAccount(List<String> accountIds);

    void deactivateAccount(List<String> accountIds);

    void initialDefaultAccount();
}
