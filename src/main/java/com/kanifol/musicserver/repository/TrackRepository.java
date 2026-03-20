package com.kanifol.musicserver.repository;

import com.kanifol.musicserver.repository.model.TrackMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TrackRepository extends JpaRepository<TrackMetadata, Long> {
    List<TrackMetadata> findByTitleContaining(String title);

    boolean existsByAlbumIdAndTrackNumber(Long albumId, Short trackNumber);

    @Query(
            value = """
                    SELECT DISTINCT t.* 
                    FROM music.track_metadata t
                    JOIN music.track_genre tg ON t.id = tg.track_id
                    JOIN music.genre g ON tg.genre_id = g.id
                    WHERE g.name IN (:genres)
                    AND t.id NOT IN (:usedIds)
                    AND t.id > (:random)
                    LIMIT 5
                    """,
            nativeQuery = true
    )
    List<TrackMetadata> findTrackBatchGrater(
            @Param("usedIds") Collection<Long> usedIds,
            @Param("genres") Collection<String> genres,
            @Param("random") Long randomId
    );

    @Query(
            value = """
                    SELECT DISTINCT t.* 
                    FROM music.track_metadata t
                    JOIN music.track_genre tg ON t.id = tg.track_id
                    JOIN music.genre g ON tg.genre_id = g.id
                    WHERE g.name IN (:genres)
                    AND t.id NOT IN (:usedIds)
                    AND t.id <= (:random)
                    LIMIT 5
                    """,
            nativeQuery = true
    )
    List<TrackMetadata> findTrackBatchLower(
            @Param("usedIds") Collection<Long> usedIds,
            @Param("genres") Collection<String> genres,
            @Param("random") Long randomId
    );

    @Query("SELECT MAX(t.id) FROM TrackMetadata t")
    Long findMaxId();
    @Query("SELECT MIN(t.id) FROM TrackMetadata t")
    Long findMinId();
}
