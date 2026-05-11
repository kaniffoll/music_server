package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.AlbumRepository;
import com.kanifol.musicserver.repository.UserRepository;
import com.kanifol.musicserver.repository.minio.MinioStreamProvider;
import com.kanifol.musicserver.repository.model.Album;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.repository.model.User;
import com.kanifol.musicserver.service.dto.res.AlbumResponse;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.exc.NoSuchAlbumException;
import com.kanifol.musicserver.service.exc.NoSuchTrackException;
import com.kanifol.musicserver.service.exc.NoSuchUserException;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {
    private final MinioDatasource minioDatasource;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    public AlbumService(MinioDatasource minioDatasource, AlbumRepository albumRepository, UserRepository userRepository) {
        this.minioDatasource = minioDatasource;
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<StreamingResponseBody> findStreamByTrackNumber(Long albumId, Short trackNumber, String rangeHeader) {
        String key = TrackMetadata.toTrackUrl(albumId, trackNumber);
        return MinioStreamProvider.getStreamByKey(key, rangeHeader, minioDatasource);
    }

    public TrackMetadataResponse findMetaDataByTrackNumber(Long albumId, Short trackNumber, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchUserException(username)
        );
        Set<Long> likedTrackIds = user.getTracks()
                .stream()
                .map(TrackMetadata::getId)
                .collect(Collectors.toSet());

        Album album = findAlbumById(albumId);
        TrackMetadata trackMetadata = album
                .getTracksMetadataSet()
                .stream()
                .filter(it -> Objects.equals(it.getTrackNumber(), trackNumber))
                .findFirst()
                .orElseThrow(() -> new NoSuchTrackException(trackNumber.longValue()));

        return DtoMappers.toDto(trackMetadata, likedTrackIds);
    }

    public byte[] findCoverByAlbumId(Long albumId) {
        try (InputStream stream =
                     minioDatasource.coverStream(Album.toCoverUrl(albumId))) {

            return stream.readAllBytes();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load cover", e);
        }
    }

    public List<TrackMetadataResponse> findTracksByAlbumId(Long albumId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchUserException(username)
        );
        Set<Long> likedTrackIds = user.getTracks()
                .stream()
                .map(TrackMetadata::getId)
                .collect(Collectors.toSet());
        Album album = findAlbumById(albumId);
        return album
                .getTracksMetadataSet()
                .stream()
                .sorted(Comparator.comparing(TrackMetadata::getTrackNumber))
                .map(track -> DtoMappers.toDto(track, likedTrackIds))
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
