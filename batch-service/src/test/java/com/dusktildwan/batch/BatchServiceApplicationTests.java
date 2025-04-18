package com.dusktildwan.batch;

import com.dusktildwan.common.DAL.repositories.music.PlatformRepository;
import com.dusktildwan.common.DAL.repositories.music.SongRepository;
import com.dusktildwan.common.DAL.repositories.music.SongSharesRepository;
import com.dusktildwan.common.DAL.repositories.users.ChatMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
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
