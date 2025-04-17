package com.dusktildwan.playlistgenerator.DAL.repositories.music;

import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
@Sql("/scripts/platforms.sql")
class PlatformRepositoryTest {
    @Autowired
    PlatformRepository platformRepository;

    @BeforeEach
    void setUp(){
        Assertions.assertThat(platformRepository.count()).isGreaterThan(0);
    }

    @Test
    void platformsExistInDatabase(){
        List<Platform> allPlatforms = platformRepository.findAll();

        Assertions.assertThat(allPlatforms)
                .extracting(Platform::getId, Platform::getName)
                .containsExactlyInAnyOrder(
                        tuple(1L, "SPOTIFY"),
                        tuple(2L, "YOUTUBE"),
                        tuple(3L, "SOUNDCLOUD"));
    }
}