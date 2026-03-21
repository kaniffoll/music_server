package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.AlbumRepository;
import com.kanifol.musicserver.repository.minio.MinioStreamProvider;
import com.kanifol.musicserver.repository.model.Album;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.service.dto.res.AlbumResponse;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.exc.NoSuchAlbumException;
import com.kanifol.musicserver.service.exc.NoSuchTrackException;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.*;

@Service
public class AlbumService {
    private final MinioDatasource minioDatasource;
    private final AlbumRepository albumRepository;

    public AlbumService(MinioDatasource minioDatasource, AlbumRepository albumRepository) {
        this.minioDatasource = minioDatasource;
        this.albumRepository = albumRepository;
    }

    public ResponseEntity<StreamingResponseBody> findStreamByTrackNumber(Long albumId, Short trackNumber, String rangeHeader) {
        String key = TrackMetadata.toTrackUrl(albumId, trackNumber);
        return MinioStreamProvider.getStreamByKey(key, rangeHeader, minioDatasource);
    }

    public TrackMetadataResponse findMetaDataByTrackNumber(Long albumId, Short trackNumber) {
        Album album = findAlbumById(albumId);
        TrackMetadata trackMetadata = album
                .getTracksMetadataSet()
                .stream()
                .filter(it -> Objects.equals(it.getTrackNumber(), trackNumber))
                .findFirst()
                .orElseThrow(() -> new NoSuchTrackException(trackNumber.longValue()));

        return DtoMappers.toDto(trackMetadata);
    }

    public StreamingResponseBody findCoverByAlbumId(Long albumId) {

        return out -> {
            try (InputStream stream = minioDatasource.coverStream(Album.toCoverUrl(albumId))) {
                stream.transferTo(out);
            }
        };
    }

    public List<TrackMetadataResponse> findTracksByAlbumId(Long albumId) {
        Album album = findAlbumById(albumId);
        return album
                .getTracksMetadataSet()
                .stream()
                .sorted(Comparator.comparing(TrackMetadata::getTrackNumber))
                .map(DtoMappers::toDto)
                .toList();
    }

    public List<AlbumResponse> findAlbumsByTitle(String title) {
        List<Album> albums = albumRepository.findByTitleContaining(title);
        if (albums.isEmpty())
            throw new NoSuchAlbumException(title);

        return albums.stream().map(DtoMappers::toDto).toList();
    }

    private Album findAlbumById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new NoSuchAlbumException(albumId));
    }
}
