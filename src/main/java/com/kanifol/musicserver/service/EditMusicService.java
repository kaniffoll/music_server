package com.kanifol.musicserver.service;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
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

        File mainFile = File.createTempFile("tmp", ".mp3");
        file.transferTo(mainFile);

        File previewFile = File.createTempFile("preview", ".mp3");
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-i", mainFile.getAbsolutePath(),
                "-ss", request.previewTimeStart(),
                "-t", "30",
                "-c", "copy",
                previewFile.getAbsolutePath()
        );

        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Process returned non-zero exit code: " + exitCode);
        }

        String previewKey = TrackMetadata.toTrackPreviewUrl(album.getId(), trackMetadata.getTrackNumber());
        String mainKey = TrackMetadata.toTrackUrl(album.getId(), trackMetadata.getTrackNumber());
        trackRepository.save(trackMetadata);

        try (InputStream inputStream = new FileInputStream(mainFile);
             InputStream previewStream = new FileInputStream(previewFile)) {
            minioDatasource.uploadTrack(
                    inputStream,
                    mainFile.length(),
                    mainKey
            );
            minioDatasource.uploadTrack(
                    previewStream,
                    previewFile.length(),
                    previewKey
            );
        } catch (Exception e) {
            trackRepository.delete(trackMetadata);
            throw e;
        } finally {
            Files.deleteIfExists(mainFile.toPath());
            Files.deleteIfExists(previewFile.toPath());
        }
    }

    public void createAlbum(String title) {
        Optional<Album> album = albumRepository.findByTitle(title);
        if (album.isPresent())
            throw new IllegalStateException("Album already exists");

        albumRepository.save(new Album(title));
    }

    @Transactional
    public void deleteTrack(Long id) throws Exception {
        TrackMetadata trackMetadata = trackRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Track not found"));
        String key = TrackMetadata.toTrackUrl(trackMetadata.getAlbum().getId(), trackMetadata.getTrackNumber());
        trackRepository.delete(trackMetadata);
        minioDatasource.deleteTrack(key);
    }

    @Transactional
    public void deleteAlbum(Long id) throws Exception {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Album not found"));
        String prefix = id + "/";
        albumRepository.delete(album);
        minioDatasource.deleteAlbum(prefix);
    }
}
