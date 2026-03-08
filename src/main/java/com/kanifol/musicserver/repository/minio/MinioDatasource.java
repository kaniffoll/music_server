package com.kanifol.musicserver.repository.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
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
    private final MinioProperties minioProperties;

    public MinioDatasource(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public InputStream audioStream(String key, long offset, long length) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (key == null || key.isBlank())
            throw new NoSuchElementException("No element found for key: " + key);

        return minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(minioProperties.getMusicBucket())
                .offset(offset)
                .length(length)
                .object(key)
                .build());
    }

    public long getObjectSize(String key) throws Exception {
        if (key == null || key.isBlank())
            throw new NoSuchElementException("No element found for key: " + key);

        return minioClient.statObject(
                StatObjectArgs
                        .builder()
                        .bucket(minioProperties.getMusicBucket())
                        .object(key)
                        .build()
        ).size();
    }

    public InputStream coverStream(String key) throws Exception {
        if (key == null || key.isBlank())
            throw new NoSuchElementException("No element found for key: " + key);

        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(minioProperties.getCoversBucket())
                        .object(key)
                        .build()
        );
    }

    public void uploadTrack(InputStream inputStream, Long size, String key) throws Exception {
        if (key == null || key.isBlank())
            throw new NoSuchElementException("No element found for key: " + key);

        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(minioProperties.getMusicBucket())
                        .object(key)
                        .stream(inputStream, size, -1)
                        .contentType("audio/mp3")
                        .build()
        );
    }

    public void deleteTrack(String key) throws Exception {
        if (key == null || key.isBlank())
            throw new NoSuchElementException("No element found for key: " + key);

        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(minioProperties.getMusicBucket())
                        .object(key)
                        .build()
        );
    }

    public void deleteAlbum(String prefix) throws Exception {
        if (prefix == null || prefix.isBlank())
            throw new NoSuchElementException("No element found with prefix: " + prefix);

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(minioProperties.getMusicBucket())
                        .prefix(prefix)
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();

            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(minioProperties.getMusicBucket())
                            .object(item.objectName())
                            .build()
            );
        }
    }
}
