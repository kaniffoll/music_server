package com.kanifol.musicserver.repository.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class MinioDatasource {
    private final MinioClient minioClient;
    private final String bucket;

    public MinioDatasource(@Value("${minio.bucket}") String bucket, MinioClient minioClient) {
        this.bucket = bucket;
        this.minioClient = minioClient;
    }

//    public InputStream audioStream(String key) {
//        System.out.println("Using endpoint: " + s3Client.serviceClientConfiguration().endpointOverride().orElse(null));
//        return s3Client.getObject(request ->
//                request.bucket(bucket).key(key).build());
//    }

    public InputStream audioStream(String key) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(bucket)
                .object(key)
                .build());
    }

}
