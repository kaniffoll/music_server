package com.kanifol.musicserver.controller;

import com.kanifol.musicserver.service.TrackService;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping(path = "/find")
public class FindController {

    private final TrackService trackService;

    public FindController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(path = "/track/{name}/stream")
    public ResponseEntity<StreamingResponseBody> findTrackStream(
            @PathVariable String name,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        return trackService.findStreamByTrackTitle(name, range);
    }

    @GetMapping(path = "/track/{name}/meta_data")
    public ResponseEntity<TrackMetadataResponse> findTrackMetaData(
            @PathVariable String name
    ) {
        return ResponseEntity.ok(trackService.findTrackByTrackTitle(name));
    }
}
