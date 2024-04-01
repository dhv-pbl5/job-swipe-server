package org.dhv.pbl5server.s3_service.controller;

import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRole;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.s3_service.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/file-storage")
@RequiredArgsConstructor
public class FileStorageController {
    private final S3Service service;

    @PreAuthorizeSystemRole
    @PostMapping("")
    public ResponseEntity<ApiDataResponse> uploadFile(MultipartFile file) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.uploadFile(file)));
    }

    @PreAuthorizeSystemRole
    @PostMapping("/multiple")
    public ResponseEntity<ApiDataResponse> uploadFiles(List<MultipartFile> files) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.uploadFiles(files)));
    }

    @PreAuthorizeSystemRole
    @GetMapping("")
    public ResponseEntity<ApiDataResponse> getUrlByFileName(@RequestParam("file_name") String fileName) {
        return ResponseEntity.ok(ApiDataResponse.successWithoutMeta(service.getFileUrl(fileName)));
    }
}
