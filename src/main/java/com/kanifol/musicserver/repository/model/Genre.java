package com.kanifol.musicserver.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "genre", schema = "music")
public class Genre {

    @Id
    private Short id;

    @Column(nullable = false, unique = true)
    String name;

    public Genre() {}

    public Short getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Genre genre)) return false;
        return Objects.equals(getId(), genre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
