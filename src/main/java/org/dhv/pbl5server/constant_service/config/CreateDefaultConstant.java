package org.dhv.pbl5server.constant_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
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
public class CreateDefaultConstant implements CommandLineRunner {
    private final ConstantRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // Create default system role
        createSystemRole();
        // Create default apply position
        createApplyPosition();
        // Create default skill
        createSkill();
        // Create default experience type
        createExperienceType();
        // Create default notification type
        createNotificationType();
    }

    /**
     * Create system role
     */
    private void createSystemRole() {
        if (!repository.findByConstantTypeStartsWith(CommonConstant.SYSTEM_ROLE_PREFIX).isEmpty()) return;
        log.info("--------------------- Creating default system role ---------------------");
        // Admin role
        var adminRole = Constant.builder()
            .constantType(ConstantType.ADMIN.getValue())
            .constantName(SystemRole.Admin.name())
            .build();
        // Company role
        var companyRole = Constant.builder()
            .constantType(ConstantType.COMPANY.getValue())
            .constantName(SystemRole.Company.name())
            .build();
        // User role
        var userRole = Constant.builder()
            .constantType(ConstantType.USER.getValue())
            .constantName(SystemRole.User.name())
            .build();
        try {
            repository.saveAll(List.of(adminRole, companyRole, userRole));
            log.info("--------------------- Successfully created default system role ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default system role: ", ex);
        }
    }

    /**
     * Create apply position
     */
    private void createApplyPosition() {
        if (!repository.findByConstantType(ConstantType.APPLY_POSITION.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default apply position ---------------------");
        var positionName = List.of(
            "HR", "Intern", "Fresher", "Junior", "Senior",
            "Technical Leader", "Project Manager", "Product Owner", "Scrum Master"
        );
        try {
            repository.saveAll(
                positionName.stream()
                    .map(name -> Constant
                        .builder()
                        .constantType(ConstantType.APPLY_POSITION.getValue())
                        .constantName(name)
                        .build()).toList()
            );
            log.info("--------------------- Successfully created default apply position ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default apply position: ", ex);
        }
    }

    /**
     * Create skill
     */
    private void createSkill() {
        if (!repository.findByConstantType(ConstantType.SKILL.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default skill ---------------------");
        var skillName = List.of(
            "Java", "Python", "C++", "C#", "JavaScript",
            "ReactJS", "Angular", "VueJS", "NodeJS", "Spring",
            "Spring Boot", "Hibernate", "JPA", "JDBC", "JSP", "Servlet", "Thymeleaf"
        );
        try {
            repository.saveAll(
                skillName.stream()
                    .map(name -> Constant
                        .builder()
                        .constantType(ConstantType.SKILL.getValue())
                        .constantName(name)
                        .build()).toList()
            );
            log.info("--------------------- Successfully created default skill ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default skill: ", ex);
        }
    }

    /**
     * Create experience type
     */
    private void createExperienceType() {
        if (!repository.findByConstantType(ConstantType.EXPERIENCE_TYPE.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default experience type ---------------------");
        var experienceName = List.of(
            "Working", "Hobby", "Personal Project", "Open Source Project", "Research"
        );
        try {
            repository.saveAll(
                experienceName.stream()
                    .map(name -> Constant
                        .builder()
                        .constantType(ConstantType.EXPERIENCE_TYPE.getValue())
                        .constantName(name)
                        .build()).toList()
            );
            log.info("--------------------- Successfully created default experience type ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default experience type: ", ex);
        }
    }

    /**
     * Create notification type
     */
    private void createNotificationType() {
        if (!repository.findByConstantType(ConstantType.NOTIFICATION_TYPE.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default notification type ---------------------");
        var notificationName = List.of(
            "Message", "Match", "Activate", "Deactivate", "Be Matched", "Be Unmatched"
        );
        try {
            repository.saveAll(
                notificationName.stream()
                    .map(name -> Constant
                        .builder()
                        .constantType(ConstantType.NOTIFICATION_TYPE.getValue())
                        .constantName(name)
                        .build()).toList()
            );
            log.info("--------------------- Successfully created default notification type ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default notification type: ", ex);
        }
    }
}
