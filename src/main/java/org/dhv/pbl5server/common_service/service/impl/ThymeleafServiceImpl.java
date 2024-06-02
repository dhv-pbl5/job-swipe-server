package org.dhv.pbl5server.mail_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.mail_service.service.ThymeleafService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThymeleafServiceImpl implements ThymeleafService {
    private final TemplateEngine templateEngine;

    @Override
    public String createContent(String template, Map<String, Object> variables) {
        final Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }
}
