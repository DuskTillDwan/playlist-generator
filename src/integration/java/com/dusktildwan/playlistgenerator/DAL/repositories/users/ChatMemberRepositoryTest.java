package com.dusktildwan.playlistgenerator.DAL.repositories.users;

import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
@Sql("/scripts/chat-member.sql")
class ChatMemberRepositoryTest {

    @Autowired
    ChatMemberRepository chatMemberRepository;

    @Autowired
    TestEntityManager testEntityManager;

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

        Assertions.assertThat(testEntityManager.find(ChatMember.class, savedMember.getId())).isEqualTo(chatMember);
    }

    @Test
    void chatMemberRepository_saveAndFindUserById(){
        ChatMember savedMember = chatMemberRepository.save(chatMember);

        testEntityManager.persist(savedMember);

        Optional<ChatMember> foundMember = chatMemberRepository.findById(1L);

        Assertions.assertThat(foundMember).contains(savedMember);
    }

    @Test
    void chatMemberRepository_findByName(){
        ChatMember savedMember = chatMemberRepository.save(chatMember);

        testEntityManager.persist(savedMember);

        Optional<ChatMember> foundMember = chatMemberRepository.findByName("Adam Savage");

        Assertions.assertThat(foundMember).contains(savedMember);
    }
}