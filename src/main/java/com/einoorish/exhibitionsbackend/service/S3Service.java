package com.einoorish.exhibitionsbackend.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3Client;

    final int SHORT_ID_LENGTH = 8;

    public void createRulesForAccess() {
       //
    }

    private Bucket getBucket(String bucketName) {
        return amazonS3Client.listBuckets().stream()
                .filter(bucket -> bucket.getName().equals(bucketName))
                .findFirst()
                .orElse(null);
    }

    private void createBucketIfNotExists(String bucketName) {
        if (getBucket(bucketName) == null) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            amazonS3Client.createBucket(createBucketRequest);
        }
    }

    public String saveFile(MultipartFile multipartFile) throws SdkClientException, IOException {
        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());
        String shortId = UUID.randomUUID().toString().substring(0,8);
        String savedFilename = shortId+multipartFile.getOriginalFilename();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String bucketName = auth.getName()
                .replace("@", "-")
                .replace("_", "-")
                .replace(".", "-");
        createBucketIfNotExists(bucketName);

        PutObjectResult objectResult = amazonS3Client.putObject(bucketName, savedFilename, multipartFile.getInputStream(), data);
        System.out.println(objectResult.getContentMd5()); //verify checksum
        return savedFilename;
    }


    public List<S3ObjectSummary> listObjects(String bucketName){
        ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries();
    }

    public byte[] getObject(String bucketName, String objectName) throws IOException {
        S3Object s3object = amazonS3Client.getObject(bucketName, objectName);
        return IOUtils.toByteArray(s3object.getObjectContent());
    }

    public String getUrlForFileName(String filename) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String bucketName = auth.getName()
                .replace("@", "-")
                .replace("_", "-")
                .replace(".", "-");
        long expirationTimeMillis = 3600000; // 1 hour

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, filename);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }


    public String getUrlForFileName(String filename, String ownerEmail) {
        String bucketName = ownerEmail
                .replace("@", "-")
                .replace("_", "-")
                .replace(".", "-");
        long expirationTimeMillis = 3600000; // 1 hour

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, filename);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis));
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public void deleteFile(final String fileName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String bucketName = auth.getName()
                .replace("@", "-")
                .replace("_", "-")
                .replace(".", "-");
        amazonS3Client.deleteObject(bucketName, fileName);
    }
}
