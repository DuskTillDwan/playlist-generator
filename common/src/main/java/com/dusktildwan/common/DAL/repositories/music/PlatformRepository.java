package com.dusktildwan.common.DAL.repositories.music;

import com.dusktildwan.common.DAL.entities.music.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
