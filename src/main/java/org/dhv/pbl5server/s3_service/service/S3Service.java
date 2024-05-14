package org.dhv.pbl5server.s3_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// git commit -m "PBL-603 upload file to s3"

public interface S3Service {
    String uploadFile(MultipartFile file);

    String uploadFile(MultipartFile replacedFile, String oldUrl);

    List<String> uploadFiles(List<MultipartFile> files);

    List<String> uploadFiles(List<MultipartFile> replacedFiles, List<String> oldUrls);

    String getFileName(String fileUrl);

    String getFileUrl(String fileName);
}
