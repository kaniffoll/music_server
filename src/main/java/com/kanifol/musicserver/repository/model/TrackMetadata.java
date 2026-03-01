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
    @Column(name = "cover_url")
    private String coverUrl;
    @Column(name = "audio_url")
    private String audioUrl;

    public TrackMetadata(String title, String artist, String album, String coverUrl, String audioUrl) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.coverUrl = coverUrl;
        this.audioUrl = audioUrl;
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
                ", cover_url='" + coverUrl + '\'' +
                ", audio_url='" + audioUrl + '\'' +
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}
