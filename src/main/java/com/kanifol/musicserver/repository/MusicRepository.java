package com.kanifol.musicserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<TrackMetadata, Long> {}
