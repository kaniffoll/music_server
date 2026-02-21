package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.MusicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController()
@RequestMapping(path = "track/")
public class MusicController {

    MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<StreamingResponseBody> findById(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        return musicService.findById(id, range);
    }
}
