package com.kanifol.musicserver.repository.minio.exc;

public class EmptyMinioPrefixException extends MinioException {
    public EmptyMinioPrefixException() {}
    @Override
    public String getCode() {
        return "exception.minio.empty-prefix-message";
    }
}
