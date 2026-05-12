package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.TrackRepository;
import com.kanifol.musicserver.repository.UserRepository;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.repository.model.User;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import com.kanifol.musicserver.service.exc.NoSuchTrackException;
import com.kanifol.musicserver.service.exc.NoSuchUserException;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTracksService {
   private final UserRepository userRepository;
   private final TrackRepository trackRepository;

    public UserTracksService(UserRepository userRepository, TrackRepository trackRepository) {
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
    }

    @Transactional
    public void addTrackForUser(Long trackId, Long userId) {
        TrackMetadata trackMetadata = trackRepository.findById(trackId)
                .orElseThrow(() -> new NoSuchTrackException(trackId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserException(userId));
        System.out.println(trackMetadata);
        user.getTracks().add(trackMetadata);
        userRepository.save(user);
    }

    @Transactional
    public Set<TrackMetadataResponse> getTracksForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserException(userId));
        Set<Long> likedTrackIds = user.getTracks()
                .stream()
                .map(TrackMetadata::getId)
                .collect(Collectors.toSet());
        return user.getTracks()
                .stream()
                .map(track -> DtoMappers.toDto(track, likedTrackIds))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void removeTrackForUser(Long trackId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserException(userId));

        TrackMetadata trackMetadata = trackRepository.findById(trackId)
                .orElseThrow(() -> new NoSuchTrackException(trackId));

        user.getTracks().remove(trackMetadata);

        userRepository.save(user);
    }
}
