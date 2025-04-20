package com.dusktildwan.soundcloudservice;

import com.dusktildwan.common.DAL.repositories.music.PlatformRepository;
import com.dusktildwan.common.DAL.repositories.music.PlaylistRepository;
import com.dusktildwan.common.DAL.repositories.music.PlaylistSongsRepository;
import com.dusktildwan.common.DAL.repositories.music.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
class SoundCloudServiceApplicationTest {

    @MockitoBean
    PlaylistRepository playlistRepository;

    @MockitoBean
    SongRepository songRepository;

    @MockitoBean
    PlaylistSongsRepository playlistSongsRepository;

    @MockitoBean
    PlatformRepository platformRepository;

    @Test
    void contextLoads() {
    }
}