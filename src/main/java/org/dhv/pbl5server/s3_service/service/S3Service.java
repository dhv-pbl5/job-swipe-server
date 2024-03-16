package org.dhv.pbl5server.s3_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    String uploadFile(MultipartFile file);

    String uploadFile(MultipartFile replacedFile, String oldFileName);

    List<String> uploadFiles(List<MultipartFile> files);

    List<String> uploadFiles(List<MultipartFile> replacedFiles, List<String> oldFileNames);

    String getFileUrl(String fileName);
}
