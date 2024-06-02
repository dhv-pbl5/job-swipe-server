package org.dhv.pbl5server.mail_service;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.mail_service.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/send")
    public String sendMail() {
        mailService.sendEmail();
        return "Mail sent";
    }
}
