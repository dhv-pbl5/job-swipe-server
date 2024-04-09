package org.dhv.pbl5server.authentication_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.CompanyRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.request.UserRegisterRequest;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class CreateDefaultAccount implements CommandLineRunner {
    private final AccountRepository repository;
    private final AuthService authService;
    private final ConstantService constantService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("unchecked")
    public void run(String... args) throws Exception {
        // If data was existed, will return
        if (!repository.findAll().isEmpty()) return;
        log.info("--------------------- Creating default account ---------------------");
        // Admin account
        var adminAccount = Account.builder()
            .email("admin@gmail.com")
            .password(passwordEncoder.encode("123456Aa"))
            .address("Default")
            .phoneNumber("Default")
            .build();
        // User account
        var userRegisterRequest = UserRegisterRequest.builder()
            .email("user@gmail.com")
            .password("123456Aa")
            .address("Default")
            .phoneNumber("Default")
            .dateOfBirth(CommonUtils.getCurrentTimestamp())
            .lastName("Nguyen Van")
            .firstName("A")
            .gender(true)
            .build();
        // Company account
        var companyRegisterRequest = CompanyRegisterRequest.builder()
            .email("company@gmail.com")
            .password("123456Aa")
            .address("Default")
            .phoneNumber("Default")
            .companyName("Default Company")
            .companyUrl("http://default-company")
            .establishedDate(CommonUtils.getCurrentTimestamp())
            .build();
        for (var item : (List<Constant>) constantService.getSystemRoles(null)) {
            if (item.getConstantName().equalsIgnoreCase(SystemRoleName.ADMIN.name()))
                adminAccount.setSystemRole(item);
            if (item.getConstantName().equalsIgnoreCase(SystemRoleName.COMPANY.name()))
                companyRegisterRequest.setSystemRole(new ConstantSelectionRequest(item.getConstantId().toString()));
            if (item.getConstantName().equalsIgnoreCase(SystemRoleName.USER.name()))
                userRegisterRequest.setSystemRole(new ConstantSelectionRequest(item.getConstantId().toString()));
        }
        try {
            repository.save(adminAccount);
            authService.register(userRegisterRequest);
            authService.register(companyRegisterRequest);
            log.info("--------------------- Successfully created default account ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default account: ", ex);
        }
    }
}
