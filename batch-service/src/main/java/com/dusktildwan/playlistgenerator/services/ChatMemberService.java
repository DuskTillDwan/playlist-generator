package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.Participant;
import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import com.dusktildwan.playlistgenerator.DAL.repositories.users.ChatMemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    private Map<String, ChatMember> memberMap = new HashMap<>();

    @PostConstruct
    public void initMemberMap(){
        memberMap = chatMemberRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChatMember::getName, ChatMember -> ChatMember));
    }

    public ChatMember saveMemberByChatParticipants(Participant participant){
        ChatMember chatMember = ChatMember.builder().name(participant.name()).build();
        //if the member exists in the map -> it already exists in the database
        //if it doesn't, then add  to the map and save
        if(isNewMember(participant.name())){
            memberMap.put(participant.name(), chatMember);
        }
        return chatMemberRepository.saveAndFlush(chatMember);
    }

    public ChatMember saveNewMember(Message message){
        ChatMember chatMember = ChatMember.builder().name(message.senderName()).build();
        memberMap.put(message.senderName(), chatMember);
        return chatMemberRepository.saveAndFlush(chatMember);
    }

    public Boolean memberExists(String participantName){
        //check cache first
        if(!isNewMember(participantName)){
            log.info("Chat Member: {} already exists", participantName);
            return true;
        }

        Optional<ChatMember> optionalChatMember = chatMemberRepository.findByName(participantName);

        //check the database -> if in database, & not cache, update cache
        optionalChatMember.ifPresent(chatMember -> memberMap.put(chatMember.getName(), chatMember));

        return optionalChatMember.isPresent();
    }

    public Boolean isNewMember(String memberName){
        return !memberMap.containsKey(memberName);
    }

    public ChatMember getChatMemberBySenderName(Message message){
        ChatMember chatMember = new ChatMember();
        Optional<ChatMember> chatMemberPresent = chatMemberRepository.findByName(message.senderName());
        return chatMemberPresent.orElse(chatMember);
    }

}
