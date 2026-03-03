package com.kanifol.musicserver.repository;

import com.kanifol.musicserver.repository.model.TrackMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<TrackMetadata, Long> {}
