package org.dhv.pbl5server.mail_service.service;

import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;

import java.sql.Timestamp;

public interface MailService {
    public void sendForgotPasswordEmail(String to, String resetPassCode);

    public void sendInterviewInvitationEmail(User candidate, Company company, Timestamp interviewTime, String interviewPosition);
}
