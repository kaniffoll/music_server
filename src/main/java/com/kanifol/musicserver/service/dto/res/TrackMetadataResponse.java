package com.kanifol.musicserver.service.dto.res;

public record TrackMetadataResponse(
        String title,
        String artist,
        String album
) { }
