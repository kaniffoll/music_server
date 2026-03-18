package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.AlbumService;
import com.kanifol.musicserver.service.TrackService;
import com.kanifol.musicserver.service.dto.res.AlbumResponse;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping(path = "/find")
public class FindController {
    private final TrackService trackService;
    private final AlbumService albumService;
    public FindController(TrackService trackService, AlbumService albumService) {
        this.trackService = trackService;
        this.albumService = albumService;
    }

    @GetMapping(path = "/track/{id}/stream")
    public ResponseEntity<StreamingResponseBody> findTrackStream(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        return trackService.findStreamById(id, range);
    }

    //TODO: проверить почему сигнатура методов отличается

    @GetMapping(path = "/track/{title}")
    public ResponseEntity<List<TrackMetadataResponse>> findTracksByName(
            @PathVariable String title
    ) {
        return ResponseEntity.ok(trackService.findTracksByTitle(title));
    }

    @GetMapping(path = "album/{title}")
    public ResponseEntity<List<AlbumResponse>> findAlbumsByTitle(@PathVariable String title) {
        try {
            return ResponseEntity.ok(albumService.findAlbumsByTitle(title));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
