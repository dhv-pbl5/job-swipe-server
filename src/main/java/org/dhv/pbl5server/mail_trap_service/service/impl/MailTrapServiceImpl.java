package org.dhv.pbl5server.mail_trap_service.service.impl;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.mail_trap_service.config.MailTrapConfig;
import org.dhv.pbl5server.mail_trap_service.service.MailTrapService;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class MailTrapServiceImpl implements MailTrapService {
    private final MailTrapConfig mailTrapConfig;
    private final OkHttpClient client;

    @Override
    public void sendForgotPasswordEmail(String to, String resetPassCode) {
        try {
            var request = mailTrapConfig.getForgotPasswordRequest(to, resetPassCode);
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                LogUtils.info("MAIL SERVICE", response.body().string());
            }
        } catch (IOException e) {
            LogUtils.error("MAIL SERVICE", e);
            throw new RuntimeException(e);
        }
    }
}
