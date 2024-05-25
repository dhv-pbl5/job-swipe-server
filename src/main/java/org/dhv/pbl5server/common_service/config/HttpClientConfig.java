package org.dhv.pbl5server.common_service.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.dhv.pbl5server.common_service.enums.HttpMethod;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    }

    public Request getRequest(String url, HttpMethod method, RequestBody body, String token) {
        Request.Builder request = null;
        switch (method) {
            case GET:
                request = new Request.Builder()
                    .url(url)
                    .get();
            case POST:
            case PUT:
            case DELETE:
            case PATCH:
                request = new Request.Builder()
                    .url(url)
                    .method(method.getValue(), body)
                    .addHeader("Content-Type", "application/json");
        }
        if (request != null) {
            if (CommonUtils.isNotEmptyOrNullString(token))
                request.addHeader("Authorization", "Bearer " + token);
            return request.build();
        }
        return null;
    }

    public Request getFormDataRequest(String url, HttpMethod method, RequestBody body, String token) {
        Request.Builder request = null;
        switch (method) {
            case GET:
                request = new Request.Builder()
                    .url(url)
                    .get();
            case POST:
            case PUT:
            case DELETE:
            case PATCH:
                request = new Request.Builder()
                    .url(url)
                    .method(method.getValue(), body)
                    .addHeader("Content-Type", "multipart/form-data");
        }
        if (request != null) {
            if (CommonUtils.isNotEmptyOrNullString(token))
                request.addHeader("Authorization", "Bearer " + token);
            return request.build();
        }
        return null;
    }
}
