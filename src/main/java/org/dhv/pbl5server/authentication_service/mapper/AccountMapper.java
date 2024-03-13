package org.dhv.pbl5server.authentication_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.RegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "systemRole", target = "systemRole")
    Account toAccount(RegisterRequest registerRequest);

    @Mapping(source = "systemRole", target = "systemRole")
    AccountResponse toAccountResponse(Account account);
}
