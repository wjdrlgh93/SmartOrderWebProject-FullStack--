package org.spring.backendspring.config.s3;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // return S3 SaveFile URL 
    // public String uploadFile(MultipartFile multipartFile, String path) throws IOException {

    //     String originalFileName = multipartFile.getOriginalFilename();
    //     String uuid = UUID.randomUUID().toString();
    //     String uniqueFileName = path + uuid + "_" + originalFileName;

    //     // upload S3 
    //     InputStream inputStream = multipartFile.getInputStream();

    //     // Use S3Template Upload -=> Using bucketName, FileName, Data 
    //     s3Template.upload(bucketName, uniqueFileName, inputStream,
    //          ObjectMetadata.builder()
    //                     .contentType(multipartFile.getContentType())
    //                         .build());
    //     return uniqueFileName;
    // }
    
    // public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {


    //     String originalFileName = multipartFile.getOriginalFilename();

    //     String uuid = UUID.randomUUID().toString();
    //     // mkr dir
    //     String filePrefix = dirName.isEmpty() ? "" : dirName + "/";
    //     String uniqueFileName = filePrefix + uuid + "_" + originalFileName;

    //     // upload S3 
    //     InputStream inputStream = multipartFile.getInputStream();

    //     s3Template.upload(bucketName, uniqueFileName, inputStream,
    //          ObjectMetadata.builder()
    //                     .contentType(multipartFile.getContentType())
    //                         .build());
    //     return uniqueFileName;
    
    // }
    
    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {

    String originalFileName = multipartFile.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();


    String filePrefix = dirName.isEmpty() ? "" : dirName;
    String uniqueFileName = filePrefix + uuid + "_" + originalFileName;

    // upload S3 
    InputStream inputStream = multipartFile.getInputStream();


    s3Template.upload(bucketName, uniqueFileName, inputStream,
        ObjectMetadata.builder()
            .contentType(multipartFile.getContentType())
            .build());
            
    return uniqueFileName;
}

    public void deleteFile(String fileName){
        s3Template.deleteObject(bucketName, fileName);
    }

    // bring File WholeName if you need.... use this...
    public String getFileUrl(String fileName) throws IOException {
        return s3Template.download(bucketName, fileName).getURL().toString();
    }
}
