package com.kanifol.musicserver.repository.minio;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@Component
public class MinioStreamProvider {

    public static ResponseEntity<StreamingResponseBody> getStreamByKey(String key, String rangeHeader, MinioDatasource minioDatasource) throws Exception {
        long fileSize = minioDatasource.getObjectSize(key);
        long start;
        long end = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] rangeParts = rangeHeader.replace("bytes=", "").split("-");
            start = Long.parseLong(rangeParts[0]);
            if (rangeParts.length > 1 && !rangeParts[1].isBlank()) {
                end = Long.parseLong(rangeParts[1]);
            }
        } else {
            start = 0;
        }

        long contentLength = end - start + 1;

        StreamingResponseBody body = out -> {
            try (InputStream stream = minioDatasource.audioStream(key, start, contentLength)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = stream.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    out.flush();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        headers.setContentLength(contentLength);
        headers.set("Accept-Ranges", "bytes");
        headers.set("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);

        return ResponseEntity
                .status(rangeHeader == null ? 200 : 206)
                .headers(headers)
                .body(body);
    }
}
