package com.dusktildwan.playlistgenerator.DAL.repositories.users;

import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
@Sql("classpath:scripts/test-db.sql")
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

        assertThat(testEntityManager.find(ChatMember.class, savedMember.getId())).isEqualTo(chatMember);
    }

    @Test
    void chatMemberRepository_saveAndFindUserById(){
        ChatMember savedMember = chatMemberRepository.save(chatMember);

        testEntityManager.persist(savedMember);

        Optional<ChatMember> foundMember = chatMemberRepository.findById(1L);

        assertThat(foundMember).contains(savedMember);
    }
}