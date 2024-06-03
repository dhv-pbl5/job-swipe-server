package org.dhv.pbl5server.profile_service.service.impl;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.dhv.pbl5server.common_service.config.HttpClientConfig;
import org.dhv.pbl5server.common_service.enums.HttpMethod;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.constant_service.enums.SystemRoleName;
import org.dhv.pbl5server.profile_service.service.NormalizeDataService;
import org.dhv.pbl5server.realtime_service.call_back.VoidCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class NormalizeDataServiceImpl implements NormalizeDataService {
    @Value("${application.recommendation-server-url}")
    private String recommendationServerUrl;
    private final OkHttpClient client;
    private final HttpClientConfig httpClientConfig;
    private final int maxThread = 4;
    private final Queue<SendRequestProcess> processQueue = new LinkedList<>();


    @Override
    public void normalizeUserData(String userId) {
        var process = new SendRequestProcess(client, httpClientConfig, String.format("%s/normalize/user/%s", recommendationServerUrl, userId), () -> {
            if (!processQueue.isEmpty())
                processQueue.poll().start();
            LogUtils.info("Normalize user data", String.format("Success %s - queueSize: %d", userId, processQueue.size()));
        });
        // 4 threads on 4 cores cpu
        if (processQueue.size() <= maxThread) process.start();
        else processQueue.add(process);
    }

    @Override
    public void normalizeCompanyData(String companyId) {
        var process = new SendRequestProcess(client, httpClientConfig, String.format("%s/normalize/company/%s", recommendationServerUrl, companyId), () -> {
            if (!processQueue.isEmpty())
                processQueue.poll().start();
            LogUtils.info("Normalize company data", String.format("Success %s - queueSize: %d", companyId, processQueue.size()));
        });
        // 4 threads on 4 cores cpu
        if (processQueue.size() <= maxThread) process.start();
        else processQueue.add(process);
    }

    @Override
    public void normalizeData(String accountId, SystemRoleName role) {
        switch (role) {
            case USER:
                normalizeUserData(accountId);
                break;
            case COMPANY:
                normalizeCompanyData(accountId);
                break;
            default:
                break;
        }
    }
}

class SendRequestProcess extends Thread {
    private final OkHttpClient client;
    private final HttpClientConfig httpClientConfig;
    private final String url;
    private final VoidCallback onDone;

    public SendRequestProcess(OkHttpClient client, HttpClientConfig config, String url, VoidCallback onDone) {
        this.client = client;
        this.httpClientConfig = config;
        this.url = url;
        this.onDone = onDone;
    }

    @Override
    public void run() {
        var request = httpClientConfig.getRequest(url, HttpMethod.POST, null, null);
        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception(response.body() != null ? response.body().string() : "Recommendation service error");
            }
        } catch (Exception e) {
            LogUtils.error("Normalize data service", e.getMessage());
        } finally {
            try {
                onDone.call();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
}
