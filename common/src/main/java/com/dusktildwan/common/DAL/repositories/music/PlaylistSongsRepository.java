package com.dusktildwan.common.DAL.repositories.music;

import com.dusktildwan.common.DAL.entities.music.PlaylistSongId;
import com.dusktildwan.common.DAL.entities.music.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongsRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {
}
