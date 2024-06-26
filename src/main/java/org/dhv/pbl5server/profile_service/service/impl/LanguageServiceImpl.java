package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.constant.RedisCacheConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.repository.RedisRepository;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.model.LanguageConstantNote;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.profile_service.entity.Language;
import org.dhv.pbl5server.profile_service.mapper.LanguageMapper;
import org.dhv.pbl5server.profile_service.payload.request.LanguageRequest;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.dhv.pbl5server.profile_service.payload.response.LanguageResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.dhv.pbl5server.profile_service.repository.LanguageRepository;
import org.dhv.pbl5server.profile_service.service.LanguageService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final RedisRepository redisRepository;
    private final LanguageMapper languageMapper;
    private final ConstantService constantService;

    @Override
    public List<LanguageResponse> insertLanguage(Account account, List<LanguageRequest> requests) {
        var languages = requests.stream().map(languageMapper::toLanguage).toList();
        // Check constant type of language and set dependency
        languages = checkConstantType(account, languages, true);
        languageRepository.saveAll(languages);
        var responses = languageRepository.findAllByAccountId(account.getAccountId())
            .stream()
            .map(languageMapper::toLanguageResponse)
            .toList();
        // Save to redis
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null) {
                    userProfile.setLanguages(responses);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null) {
                    companyProfile.setLanguages(responses);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
            default:
                break;
        }
        return responses;
    }

    @Override
    public List<LanguageResponse> updateLanguage(Account account, List<LanguageRequest> requests) {
        List<Language> languages = new ArrayList<>();
        for (var request : requests) {
            if (request.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.LANGUAGE_ID_REQUIRED);
            var language = languageRepository.findByIdAndAccountId(request.getId(), account.getAccountId())
                .orElseThrow(() -> new BadRequestException(ErrorMessageConstant.LANGUAGE_NOT_FOUND));
            language = languageMapper.toLanguage(language, request);
            languages.add(language);
        }
        // Check constant type of language and set dependency
        languages = checkConstantType(account, languages, false);
        languageRepository.saveAll(languages);
        var responses = languages.stream().map(languageMapper::toLanguageResponse).toList();
        // Save to redis
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null) {
                    var languagesInRedis = userProfile.getLanguages();
                    for (var response : responses) {
                        for (var i = 0; i < languagesInRedis.size(); i++) {
                            if (languagesInRedis.get(i).getId().compareTo(response.getId()) == 0) {
                                languagesInRedis.set(i, response);
                                break;
                            }
                        }
                    }
                    userProfile.setLanguages(languagesInRedis);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null) {
                    var languagesInRedis = companyProfile.getLanguages();
                    for (var response : responses) {
                        for (var i = 0; i < languagesInRedis.size(); i++) {
                            if (languagesInRedis.get(i).getId().compareTo(response.getId()) == 0) {
                                languagesInRedis.set(i, response);
                                break;
                            }
                        }
                    }
                    companyProfile.setLanguages(languagesInRedis);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
            default:
                break;
        }
        return responses;
    }

    @Override
    public List<LanguageResponse> getLanguages(String accountId) {
        return languageRepository.findAllByAccountId(UUID.fromString(accountId))
            .stream()
            .map(languageMapper::toLanguageResponse)
            .toList();
    }

    @Override
    public ApiDataResponse getLanguages(String accountId, Pageable pageable) {
        var page = languageRepository.findAllByAccountId(UUID.fromString(accountId), pageable);
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(languageMapper::toLanguageResponse)
                .toList(),
            PageUtils.makePageInfo(page));
    }

    @Override
    public LanguageResponse getLanguageById(String accountId, String languageId) {
        var language = languageRepository.findByIdAndAccountId(UUID.fromString(languageId), UUID.fromString(accountId))
            .orElseThrow(() -> new BadRequestException(ErrorMessageConstant.LANGUAGE_NOT_FOUND));
        return languageMapper.toLanguageResponse(language);
    }

    @Override
    public void deleteLanguages(Account account, List<String> ids) {
        var role = AbstractEnum.fromString(SystemRoleName.values(), account.getSystemRole().getConstantName());
        List<LanguageResponse> languages = List.of();
        switch (role) {
            case USER:
                var userProfile = getUserProfileFromRedis(account.getAccountId().toString());
                if (userProfile != null)
                    languages = userProfile.getLanguages();
                for (var id : ids) {
                    var language = languageRepository.findByIdAndAccountId(UUID.fromString(id), account.getAccountId())
                        .orElseThrow(() -> new BadRequestException(ErrorMessageConstant.LANGUAGE_NOT_FOUND));
                    languageRepository.delete(language);
                    if (!languages.isEmpty())
                        languages.removeIf(e -> e.getId().compareTo(UUID.fromString(id)) == 0);
                }
                if (userProfile != null) {
                    userProfile.setLanguages(languages);
                    saveUserProfileToRedis(userProfile);
                }
                break;
            case COMPANY:
                var companyProfile = getCompanyProfileFromRedis(account.getAccountId().toString());
                if (companyProfile != null)
                    languages = companyProfile.getLanguages();
                for (var id : ids) {
                    var language = languageRepository.findByIdAndAccountId(UUID.fromString(id), account.getAccountId())
                        .orElseThrow(() -> new BadRequestException(ErrorMessageConstant.LANGUAGE_NOT_FOUND));
                    languageRepository.delete(language);
                    if (!languages.isEmpty())
                        languages.removeIf(e -> e.getId().compareTo(UUID.fromString(id)) == 0);
                }
                if (companyProfile != null) {
                    companyProfile.setLanguages(languages);
                    saveCompanyProfileToRedis(companyProfile);
                }
                break;
            default:
                break;
        }
    }

    private List<Language> checkConstantType(Account account, List<Language> languages, boolean isInsert) {
        // Check all language constant type
        constantService.checkConstantWithType(languages.stream().map(e -> e.getLanguage().getConstantId()).toList(),
            ConstantTypePrefix.LANGUAGES);
        return languages.stream().peek(language -> {
            if (isInsert)
                language.setId(null);
            language.setAccount(account);
            var constant = constantService.getConstantById(language.getLanguage().getConstantId());
            if (!checkValidScore(constant.getNote(), language.getLanguageScore()))
                throw new BadRequestException(ErrorMessageConstant.LANGUAGE_SCORE_INVALID);
            language.setLanguage(Constant.builder()
                .constantId(constant.getConstantId())
                .constantName(constant.getConstantName())
                .constantType(constant.getConstantType())
                .note(constant.getNote())
                .build());
        }).toList();
    }

    private boolean checkValidScore(Object validateObj, String scoreStr) {
        try {
            if (validateObj == null)
                return true;
            var note = CommonUtils.decodeJson(CommonUtils.convertToJson(validateObj), LanguageConstantNote.class);
            // If values not null
            if (note != null && note.getValues() != null && !note.getValues().contains(scoreStr))
                return false;
            else if (note != null && note.getValues() != null && note.getValues().contains(scoreStr))
                return true;
            // If not required points, return true
            if (note == null
                || (note.getValidate().getRequiredPoints() != null && !note.getValidate().getRequiredPoints()))
                return true;
            // If required points, check score
            var validator = note.getValidate();
            var score = Double.parseDouble(scoreStr);
            if (validator.getMin() != null && score < validator.getMin())
                return false;
            if (validator.getMax() != null && score > validator.getMax())
                return false;
            if (validator.getDivisible() != null && score % validator.getDivisible() != 0)
                return false;
            return true;
        } catch (Exception e) {
            LogUtils.error("LANGUAGE SERVICE IMPL", e);
            return false;
        }
    }

    private UserProfileResponse getUserProfileFromRedis(String accountId) {
        var profile = redisRepository.findByHashKey(
            RedisCacheConstant.PROFILE_KEY,
            RedisCacheConstant.USER_PROFILE_HASH(accountId));
        if (profile == null)
            return null;
        return CommonUtils.decodeJson(profile.toString(), UserProfileResponse.class);
    }

    private void saveUserProfileToRedis(UserProfileResponse profile) {
        redisRepository.save(
            RedisCacheConstant.PROFILE_KEY,
            RedisCacheConstant.USER_PROFILE_HASH(profile.getAccountId().toString()),
            profile);
    }

    private CompanyProfileResponse getCompanyProfileFromRedis(String accountId) {
        var profile = redisRepository.findByHashKey(
            RedisCacheConstant.PROFILE_KEY,
            RedisCacheConstant.COMPANY_PROFILE_HASH(accountId));
        if (profile == null)
            return null;
        return CommonUtils.decodeJson(profile.toString(), CompanyProfileResponse.class);
    }

    private void saveCompanyProfileToRedis(CompanyProfileResponse profile) {
        redisRepository.save(
            RedisCacheConstant.PROFILE_KEY,
            RedisCacheConstant.COMPANY_PROFILE_HASH(profile.getAccountId().toString()),
            profile);
    }
}
