package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.payload.request.CompanyProfileRequest;
import org.dhv.pbl5server.profile_service.payload.response.CompanyProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = SpringMapStructConfig.class, uses = {ConstantMapper.class, LanguageMapper.class, ApplicationPositionMapper.class})
public interface CompanyMapper {
    public static final String NAMED_ToCompanyResponseWithBasicInfoOnly = "toCompanyResponseWithBasicInfoOnly";

    @Mapping(source = "company.account.email", target = "email")
    @Mapping(source = "company.account.accountStatus", target = "accountStatus")
    @Mapping(source = "company.account.address", target = "address")
    @Mapping(source = "company.account.avatar", target = "avatar")
    @Mapping(source = "company.account.phoneNumber", target = "phoneNumber")
    @Mapping(source = "company.account.systemRole", target = "systemRole")
    @Mapping(source = "company.account.accountId", target = "accountId")
    @Mapping(source = "company.account.deletedAt", target = "deletedAt")
    @Mapping(source = "company.account.applicationPositions", target = "applicationPositions", qualifiedByName = ApplicationPositionMapper.NAMED_ToApplicationPositionResponse)
    @Mapping(source = "company.account.languages", target = "languages", qualifiedByName = LanguageMapper.NAMED_ToLanguageResponse)
    @Mapping(source = "company.others", target = "others")
    CompanyProfileResponse toCompanyResponse(Company company);

    @Named(NAMED_ToCompanyResponseWithBasicInfoOnly)
    @Mapping(source = "company.account.email", target = "email")
    @Mapping(source = "company.account.accountStatus", target = "accountStatus")
    @Mapping(source = "company.account.address", target = "address")
    @Mapping(source = "company.account.avatar", target = "avatar")
    @Mapping(source = "company.account.phoneNumber", target = "phoneNumber")
    @Mapping(source = "company.account.systemRole", target = "systemRole")
    @Mapping(source = "company.account.accountId", target = "accountId")
    @Mapping(source = "company.account.deletedAt", target = "deletedAt")
    @Mapping(source = "company.account.applicationPositions", target = "applicationPositions", ignore = true)
    @Mapping(source = "company.account.languages", target = "languages", ignore = true)
    @Mapping(source = "company.others", target = "others")
    CompanyProfileResponse toCompanyResponseWithBasicInfoOnly(Company company);


    @Mapping(source = "request.companyName", target = "companyName")
    @Mapping(source = "request.companyUrl", target = "companyUrl")
    @Mapping(source = "request.establishedDate", target = "establishedDate")
    @Mapping(source = "request.others", target = "others")
    @Mapping(source = "company.account", target = "account")
    Company toCompany(Company company, CompanyProfileRequest request);

    @Mapping(source = "request.accountStatus", target = "accountStatus")
    @Mapping(source = "request.address", target = "address")
    @Mapping(source = "request.phoneNumber", target = "phoneNumber")
    @Mapping(source = "account.applicationPositions", target = "applicationPositions", ignore = true)
    @Mapping(source = "account.languages", target = "languages", ignore = true)
    Account toAccount(Account account, CompanyProfileRequest request);
}
