package org.dhv.pbl5server.s3_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;

@Configuration
@Getter
public class S3ApplicationProperty {
    @Value("${application.s3.bucket-name}")
    private String awsBucketName;

    public GetObjectRequest getObjectRequest(String keyName) {
        return GetObjectRequest.builder()
            .bucket(awsBucketName)
            .key(keyName)
            .build();
    }

    public PutObjectRequest putObjectRequest(String keyName) {
        return PutObjectRequest.builder()
            .bucket(awsBucketName)
            .key(keyName)
            .build();
    }

    public DeleteObjectRequest deleteObjectRequest(String keyName) {
        return DeleteObjectRequest.builder()
            .bucket(awsBucketName)
            .key(keyName)
            .build();
    }

    public DeleteObjectsRequest deleteObjectsRequest(List<String> keyNames) {
        var keys = keyNames.stream().map(key -> ObjectIdentifier.builder().key(key).build()).toList();
        var del = Delete.builder().objects(keys).build();
        return DeleteObjectsRequest.builder()
            .bucket(awsBucketName)
            .delete(del)
            .build();
    }

    public GetUrlRequest getUrlRequest(String keyName) {
        return GetUrlRequest.builder()
            .bucket(awsBucketName)
            .key(keyName)
            .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.AP_SOUTHEAST_1)
            .build();
    }
}
