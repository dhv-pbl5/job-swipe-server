package org.dhv.pbl5server.admin_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.admin_service.service.AdminService;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.authentication_service.payload.request.CompanyRegisterRequest;
import org.dhv.pbl5server.authentication_service.payload.request.UserRegisterRequest;
import org.dhv.pbl5server.authentication_service.repository.AccountRepository;
import org.dhv.pbl5server.authentication_service.service.AuthService;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.exception.NotFoundObjectException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.constant_service.entity.Constant;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.constant_service.payload.ConstantSelectionRequest;
import org.dhv.pbl5server.constant_service.service.ConstantService;
import org.dhv.pbl5server.notification_service.entity.NotificationType;
import org.dhv.pbl5server.notification_service.service.NotificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AccountRepository repository;
    private final ConstantService constantService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    @Override
    public void activateAccount(Account adminAccount, List<String> accountIds) {
        for (var id : accountIds) {
            Account account = repository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
            if (account.getDeletedAt() == null)
                throw new BadRequestException(ErrorMessageConstant.ACCOUNT_IS_ACTIVE);
            account.setDeletedAt(null);
            account.setUpdatedAt(CommonUtils.getCurrentTimestamp());
            repository.save(account);
            // Realtime notification
            notificationService.createNotification(
                    UUID.fromString(id),
                    adminAccount,
                    account,
                    NotificationType.ADMIN_ACTIVATE_ACCOUNT);
        }

    }

    @Override
    public void deactivateAccount(Account adminAccount, List<String> accountIds) {
        for (var id : accountIds) {
            Account account = repository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new NotFoundObjectException(ErrorMessageConstant.ACCOUNT_NOT_FOUND));
            if (account.getDeletedAt() != null)
                throw new BadRequestException(ErrorMessageConstant.ACCOUNT_IS_NOT_ACTIVE);
            account.setDeletedAt(CommonUtils.getCurrentTimestamp());
            account.setUpdatedAt(CommonUtils.getCurrentTimestamp());
            repository.save(account);
            // Realtime notification
            notificationService.createNotification(
                    UUID.fromString(id),
                    adminAccount,
                    account,
                    NotificationType.ADMIN_DEACTIVATE_ACCOUNT);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialDefaultAccount() {
        // Admin account
        var adminAccount = Account.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("123456Aa"))
                .address("54 Nguyen Luong Bang, Hoa Khanh Bac, Lien Chieu, Da Nang")
                .phoneNumber("0348219257")
                .build();
        // User account
        var userRegisterRequest = UserRegisterRequest.builder()
                .email("user@gmail.com")
                .password("123456Aa")
                .address("54 Nguyen Luong Bang, Hoa Khanh Bac, Lien Chieu, Da Nang")
                .phoneNumber("0348219257")
                .dateOfBirth(CommonUtils.getCurrentTimestamp())
                .lastName("Pham Thanh")
                .firstName("Vinh")
                .gender(true)
                .build();
        // Company account
        var companyRegisterRequest = CompanyRegisterRequest.builder()
                .email("company@gmail.com")
                .password("123456Aa")
                .address("54 Nguyen Luong Bang, Hoa Khanh Bac, Lien Chieu, Da Nang")
                .phoneNumber("0348219257")
                .companyName("DHV job swipe")
                .companyUrl("https://github.com/dhv-pbl5")
                .establishedDate(CommonUtils.getCurrentTimestamp())
                .build();
        for (var item : (List<Constant>) constantService.getSystemRoles(null)) {
            var role = AbstractEnum.fromString(SystemRoleName.values(), item.getConstantName());
            if (role == SystemRoleName.ADMIN)
                adminAccount.setSystemRole(item);
            if (role == SystemRoleName.COMPANY)
                companyRegisterRequest.setSystemRole(new ConstantSelectionRequest(item.getConstantId().toString()));
            if (role == SystemRoleName.USER)
                userRegisterRequest.setSystemRole(new ConstantSelectionRequest(item.getConstantId().toString()));
        }
        // Create admin account
        try {
            if (repository.existsByEmail(adminAccount.getEmail()))
                throw new Exception("Admin account already exists");
            repository.save(adminAccount);
        } catch (Exception e) {
            LogUtils.error("Initial account", e.toString());
        }
        // Create user account
        try {
            authService.register(userRegisterRequest);

        } catch (Exception e) {
            LogUtils.error("Initial account", e.toString());
        }
        // Create company account
        try {
            authService.register(companyRegisterRequest);
        } catch (Exception e) {
            LogUtils.error("Initial account", e.toString());
        }
    }
}
