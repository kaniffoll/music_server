package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.TrackRepository;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.NoSuchElementException;

@Service
public class MusicService {

    private final TrackRepository trackRepository;
    private final MinioDatasource minioDatasource;

    public MusicService(TrackRepository trackRepository, MinioDatasource minioDatasource) {
        this.trackRepository = trackRepository;
        this.minioDatasource = minioDatasource;
    }

    public ResponseEntity<StreamingResponseBody> findStreamById(Long id, String rangeHeader) {
        long fileSize;
        String key = TrackMetadata.toTrackUrl(id);

        try {
            fileSize = minioDatasource.getObjectSize(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    public TrackMetadataResponse findMetaDataById(Long id) {
        TrackMetadata trackMetadata = trackRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Track with id " + id + " not found"));

        return DtoMappers.toDto(trackMetadata);
    }

    public StreamingResponseBody findCoverById(Long id) {

        return out -> {
            try (InputStream stream = minioDatasource.coverStream(TrackMetadata.toCoverUrl(id))) {
                stream.transferTo(out);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
