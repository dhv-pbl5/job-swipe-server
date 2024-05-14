package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.constant.RedisCacheConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.repository.CrudDbJsonArrayRepository;
import org.dhv.pbl5server.common_service.repository.RedisRepository;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.mapper.CompanyMapper;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.CompanyProfileRequest;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.repository.LanguageRepository;
import org.dhv.pbl5server.profile_service.service.ApplicationPositionService;
import org.dhv.pbl5server.profile_service.service.CompanyService;
import org.dhv.pbl5server.s3_service.service.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

// git commit -m "PBL-538 company profile"
// git commit -m "PBL-534 application position"
// git commit -m "PBL-523 update company profile"

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository repository;
    private final CrudDbJsonArrayRepository<OtherDescription, UUID> otherRepository;
    private final LanguageRepository languageRepository;
    private final AccountRepository accountRepository;
    private final RedisRepository redisRepository;
    private final ApplicationPositionService applicationPositionService;
    private final CompanyMapper mapper;
    private final S3Service s3Service;

    @Override
    public CompanyProfileResponse getCompanyProfile(Account account) {
        // Redis
        var companyProfileInRedis = getCompanyProfileFromRedis(account.getAccountId().toString());
        if (companyProfileInRedis != null)
            return companyProfileInRedis;
        // Database
        var company = getAllDataByAccountId(account.getAccountId());
        return mapper.toCompanyResponse(company);
    }

    @Override
    public CompanyProfileResponse getCompanyProfileById(String accountId) {
        // Redis
        var companyProfileInRedis = getCompanyProfileFromRedis(accountId);
        if (companyProfileInRedis != null)
            return companyProfileInRedis;
        // Database
        var company = getAllDataByAccountId(UUID.fromString(accountId));
        return mapper.toCompanyResponse(company);
    }

    @Override
    public CompanyProfileResponse updateCompanyProfile(Account account, CompanyProfileRequest request) {
        var company = repository.findById(account.getAccountId())
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        company = mapper.toCompany(company, request);
        company.setAccount(mapper.toAccount(account, request));
        company = repository.save(company);
        // Save to redis
        company = getAllDataByAccountId(company, account.getAccountId());
        return mapper.toCompanyResponse(company);
    }

    @Override
    public CompanyProfileResponse insertOtherDescriptions(Account account, List<OtherDescription> request) {
        // Remove id if exist
        request.forEach(e -> e.setId(null));
        var company = repository.findById(account.getAccountId())
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        company.setOthers(otherRepository.saveAll(company.getOthers(), request));
        company = repository.save(company);
        // Save to redis
        company = getAllDataByAccountId(company, account.getAccountId());
        return mapper.toCompanyResponse(company);
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
        company = repository.save(company);
        // Save to redis
        company = getAllDataByAccountId(company, account.getAccountId());
        return mapper.toCompanyResponse(company);
    }

    @Override
    public void deleteOtherDescriptions(Account account, List<String> ids) {
        var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
        var company = repository.findById(account.getAccountId())
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        if (checkDeleteIdsRequest(company.getOthers().stream().map(OtherDescription::getId).toList(), ids)) {
            var result = otherRepository.deleteAllById(company.getOthers(),
                    ids.stream().map(UUID::fromString).toList());
            company.setOthers(result);
            repository.save(company);
            // Save to redis
            if (companyProfile != null) {
                companyProfile.setOthers(result);
                redisRepository.save(
                        RedisCacheConstant.PROFILE,
                        RedisCacheConstant.COMPANY_PROFILE_HASH(account.getAccountId().toString()),
                        companyProfile);
            }
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
    public String updateAvatar(Account account, MultipartFile file) {
        var url = CommonUtils.isEmptyOrNullString(account.getAvatar())
                ? s3Service.uploadFile(file)
                : s3Service.uploadFile(file, account.getAvatar());
        account.setAvatar(url);
        accountRepository.save(account);
        // Save to redis
        var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
        if (companyProfile != null) {
            companyProfile.setAvatar(url);
            redisRepository.save(
                    RedisCacheConstant.PROFILE,
                    RedisCacheConstant.COMPANY_PROFILE_HASH(account.getAccountId().toString()),
                    companyProfile);
        }
        return url;
    }

    @Override
    public Company getAllDataByAccountId(UUID accountId) {
        var company = repository.findById(accountId)
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.COMPANY_PROFILE_NOT_FOUND));
        var account = company.getAccount();
        account = applicationPositionService.getAccountWithAllApplicationPositions(accountId);
        account.setLanguages(languageRepository.findAllByAccountId(accountId));
        company.setAccount(account);
        // Save to redis
        redisRepository.save(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.COMPANY_PROFILE_HASH(company.getAccountId().toString()),
                mapper.toCompanyResponse(company));
        return company;
    }

    @Override
    public Company getAllDataByAccountId(Company company, UUID accountId) {
        var account = company.getAccount();
        account = applicationPositionService.getAccountWithAllApplicationPositions(accountId);
        account.setLanguages(languageRepository.findAllByAccountId(accountId));
        company.setAccount(account);
        // Save to redis
        redisRepository.save(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.COMPANY_PROFILE_HASH(company.getAccountId().toString()),
                mapper.toCompanyResponse(company));
        return company;
    }

    @Override
    public ApiDataResponse getAllData(Pageable pageable) {
        Page<Company> page = repository.findAll(pageable);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(mapper::toCompanyResponseWithBasicInfoOnly)
                .toList(),
                PageUtils.makePageInfo(page));
    }

    private boolean checkDeleteIdsRequest(List<UUID> data, List<String> ids) {
        for (var id : ids) {
            if (!data.contains(UUID.fromString(id)))
                throw new BadRequestException(ErrorMessageConstant.DELETE_IDS_REQUEST_HAVE_ONE_NOT_FOUND);
        }
        return true;
    }

    private CompanyProfileResponse getCompanyProfileFromRedis(String accountId) {
        var profile = redisRepository.findByHashKey(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.COMPANY_PROFILE_HASH(accountId));
        if (profile == null)
            return null;
        return CommonUtils.decodeJson(profile.toString(), CompanyProfileResponse.class);
    }
}
