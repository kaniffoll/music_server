package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.UserTracksService;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "user/")
public class UserTracksController {
    private final UserTracksService userTracksService;
    public UserTracksController(UserTracksService userTracksService) {
        this.userTracksService = userTracksService;
    }

    @PostMapping(path = "{userId}/track/{trackId}")
    public ResponseEntity<?> addTrack(
            @PathVariable("userId") Long userId,
            @PathVariable("trackId") Long trackId
    ) {
        userTracksService.addTrackForUser(trackId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{userId}/track")
    public ResponseEntity<Set<TrackMetadataResponse>> getTracksForUser(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(
                userTracksService.getTracksForUser(userId)
        );
    }
}
