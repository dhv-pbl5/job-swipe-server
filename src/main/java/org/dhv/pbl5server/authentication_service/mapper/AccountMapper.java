package org.dhv.pbl5server.authentication_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.ApplicationPositionRequest;
import org.dhv.pbl5server.authentication_service.payload.request.CompanyRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.request.UserRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
    config = SpringMapStructConfig.class,
    uses = {
        ConstantMapper.class,
        ApplicationPositionMapper.class,
        ApplicationSkillMapper.class
    }
)
public interface AccountMapper {
    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(UserRegisterRequest request);

    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(CompanyRegisterRequest request);

    @Mapping(source = "systemRole", target = "systemRole")
    @Mapping(source = "applicationPositions", target = "applicationPositions", ignore = true)
    AccountResponse toAccountResponse(Account account);

    @Mapping(source = "systemRole", target = "systemRole")
    @Mapping(source = "applicationPositions", target = "applicationPositions")
    AccountResponse toAccountResponseWithApplicationPositions(Account account);

    @Mapping(source = "account.systemRole", target = "systemRole")
    @Mapping(source = "request", target = "applicationPositions")
    Account toAccount(Account account, List<ApplicationPositionRequest> request);
}
