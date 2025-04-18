package com.dusktildwan.playlistgenerator;

import com.dusktildwan.playlistgenerator.DAL.repositories.music.PlatformRepository;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.SongRepository;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.SongSharesRepository;
import com.dusktildwan.playlistgenerator.DAL.repositories.users.ChatMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
 class BatchServiceApplicationTests {

	@MockitoBean
	ChatMemberRepository chatMemberRepository;

	@MockitoBean
	PlatformRepository platformRepository;

	@MockitoBean
	SongRepository songRepository;

	@MockitoBean
	SongSharesRepository songSharesRepository;

	@Test
	void contextLoads() {
	}

}
