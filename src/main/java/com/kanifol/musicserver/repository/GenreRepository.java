package com.kanifol.musicserver.repository;

import com.kanifol.musicserver.repository.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Short> {
    List<Genre> findByNameIn(Collection<String> names);
    Optional<Genre> findByName(String name);
}
