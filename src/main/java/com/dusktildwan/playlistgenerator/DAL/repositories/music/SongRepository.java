package com.dusktildwan.playlistgenerator.DAL.repositories.music;

import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByUrl(String url);

    Song findSongByUrl(String songUrl);
}
