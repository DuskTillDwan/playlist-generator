package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {

    @Mock
    PlatformRepository platformRepository;

    @InjectMocks
    PlatformService platformService;

    Platform spotify = Platform.builder().id(1L).name("SPOTIFY").build();

    Platform youtube = Platform.builder().id(2L).name("YOUTUBE").build();

    Platform soundcloud = Platform.builder().id(3L).name("SOUNDCLOUD").build();


    @Test
    void getPlatformByName() {
        when(platformRepository.findAll()).thenReturn(List.of(spotify, youtube, soundcloud));

        platformService.loadPlatforms();

        Platform platformReturned = platformService.getPlatformByName("SPOTIFY");

        verify(platformRepository, times(1)).findAll();
        assertThat(platformReturned).isEqualTo(spotify);
    }

    @Test
    void getPlatformByName_PlatformDoesNotExist() {
        when(platformRepository.findAll()).thenReturn(List.of(spotify, youtube, soundcloud));

        platformService.loadPlatforms();

        Platform platformReturned = platformService.getPlatformByName("New Service");

        verify(platformRepository, times(1)).findAll();
        assertThat(platformReturned).isNull();
    }

    @Test
    void platformExists() {
        when(platformRepository.findAll()).thenReturn(List.of(spotify, youtube, soundcloud));

        platformService.loadPlatforms();

        Boolean platformReturned = platformService.platformExists("SPOTIFY");

        verify(platformRepository, times(1)).findAll();
        assertThat(platformReturned).isTrue();
    }

    @Test
    void platformExists_returnsFalseForUnsupportedPlatform() {
        when(platformRepository.findAll()).thenReturn(List.of(spotify, youtube, soundcloud));

        platformService.loadPlatforms();

        Boolean platformReturned = platformService.platformExists("Unsupported Platform");

        verify(platformRepository, times(1)).findAll();
        assertThat(platformReturned).isFalse();
    }
}