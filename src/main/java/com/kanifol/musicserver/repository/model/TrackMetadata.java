package com.kanifol.musicserver.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "track_metadata", schema = "music")
public class TrackMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    private String album;
    private String cover_url;
    private String audio_url;

    public TrackMetadata(String title, String artist, String album, String cover_url, String audio_url) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.cover_url = cover_url;
        this.audio_url = audio_url;
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
                ", cover_url='" + cover_url + '\'' +
                ", audio_url='" + audio_url + '\'' +
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

    public String getCover_url() {
        return cover_url;
    }

    public String getAudio_url() {
        return audio_url;
    }
}
