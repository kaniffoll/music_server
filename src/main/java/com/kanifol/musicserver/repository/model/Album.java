package com.kanifol.musicserver.repository.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "album", schema = "music")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "album")
    private Set<TrackMetadata> tracksMetadataSet = new HashSet<>();

    public Album() {
    }

    public Set<TrackMetadata> getTracksMetadataSet() {
        return tracksMetadataSet;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //TODO: формат фото захардкожен
    public static String toCoverUrl(Long id) {
        return id.toString() + ".jpeg";
    }
}
