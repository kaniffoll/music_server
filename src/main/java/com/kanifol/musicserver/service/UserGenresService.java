package com.kanifol.musicserver.service;

import com.kanifol.musicserver.repository.GenreRepository;
import com.kanifol.musicserver.repository.UserRepository;
import com.kanifol.musicserver.repository.model.Genre;
import com.kanifol.musicserver.repository.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGenresService {

    private final GenreRepository genreRepository;
    private final UserRepository userRepository;

    public UserGenresService(GenreRepository genreRepository, UserRepository userRepository) {
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void addGenresForUser(Set<String> genresNames, Long userId) {
        List<Genre> genres = genreRepository.findByNameIn(genresNames);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getGenres().addAll(genres);
        userRepository.save(user);
    }

    @Transactional
    public void removeGenreForUser(String genreName, Long userId) {
        Genre genre = genreRepository.findByName(genreName)
                .orElseThrow(() -> new RuntimeException("Genre Not Found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getGenres().remove(genre);
    }

    public Set<String> getGenresNamesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getGenresNames();
    }

    public Set<String> getAllGenresNames() {
        return genreRepository.findAll().stream().map(Genre::toString).collect(Collectors.toSet());
    }
}
