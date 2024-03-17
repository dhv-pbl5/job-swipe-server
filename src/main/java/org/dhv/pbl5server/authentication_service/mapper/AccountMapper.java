package org.dhv.pbl5server.authentication_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.CompanyRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.request.UserRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(UserRegisterRequest request);

    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(CompanyRegisterRequest request);

    @Mapping(source = "systemRole", target = "systemRole")
    AccountResponse toAccountResponse(Account account);
}
