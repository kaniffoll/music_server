package com.kanifol.musicserver.repository.minio.exc;

public class EmptyMinioKeyException extends MinioException {
    public EmptyMinioKeyException() {}
    @Override
    public String getCode() {
        return "exception.minio.empty-key-message";
    }
}
