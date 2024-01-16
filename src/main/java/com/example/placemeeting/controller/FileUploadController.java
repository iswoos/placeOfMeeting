package com.example.placemeeting.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestPart("file") MultipartFile file) throws IOException {
        // 파일 처리 로직을 여기에 추가
        // file 변수에 업로드된 파일이 전달됩니다.

        // S3에 저장할 고유한 키 생성
        String key = generateUniqueKey(file.getOriginalFilename());

        // S3에 파일 업로드
        uploadFileToS3(key, file);

        // S3에 업로드된 파일의 URL 반환
        return "File uploaded successfully. URL: " + generateS3FileUrl(key);
    }

    private String generateUniqueKey(String originalFilename) {
        // 파일명 앞에 UUID를 붙여 고유한 키 생성
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    private void uploadFileToS3(String key, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata));
    }

    private String generateS3FileUrl(String key) {
        // S3에 업로드된 파일의 URL 생성
        return amazonS3.getUrl(bucketName, key).toString();
    }
}