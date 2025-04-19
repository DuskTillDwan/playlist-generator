package com.dusktildwan.spotifyservice;

import com.dusktildwan.common.DAL.repositories.music.PlatformRepository;
import com.dusktildwan.common.DAL.repositories.music.PlaylistRepository;
import com.dusktildwan.common.DAL.repositories.music.PlaylistSongsRepository;
import com.dusktildwan.common.DAL.repositories.music.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class SpotifyServiceApplicationTest {

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
