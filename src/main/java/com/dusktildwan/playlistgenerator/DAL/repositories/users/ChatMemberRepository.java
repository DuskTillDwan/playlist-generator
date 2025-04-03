package com.dusktildwan.playlistgenerator.DAL.repositories.users;

import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    Optional<ChatMember> findByName(String name);
}
