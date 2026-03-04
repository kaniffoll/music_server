package com.kanifol.musicserver.service.mappers;

import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;

public class DtoMappers {
    public static TrackMetadataResponse toDto(TrackMetadata trackMetadata) {
        return new TrackMetadataResponse(
                trackMetadata.getTitle(),
                trackMetadata.getArtist(),
                trackMetadata.getAlbum()
        );
    }
}
