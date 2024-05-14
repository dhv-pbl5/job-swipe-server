package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.payload.request.UserAwardRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserBasicInfoRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserEducationRequest;
import org.dhv.pbl5server.profile_service.payload.request.UserExperienceRequest;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

// git commit -m "PBL-536 user profile"
// git commit -m "PBL-559 user experience"

@Mapper(config = SpringMapStructConfig.class, uses = {
        ConstantMapper.class,
        EducationMapper.class,
        AwardMapper.class,
        ExperienceMapper.class,
        ApplicationPositionMapper.class,
        ApplicationSkillMapper.class,
        LanguageMapper.class
})
public interface UserMapper {
    public static final String NAMED_ToUserProfileResponseWithBasicInfoOnly = "toUserProfileResponseWithBasicInfoOnly";

    @Mapping(source = "user.account.email", target = "email")
    @Mapping(source = "user.account.accountStatus", target = "accountStatus")
    @Mapping(source = "user.account.address", target = "address")
    @Mapping(source = "user.account.avatar", target = "avatar")
    @Mapping(source = "user.account.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.account.systemRole", target = "systemRole")
    @Mapping(source = "user.account.accountId", target = "accountId")
    @Mapping(source = "user.account.deletedAt", target = "deletedAt")
    @Mapping(source = "user.account.applicationPositions", target = "applicationPositions", qualifiedByName = ApplicationPositionMapper.NAMED_ToApplicationPositionResponse)
    @Mapping(source = "user.account.languages", target = "languages", qualifiedByName = LanguageMapper.NAMED_ToLanguageResponse)
    @Mapping(source = "user.others", target = "others")
    @Mapping(source = "user.educations", target = "educations")
    @Mapping(source = "user.awards", target = "awards")
    @Mapping(source = "user.experiences", target = "experiences")
    UserProfileResponse toUserProfileResponse(User user);

    @Named(NAMED_ToUserProfileResponseWithBasicInfoOnly)
    @Mapping(source = "user.account.email", target = "email")
    @Mapping(source = "user.account.accountStatus", target = "accountStatus")
    @Mapping(source = "user.account.address", target = "address")
    @Mapping(source = "user.account.avatar", target = "avatar")
    @Mapping(source = "user.account.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.account.systemRole", target = "systemRole")
    @Mapping(source = "user.account.accountId", target = "accountId")
    @Mapping(source = "user.account.deletedAt", target = "deletedAt")
    @Mapping(source = "user.account.applicationPositions", target = "applicationPositions", ignore = true)
    @Mapping(source = "user.account.languages", target = "languages", ignore = true)
    @Mapping(source = "user.others", target = "others")
    @Mapping(source = "user.educations", target = "educations", ignore = true)
    @Mapping(source = "user.awards", target = "awards", ignore = true)
    @Mapping(source = "user.experiences", target = "experiences", ignore = true)
    UserProfileResponse toUserProfileResponseWithBasicInfoOnly(User user);

    @Mapping(source = "request.firstName", target = "firstName")
    @Mapping(source = "request.lastName", target = "lastName")
    @Mapping(source = "request.gender", target = "gender")
    @Mapping(source = "request.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "request.summaryIntroduction", target = "summaryIntroduction")
    @Mapping(source = "request.socialMediaLink", target = "socialMediaLink")
    @Mapping(source = "user.others", target = "others")
    @Mapping(source = "user.educations", target = "educations", ignore = true)
    @Mapping(source = "user.awards", target = "awards", ignore = true)
    @Mapping(source = "user.experiences", target = "experiences", ignore = true)
    @Mapping(source = "user.account", target = "account")
    User toUser(User user, UserBasicInfoRequest request);

    @Mapping(source = "user.others", target = "others")
    @Mapping(source = "requests", target = "educations")
    @Mapping(source = "user.awards", target = "awards", ignore = true)
    @Mapping(source = "user.experiences", target = "experiences", ignore = true)
    @Mapping(source = "user.account", target = "account")
    User toUserWithinListEducations(User user, List<UserEducationRequest> requests);

    @Mapping(source = "user.others", target = "others")
    @Mapping(source = "user.educations", target = "educations", ignore = true)
    @Mapping(source = "requests", target = "awards")
    @Mapping(source = "user.experiences", target = "experiences", ignore = true)
    @Mapping(source = "user.account", target = "account")
    User toUserWithinListAwards(User user, List<UserAwardRequest> requests);

    @Mapping(source = "user.others", target = "others")
    @Mapping(source = "user.educations", target = "educations", ignore = true)
    @Mapping(source = "user.awards", target = "awards", ignore = true)
    @Mapping(source = "requests", target = "experiences")
    @Mapping(source = "user.account", target = "account")
    User toUserWithinListExperiences(User user, List<UserExperienceRequest> requests);

    @Mapping(source = "request.accountStatus", target = "accountStatus")
    @Mapping(source = "request.address", target = "address")
    @Mapping(source = "request.phoneNumber", target = "phoneNumber")
    @Mapping(source = "account.applicationPositions", target = "applicationPositions", ignore = true)
    @Mapping(source = "account.languages", target = "languages", ignore = true)
    Account toAccount(Account account, UserBasicInfoRequest request);
}
