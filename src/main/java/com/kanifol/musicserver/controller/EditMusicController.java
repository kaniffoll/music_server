package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.EditMusicService;
import com.kanifol.musicserver.service.dto.req.UploadTrackMetadataRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "upload/")
public class EditMusicController {

    private final EditMusicService editMusicService;

    public EditMusicController(EditMusicService editMusicService) {
        this.editMusicService = editMusicService;
    }

    @PostMapping(path = "track")
    public ResponseEntity<String> uploadTrack(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") UploadTrackMetadataRequest uploadTrackMetadataRequest
    ) {
        try {
            editMusicService.uploadTrack(file, uploadTrackMetadataRequest);
            return ResponseEntity.ok("Track uploaded");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "track/{id}")
    public ResponseEntity<String> deleteTrack(
            @PathVariable Long id
    ) {
        try {
            editMusicService.deleteTrack(id);
            return ResponseEntity.ok("Track deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "album/{id}")
    public ResponseEntity<String> deleteAlbum(
            @PathVariable Long id
    ) {
        try {
            editMusicService.deleteAlbum(id);
            return ResponseEntity.ok("Album deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path = "album/{title}")
    public ResponseEntity<String> createAlbum(
            @PathVariable String title
    ) {
        try {
            editMusicService.createAlbum(title);
            return ResponseEntity.ok("Album created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}