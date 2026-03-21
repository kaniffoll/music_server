package com.kanifol.musicserver.repository.minio.exc;

public class GetObjectException extends MinioException {
    public GetObjectException() {}
    @Override
    public String getCode() {
        return "exception.minio.get-object-message";
    }
}
