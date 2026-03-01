package com.kanifol.musicserver.service.dto;

public record TrackMetadataResponse(
        String title,
        String artist,
        String album
) { }
