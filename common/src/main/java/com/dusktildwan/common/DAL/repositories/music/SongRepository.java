package com.dusktildwan.common.DAL.repositories.music;

import com.dusktildwan.common.DAL.entities.music.Platform;
import com.dusktildwan.common.DAL.entities.music.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByUrl(String url);

    Song findSongByUrl(String songUrl);

    List<Song> findAllByPlatformId(Platform platform);
}
