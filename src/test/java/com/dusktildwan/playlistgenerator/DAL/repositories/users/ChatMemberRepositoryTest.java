package com.dusktildwan.playlistgenerator.DAL.repositories.users;

import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
@Sql("classpath:scripts/test-db.sql")
class ChatMemberRepositoryTest {

    @Autowired
    ChatMemberRepository chatMemberRepository;

    ChatMember chatMember;


    @BeforeEach
    void createChatMember(){
         chatMember = ChatMember.builder()
                .facebookId("someid")
                .name("Adam Savage")
                .build();
    }

    @Test
    void chatMemberRepository_savesUser(){
        ChatMember savedMember = chatMemberRepository.save(chatMember);

        assertEquals(1L, savedMember.getId());
        assertEquals("Adam Savage", savedMember.getName());
        assertEquals("someid", savedMember.getFacebookId());
    }

    @Test
    void chatMemberRepository_saveAndFindUserById(){
        chatMemberRepository.save(chatMember);

        Optional<ChatMember> foundMember = chatMemberRepository.findById(1L);

        assertTrue(foundMember.isPresent());
        assertEquals(1L, foundMember.get().getId());
        assertEquals("Adam Savage", foundMember.get().getName());
        assertEquals("someid", foundMember.get().getFacebookId());
    }
}