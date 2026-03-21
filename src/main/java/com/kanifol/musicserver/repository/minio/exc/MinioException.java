package com.kanifol.musicserver.repository.minio.exc;

public abstract class MinioException extends RuntimeException {
    public abstract String getCode();
}
