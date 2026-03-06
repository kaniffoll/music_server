package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.TrackRepository;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.repository.minio.MinioStreamProvider;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final MinioDatasource minioDatasource;

    public TrackService(TrackRepository trackRepository, MinioDatasource minioDatasource) {
        this.trackRepository = trackRepository;
        this.minioDatasource = minioDatasource;
    }

    public ResponseEntity<StreamingResponseBody> findStreamById(Long id, String rangeHeader) {
        TrackMetadata trackMetadata = trackRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No track with " + id));
        String key = TrackMetadata.toTrackUrl(trackMetadata.getAlbum().getId(), trackMetadata.getTrackNumber());
        try {
            return MinioStreamProvider.getStreamByKey(key, rangeHeader, minioDatasource);
        } catch (Exception e) {
            throw new NoSuchElementException("No track with " + id);
        }
    }

    public List<TrackMetadataResponse> findTracksByTitle(String title) {
        List<TrackMetadata> trackMetadataList = trackRepository.findByTitleContaining(title)
                .orElseThrow(() -> new NoSuchElementException("No track with " + title));
        return trackMetadataList
                .stream()
                .map(DtoMappers::toDto)
                .toList();
    }
}
