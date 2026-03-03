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
    private String album;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "track_genre",
            schema = "music",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public TrackMetadata(String title, String artist, String album, Set<Genre> genres) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genres = genres;
    }

    public TrackMetadata() {
    }

    @Override
    public String toString() {
        return "TrackMetadata{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
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

    public String getAlbum() {
        return album;
    }

    public static String toTrackUrl(Long id) {
        return id.toString() + ".mp3";
    }

    //TODO: нужно сделать поддержку других форматов
    public static String toCoverUrl(Long id) {
        return id.toString() + ".jpeg";
    }
}
