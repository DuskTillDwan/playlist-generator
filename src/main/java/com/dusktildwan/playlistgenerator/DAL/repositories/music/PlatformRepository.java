package com.dusktildwan.playlistgenerator.DAL.repositories.music;

import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
