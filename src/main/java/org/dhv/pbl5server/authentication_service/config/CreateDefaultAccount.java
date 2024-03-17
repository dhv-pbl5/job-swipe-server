package org.dhv.pbl5server.authentication_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.enums.SystemRole;
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
    private final ConstantService constantService;
    private final PasswordEncoder passwordEncoder;

    @Override
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
        var userAccount = Account.builder()
            .email("user@gmail.com")
            .password(passwordEncoder.encode("123456Aa"))
            .address("Default")
            .phoneNumber("Default")
            .build();
        // Company account
        var companyAccount = Account.builder()
            .email("company@gmail.com")
            .password(passwordEncoder.encode("123456Aa"))
            .address("Default")
            .phoneNumber("Default")
            .build();
        for (var item : constantService.getConstantsByType(ConstantType.SYSTEM_ROLE.name())) {
            if (item.getConstantName().equalsIgnoreCase(SystemRole.ADMIN.name()))
                adminAccount.setSystemRole(item);
            if (item.getConstantName().equalsIgnoreCase(SystemRole.COMPANY.name()))
                companyAccount.setSystemRole(item);
            if (item.getConstantName().equalsIgnoreCase(SystemRole.USER.name()))
                userAccount.setSystemRole(item);
        }
        try {
            repository.saveAll(List.of(adminAccount, companyAccount, userAccount));
            log.info("Successfully created default account");
        } catch (Exception ex) {
            log.error("Error when creating default account: ", ex);
        }
    }
}
