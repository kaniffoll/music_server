package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.AlbumRepository;
import com.kanifol.musicserver.repository.minio.MinioStreamProvider;
import com.kanifol.musicserver.repository.model.Album;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.service.dto.res.AlbumResponse;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
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
        try {
            return MinioStreamProvider.getStreamByKey(key, rangeHeader, minioDatasource);
        } catch (Exception e) {
            throw new NoSuchElementException("Track with " + key + " not found");
        }
    }

    public TrackMetadataResponse findMetaDataByTrackNumber(Long albumId, Short trackNumber) {
        Album album = findAlbumById(albumId);
        TrackMetadata trackMetadata = album
                .getTracksMetadataSet()
                .stream()
                .filter(it -> Objects.equals(it.getTrackNumber(), trackNumber))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Track with id " + trackNumber + " not found"));

        return DtoMappers.toDto(trackMetadata);
    }

    public StreamingResponseBody findCoverByAlbumId(Long albumId) {

        return out -> {
            try (InputStream stream = minioDatasource.coverStream(Album.toCoverUrl(albumId))) {
                stream.transferTo(out);
            } catch (Exception e) {
                throw new RuntimeException(e);
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

    public List<AlbumResponse> findAlbumsByName(String albumName) {
        List<Album> albums = albumRepository.findByNameContaining(albumName)
                .orElseThrow(() -> new NoSuchElementException("No album with name " + albumName));
        return albums.stream().map(DtoMappers::toDto).toList();
    }

    private Album findAlbumById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new NoSuchElementException("Album with id " + albumId + " not found"));
    }
}
