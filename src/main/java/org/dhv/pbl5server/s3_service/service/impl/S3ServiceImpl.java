package org.dhv.pbl5server.s3_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.common_service.constant.ErrorMessageConstant;
import org.dhv.pbl5server.common_service.exception.BadRequestException;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.dhv.pbl5server.common_service.utils.LogUtils;
import org.dhv.pbl5server.s3_service.config.S3ApplicationProperty;
import org.dhv.pbl5server.s3_service.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URL;
import java.util.List;

// git commit -m "PBL-603 upload file to s3"

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {
    private final S3ApplicationProperty s3Config;
    private final S3Client s3;
    private final String BUCKET_URL = "https://pbl5-bucket.s3.ap-southeast-1.amazonaws.com/";
    private final String S3_DEBUG_PREFIX = "S3";

    public String uploadFile(MultipartFile file) {
        try {
            var fileName = generateFileName(file);
            s3.putObject(s3Config.putObjectRequest(fileName), RequestBody.fromBytes(file.getBytes()));
            return "%s%s".formatted(BUCKET_URL, fileName);
        } catch (Exception e) {
            LogUtils.error(S3_DEBUG_PREFIX, "Error occurred while uploading file:", e);
            throw new BadRequestException(ErrorMessageConstant.UPLOAD_FILE_FAILED);
        }
    }

    public String uploadFile(MultipartFile replacedFile, String oldUrl) {
        try {
            var oldFileName = getFileName(oldUrl);
            if (CommonUtils.isEmptyOrNullString(oldFileName) || !deleteFile(oldFileName))
                throw new BadRequestException(ErrorMessageConstant.DELETE_FILE_FAILED);
            var fileName = generateFileName(replacedFile);
            s3.putObject(s3Config.putObjectRequest(fileName), RequestBody.fromBytes(replacedFile.getBytes()));
            return "%s%s".formatted(BUCKET_URL, fileName);
        } catch (Exception e) {
            LogUtils.error(S3_DEBUG_PREFIX, "Error occurred while uploading file:", e);
            throw new BadRequestException(ErrorMessageConstant.UPLOAD_FILE_FAILED);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                return uploadFile(file);
            } catch (Exception e) {
                LogUtils.error(S3_DEBUG_PREFIX, "Error occurred while uploading file:", e);
                throw new BadRequestException(ErrorMessageConstant.UPLOAD_FILE_FAILED);
            }
        }).toList();
    }

    public List<String> uploadFiles(List<MultipartFile> replacedFiles, List<String> oldUrls) {
        var oldFileNames = oldUrls.stream().map(this::getFileName).toList();
        s3.deleteObjects(s3Config.deleteObjectsRequest(oldFileNames));
        return replacedFiles.stream().map(file -> {
            try {
                return uploadFile(file);
            } catch (Exception e) {
                LogUtils.error(S3_DEBUG_PREFIX, "Error occurred while uploading file:", e);
                throw new BadRequestException(ErrorMessageConstant.UPLOAD_FILE_FAILED);
            }
        }).toList();
    }

    @Override
    public String getFileName(String fileUrl) {
        if (CommonUtils.isEmptyOrNullString(fileUrl))
            return null;
        var arr = fileUrl.split("/");
        return arr[arr.length - 1];
    }

    public String getFileUrl(String fileName) {
        try {
            URL url = s3.utilities().getUrl(s3Config.getUrlRequest(fileName));
            return url.toString();
        } catch (Exception e) {
            LogUtils.error(S3_DEBUG_PREFIX, "Getting file url:", e);
            throw new BadRequestException(ErrorMessageConstant.FILE_NOT_FOUND);
        }
    }

    private boolean deleteFile(String fileName) {
        s3.deleteObject(s3Config.deleteObjectRequest(fileName));
        LogUtils.info(S3_DEBUG_PREFIX, "Deleted file:", fileName);
        return true;
    }

    private String generateFileName(MultipartFile file) {
        var fileName = "%s-%s"
                .formatted(CommonUtils.getCurrentTimestamp().toString(), file.getOriginalFilename())
                .replaceAll(" ", "_");
        LogUtils.info(S3_DEBUG_PREFIX, "Generated file name:", fileName);
        return fileName;
    }
}
