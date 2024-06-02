package org.dhv.pbl5server.common_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BaseMailService {
    void sendMailWithContent(String to, String subject, String content);

    void sendMailWithContent(String to, String[] cc, String subject, String content);

    void sendMailWithFile(String to, String subject, String content, MultipartFile[] files);

    void sendMailWithFile(String to, String[] cc, String subject, String content, MultipartFile[] files);

    void sendMailWithTemplate(String to, String subject, String template, Map<String, Object> variables);

    void sendMailWithTemplate(String to, String[] cc, String subject, String template, Map<String, Object> variables);

    void sendMailWithTemplate(String to, String subject, MultipartFile[] files, String template, Map<String, Object> variables);

    void sendMailWithTemplate(String to, String[] cc, String subject, MultipartFile[] files, String template, Map<String, Object> variables);
}
