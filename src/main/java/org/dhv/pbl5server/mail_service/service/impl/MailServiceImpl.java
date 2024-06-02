package org.dhv.pbl5server.mail_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.service.BaseMailService;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.mail_service.service.MailService;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final BaseMailService baseMailService;

    @Override
    public void sendForgotPasswordEmail(String to, String resetPassCode) {
        baseMailService.sendMailWithTemplate(
            to,
            "Reset Your Password \uD83D\uDD11",
            "reset-password-email.html",
            Map.of(
                "email", to,
                "reset_pass_code", resetPassCode
            ));
    }

    @Override
    public void sendInterviewInvitationEmail(User candidate, Company company, Timestamp interviewTime, String interviewPosition) {
        baseMailService.sendMailWithTemplate(
            candidate.getAccount().getEmail(),
            String.format("INTERVIEW INVITATION [%s]", company.getCompanyName()),
            "interview-invitation-email.html",
            Map.of(
                "candidate_name", candidate.getFullName(),
                "job_position", interviewPosition,
                "company_name", company.getCompanyName(),
                "interview_time", CommonUtils.convertTimestampToString(interviewTime, "dd/MM/yyyy HH:mm"),
                "company_address", company.getAccount().getAddress(),
                "company_phone", company.getAccount().getPhoneNumber(),
                "company_email", company.getAccount().getEmail()
            ));
    }
}
