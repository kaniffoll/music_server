package com.kanifol.musicserver.repository.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

@Component
public class MinioDatasource {
    private final MinioClient minioClient;
    private final String musicBucket;
    private final String coversBucket;

    public MinioDatasource(@Value("${minio.music-bucket}") String musicBucket, @Value("${minio.covers-bucket}") String coversBucket, MinioClient minioClient) {
        this.musicBucket = musicBucket;
        this.minioClient = minioClient;
        this.coversBucket = coversBucket;
    }

    public InputStream audioStream(String key, long offset, long length) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(musicBucket)
                .offset(offset)
                .length(length)
                .object(key)
                .build());
    }

    public long getObjectSize(String key) throws Exception {
        return minioClient.statObject(
                StatObjectArgs
                        .builder()
                        .bucket(musicBucket)
                        .object(key)
                        .build()
        ).size();
    }

    public InputStream coverStream(String key) throws Exception {
        if (key == null || key.isBlank())
            throw new NoSuchElementException("No element found for key: " + key);

        return minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(coversBucket)
                .object(key)
                .build()
        );
    }
}
