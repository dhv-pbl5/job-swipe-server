package org.dhv.pbl5server.mail_trap_service.model;

import lombok.Getter;
import lombok.Setter;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.util.List;
import java.util.Map;

// git commit -m "PBL-518 forgot password for company"

@Getter
@Setter
@JsonSnakeCaseNaming
public class MailTrapTemplateRequestBody {
    private Map<String, String> from;
    private List<Map<String, String>> to;
    private String templateUuid;
    private Object templateVariables;

    private MailTrapTemplateRequestBody() {
    }

    public static MailTrapTemplateRequestBody create(String fromEmail, String fromEmailName, String toEmail,
            String templateUuid, Object templateVariables) {
        MailTrapTemplateRequestBody requestBody = new MailTrapTemplateRequestBody();
        requestBody.setFrom(Map.of("email", fromEmail, "name", fromEmailName));
        requestBody.setTo(List.of(Map.of("email", toEmail)));
        requestBody.setTemplateUuid(templateUuid);
        requestBody.setTemplateVariables(templateVariables);
        return requestBody;
    }

    public static MailTrapTemplateRequestBody create(String fromEmail, String fromEmailName, List<String> toEmails,
            String templateUuid, Object templateVariables) {
        MailTrapTemplateRequestBody requestBody = new MailTrapTemplateRequestBody();
        requestBody.setFrom(Map.of("email", fromEmail, "name", fromEmailName));
        requestBody.setTo(toEmails.stream().map(email -> Map.of("email", email)).toList());
        requestBody.setTemplateUuid(templateUuid);
        requestBody.setTemplateVariables(templateVariables);
        return requestBody;
    }
}
