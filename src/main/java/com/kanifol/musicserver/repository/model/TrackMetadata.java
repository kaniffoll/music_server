package com.kanifol.musicserver.repository.model;

import jakarta.persistence.*;

import java.util.HashSet;
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

    public TrackMetadata(String title, String artist, Short trackNumber, Set<Genre> genres, Album album) {
        this.title = title;
        this.artist = artist;
        this.trackNumber = trackNumber;
        this.genres = genres;
        this.album = album;
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
}
