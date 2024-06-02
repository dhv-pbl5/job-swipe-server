package org.dhv.pbl5server.mail_service.service;

public interface MailService {
    public void sendForgotPasswordEmail(String to, String resetPassCode);

    public void sendEmail();
}
