package com.kanifol.musicserver.repository;

import com.kanifol.musicserver.repository.model.TrackMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<TrackMetadata, Long> {
    Optional<TrackMetadata> findByTitle(String title);
}
