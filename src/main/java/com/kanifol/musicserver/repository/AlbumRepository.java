package com.kanifol.musicserver.repository;

import com.kanifol.musicserver.repository.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<List<Album>> findByNameContaining(String name);
}
