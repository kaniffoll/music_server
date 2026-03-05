package com.kanifol.musicserver.repository;

import com.kanifol.musicserver.repository.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
