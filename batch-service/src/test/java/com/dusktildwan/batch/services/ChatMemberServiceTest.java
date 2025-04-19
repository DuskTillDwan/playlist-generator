package com.dusktildwan.batch.services;

import com.dusktildwan.common.DAL.DTO.Message;
import com.dusktildwan.common.DAL.DTO.Participant;
import com.dusktildwan.common.DAL.DTO.SharedSong;
import com.dusktildwan.common.DAL.entities.users.ChatMember;
import com.dusktildwan.common.DAL.repositories.users.ChatMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ChatMemberServiceTest {

    @Mock
    ChatMemberRepository chatMemberRepository;

    @InjectMocks
    ChatMemberService chatMemberService;

    Participant participantReed = new Participant("Reed Richards");

    Message message = new Message("Reed Richards",
            1740223403518L,
            "This is content, okay?",
            new SharedSong("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt"));

    ChatMember chatMemberReed = ChatMember.builder()
            .name("Reed Richards")
            .build();

    ChatMember chatMemberSusan = ChatMember.builder()
            .name("Susan Storm")
            .build();

    @Test
    void saveMemberByChatParticipants_returnsExpectedMember() {
        when(chatMemberRepository.saveAndFlush(any())).thenReturn(chatMemberReed);

        ChatMember actualChatMember = chatMemberService.saveMemberByChatParticipants(participantReed);

        verify(chatMemberRepository, times(1)).saveAndFlush(any());
        assertThat(actualChatMember.getName()).isEqualTo(participantReed.name());
    }

    @Test
    void saveMemberByChatParticipants_ShouldSaveNewMember() {
        when(chatMemberRepository.saveAndFlush(any())).thenReturn(chatMemberReed);
        when(chatMemberRepository.findAll()).thenReturn(List.of(chatMemberSusan));

        chatMemberService.initMemberMap();

        ChatMember actualChatMember = chatMemberService.saveMemberByChatParticipants(participantReed);

        verify(chatMemberRepository, times(1)).saveAndFlush(any());
        assertThat(actualChatMember.getName()).isEqualTo(participantReed.name());
    }

    @Test
    void saveNewMember() {
        when(chatMemberRepository.saveAndFlush(any())).thenReturn(chatMemberReed);

        ChatMember actualChatMember = chatMemberService.saveNewMember(message);

        verify(chatMemberRepository, times(1)).saveAndFlush(any());
        assertThat(actualChatMember.getName()).isEqualTo(message.senderName());
    }

    @Test
    void memberExists_inDatabase_thenReturnTrue() {
        when(chatMemberRepository.findByName(anyString())).thenReturn(Optional.ofNullable(chatMemberReed));

        Boolean memberFound = chatMemberService.memberExists(chatMemberReed.getName());

        verify(chatMemberRepository, times(1)).findByName(participantReed.name());
        assertThat(memberFound).isTrue();

    }

    @Test
    void memberExists_memberNotInDatabase_thenReturnFalse() {
        when(chatMemberRepository.findByName(anyString())).thenReturn(Optional.empty());

        Boolean memberNotFound = chatMemberService.memberExists(chatMemberReed.getName());

        verify(chatMemberRepository, times(1)).findByName(participantReed.name());
        assertThat(memberNotFound).isFalse();
    }

    @Test
    void memberExists_existsInCache_ReturnsTrue(CapturedOutput capturedOutput) {
        when(chatMemberRepository.findAll()).thenReturn(List.of(chatMemberReed, chatMemberSusan));

        chatMemberService.initMemberMap();

        verify(chatMemberRepository, times(1)).findAll();
        assertThat(chatMemberService.memberExists(participantReed.name())).isTrue();
        verify(chatMemberRepository, never()).findByName(participantReed.name());
        assertThat(capturedOutput.getOut()).contains("Chat Member: "+ participantReed.name() +" already exists");
    }

    @Test
    void memberExists_DoesNotExistInCache_FetchesFromDatabase() {
        when(chatMemberRepository.findAll()).thenReturn(List.of(chatMemberReed, chatMemberSusan));

        chatMemberService.initMemberMap();

        verify(chatMemberRepository, times(1)).findAll();
        assertThat(chatMemberService.memberExists("Dj Ramos")).isFalse();
        verify(chatMemberRepository, times(1)).findByName("Dj Ramos");
    }


    @Test
    void isNewMember_ReturnsFalseForExistingMember() {
        when(chatMemberRepository.findAll()).thenReturn(List.of(chatMemberReed, chatMemberSusan));

        chatMemberService.initMemberMap();

        verify(chatMemberRepository, times(1)).findAll();
        assertThat(chatMemberService.isNewMember(participantReed.name())).isFalse();
    }

    @Test
    void isNewMember_ReturnsTrueForNotExistingMember() {
        when(chatMemberRepository.findAll()).thenReturn(List.of(chatMemberReed, chatMemberSusan));

        chatMemberService.initMemberMap();

        verify(chatMemberRepository, times(1)).findAll();
        assertThat(chatMemberService.isNewMember("Dj Ramos")).isTrue();
    }


    @Test
    void getChatMemberBySenderName_returnsChatMemberIfInDatabase() {
        when(chatMemberRepository.findByName(anyString())).thenReturn(Optional.of(chatMemberReed));

        ChatMember memberFoundByName = chatMemberService.getChatMemberBySenderName(message);

        verify(chatMemberRepository, times(1)).findByName(participantReed.name());
        assertThat(memberFoundByName).isEqualTo(chatMemberReed);
    }

    @Test
    void getChatMemberBySenderName_returnsEmptyChatMemberWhenNotInDatabase() {
        when(chatMemberRepository.findByName(anyString())).thenReturn(Optional.empty());

        ChatMember returnedMember = chatMemberService.getChatMemberBySenderName(message);

        assertThat(returnedMember).isNotNull();
        assertThat(returnedMember.getName()).isNull();
        assertThat(returnedMember.getId()).isNull();
    }
}