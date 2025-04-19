package com.dusktildwan.common.DAL.repositories.music;

import com.dusktildwan.common.DAL.entities.music.Platform;
import com.dusktildwan.common.DAL.entities.music.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findByExternalIdAndPlatform(String externalId, Platform spotify);
}
