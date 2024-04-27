package org.dhv.pbl5server.authentication_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.CompanyRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.request.UserRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.mapper.ApplicationPositionMapper;
import org.dhv.pbl5server.profile_service.mapper.ApplicationSkillMapper;
import org.dhv.pbl5server.profile_service.mapper.LanguageMapper;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationPositionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(
    config = SpringMapStructConfig.class,
    uses = {
        ConstantMapper.class,
        ApplicationPositionMapper.class,
        ApplicationSkillMapper.class,
        LanguageMapper.class
    }
)
public interface AccountMapper {
    public static final String NAMED_ToAccountResponse = "toAccountResponse";

    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(UserRegisterRequest request);

    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(CompanyRegisterRequest request);

    @Named(NAMED_ToAccountResponse)
    @Mapping(source = "systemRole", target = "systemRole")
    @Mapping(source = "applicationPositions", target = "applicationPositions", ignore = true)
    @Mapping(source = "languages", target = "languages", ignore = true)
    AccountResponse toAccountResponse(Account account);

    @Mapping(source = "systemRole", target = "systemRole")
    @Mapping(source = "applicationPositions", target = "applicationPositions")
    AccountResponse toAccountResponseWithApplicationPositions(Account account);

    @Mapping(source = "account.systemRole", target = "systemRole")
    @Mapping(source = "request", target = "applicationPositions")
    Account toAccount(Account account, List<ApplicationPositionRequest> request);
}
