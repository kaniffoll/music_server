package com.kanifol.musicserver.repository.minio.exc;

public class PutObjectException extends MinioException {
    public PutObjectException() {}
    @Override
    public String getCode() {
        return "exception.minio.put-object-message";
    }
}
