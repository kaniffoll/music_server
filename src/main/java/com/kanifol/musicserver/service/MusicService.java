package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.MusicRepository;
import com.kanifol.musicserver.repository.TrackMetadata;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MusicService {

    private final MusicRepository musicRepository;
    private final MinioDatasource minioDatasource;

    public MusicService(MusicRepository musicRepository, MinioDatasource minioDatasource) {
        this.musicRepository = musicRepository;
        this.minioDatasource = minioDatasource;
    }

    public ResponseEntity<StreamingResponseBody> findById(Long id) {
        Optional<TrackMetadata> optionalTrackMetadata = musicRepository.findById(id);
        if (optionalTrackMetadata.isEmpty())
            throw new NoSuchElementException("Track with id " + id + " not found");

        StreamingResponseBody body = out -> {
            try (InputStream stream = minioDatasource.audioStream(optionalTrackMetadata.get().getAudio_url())) {
                stream.transferTo(out);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        headers.set("Accept-Ranges", "bytes");

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }
}
