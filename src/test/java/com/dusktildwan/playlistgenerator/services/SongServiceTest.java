package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;
import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class SongServiceTest {

    @Mock
    SongRepository songRepository;

    @Mock
    PlatformService platformService;

    @Mock
    SongSharesService songSharesService;

    @InjectMocks
    SongService songService;

    Platform spotify = new Platform(1L, "SPOTIFY");
    Platform youtube = new Platform(2L, "YOUTUBE");
    Platform soundcloud = new Platform(3L, "SOUNDCLOUD");

    Message spotifyMessage = new Message("Bruce Banner",
            1740223403518L,
            "This is content",
            new SharedSong("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt"));

    Message youtubeMessage = new Message("Bruce Banner",
                1740223403518L,
                "This is content",
                new SharedSong("https://www.youtube.com/watch?v=UUxheK7soS4"));

    Message soundCloudMessage = new Message("Bruce Banner",
                1740223403518L,
                "This is content",
                new SharedSong("https://on.soundcloud.com/vJsnHfadHpfDyfK9A"));

    Song expectedSong = Song.builder().url("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt").build();

    @Test
    void shouldSaveSpotifySongAndSongShareToDatabase() {
        when(platformService.getPlatformByName("SPOTIFY")).thenReturn(spotify);
        when(songRepository.save(any(Song.class))).thenReturn(expectedSong);

        Song actual = songService.saveSongAndSongShareToDatabase(spotifyMessage);

        verify(platformService, times(1)).getPlatformByName(spotify.getName());
        verify(songSharesService, times(1)).saveSharedSongToDatabase(any(Song.class), any(Message.class));
        assertThat(actual).isEqualTo(expectedSong);
    }

    @Test
    void shouldSaveYoutubeSongAndSongShareToDatabase() {
        when(platformService.getPlatformByName("YOUTUBE")).thenReturn(youtube);
        expectedSong.setUrl("https://www.youtube.com/watch?v=UUxheK7soS4");
        when(songRepository.save(any(Song.class))).thenReturn(expectedSong);

        Song actual = songService.saveSongAndSongShareToDatabase(youtubeMessage);

        verify(platformService, times(1)).getPlatformByName(youtube.getName());
        verify(songSharesService, times(1)).saveSharedSongToDatabase(any(Song.class), any(Message.class));
        assertThat(actual).isEqualTo(expectedSong);
    }

    @Test
    void shouldSaveSoundcloudSongAndSongShareToDatabase() {
        when(platformService.getPlatformByName("SOUNDCLOUD")).thenReturn(soundcloud);
        expectedSong.setUrl("https://on.soundcloud.com/vJsnHfadHpfDyfK9A");
        when(songRepository.save(any(Song.class))).thenReturn(expectedSong);

        Song actual = songService.saveSongAndSongShareToDatabase(soundCloudMessage);

        verify(platformService, times(1)).getPlatformByName(soundcloud.getName());
        verify(songSharesService, times(1)).saveSharedSongToDatabase(any(Song.class), any(Message.class));
        assertThat(actual).isEqualTo(expectedSong);
    }

    @Test
    void shouldSaveUnknownSongAndSongShareToDatabase() {
        when(platformService.getPlatformByName("SOUNDCLOUD")).thenReturn(soundcloud);
        expectedSong.setUrl("https://on.soundcloud.com/vJsnHfadHpfDyfK9A");
        when(songRepository.save(any(Song.class))).thenReturn(expectedSong);

        Song actual = songService.saveSongAndSongShareToDatabase(soundCloudMessage);

        verify(platformService, times(1)).getPlatformByName(soundcloud.getName());
        verify(songSharesService, times(1)).saveSharedSongToDatabase(any(Song.class), any(Message.class));
        assertThat(actual).isEqualTo(expectedSong);
    }

    @Test
    void shouldSkipRepeatSongByUrl(CapturedOutput capturedOutput) {
        when(platformService.getPlatformByName(any())).thenReturn(spotify);
        when(songRepository.existsByUrl(anyString())).thenReturn(true);

        Song actual = songService.saveSongAndSongShareToDatabase(spotifyMessage);

        verify(platformService, times(1)).getPlatformByName(anyString());
        verify(songSharesService, times(1)).saveSharedSongToDatabase(any(Song.class), any(Message.class));
        assertThat(actual).isNull();
        assertThat(capturedOutput.getOut()).contains("SONG EXISTS IN DB FOR URL:");
    }

    @Test
    void songExistsByUrlReturnsTrue() {
        when(songRepository.existsByUrl(anyString())).thenReturn(true);

        assertThat(songService.songExistsByUrl(expectedSong.getUrl())).isEqualTo(true);
    }
    @Test
    void songExistsByUrlReturnsFalse() {
        when(songRepository.existsByUrl(anyString())).thenReturn(false);

        assertThat(songService.songExistsByUrl(expectedSong.getUrl())).isEqualTo(false);
    }

    @Test
    void getSongByUrl() {
        when(songRepository.findSongByUrl(anyString())).thenReturn(expectedSong);

        assertThat(songService.getSongByUrl(anyString())).isEqualTo(expectedSong);
    }
}