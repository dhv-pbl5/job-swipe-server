package org.dhv.pbl5server.mail_trap_service.service;

// git commit -m "PBL-518 forgot password for company"

public interface MailTrapService {
    public void sendForgotPasswordEmail(String to, String resetPassCode);
}
