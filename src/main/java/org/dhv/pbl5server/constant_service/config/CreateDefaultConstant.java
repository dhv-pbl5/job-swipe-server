package org.dhv.pbl5server.constant_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.ConstantTypePrefix;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.repository.ConstantRepository;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (!repository.findByConstantTypeStartsWith(ConstantTypePrefix.SYSTEM_ROLE.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default system role ---------------------");
        // Admin role
        var adminRole = Constant.builder()
            .constantType("%s100".formatted(ConstantTypePrefix.SYSTEM_ROLE.getValue()))
            .constantName(SystemRoleName.ADMIN.getValue())
            .build();
        // Company role
        var companyRole = Constant.builder()
            .constantType("%s110".formatted(ConstantTypePrefix.SYSTEM_ROLE.getValue()))
            .constantName(SystemRoleName.COMPANY.getValue())
            .build();
        // User role
        var userRole = Constant.builder()
            .constantType("%s120".formatted(ConstantTypePrefix.SYSTEM_ROLE.getValue()))
            .constantName(SystemRoleName.USER.getValue())
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
        if (!repository.findByConstantTypeStartsWith(ConstantTypePrefix.APPLY_POSITION.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default apply position ---------------------");
        var positionName = List.of(
            "HR", "Intern", "Fresher", "Junior", "Senior",
            "Technical Leader", "Project Manager", "Product Owner", "Scrum Master"
        );
        try {
            List<Integer> randomList = new ArrayList<>();
            repository.saveAll(
                positionName.stream()
                    .map(name -> {
                        int randomNum = getRandomNumber(100, 999, randomList);
                        randomList.add(randomNum);
                        return Constant
                            .builder()
                            .constantType(ConstantTypePrefix.APPLY_POSITION.getValue() + randomNum)
                            .constantName(name)
                            .build();
                    }).toList()
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
        if (!repository.findByConstantTypeStartsWith(ConstantTypePrefix.SKILL.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default skill ---------------------");
        var skillName = List.of(
            "Java", "Python", "C++", "C#", "JavaScript",
            "ReactJS", "Angular", "VueJS", "NodeJS", "Spring",
            "Spring Boot", "Hibernate", "JPA", "JDBC", "JSP", "Servlet", "Thymeleaf"
        );
        try {
            List<Integer> randomList = new ArrayList<>();
            repository.saveAll(
                skillName.stream()
                    .map(name -> {
                        int randomNum = getRandomNumber(100, 999, randomList);
                        randomList.add(randomNum);
                        return Constant
                            .builder()
                            .constantType(ConstantTypePrefix.SKILL.getValue() + randomNum)
                            .constantName(name)
                            .build();
                    }).toList()
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
        if (!repository.findByConstantTypeStartsWith(ConstantTypePrefix.EXPERIENCE_TYPE.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default experience type ---------------------");
        var experienceName = List.of(
            "Working", "Hobby", "Personal Project", "Open Source Project", "Research"
        );
        try {
            List<Integer> randomList = new ArrayList<>();
            repository.saveAll(
                experienceName.stream()
                    .map(name -> {
                        int randomNum = getRandomNumber(100, 999, randomList);
                        randomList.add(randomNum);
                        return Constant
                            .builder()
                            .constantType(ConstantTypePrefix.EXPERIENCE_TYPE.getValue() + randomNum)
                            .constantName(name)
                            .build();
                    }).toList()
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
        if (!repository.findByConstantTypeStartsWith(ConstantTypePrefix.NOTIFICATION_TYPE.getValue()).isEmpty()) return;
        log.info("--------------------- Creating default notification type ---------------------");
        try {
            List<Integer> randomList = new ArrayList<>();
            repository.saveAll(
                Arrays.stream(NotificationType.values()).map(type -> Constant
                        .builder()
                        .constantType(type.constantType())
                        .constantName(type.getNotificationName())
                        .build())
                    .toList());
            log.info("--------------------- Successfully created default notification type ---------------------");
        } catch (Exception ex) {
            log.error("Error when creating default notification type: ", ex);
        }
    }

    public int getRandomNumber(int min, int max, List<Integer> randomList) {
        int randomNum = -1;
        do {
            randomNum = (int) (Math.random() * (max - min + 1) + min);
        } while (randomList.contains(randomNum));
        return randomNum;
    }
}
