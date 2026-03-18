package com.kanifol.musicserver.service.dto.req;

import java.util.Set;

public record UploadTrackMetadataRequest(
        String title,
        String artist,
        Short trackNumber,
        Long albumId,
        Set<String> genres,
        String previewTimeStart
) {
}
