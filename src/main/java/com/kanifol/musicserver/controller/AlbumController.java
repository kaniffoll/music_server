package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.AlbumService;
import com.kanifol.musicserver.service.dto.res.AlbumResponse;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController()
@RequestMapping(path = "album/")
public class AlbumController {
    private final AlbumService albumService;
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping(path = "{albumId}/track/{trackNumber}/stream")
    public ResponseEntity<StreamingResponseBody> findStreamById(
            @PathVariable Long albumId,
            @PathVariable Short trackNumber,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        return albumService.findStreamByTrackNumber(albumId, trackNumber, range);
    }

    @GetMapping(path = "{albumId}/track/{trackNumber}/meta_data")
    public ResponseEntity<TrackMetadataResponse> findMetaDataById(
            @PathVariable Long albumId,
            @PathVariable Short trackNumber
    ) {
        return ResponseEntity.ok(albumService.findMetaDataByTrackNumber(albumId, trackNumber));
    }

    @GetMapping("{albumId}/cover")
    public ResponseEntity<byte[]> findCoverById(@PathVariable Long albumId) {

        byte[] image = albumService.findCoverByAlbumId(albumId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping(path = "{albumId}/tracks")
    public ResponseEntity<List<TrackMetadataResponse>> findTracksByAlbumId(
            @PathVariable Long albumId
    ) {
        return ResponseEntity.ok(albumService.findTracksByAlbumId(albumId));
    }
}
