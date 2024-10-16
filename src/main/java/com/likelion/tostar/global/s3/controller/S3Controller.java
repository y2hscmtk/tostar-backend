package com.likelion.tostar.global.s3.controller;

import com.likelion.tostar.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = s3Service.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    // 이미지 다운로드 링크 제공
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) throws IOException {
        byte[] fileData = s3Service.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }


    // 이미지 URL 조회 - 프론트엔드 렌더링시 사용
    @GetMapping("/image-url")
    public ResponseEntity<String> getImageUrl(@RequestParam("fileName") String fileName) {
        String imageUrl = s3Service.getFileUrl(fileName);
        return ResponseEntity.ok(imageUrl);
    }
}
