package org.dhv.pbl5server.profile_service.service;

import org.dhv.pbl5server.common_service.model.ApiDataResponse;

import java.util.Optional;
import java.util.UUID;

public interface CompanyService {
    Optional<ApiDataResponse> getCompanyById(UUID id);
}
