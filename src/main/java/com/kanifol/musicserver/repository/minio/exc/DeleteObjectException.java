package com.kanifol.musicserver.repository.minio.exc;

public class DeleteObjectException extends MinioException {
    @Override
    public String getCode() {
        return "exception.minio.delete-object-message";
    }
}
