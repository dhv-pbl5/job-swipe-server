package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.repository.CrudDbJsonArrayRepository;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.mapper.CompanyMapper;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.CompanyProfileRequest;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.service.ApplicationPositionService;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository repository;
    private final CrudDbJsonArrayRepository<OtherDescription, UUID> otherRepository;
    private final ApplicationPositionService applicationPositionService;
    private final CompanyMapper mapper;

    @Override
    public CompanyProfileResponse getCompanyProfile(Account account) {
        var company = getAllDataByAccountId(account.getAccountId());
        return mapper.toCompanyResponse(company);
    }

    @Override
    public CompanyProfileResponse getCompanyProfileById(String accountId) {
        var company = getAllDataByAccountId(UUID.fromString(accountId));
        return mapper.toCompanyResponse(company);
    }

    @Override
    public CompanyProfileResponse updateCompanyProfile(Account account, CompanyProfileRequest request) {
        var company = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        company = mapper.toCompany(company, request);
        repository.save(company);
        return mapper.toCompanyResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public CompanyProfileResponse insertOtherDescriptions(Account account, List<OtherDescription> request) {
        // Remove id if exist
        request.forEach(e -> e.setId(null));
        var company = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        company.setOthers(otherRepository.saveAll(company.getOthers(), request));
        repository.save(company);
        return mapper.toCompanyResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public CompanyProfileResponse updateOtherDescriptions(Account account, List<OtherDescription> request) {
        var company = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        for (var req : request) {
            if (req.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.OTHER_DESCRIPTION_ID_IS_REQUIRED);
            if (!otherRepository.existsById(company.getOthers(), req.getId()))
                throw new NotFoundObjectException(ErrorMessageConstant.OTHER_DESCRIPTION_NOT_FOUND);
        }
        company.setOthers(otherRepository.saveAll(company.getOthers(), request));
        repository.save(company);
        return mapper.toCompanyResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public void deleteOtherDescriptions(Account account, List<String> ids) {
        var company = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        if (checkDeleteIdsRequest(company.getOthers().stream().map(OtherDescription::getId).toList(), ids)) {
            var result = otherRepository.deleteAllById(company.getOthers(), ids.stream().map(UUID::fromString).toList());
            company.setOthers(result);
            repository.save(company);
        }
    }

    @Override
    public OtherDescription getOtherDescriptionById(String companyId, String id) {
        var company = repository.findById(UUID.fromString(companyId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        return otherRepository.findById(company.getOthers(), UUID.fromString(id))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.OTHER_DESCRIPTION_NOT_FOUND));
    }

    @Override
    public Company getAllDataByAccountId(UUID accountId) {
        var company = repository.findById(accountId).orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        company.setAccount(applicationPositionService.getAccountWithAllApplicationPositions(accountId));
        return company;
    }

    @Override
    public List<CompanyProfileResponse> getAllData() {
        List<Company> companies = repository.findAll();
        return companies.stream()
            .map(u -> mapper.toCompanyResponse(
                getAllDataByAccountId(u.getAccountId()
                ))
            ).toList();
    }

    private boolean checkDeleteIdsRequest(List<UUID> data, List<String> ids) {
        for (var id : ids) {
            if (!data.contains(UUID.fromString(id)))
                throw new BadRequestException(ErrorMessageConstant.DELETE_IDS_REQUEST_HAVE_ONE_NOT_FOUND);
        }
        return true;
    }
}
