package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.UserService;
import com.kanifol.musicserver.service.UserTracksService;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "user/")
public class UserTracksController {
    private final UserTracksService userTracksService;
    private final UserService userService;

    public UserTracksController(UserTracksService userTracksService, UserService userService) {
        this.userTracksService = userTracksService;
        this.userService = userService;
    }

    @PostMapping(path = "{username}/track/{trackId}")
    public ResponseEntity<?> addTrack(
            @PathVariable("username") String username,
            @PathVariable("trackId") Long trackId
    ) {
        Long userId = userService.getUserByUsername(username).getId();
        userTracksService.addTrackForUser(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{username}/track")
    public ResponseEntity<Set<TrackMetadataResponse>> getTracksForUser(
            @PathVariable("username") String username
    ) {
        Long userId = userService.getUserByUsername(username).getId();
        return ResponseEntity.ok(
                userTracksService.getTracksForUser(userId)
        );
    }

    @GetMapping(path = "{username}/track/{trackId}")
    public ResponseEntity<?> deleteTrack(
            @PathVariable("username") String username,
            @PathVariable("trackId") Long trackId
    ) {
        Long userId = userService.getUserByUsername(username).getId();
        userTracksService.removeTrackForUser(trackId, userId);
        return ResponseEntity.ok().build();
    }
}
