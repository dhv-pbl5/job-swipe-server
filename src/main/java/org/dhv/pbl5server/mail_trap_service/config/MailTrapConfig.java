package org.dhv.pbl5server.mail_trap_service.config;

import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.mail_trap_service.model.ForgotPasswordTemplateVariable;
import org.dhv.pbl5server.mail_trap_service.model.MailTrapTemplateRequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@Getter
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

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient().newBuilder().build();
    }

    /*
        Public methods
     */
    public Request getForgotPasswordRequest(String toEmail, String resetPassCode) {
        return getRequest("POST", getRequestBody(
            toEmail,
            forgotPasswordTemplateUuid,
            ForgotPasswordTemplateVariable.builder()
                .userEmail(toEmail)
                .resetPassCode(resetPassCode)
                .build()));
    }

    /*
        Private methods
     */
    private Request getRequest(String method, RequestBody body) {
        return new Request.Builder()
            .url(apiUrl)
            .method(method, body)
            .addHeader("Authorization", "Bearer %s".formatted(apiKey))
            .addHeader("Content-Type", "application/json")
            .build();
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
