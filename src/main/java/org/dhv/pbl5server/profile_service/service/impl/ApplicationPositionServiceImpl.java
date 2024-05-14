package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.constant.RedisCacheConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.repository.RedisRepository;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.profile_service.entity.ApplicationPosition;
import org.dhv.pbl5server.profile_service.mapper.ApplicationPositionMapper;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationPositionResponse;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationSkillResponse;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.dhv.pbl5server.profile_service.repository.ApplicationPositionRepository;
import org.dhv.pbl5server.profile_service.repository.ApplicationSkillRepository;
import org.dhv.pbl5server.profile_service.service.ApplicationPositionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// git commit -m "PBL-538 company profile"

@Service
@RequiredArgsConstructor
public class ApplicationPositionServiceImpl implements ApplicationPositionService {
    private final ApplicationPositionRepository repository;
    private final ApplicationSkillRepository applicationSkillRepository;
    private final AccountRepository accountRepository;
    private final RedisRepository redisRepository;
    private final ApplicationPositionMapper mapper;
    private final ConstantService constantService;

    @Override
    public List<ApplicationPositionResponse> insertApplicationPositions(Account account,
            List<ApplicationPositionRequest> request) {
        var aps = request.stream().map(mapper::toApplicationPosition).toList();
        // Check constant type of apply_position and apply_skill and set dependency
        aps = checkConstantType(account, aps, true);
        repository.saveAll(aps);
        var responses = getAccountWithAllApplicationPositions(account.getAccountId())
                .getApplicationPositions()
                .stream()
                .map(mapper::toApplicationPositionResponse)
                .toList();
        // Save to redis
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null) {
                    userProfile.setApplicationPositions(responses);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null) {
                    companyProfile.setApplicationPositions(responses);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
        }
        return responses;
    }

    @Override
    public List<ApplicationPositionResponse> updateApplicationPosition(Account account,
            List<ApplicationPositionRequest> requests) {
        List<ApplicationPosition> aps = new ArrayList<>();
        for (var request : requests) {
            if (request.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.APPLICATION_POSITION_ID_REQUIRED);
            var ap = repository.findByIdAndAccountId(account.getAccountId(), request.getId())
                    .orElseThrow(
                            () -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
            ap = mapper.toApplicationPosition(ap, request);
            aps.add(ap);
        }
        // Check constant type of apply_position and apply_skill and set dependency
        aps = checkConstantType(account, aps, false);
        repository.saveAll(aps);
        var responses = aps.stream().map(e -> mapper
                .toApplicationPositionResponse(repository.fetchAllDataApplicationSkillById(e.getId()).orElseThrow()))
                .toList();
        // Save to redis
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null) {
                    var apsInRedis = userProfile.getApplicationPositions();
                    for (var response : responses) {
                        for (var i = 0; i < apsInRedis.size(); i++) {
                            if (apsInRedis.get(i).getId().compareTo(response.getId()) == 0) {
                                apsInRedis.set(i, response);
                                break;
                            }
                        }
                    }
                    userProfile.setApplicationPositions(apsInRedis);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null) {
                    var apsInRedis = companyProfile.getApplicationPositions();
                    for (var response : responses) {
                        for (var i = 0; i < apsInRedis.size(); i++) {
                            if (apsInRedis.get(i).getId().compareTo(response.getId()) == 0) {
                                apsInRedis.set(i, response);
                                break;
                            }
                        }
                    }
                    companyProfile.setApplicationPositions(apsInRedis);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
        }
        return responses;
    }

    @Override
    public ApplicationPositionResponse insertOrUpdateApplicationSkills(Account account, String applicationPositionId,
            List<ApplicationSkillRequest> requests) {
        if (applicationPositionId == null)
            throw new BadRequestException(ErrorMessageConstant.APPLICATION_POSITION_ID_REQUIRED);
        var ap = repository.findByIdAndAccountId(account.getAccountId(), UUID.fromString(applicationPositionId))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
        ap = mapper.toApplicationPosition(ap, requests);
        // Check constant type of apply_position and apply_skill and set dependency
        ap = checkConstantType(account, List.of(ap), false).get(0);
        repository.save(ap);
        var response = mapper
                .toApplicationPositionResponse(repository.fetchAllDataApplicationSkillById(ap.getId()).orElseThrow());
        // Save to redis
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null) {
                    var aps = userProfile.getApplicationPositions();
                    for (var i = 0; i < aps.size(); i++) {
                        if (aps.get(i).getId().compareTo(response.getId()) == 0) {
                            aps.set(i, response);
                            break;
                        }
                    }
                    userProfile.setApplicationPositions(aps);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null) {
                    var aps = companyProfile.getApplicationPositions();
                    for (var i = 0; i < aps.size(); i++) {
                        if (aps.get(i).getId().compareTo(response.getId()) == 0) {
                            aps.set(i, response);
                            break;
                        }
                    }
                    companyProfile.setApplicationPositions(aps);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
        }
        return response;
    }

    @Override
    public List<ApplicationPositionResponse> getApplicationPositions(String accountId) {
        var aps = repository.findAllByAccountId(UUID.fromString(accountId));
        if (aps == null || aps.isEmpty())
            return Collections.emptyList();
        for (var ap : aps)
            ap.setSkills(applicationSkillRepository.findAllByApplicationPositionId(ap.getId()));
        return aps.stream().map(mapper::toApplicationPositionResponse).toList();
    }

    @Override
    public ApiDataResponse getApplicationPositions(String accountId, Pageable pageable) {
        var page = repository.findAllByAccountId(UUID.fromString(accountId), pageable);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(e -> {
                    e.setSkills(applicationSkillRepository.findAllByApplicationPositionId(e.getId()));
                    return mapper.toApplicationPositionResponse(e);
                })
                .toList(),
                PageUtils.makePageInfo(page));
    }

    @Override
    public ApplicationPositionResponse getApplicationPositionById(String accountId, String applicationPositionId) {
        var ap = repository.findByIdAndAccountId(UUID.fromString(accountId), UUID.fromString(applicationPositionId))
                .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
        ap.setSkills(applicationSkillRepository.findAllByApplicationPositionId(ap.getId()));
        return mapper.toApplicationPositionResponse(ap);
    }

    @Override
    public void deleteApplicationPositions(Account account, List<String> ids) {
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        List<ApplicationPositionResponse> aprs = List.of();
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null)
                    aprs = userProfile.getApplicationPositions();
                for (var id : ids) {
                    var ap = repository.fetchAllDataApplicationSkillById(UUID.fromString(id))
                            .orElseThrow(() -> new NotFoundObjectException(
                                    ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
                    if (account.getAccountId().compareTo(ap.getAccount().getAccountId()) != 0)
                        throw new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND);
                    repository.delete(ap);
                    if (!aprs.isEmpty())
                        aprs.removeIf(e -> e.getId().compareTo(UUID.fromString(id)) == 0);
                }
                if (userProfile != null) {
                    userProfile.setApplicationPositions(aprs);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null)
                    aprs = companyProfile.getApplicationPositions();
                for (var id : ids) {
                    var ap = repository.fetchAllDataApplicationSkillById(UUID.fromString(id))
                            .orElseThrow(() -> new NotFoundObjectException(
                                    ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
                    if (account.getAccountId().compareTo(ap.getAccount().getAccountId()) != 0)
                        throw new NotFoundObjectException(ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND);
                    repository.delete(ap);
                    if (!aprs.isEmpty())
                        aprs.removeIf(e -> e.getId().compareTo(UUID.fromString(id)) == 0);
                }
                if (companyProfile != null) {
                    companyProfile.setApplicationPositions(aprs);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
        }
    }

    @Override
    public void deleteApplicationSkills(Account account, String applicationPositionId, List<String> ids) {
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        List<ApplicationPositionResponse> aprs = List.of();
        List<ApplicationSkillResponse> akrs = List.of();
        int index = -1;
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null) {
                    aprs = userProfile.getApplicationPositions();
                    for (var i = 0; i < aprs.size(); i++) {
                        if (aprs.get(i).getId().compareTo(UUID.fromString(applicationPositionId)) == 0) {
                            akrs = aprs.get(i).getSkills();
                            index = i;
                            break;
                        }
                    }
                }
                for (var id : ids) {
                    var ap = repository
                            .findByIdAndAccountId(account.getAccountId(), UUID.fromString(applicationPositionId))
                            .orElseThrow(() -> new NotFoundObjectException(
                                    ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
                    var aps = applicationSkillRepository
                            .findByIdAndApplicationPositionId(ap.getId(), UUID.fromString(id))
                            .orElseThrow(() -> new NotFoundObjectException(
                                    ErrorMessageConstant.APPLICATION_SKILL_NOT_FOUND));
                    applicationSkillRepository.delete(aps);
                    if (!akrs.isEmpty())
                        akrs.removeIf(e -> e.getId().compareTo(UUID.fromString(id)) == 0);
                }
                if (userProfile != null) {
                    var tmp = aprs.get(index);
                    tmp.setSkills(akrs);
                    aprs.set(index, tmp);
                    userProfile.setApplicationPositions(aprs);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null) {
                    aprs = companyProfile.getApplicationPositions();
                    for (var i = 0; i < aprs.size(); i++) {
                        if (aprs.get(i).getId().compareTo(UUID.fromString(applicationPositionId)) == 0) {
                            akrs = aprs.get(i).getSkills();
                            index = i;
                            break;
                        }
                    }
                }
                for (var id : ids) {
                    var ap = repository
                            .findByIdAndAccountId(account.getAccountId(), UUID.fromString(applicationPositionId))
                            .orElseThrow(() -> new NotFoundObjectException(
                                    ErrorMessageConstant.APPLICATION_POSITION_NOT_FOUND));
                    var aps = applicationSkillRepository
                            .findByIdAndApplicationPositionId(ap.getId(), UUID.fromString(id))
                            .orElseThrow(() -> new NotFoundObjectException(
                                    ErrorMessageConstant.APPLICATION_SKILL_NOT_FOUND));
                    applicationSkillRepository.delete(aps);
                    if (!akrs.isEmpty())
                        akrs.removeIf(e -> e.getId().compareTo(UUID.fromString(id)) == 0);
                }
                if (companyProfile != null) {
                    var tmp = aprs.get(index);
                    tmp.setSkills(akrs);
                    aprs.set(index, tmp);
                    companyProfile.setApplicationPositions(aprs);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
        }
    }

    @Override
    public Account getAccountWithAllApplicationPositions(UUID id) {
        var account = accountRepository.fetchAllDataApplicationPositionByAccountId(id).orElseThrow(
                () -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
        for (var ap : account.getApplicationPositions()) {
            ap.setSkills(applicationSkillRepository.findAllByApplicationPositionId(ap.getId()));
        }
        return account;
    }

    private List<ApplicationPosition> checkConstantType(Account account, List<ApplicationPosition> aps,
            boolean isInsert) {
        // Check all apply_position constant type
        constantService.checkConstantWithType(aps.stream().map(e -> e.getApplyPosition().getConstantId()).toList(),
                ConstantTypePrefix.APPLY_POSITIONS);
        return aps.stream().peek(ap -> {
            if (isInsert)
                ap.setId(null);
            var positionConstant = constantService.getConstantById(ap.getApplyPosition().getConstantId());
            ap.setApplyPosition(Constant.builder()
                    .constantId(positionConstant.getConstantId())
                    .constantType(positionConstant.getConstantType())
                    .constantName(positionConstant.getConstantName())
                    .note(positionConstant.getNote())
                    .build());
            var salaryConstant = constantService.getConstantById(ap.getSalaryRange().getConstantId());
            ap.setSalaryRange(Constant.builder()
                    .constantId(salaryConstant.getConstantId())
                    .constantType(salaryConstant.getConstantType())
                    .constantName(salaryConstant.getConstantName())
                    .note(salaryConstant.getNote())
                    .build());
            ap.setAccount(account);
            if (CommonUtils.isNotEmptyOrNullList(ap.getSkills())) {
                // Check all apply_skill constant type
                constantService.checkConstantWithType(
                        ap.getSkills().stream().map(e -> e.getSkill().getConstantId()).toList(),
                        ConstantTypePrefix.SKILLS);
                ap.setSkills(ap.getSkills().stream().peek(skill -> {
                    if (isInsert)
                        skill.setId(null);
                    var skillConstant = constantService.getConstantById(skill.getSkill().getConstantId());
                    skill.setSkill(Constant.builder()
                            .constantId(skillConstant.getConstantId())
                            .constantType(skillConstant.getConstantType())
                            .constantName(skillConstant.getConstantName())
                            .note(skillConstant.getNote())
                            .build());
                    skill.setApplicationPosition(ap);
                }).toList());
            }
        }).toList();
    }

    private UserProfileResponse getUserProfileFromRedis(String accountId) {
        var profile = redisRepository.findByHashKey(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.USER_PROFILE_HASH(accountId));
        if (profile == null)
            return null;
        return CommonUtils.decodeJson(profile.toString(), UserProfileResponse.class);
    }

    private void saveUserProfileToRedis(UserProfileResponse profile) {
        redisRepository.save(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.USER_PROFILE_HASH(profile.getAccountId().toString()),
                profile);
    }

    private CompanyProfileResponse getCompanyProfileFromRedis(String accountId) {
        var profile = redisRepository.findByHashKey(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.COMPANY_PROFILE_HASH(accountId));
        if (profile == null)
            return null;
        return CommonUtils.decodeJson(profile.toString(), CompanyProfileResponse.class);
    }

    private void saveCompanyProfileToRedis(CompanyProfileResponse profile) {
        redisRepository.save(
                RedisCacheConstant.PROFILE,
                RedisCacheConstant.COMPANY_PROFILE_HASH(profile.getAccountId().toString()),
                profile);
    }
}
