package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository repository;

    @Override
    public Optional<ApiDataResponse> getCompanyById(UUID id) {
        return Optional.empty();
    }
}
