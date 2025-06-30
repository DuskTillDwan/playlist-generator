package com.dusktildwan.common.DAL.entities.music;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PlaylistSongIdTest {

    PlaylistSongId expectedPlaylistSongId = new PlaylistSongId(1L, 2L);

    @Test
    void testEquals() {
        PlaylistSongId actualPlaylistId = new PlaylistSongId(1L, 2L);
        assertThat(expectedPlaylistSongId).isEqualTo(actualPlaylistId);
    }

    @Test
    void testNotEquals() {
        PlaylistSongId newPlaylistSongId = new PlaylistSongId(1L, 1L);
        assertThat(expectedPlaylistSongId).isNotEqualTo(newPlaylistSongId);
    }

    @Test
    void testSelfEqualsAndNotNull() {
        PlaylistSongId self = expectedPlaylistSongId;
        assertThat(expectedPlaylistSongId).isEqualTo(self);
        assertThat(expectedPlaylistSongId).isNotNull();
    }

    @Test
    void hashcode(){
        PlaylistSongId self = expectedPlaylistSongId;
        assertThat(expectedPlaylistSongId.hashCode()).isEqualTo(self.hashCode());
    }
}