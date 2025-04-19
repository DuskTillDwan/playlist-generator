package com.dusktildwan.common.DAL.entities.music;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SongShareId implements Serializable {
    @Column(name = "song_id")
    private Long songId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "shared_at")
    private LocalDateTime sharedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SongShareId that = (SongShareId) o;
        return Objects.equals(songId, that.songId) && Objects.equals(userId, that.userId) && Objects.equals(sharedAt, that.sharedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, userId, sharedAt);
    }
}
