package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.UserGenresService;
import com.kanifol.musicserver.service.UserService;
import com.kanifol.musicserver.service.dto.req.AddGenresRequest;
import com.kanifol.musicserver.service.dto.req.RemoveGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "user/")
public class UserGenresController {
    private final UserGenresService userGenresService;
    private final UserService userService;

    public UserGenresController(UserGenresService userGenresService, UserService userService) {
        this.userGenresService = userGenresService;
        this.userService = userService;
    }

    @DeleteMapping(path = "{username}/remove_genre")
    public ResponseEntity<Void> removeGenreForUser(
            @RequestBody RemoveGenreRequest removeGenreRequest,
            @PathVariable("username") String username
    ) {
        Long userId = userService.getUserByUsername(username).getId();
        userGenresService.removeGenreForUser(removeGenreRequest.genreName(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{username}/add_genres")
    public ResponseEntity<Void> addGenresForUser(
            @RequestBody AddGenresRequest addGenresRequest,
            @PathVariable("username") String username
    ) {
        Long userId = userService.getUserByUsername(username).getId();
        userGenresService.addGenresForUser(addGenresRequest.genreNames(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{username}/user_genres")
    public ResponseEntity<Set<String>> getUserGenresForUser(
            @PathVariable("username") String username
    ) {
        Long userId = userService.getUserByUsername(username).getId();
        return ResponseEntity.ok(userGenresService.getGenresNamesForUser(userId));
    }

    @GetMapping(path = "all_genres")
    public Set<String> getAllGenres() {
        return userGenresService.getAllGenresNames();
    }
}
