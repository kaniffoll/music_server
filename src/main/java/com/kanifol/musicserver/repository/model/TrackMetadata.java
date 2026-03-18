package com.kanifol.musicserver.repository.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "track_metadata", schema = "music")
public class TrackMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    @Column(name = "track_number")
    private Short trackNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "track_genre",
            schema = "music",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();
    public Album getAlbum() {
        return album;
    }

    public TrackMetadata(String title, String artist, Short trackNumber) {
        this.title = title;
        this.artist = artist;
        this.trackNumber = trackNumber;
    }

    public TrackMetadata() {
    }

    @Override
    public String toString() {
        return "TrackMetadata{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Short getTrackNumber() {
        return trackNumber;
    }

    public static String toTrackUrl(Long albumId, Short trackNumber) {
        return albumId.toString() + "/" + trackNumber.toString() + ".mp3";
    }

    public static String toTrackPreviewUrl(Long albumId, Short trackNumber) {
        return albumId.toString() + "/" + trackNumber.toString() + "preview.mp3";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TrackMetadata that)) return false;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getArtist(), that.getArtist()) && Objects.equals(getTrackNumber(), that.getTrackNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getArtist(), getTrackNumber());
    }
}
