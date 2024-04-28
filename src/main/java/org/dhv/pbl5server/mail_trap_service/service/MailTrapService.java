package org.dhv.pbl5server.mail_trap_service.service;

public interface MailTrapService {
    public void sendForgotPasswordEmail(String to, String resetPassCode);
}
