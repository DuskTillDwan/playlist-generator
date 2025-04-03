package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;
import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShares;
import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.SongSharesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongSharesServiceTest {

    @Mock
    SongSharesRepository songSharesRepository;

    @Mock
    ChatMemberService chatMemberService;

    @InjectMocks
    SongSharesService songSharesService;

    ChatMember chatMember = ChatMember.builder().name("Bruce Banner").build();

    Message message = new Message("Bruce Banner",
            1740223403518L,
            "This is content",
            new SharedSong("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt"));

    Song song = Song.builder().url("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt").build();

    SongShares songShares = SongShares.builder()
            .song(song)
            .user(chatMember)
            .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
            .build();

    @Test
    void saveSharedSongToDatabase() {
        when(songSharesRepository.save(any())).thenReturn(songShares);
        when(chatMemberService.getChatMemberBySenderName(any())).thenReturn(chatMember);

        SongShares savedSong = songSharesService.saveSharedSongToDatabase(song, message);

        assertThat(savedSong).isEqualTo(songShares);
    }

    @Test
    void saveSharedSongToDatabase_ExceptionThrownOnRepeatShare() {
        when(chatMemberService.getChatMemberBySenderName(any())).thenReturn(chatMember);
        when(songSharesRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> songSharesService.saveSharedSongToDatabase(song, message))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}