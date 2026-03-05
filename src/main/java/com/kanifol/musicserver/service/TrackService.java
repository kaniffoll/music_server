package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.TrackRepository;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.repository.minio.MinioStreamProvider;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.NoSuchElementException;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final MinioDatasource minioDatasource;

    public TrackService(TrackRepository trackRepository, MinioDatasource minioDatasource) {
        this.trackRepository = trackRepository;
        this.minioDatasource = minioDatasource;
    }

    @Transactional
    public ResponseEntity<StreamingResponseBody> findStreamByTrackTitle(String title, String rangeHeader) {
        TrackMetadata trackMetadata = trackRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Track Not Found"));

        String key = TrackMetadata.toTrackUrl(trackMetadata.getAlbum().getId(), trackMetadata.getTrackNumber());
        try {
            return MinioStreamProvider.getStreamByKey(key, rangeHeader, minioDatasource);
        } catch (Exception e) {
            throw new NoSuchElementException("Track with " + key + " not found");
        }
    }

    @Transactional
    public TrackMetadataResponse findTrackByTrackTitle(String title) {
        return DtoMappers.toDto(trackRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Track Not Found")));
    }
}
