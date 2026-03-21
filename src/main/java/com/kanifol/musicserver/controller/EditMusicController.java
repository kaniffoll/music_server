package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.EditMusicService;
import com.kanifol.musicserver.service.dto.req.UploadTrackMetadataRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "edit/")
public class EditMusicController {
    private final EditMusicService editMusicService;

    public EditMusicController(EditMusicService editMusicService) {
        this.editMusicService = editMusicService;
    }

    @PostMapping(path = "track")
    public ResponseEntity<String> uploadTrack(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") UploadTrackMetadataRequest uploadTrackMetadataRequest
    ) throws IOException {
        editMusicService.uploadTrack(file, uploadTrackMetadataRequest);
        return ResponseEntity.ok("Track uploaded");
    }

    @DeleteMapping(path = "track/{id}")
    public ResponseEntity<String> deleteTrack(
            @PathVariable Long id
    ) {
        editMusicService.deleteTrack(id);
        return ResponseEntity.ok("Track deleted");
    }

    @DeleteMapping(path = "album/{id}")
    public ResponseEntity<String> deleteAlbum(
            @PathVariable Long id
    ) {
        editMusicService.deleteAlbum(id);
        return ResponseEntity.ok("Album deleted");
    }

    @PostMapping(path = "album/{title}")
    public ResponseEntity<String> createAlbum(
            @PathVariable String title
    ) {
        editMusicService.createAlbum(title);
        return ResponseEntity.ok("Album created");
    }
}