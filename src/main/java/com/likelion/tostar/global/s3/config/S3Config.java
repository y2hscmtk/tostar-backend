package com.likelion.tostar.global.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// S3 관련 구성 클래스
@Configuration
public class S3Config {
    @Value("${s3.credentials.access-key}")
    private String accessKey;
    @Value("${s3.credentials.secret-key}")
    private String secretKey;
    @Value("${s3.credentials.region}")
    private String region;

    // S3 Client 인스턴스 생성 및 Bean으로 등록
    @Bean
    public AmazonS3Client s3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return (AmazonS3Client) AmazonS3Client.builder()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}

