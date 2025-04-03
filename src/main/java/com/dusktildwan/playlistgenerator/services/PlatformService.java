package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.PlatformRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlatformService {

    private final PlatformRepository platformRepository;

    private Map<String, Platform> platformMap = new HashMap<>();

    @PostConstruct
    public void loadPlatforms() {
        platformMap = platformRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Platform::getName, Platform -> Platform));
        log.info("platformMap: {}", platformMap.values());
    }

    public Platform getPlatformByName(String platformName) {
        return platformMap.get(platformName);
    }

    public boolean platformExists(String platformName){
        return platformMap.containsKey(platformName);
    }

}
