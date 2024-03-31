package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.repository.CrudDbJsonArrayRepository;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.entity.UserAward;
import org.dhv.pbl5server.profile_service.entity.UserEducation;
import org.dhv.pbl5server.profile_service.entity.UserExperience;
import org.dhv.pbl5server.profile_service.mapper.AwardMapper;
import org.dhv.pbl5server.profile_service.mapper.EducationMapper;
import org.dhv.pbl5server.profile_service.mapper.ExperienceMapper;
import org.dhv.pbl5server.profile_service.mapper.UserMapper;
import org.dhv.pbl5server.profile_service.model.OtherDescription;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserAwardResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserEducationResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserExperienceResponse;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.dhv.pbl5server.profile_service.repository.UserAwardRepository;
import org.dhv.pbl5server.profile_service.repository.UserEducationRepository;
import org.dhv.pbl5server.profile_service.repository.UserExperienceRepository;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.dhv.pbl5server.profile_service.service.UserService;
import org.dhv.pbl5server.s3_service.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final CrudDbJsonArrayRepository<OtherDescription, UUID> otherDescriptionRepository;
    private final AccountRepository accountRepository;
    private final UserEducationRepository educationRepository;
    private final UserAwardRepository awardRepository;
    private final UserExperienceRepository experienceRepository;
    private final UserMapper userMapper;
    private final EducationMapper educationMapper;
    private final AwardMapper awardMapper;
    private final ExperienceMapper experienceMapper;
    private final S3Service s3Service;

    @Override
    public UserProfileResponse getUserProfile(Account account) {
        User user = getAllDataByAccountId(account.getAccountId());
        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse getUserProfileById(String id) {
        return userMapper.toUserProfileResponse(getAllDataByAccountId(UUID.fromString(id)));
    }

    @Override
    public UserAwardResponse getUserAwardById(String id) {
        return awardMapper.toUserAwardResponse(awardRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.AWARD_NOT_FOUND)));
    }

    @Override
    public UserEducationResponse getUserEducationById(String id) {
        return educationMapper.toUserEducationResponse(educationRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.EDUCATION_NOT_FOUND)));
    }

    @Override
    public UserExperienceResponse getUserExperienceById(String id) {
        return experienceMapper.toUserExperienceResponse(experienceRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.EXPERIENCE_NOT_FOUND)));
    }

    @Override
    public OtherDescription getUserOtherDescriptionById(String userId, String id) {
        var user = repository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        return otherDescriptionRepository.findById(user.getOthers(), UUID.fromString(id))
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.OTHER_DESCRIPTION_NOT_FOUND));
    }

    @Override
    public UserProfileResponse insertEducations(Account account, List<UserEducationRequest> request) {
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        // Remove id if exist
        request.forEach(e -> e.setId(null));
        var updatedUser = userMapper.toUserWithinListEducations(user, request);
        for (var education : updatedUser.getEducations()) {
            if (education.getStudyEndTime().before(education.getStudyStartTime()))
                throw new NotFoundObjectException(ErrorMessageConstant.EDUCATION_TIME_INVALID);
            education.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse insertExperiences(Account account, List<UserExperienceRequest> request) {
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        // Remove id if exist
        request.forEach(e -> e.setId(null));
        var updatedUser = userMapper.toUserWithinListExperiences(user, request);
        for (var experience : updatedUser.getExperiences()) {
            if (experience.getExperienceEndTime().before(experience.getExperienceStartTime()))
                throw new NotFoundObjectException(ErrorMessageConstant.EXPERIENCE_TIME_INVALID);
            experience.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse insertOtherDescriptions(Account account, List<OtherDescription> request) {
        // Remove id if exist
        request.forEach(e -> e.setId(null));
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        user.setOthers(otherDescriptionRepository.saveAll(user.getOthers(), request));
        repository.save(user);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse insertAwards(Account account, List<UserAwardRequest> request) {
        // Remove id if exist
        request.forEach(e -> e.setId(null));
        var user = repository.fetchAllDataEducationById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListAwards(user, request);
        for (var award : updatedUser.getAwards()) {
            award.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse updateBasicInfo(Account account, UserBasicInfoRequest request) {
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUser(user, request);
        var updatedAccount = userMapper.toAccount(account, request);
        updatedUser.setAccount(updatedAccount);
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse updateEducations(Account account, List<UserEducationRequest> request) {
        for (var req : request) {
            if (req.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.EDUCATION_ID_IS_REQUIRED);
            if (!educationRepository.existsById(req.getId()))
                throw new NotFoundObjectException(ErrorMessageConstant.EDUCATION_NOT_FOUND);
        }
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListEducations(user, request);
        for (var education : updatedUser.getEducations()) {
            if (education.getStudyEndTime().before(education.getStudyStartTime()))
                throw new NotFoundObjectException(ErrorMessageConstant.EDUCATION_TIME_INVALID);
            education.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse updateExperiences(Account account, List<UserExperienceRequest> request) {
        for (var req : request) {
            if (req.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.EXPERIENCE_ID_IS_REQUIRED);
            if (!experienceRepository.existsById(req.getId()))
                throw new NotFoundObjectException(ErrorMessageConstant.EXPERIENCE_NOT_FOUND);
        }
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListExperiences(user, request);
        for (var experience : updatedUser.getExperiences()) {
            if (experience.getExperienceEndTime().before(experience.getExperienceStartTime()))
                throw new NotFoundObjectException(ErrorMessageConstant.EXPERIENCE_TIME_INVALID);
            experience.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse updateOtherDescriptions(Account account, List<OtherDescription> request) {
        var user = repository.findById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        for (var req : request) {
            if (req.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.OTHER_DESCRIPTION_ID_IS_REQUIRED);
            if (!otherDescriptionRepository.existsById(user.getOthers(), req.getId()))
                throw new NotFoundObjectException(ErrorMessageConstant.OTHER_DESCRIPTION_NOT_FOUND);
        }
        user.setOthers(otherDescriptionRepository.saveAll(user.getOthers(), request));
        repository.save(user);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public UserProfileResponse updateAwards(Account account, List<UserAwardRequest> request) {
        for (var req : request) {
            if (req.getId() == null)
                throw new BadRequestException(ErrorMessageConstant.AWARD_ID_IS_REQUIRED);
            if (!awardRepository.existsById(req.getId()))
                throw new NotFoundObjectException(ErrorMessageConstant.AWARD_NOT_FOUND);
        }
        var user = repository.fetchAllDataEducationById(account.getAccountId())
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        var updatedUser = userMapper.toUserWithinListAwards(user, request);
        for (var award : updatedUser.getAwards()) {
            award.setUser(user);
        }
        repository.save(updatedUser);
        return userMapper.toUserProfileResponse(getAllDataByAccountId(account.getAccountId()));
    }

    @Override
    public String updateAvatar(Account account, MultipartFile file) {
        var url = CommonUtils.isEmptyOrNullString(account.getAvatar())
            ? s3Service.uploadFile(file)
            : s3Service.uploadFile(file, account.getAvatar());
        account.setAvatar(url);
        accountRepository.save(account);
        return url;
    }

    @Override
    public void deleteEducations(Account account, List<String> ids) {
        var user = repository.fetchAllDataEducationById(account.getAccountId()).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND)
        );
        if (checkDeleteIdsRequest(user.getEducations().stream().map(UserEducation::getId).toList(), ids)) {
            ids.forEach(id -> educationRepository.deleteById(UUID.fromString(id)));
        }
    }

    @Override
    public void deleteAwards(Account account, List<String> ids) {
        var user = repository.fetchAllDataAwardById(account.getAccountId()).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND)
        );
        if (checkDeleteIdsRequest(user.getAwards().stream().map(UserAward::getId).toList(), ids)) {
            ids.forEach(id -> awardRepository.deleteById(UUID.fromString(id)));
        }
    }

    @Override
    public void deleteExperiences(Account account, List<String> ids) {
        var user = repository.fetchAllDataExperienceById(account.getAccountId()).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND)
        );
        if (checkDeleteIdsRequest(user.getExperiences().stream().map(UserExperience::getId).toList(), ids)) {
            ids.forEach(id -> experienceRepository.deleteById(UUID.fromString(id)));
        }
    }

    @Override
    public void deleteOtherDescriptions(Account account, List<String> ids) {
        var user = repository.findById(account.getAccountId()).orElseThrow(
            () -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND)
        );
        if (checkDeleteIdsRequest(user.getOthers().stream().map(OtherDescription::getId).toList(), ids)) {
            var result = otherDescriptionRepository.deleteAllById(user.getOthers(), ids.stream().map(UUID::fromString).toList());
            user.setOthers(result);
            repository.save(user);
        }
    }


    @Override
    public User getAllDataByAccountId(UUID accountId) {
        // Education
        User user = repository.fetchAllDataEducationById(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND));
        // Experience
        user.setExperiences(repository.fetchAllDataExperienceById(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND))
            .getExperiences());
        // Award
        user.setAwards(repository.fetchAllDataAwardById(accountId)
            .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.USER_NOT_FOUND))
            .getAwards());
        return user;
    }

    private boolean checkDeleteIdsRequest(List<UUID> data, List<String> ids) {
        for (var id : ids) {
            if (!data.contains(UUID.fromString(id)))
                throw new BadRequestException(ErrorMessageConstant.DELETE_IDS_REQUEST_HAVE_ONE_NOT_FOUND);
        }
        return true;
    }
}
