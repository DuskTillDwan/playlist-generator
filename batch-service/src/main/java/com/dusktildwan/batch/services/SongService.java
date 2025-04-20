package com.dusktildwan.batch.services;

import com.dusktildwan.batch.util.PlatformType;
import com.dusktildwan.common.DAL.DTO.Message;
import com.dusktildwan.common.DAL.entities.music.Platform;
import com.dusktildwan.common.DAL.entities.music.Song;
import com.dusktildwan.common.DAL.repositories.music.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    private final PlatformService platformService;

    private final SongSharesService songSharesService;

    public Song saveSongAndSongShareToDatabase(Message message, PlatformType platformType) {
        String songUrl = message.share().link();
        Platform platform = platformService.getPlatformByName(platformType.name());

        if(songExistsByUrl(songUrl)){
            log.info("SONG EXISTS IN DB FOR URL: {}", songUrl);
            Song repeatSong = getSongByUrl(songUrl);
            songSharesService.saveSharedSongToDatabase(repeatSong, message);
            return null;
        }

        Song newSong = Song.builder()
                .platformId(platform)
                .url(songUrl)
                .build();

        Song savedSong = songRepository.save(newSong);
        log.info("Song saved: {}", savedSong.getUrl());
        songSharesService.saveSharedSongToDatabase(newSong, message);

        return savedSong;
    }

    public boolean songExistsByUrl(String songUrl){
        return songRepository.existsByUrl(songUrl);
    }

    public Song getSongByUrl(String songUrl) {
        return songRepository.findSongByUrl(songUrl);
    }
}
