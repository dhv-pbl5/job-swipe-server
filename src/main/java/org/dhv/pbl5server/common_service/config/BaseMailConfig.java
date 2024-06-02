package org.dhv.pbl5server.common_service.config;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.common_service.service.ThymeleafService;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class BaseMailConfig {
    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;
    private final ThymeleafService thymeleafService;

    public MimeMessage createMimeMessage(
        String to,
        String[] cc,
        String subject,
        String content,
        MultipartFile[] files,
        String template,
        Map<String, Object> variables
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = CommonUtils.isNotEmptyOrNullList(files) ?
                new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()) :
                new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            if (CommonUtils.isNotEmptyOrNullList(cc)) {
                helper.setCc(cc);
            }
            if (CommonUtils.isNotEmptyOrNullString(content)) {
                helper.setText(content, false);
            }
            if (CommonUtils.isNotEmptyOrNullList(files)) {
                for (MultipartFile file : files) {
                    helper.addAttachment(
                        Objects.requireNonNull(file.getOriginalFilename()),
                        new ByteArrayDataSource(file.getBytes(), file.getContentType())
                    );
                }
            }
            if (CommonUtils.isNotEmptyOrNullString(template)) {
                helper.setText(thymeleafService.createContent(template, variables), true);
            }
            return helper.getMimeMessage();
        } catch (Exception e) {
            LogUtils.error("MAIL SERVICE", e);
            return null;
        }
    }

    public void sendEmail(MimeMessage message) {
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            LogUtils.error("MAIL SERVICE", e);
        }
    }
}
