package com.kanifol.musicserver.service.dto.res;

public record TrackMetadataResponse(
        Long id,
        String title,
        String artist,
        Long albumId,
        Short trackNumber
) { }
