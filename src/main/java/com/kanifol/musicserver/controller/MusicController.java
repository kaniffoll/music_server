package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.MusicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController()
@RequestMapping(path = "track/")
public class MusicController {

    MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<StreamingResponseBody> findById(@PathVariable Long id) {
        return musicService.findById(id);
    }
}
