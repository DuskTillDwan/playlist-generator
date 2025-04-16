package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.FacebookChat;
import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.Participant;
import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.dusktildwan.playlistgenerator.util.PlatformDetector.detectPlatform;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final SongService songService;

    private final ChatMemberService chatMemberService;

    private final PlatformService platformService;


    public void processMessages(FacebookChat facebookChat) {
        for (Message message : facebookChat.messages()) {

            if (message.share() == null || message.share().link() == null){
                continue;
            }
            String songUrl = message.share().link();

            if(chatMemberService.isNewMember(message.senderName())){
                ChatMember newChatMember = chatMemberService.saveNewMember(message);
                log.info("New Chat Member not apart of participants added: {}", newChatMember.getName());
            }

            if (!platformService.platformExists(detectPlatform(songUrl))) {
                log.error("UNSUPPORTED PLATFORM RECEIVED: {}", songUrl);
                continue;
            }

            songService.saveSongAndSongShareToDatabase(message);
        }
    }

    public void processParticipants(FacebookChat facebookChat) {
        for(Participant participant : facebookChat.participants()){
            //create chatMembers from participants
            if(!chatMemberService.memberExists(participant.name())){
                ChatMember savedSuccessfully = chatMemberService.saveMemberByChatParticipants(participant);
                log.info("NEW MEMBER SAVED: {}",  savedSuccessfully.getName());
            }
        }
    }
}
