package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.MusicService;
import com.kanifol.musicserver.service.dto.TrackMetadataResponse;
import org.springframework.http.MediaType;
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

    //TODO: стоит позже поменять формат хранения в minio картинки и аудио, чтобы завязать это на id трека.

    @GetMapping(path = "{id}/stream")
    public ResponseEntity<StreamingResponseBody> findStreamById(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        return musicService.findStreamById(id, range);
    }

    @GetMapping(path = "{id}/meta_data")
    public ResponseEntity<TrackMetadataResponse> findMetaDataById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(musicService.findMetaDataById(id));
    }

    @GetMapping(path = "{id}/cover")
    public ResponseEntity<StreamingResponseBody> findCoverById(
            @PathVariable Long id
    ) {
        StreamingResponseBody body = musicService.findCoverById(id);
        //TODO: стоит вынести в бд данные о хранимом типе
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(body);
    }
}
