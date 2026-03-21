package com.kanifol.musicserver.repository.minio;

import com.kanifol.musicserver.repository.minio.exc.*;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MinioDatasource {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    public MinioDatasource(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public InputStream audioStream(String key, long offset, long length) {
        if (key == null || key.isBlank())
            throw new EmptyMinioKeyException();

        try {
            return minioClient.getObject(GetObjectArgs
                    .builder()
                    .bucket(minioProperties.getMusicBucket())
                    .offset(offset)
                    .length(length)
                    .object(key)
                    .build());
        } catch (Exception e) {
            throw new GetObjectException();
        }
    }

    public long getObjectSize(String key) {
        if (key == null || key.isBlank())
            throw new EmptyMinioKeyException();

        try {
            return minioClient.statObject(
                    StatObjectArgs
                            .builder()
                            .bucket(minioProperties.getMusicBucket())
                            .object(key)
                            .build()
            ).size();
        } catch (Exception e) {
            throw new GetObjectException();
        }
    }

    public InputStream coverStream(String key) {
        if (key == null || key.isBlank())
            throw new EmptyMinioKeyException();

        try {
            return minioClient.getObject(
                    GetObjectArgs
                            .builder()
                            .bucket(minioProperties.getCoversBucket())
                            .object(key)
                            .build()
            );
        } catch (Exception e) {
            throw new GetObjectException();
        }
    }

    public void uploadTrack(InputStream inputStream, Long size, String key) {
        if (key == null || key.isBlank())
            throw new EmptyMinioKeyException();

        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(minioProperties.getMusicBucket())
                            .object(key)
                            .stream(inputStream, size, -1)
                            .contentType("audio/mp3")
                            .build()
            );
        } catch (Exception e) {
            throw new PutObjectException();
        }
    }

    public void deleteTrack(String key) {
        if (key == null || key.isBlank())
            throw new EmptyMinioKeyException();

        try {
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(minioProperties.getMusicBucket())
                            .object(key)
                            .build()
            );
        } catch (Exception e) {
            throw new DeleteObjectException();
        }
    }

    public void deleteAlbum(String prefix) {
        if (prefix == null || prefix.isBlank())
            throw new EmptyMinioPrefixException();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(minioProperties.getMusicBucket())
                        .prefix(prefix)
                        .build()
        );

        try {
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
        } catch (Exception e) {
            throw new DeleteObjectException();
        }
    }
}
