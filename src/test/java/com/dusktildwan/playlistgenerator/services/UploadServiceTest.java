package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.FacebookChat;
import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.Participant;
import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;
import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class UploadServiceTest {
    @Mock
    SongService songService;

    @Mock
    ChatMemberService chatMemberService;

    @Mock
    PlatformService platformService;

    @InjectMocks
    UploadService uploadService;

    Message spotifyMessage = new Message("Bruce Banner",
            1740223403518L,
            "This is content",
            new SharedSong("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt"));

    Message youtubeMessage = new Message("Mister Rodgers",
            1740223403518L,
            "This is content",
            new SharedSong("https://www.youtube.com/watch?v=UUxheK7soS4"));

    Message soundCloudMessage = new Message("Susan Storm",
            1740223403518L,
            "This is content",
            new SharedSong("https://on.soundcloud.com/vJsnHfadHpfDyfK9A"));

    Message unknownPlatformMessage = new Message("Louis Bard",
            1740223403518L,
            "This is content",
            new SharedSong("somegarbage.com/vJsnHfadHpfDyfK9A"));

    Message emptyShareLinkMessage = new Message("Susan Storm",
            1740223403518L,
            "This is content",
            new SharedSong(null));

    Message nullShareMessage = new Message("Susan Storm",
            1740223403518L,
            "This is content",
            null);

    FacebookChat facebookChat = new FacebookChat(List.of(new Participant("Susan Storm"),
            new Participant("Mister Rodgers"),
            new Participant("Bruce Banner")),
            List.of(spotifyMessage, soundCloudMessage, youtubeMessage, unknownPlatformMessage));

    @Test
    void processMessages_AllNewParticipants(CapturedOutput capturedOutput){
        for(Participant participant : facebookChat.participants()){
            when(chatMemberService.memberExists(participant.name())).thenReturn(false);
            when(chatMemberService.saveMemberByChatParticipants(participant))
                    .thenReturn(ChatMember.builder().name(participant.name()).build());
        }
        uploadService.processParticipants(facebookChat);

        for(Participant participant : facebookChat.participants()){
            verify(chatMemberService, times(1)).memberExists(participant.name());
            verify(chatMemberService, times(1)).saveMemberByChatParticipants(participant);
            assertThat(capturedOutput.getOut()).contains("NEW MEMBER SAVED: " + participant.name());
        }
    }
    @Test
    void processNoNewParticipants(){
        for(Participant participant : facebookChat.participants()){
            when(chatMemberService.memberExists(participant.name())).thenReturn(true);
        }
        uploadService.processParticipants(facebookChat);

        verify(chatMemberService, times(facebookChat.participants().size())).memberExists(any());
        verify(chatMemberService, never()).saveMemberByChatParticipants(any());
    }

    @Test
    void processMessagesAndNewParticipants(){
        when(chatMemberService.isNewMember(anyString())).thenReturn(true);
        when(platformService.platformExists("SPOTIFY")).thenReturn(true);
        when(platformService.platformExists("SOUNDCLOUD")).thenReturn(true);
        when(platformService.platformExists("YOUTUBE")).thenReturn(true);
        when(platformService.platformExists("UNKNOWN")).thenReturn(false);


        for(Message message : facebookChat.messages()){
            when(chatMemberService.saveNewMember(message)).thenReturn(ChatMember.builder().name(message.senderName()).build());
        }

        uploadService.processMessages(facebookChat);

        verify(chatMemberService, times(facebookChat.messages().size())).saveNewMember(any());
        verify(platformService).platformExists("SPOTIFY");
        verify(platformService).platformExists("SOUNDCLOUD");
        verify(platformService).platformExists("YOUTUBE");
        verify(platformService).platformExists("UNKNOWN");

        //calls save song for every platform except "UNKNOWN"
        verify(songService, times(facebookChat.messages().size()-1)).saveSongAndSongShareToDatabase(any());
    }

    @Test
    void processMessagesAndNoNewParticipants(CapturedOutput capturedOutput){
        when(chatMemberService.isNewMember(anyString())).thenReturn(false);

        when(platformService.platformExists("SPOTIFY")).thenReturn(true);
        when(platformService.platformExists("SOUNDCLOUD")).thenReturn(true);
        when(platformService.platformExists("YOUTUBE")).thenReturn(true);
        when(platformService.platformExists("UNKNOWN")).thenReturn(false);


        uploadService.processMessages(facebookChat);

        verify(chatMemberService, never()).saveNewMember(any());

        verify(platformService).platformExists("SPOTIFY");
        verify(platformService).platformExists("SOUNDCLOUD");
        verify(platformService).platformExists("YOUTUBE");
        verify(platformService).platformExists("UNKNOWN");

        //calls save song for every platform except "UNKNOWN"
        verify(songService, times(facebookChat.messages().size()-1)).saveSongAndSongShareToDatabase(any());
        assertThat(capturedOutput.getOut()).contains("UNSUPPORTED PLATFORM RECEIVED:");
    }

    @Test
    void processMessages_OneNewParticipant(CapturedOutput capturedOutput){
        when(chatMemberService.isNewMember(anyString())).thenReturn(false);
        when(chatMemberService.isNewMember("Susan Storm")).thenReturn(true);
        when(chatMemberService.saveNewMember(soundCloudMessage)).thenReturn(ChatMember.builder().name("Susan Storm").build());
        when(platformService.platformExists("SPOTIFY")).thenReturn(true);
        when(platformService.platformExists("SOUNDCLOUD")).thenReturn(true);
        when(platformService.platformExists("YOUTUBE")).thenReturn(true);
        when(platformService.platformExists("UNKNOWN")).thenReturn(false);


        uploadService.processMessages(facebookChat);

        verify(chatMemberService, times(1)).saveNewMember(soundCloudMessage);

        verify(platformService).platformExists("SPOTIFY");
        verify(platformService).platformExists("SOUNDCLOUD");
        verify(platformService).platformExists("YOUTUBE");
        verify(platformService).platformExists("UNKNOWN");

        //calls save song for every platform except "UNKNOWN"
        verify(songService, times(facebookChat.messages().size()-1)).saveSongAndSongShareToDatabase(any());
        assertThat(capturedOutput.getOut()).contains("New Chat Member not apart of participants added: "+soundCloudMessage.senderName());
        assertThat(capturedOutput.getOut()).contains("UNSUPPORTED PLATFORM RECEIVED:");
    }

    @Test
    void processMessagesWithEmptySharedLink() {
        FacebookChat chatWithEmptyLink = new FacebookChat(
                facebookChat.participants(),
                List.of(emptyShareLinkMessage)
        );
        uploadService.processMessages(chatWithEmptyLink);

        verifyNoInteractions(platformService);
        verify(songService, never()).saveSongAndSongShareToDatabase(any());
    }
    @Test
    void processMessagesWithNullShare() {
        FacebookChat chatWithEmptyLink = new FacebookChat(
                facebookChat.participants(),
                List.of(nullShareMessage)
        );
        uploadService.processMessages(chatWithEmptyLink);

        verifyNoInteractions(platformService);
        verify(songService, never()).saveSongAndSongShareToDatabase(any());
    }

}