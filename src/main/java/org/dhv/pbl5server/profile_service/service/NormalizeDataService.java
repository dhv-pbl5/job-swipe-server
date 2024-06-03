package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.constant_service.enums.SystemRoleName;

public interface NormalizeDataService {
    public void normalizeUserData(String userId);

    public void normalizeCompanyData(String companyId);

    public void normalizeData(String accountId, SystemRoleName role);
}
