package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.payload.response.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.accountStatus", target = "accountStatus")
    @Mapping(source = "account.address", target = "address")
    @Mapping(source = "account.avatar", target = "avatar")
    @Mapping(source = "account.phoneNumber", target = "phoneNumber")
    @Mapping(source = "account.systemRole", target = "systemRole")
    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.deletedAt", target = "deletedAt")
    @Mapping(source = "account.applicationPositions", target = "applicationPositions")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.gender", target = "gender")
    @Mapping(source = "user.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "user.summaryIntroduction", target = "summaryIntroduction")
    @Mapping(source = "user.socialMediaLink", target = "socialMediaLink")
    @Mapping(source = "user.createdAt", target = "createdAt")
    @Mapping(source = "user.updatedAt", target = "updatedAt")
    @Mapping(source = "user.other", target = "other")
    @Mapping(source = "user.educations", target = "educations")
    @Mapping(source = "user.awards", target = "awards")
    @Mapping(source = "user.experiences", target = "experiences")
    UserProfileResponse toUserProfileResponse(Account account, User user);

    User toUser(Account account);
}
