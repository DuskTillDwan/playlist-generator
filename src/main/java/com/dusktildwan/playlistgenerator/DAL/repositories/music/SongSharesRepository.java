package com.dusktildwan.playlistgenerator.DAL.repositories.music;

import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShareId;
import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongSharesRepository extends JpaRepository<SongShares, SongShareId> {
}
