package com.dusktildwan.batch.DAL.entities.music;

import com.dusktildwan.common.DAL.entities.music.SongShareId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SongShareIdTest {

    LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
    SongShareId expectedSongShare = new SongShareId(1L, 2L, now);

    @Test
    void testEquals() {
        SongShareId actualSongShare = new SongShareId(1L, 2L, now);
        assertThat(actualSongShare).isEqualTo(expectedSongShare);
    }

    @Test
    void testNotEquals() {
        SongShareId different = new SongShareId(1L, 3L, now);
        assertThat(different).isNotEqualTo(expectedSongShare);
    }

    @Test
    void testEqualsAndNotNull() {
        SongShareId self = new SongShareId(1L, 3L, now);
        assertThat(self).isEqualTo(self);
        assertThat(self).isNotEqualTo(null);
    }

    @Test
    void testHashCode() {
        SongShareId actualSongShare = new SongShareId(1L, 2L, now);
        assertEquals(expectedSongShare.hashCode(), actualSongShare.hashCode());
    }
}