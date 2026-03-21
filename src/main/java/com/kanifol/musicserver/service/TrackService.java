package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.RedisRepository;
import com.kanifol.musicserver.repository.TrackRepository;
import com.kanifol.musicserver.repository.UserRepository;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.repository.minio.MinioStreamProvider;
import com.kanifol.musicserver.repository.model.Genre;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.exc.NoSuchTrackException;
import com.kanifol.musicserver.service.exc.NoSuchUserException;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import com.kanifol.musicserver.service.model.TrackKeyType;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kanifol.musicserver.service.model.TrackKeyType.MAIN;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final MinioDatasource minioDatasource;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;

    public TrackService(
            TrackRepository trackRepository,
            MinioDatasource minioDatasource,
            RedisRepository redisRepository,
            UserRepository userRepository
    ) {
        this.trackRepository = trackRepository;
        this.minioDatasource = minioDatasource;
        this.redisRepository = redisRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<StreamingResponseBody> findStreamById(
            Long id,
            String rangeHeader,
            TrackKeyType trackKeyType
    ) {
        TrackMetadata trackMetadata = trackRepository.findById(id)
                .orElseThrow(() -> new NoSuchTrackException(id));
        Long albumId = trackMetadata.getAlbum().getId();
        Short trackNumber = trackMetadata.getTrackNumber();
        String key = trackKeyType == MAIN ? TrackMetadata.toTrackUrl(albumId, trackNumber) :
                TrackMetadata.toTrackPreviewUrl(albumId, trackNumber);
        try {
            return MinioStreamProvider.getStreamByKey(key, rangeHeader, minioDatasource);
        } catch (Exception e) {
            throw new NoSuchTrackException(id);
        }
    }

    public List<TrackMetadataResponse> findTracksByTitle(String title) {
        List<TrackMetadata> trackMetadataList = trackRepository.findByTitleContaining(title);
        if (trackMetadataList.isEmpty())
            throw new NoSuchTrackException(title);
        return trackMetadataList
                .stream()
                .map(DtoMappers::toDto)
                .toList();
    }

    public List<TrackMetadataResponse> findTracksBatchByUserGenres(String username) {
        Set<String> genres = redisRepository.getUserFavoriteGenres(username);
        if (genres.isEmpty()) {
            genres = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new NoSuchUserException(username))
                    .getGenres()
                    .stream()
                    .map(Genre::toString)
                    .collect(Collectors.toSet());
            redisRepository.addUserFavoriteGenres(
                    username,
                    genres
            );
        }
        Set<Long> usedIds = redisRepository.getUsedIdsForUser(username)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        Long random = RandomUtils.insecure().randomLong(
                trackRepository.findMinId(),
                trackRepository.findMaxId()
        );
        if (usedIds.isEmpty()) usedIds.add(-1L);

        List<TrackMetadata> trackMetadataList = trackRepository.findTrackBatchGrater(usedIds, genres, random);
        if (trackMetadataList.isEmpty()) {
            trackMetadataList = trackRepository.findTrackBatchLower(usedIds, genres, random);
        }

        redisRepository.addUsedIdsForUser(
                username,
                trackMetadataList.stream()
                        .map(it -> it.getId().toString())
                        .collect(Collectors.toList()));
        return trackMetadataList.stream().map(DtoMappers::toDto).collect(Collectors.toList());
    }
}
