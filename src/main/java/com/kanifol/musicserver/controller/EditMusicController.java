package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.dto.EditMusicService;
import com.kanifol.musicserver.service.dto.req.UploadTrackMetadataRequest;
import org.springframework.http.ResponseEntity;
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
}