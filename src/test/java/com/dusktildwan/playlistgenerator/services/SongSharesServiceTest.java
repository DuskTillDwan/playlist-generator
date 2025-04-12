package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;
import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShareId;
import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShares;
import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.SongSharesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
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

    SongShareId songShareId = SongShareId.builder()
            .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
            .userId(chatMember.getId())
            .songId(song.getId()).build();

    SongShares songShares = SongShares.builder()
            .id(songShareId)
            .song(song)
            .user(chatMember)
            .build();

    @Test
    void saveSharedSongToDatabase() {
        when(songSharesRepository.save(any())).thenReturn(songShares);
        when(chatMemberService.getChatMemberBySenderName(any())).thenReturn(chatMember);


        assertThat(songSharesService.saveSharedSongToDatabase(song, message)).isEqualTo(songShares);
    }

    @Test
    void saveSharedSongToDatabase_ExceptionThrownOnRepeatShare(CapturedOutput capturedOutput) {
        when(chatMemberService.getChatMemberBySenderName(any())).thenReturn(chatMember);

        doThrow(DataIntegrityViolationException.class).when(songSharesRepository).save(any(SongShares.class));

        songSharesService.saveSharedSongToDatabase(song, message);

        verify(songSharesRepository, times(1)).save(any());
        assertThat(capturedOutput.getOut()).contains("Message has already been processed:");
    }

}