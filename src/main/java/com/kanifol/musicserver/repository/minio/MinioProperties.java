package com.kanifol.musicserver.repository.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String musicBucket;
    private String coversBucket;

    public String getMusicBucket() {
        return musicBucket;
    }

    public String getCoversBucket() {
        return coversBucket;
    }

    public String getAccessKey() {
        return accessKey;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public String getEndpoint() {
        return endpoint;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setMusicBucket(String musicBucket) {
        this.musicBucket = musicBucket;
    }

    public void setCoversBucket(String coversBucket) {
        this.coversBucket = coversBucket;
    }
}

