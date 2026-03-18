package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.UserGenresService;
import com.kanifol.musicserver.service.dto.req.AddGenresRequest;
import com.kanifol.musicserver.service.dto.req.RemoveGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping(path = "user/")
public class UserGenresController {
    private final UserGenresService userGenresService;
    public UserGenresController(UserGenresService userGenresService) {
        this.userGenresService = userGenresService;
    }

    @DeleteMapping(path = "{id}/remove_genre")
    public ResponseEntity<Void> removeGenreForUser(
            @RequestBody RemoveGenreRequest removeGenreRequest,
            @PathVariable("id") Long userId
    ) {
        try {
            userGenresService.removeGenreForUser(removeGenreRequest.genreName(), userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "{id}/add_genres")
    public ResponseEntity<Void> addGenresForUser(
            @RequestBody AddGenresRequest addGenresRequest,
            @PathVariable("id") Long userId
    ) {
        try {
            userGenresService.addGenresForUser(addGenresRequest.genreNames(), userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}/user_genres")
    public ResponseEntity<Set<String>> getUserGenresForUser(
            @PathVariable("id") Long userId
    ) {
        try {
            return ResponseEntity.ok(userGenresService.getGenresNamesForUser(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "all_genres")
    public Set<String> getAllGenres() {
        return userGenresService.getAllGenresNames();
    }
}
