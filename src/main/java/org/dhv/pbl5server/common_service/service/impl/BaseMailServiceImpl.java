package org.dhv.pbl5server.common_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.config.BaseMailConfig;
import org.dhv.pbl5server.common_service.service.BaseMailService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaseMailServiceImpl implements BaseMailService {
    private final BaseMailConfig baseMailConfig;


    @Override
    public void sendMailWithContent(String to, String subject, String content) {
        var message = baseMailConfig.createMimeMessage(
            to,
            null,
            subject,
            content,
            null,
            null,
            null);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithContent(String to, String[] cc, String subject, String content) {
        var message = baseMailConfig.createMimeMessage(
            to,
            cc,
            subject,
            content,
            null,
            null,
            null);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithFile(String to, String subject, String content, MultipartFile[] files) {
        var message = baseMailConfig.createMimeMessage(
            to,
            null,
            subject,
            content,
            files,
            null,
            null);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithFile(String to, String[] cc, String subject, String content, MultipartFile[] files) {
        var message = baseMailConfig.createMimeMessage(
            to,
            cc,
            subject,
            content,
            files,
            null,
            null);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithTemplate(String to, String subject, String template, Map<String, Object> variables) {
        var message = baseMailConfig.createMimeMessage(
            to,
            null,
            subject,
            null,
            null,
            template,
            variables);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithTemplate(String to, String[] cc, String subject, String template, Map<String, Object> variables) {
        var message = baseMailConfig.createMimeMessage(
            to,
            cc,
            subject,
            null,
            null,
            template,
            variables);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithTemplate(String to, String subject, MultipartFile[] files, String template, Map<String, Object> variables) {
        var message = baseMailConfig.createMimeMessage(
            to,
            null,
            subject,
            null,
            files,
            template,
            variables);
        baseMailConfig.sendEmail(message);
    }

    @Override
    public void sendMailWithTemplate(String to, String[] cc, String subject, MultipartFile[] files, String template, Map<String, Object> variables) {
        var message = baseMailConfig.createMimeMessage(
            to,
            cc,
            subject,
            null,
            files,
            template,
            variables);
        baseMailConfig.sendEmail(message);
    }
}
