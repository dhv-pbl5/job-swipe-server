package org.dhv.pbl5server.constant_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantType;
import org.dhv.pbl5server.constant_service.enums.SystemRole;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
@Order(1)
public class CreateDefaultSystemRole implements CommandLineRunner {
    private final ConstantRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // If data was existed, will return
        if (!repository.findAll().isEmpty()) return;
        log.info("--------------------- Creating default system role ---------------------");
        // Admin role
        var adminRole = Constant.builder()
            .constantType(ConstantType.ADMIN.getValue())
            .constantName(SystemRole.ADMIN.name())
            .build();
        // Company role
        var companyRole = Constant.builder()
            .constantType(ConstantType.COMPANY.getValue())
            .constantName(SystemRole.COMPANY.name())
            .build();
        // User role
        var userRole = Constant.builder()
            .constantType(ConstantType.USER.getValue())
            .constantName(SystemRole.USER.name())
            .build();
        try {
            repository.saveAll(List.of(adminRole, companyRole, userRole));
            log.info("--------------------- Successfully created default system role ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default system role: ", ex);
        }
    }
}
