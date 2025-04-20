package com.dusktildwan.batch.services;

import com.dusktildwan.batch.util.PlatformType;
import com.dusktildwan.common.DAL.DTO.FacebookChat;
import com.dusktildwan.common.DAL.DTO.Message;
import com.dusktildwan.common.DAL.DTO.Participant;
import com.dusktildwan.common.DAL.entities.users.ChatMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

import static com.dusktildwan.batch.util.PlatformDetector.detectPlatform;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final SongService songService;

    private final ChatMemberService chatMemberService;

    private final PlatformService platformService;

    public void processMessages(FacebookChat facebookChat) {
        for (Message message : facebookChat.messages()) {
            handleMessage(message);
        }
    }

    private void handleMessage(Message message) {
        if (message.share() == null || message.share().link() == null){
            return;
        }
        String songUrl = message.share().link();
        PlatformType platformType = detectPlatform(songUrl);

        if(chatMemberService.isNewMember(message.senderName())){
            ChatMember newChatMember = chatMemberService.saveNewMember(message);
            log.info("New Chat Member not apart of participants added: {}", newChatMember.getName());
        }

        if (!platformService.platformExists(platformType.name())) {
            log.error("UNSUPPORTED PLATFORM RECEIVED: {}", songUrl);
            return;
        }

        if(!isValidTrack(platformType, songUrl)){
            log.error("Not valid track: {} for platform {}", songUrl, platformType);
            return;
        }
        songService.saveSongAndSongShareToDatabase(message, platformType);
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

    private boolean isValidTrack(PlatformType platformName, String songUrl) {
        switch (platformName){
            case SPOTIFY -> {
                return isSpotifyTrack(songUrl);
            }
            case SOUNDCLOUD -> {
                return isSoundCloudTrack(songUrl);
            }
            case YOUTUBE -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isSpotifyTrack(String songUrl) {
        try {
            URI uri = new URI(songUrl);
            String path = uri.getPath();
            return path.startsWith("/track/");
        } catch (URISyntaxException e) {
            log.error("URISyntaxException occurred: SKIPPING SONG {}", songUrl);
        }
        return false;
    }

    private boolean isSoundCloudTrack(String songUrl) { //pending soundcloud integration
        log.info("soundcloud integration pending, assuming valid tracks for now {}", songUrl);
        return true;
    }
}
