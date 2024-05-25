package org.dhv.pbl5server.mail_trap_service.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.dhv.pbl5server.common_service.config.HttpClientConfig;
import org.dhv.pbl5server.common_service.enums.HttpMethod;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.mail_trap_service.model.ForgotPasswordTemplateVariable;
import org.dhv.pbl5server.mail_trap_service.model.MailTrapTemplateRequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@Getter
@RequiredArgsConstructor
public class MailTrapConfig {
    @Value("${application.mail-trap.url}")
    private String apiUrl;
    @Value("${application.mail-trap.api-key}")
    private String apiKey;
    @Value("${application.mail-trap.from-email}")
    private String fromEmail;
    @Value("${application.mail-trap.from-email-name}")
    private String fromEmailName;
    @Value("${application.mail-trap.forgot-password-email-template-id}")
    private String forgotPasswordTemplateUuid;
    private final HttpClientConfig httpClientConfig;

    public Request getForgotPasswordRequest(String toEmail, String resetPassCode) {
        return httpClientConfig.getRequest(
            apiUrl,
            HttpMethod.POST,
            getRequestBody(
                toEmail,
                forgotPasswordTemplateUuid,
                ForgotPasswordTemplateVariable.builder()
                    .userEmail(toEmail)
                    .resetPassCode(resetPassCode)
                    .build()),
            apiKey
        );
    }

    private RequestBody getRequestBody(String toEmail, String templateUuid, Object templateVariables) {
        MediaType mediaType = MediaType.parse("application/json");
        var mailTrapRequestBody = MailTrapTemplateRequestBody.create(
            fromEmail,
            fromEmailName,
            toEmail,
            templateUuid,
            templateVariables
        );
        return RequestBody.create(Objects.requireNonNull(CommonUtils.convertToJson(mailTrapRequestBody)), mediaType);
    }
}
