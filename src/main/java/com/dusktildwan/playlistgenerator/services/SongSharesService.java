package com.dusktildwan.playlistgenerator.services;

import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShares;
import com.dusktildwan.playlistgenerator.DAL.repositories.music.SongSharesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongSharesService {

    private final ChatMemberService chatMemberService;

    private final SongSharesRepository songSharesRepository;


    public SongShares saveSharedSongToDatabase(Song song, Message message) {
        SongShares songShare = SongShares.builder()
                .song(song)
                .user(chatMemberService.getChatMemberBySenderName(message))
                .sharedAt(instantToLocalDateTime(message))
                .build();
        try{
            return songSharesRepository.save(songShare);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            //swallow exception
            log.error("Message has already been processed: USER: {}, shared_at: {}, link: {}",
                    message.senderName(), message.timestampMS(), message.share().link());
        }
        return null;
    }

    private static LocalDateTime instantToLocalDateTime(Message message) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault());
    }
}