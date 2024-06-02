package org.dhv.pbl5server.mail_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.service.BaseMailService;
import org.dhv.pbl5server.mail_service.service.MailService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final BaseMailService baseMailService;

    @Override
    public void sendForgotPasswordEmail(String to, String resetPassCode) {

    }

    @Override
    public void sendEmail() {
        baseMailService.sendMailWithTemplate(
            "hg22092004@gmail.com",
            "Chao em",
            "reset-password-email.html",
            null
        );
    }
}
