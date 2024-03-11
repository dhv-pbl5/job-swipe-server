package org.dhv.pbl5server.authentication_service.mapper;

import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.RegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.response.AccountResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(RegisterRequest registerRequest);

    AccountResponse toAccountResponse(Account account);
}
