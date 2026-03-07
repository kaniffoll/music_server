package com.kanifol.musicserver.service.dto;

import com.kanifol.musicserver.repository.AlbumRepository;
import com.kanifol.musicserver.repository.GenreRepository;
import com.kanifol.musicserver.repository.TrackRepository;
import com.kanifol.musicserver.repository.minio.MinioDatasource;
import com.kanifol.musicserver.repository.model.Album;
import com.kanifol.musicserver.repository.model.Genre;
import com.kanifol.musicserver.repository.model.TrackMetadata;
import com.kanifol.musicserver.service.dto.req.UploadTrackMetadataRequest;
import com.kanifol.musicserver.service.mappers.DtoMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class EditMusicService {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final GenreRepository genreRepository;
    private final MinioDatasource minioDatasource;

    public EditMusicService(AlbumRepository albumRepository, TrackRepository trackRepository, GenreRepository genreRepository, MinioDatasource minioDatasource) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.genreRepository = genreRepository;
        this.minioDatasource = minioDatasource;
    }

    @Transactional
    public void uploadTrack(
            MultipartFile file,
            UploadTrackMetadataRequest request
    ) throws Exception {
        TrackMetadata trackMetadata = DtoMappers.toModel(request);
        Album album = albumRepository.findById(request.albumId())
                .orElseThrow(() -> new NoSuchElementException("Album not found"));

        if (trackRepository.existsByAlbumIdAndTrackNumber(album.getId(), trackMetadata.getTrackNumber()))
            throw new IllegalStateException("Track already exists in album");

        Set<Genre> genres = new HashSet<>(genreRepository.findByNameIn(request.genres()));

        if (genres.isEmpty())
            throw new NoSuchElementException("Genre not found");

        trackMetadata.setAlbum(album);
        trackMetadata.setGenres(genres);

        String key = TrackMetadata.toTrackUrl(album.getId(), trackMetadata.getTrackNumber());
        trackRepository.save(trackMetadata);

        try (InputStream inputStream = file.getInputStream()) {
            minioDatasource.uploadTrack(
                    inputStream,
                    file.getSize(),
                    key
            );
        } catch (Exception e) {
            trackRepository.delete(trackMetadata);
            throw e;
        }
    }
}
