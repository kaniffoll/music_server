package com.kanifol.musicserver.service.mappers;

import com.kanifol.musicserver.repository.model.Album;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.service.dto.res.AlbumResponse;
import com.kanifol.musicserver.service.dto.res.TrackMetadataResponse;

public class DtoMappers {
    public static TrackMetadataResponse toDto(TrackMetadata trackMetadata) {
        return new TrackMetadataResponse(
                trackMetadata.getId(),
                trackMetadata.getTitle(),
                trackMetadata.getArtist(),
                trackMetadata.getAlbum().getId(),
                trackMetadata.getTrackNumber()
        );
    }

    public static AlbumResponse toDto(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getName()
        );
    }
}
