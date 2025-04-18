package com.dusktildwan.batch.services;

import com.dusktildwan.common.DAL.DTO.Message;
import com.dusktildwan.common.DAL.entities.music.Song;
import com.dusktildwan.common.DAL.entities.music.SongShareId;
import com.dusktildwan.common.DAL.entities.music.SongShares;
import com.dusktildwan.common.DAL.repositories.music.SongSharesRepository;
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

        SongShareId songShareId = createId(song, message);

        if (songSharesRepository.existsById(songShareId)) {
            log.info("Message has already been processed: USER: {}, shared_at: {}, link: {}",
                    message.senderName(), message.timestampMS(), message.share().link());
            return null;
        }

        SongShares songShare = SongShares.builder()
                .id(songShareId)
                .song(song)
                .user(chatMemberService.getChatMemberBySenderName(message))
                .build();
        try{
            SongShares saved = songSharesRepository.save(songShare);
            log.info("CREATED NEW SONG SHARE FOR SONG: {}, USER: {}", song.getUrl(), message.senderName());
            return saved;
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            //swallow exception
            log.error("DataIntegrityViolation Caught: Message duplicate processed for: USER: {}, shared_at: {}, link: {}",
                    message.senderName(), message.timestampMS(), message.share().link());
        }
        return null;
    }

    private SongShareId createId(Song song, Message message){
        return SongShareId.builder()
                .songId(song.getId())
                .userId(chatMemberService.getChatMemberBySenderName(message).getId())
                .sharedAt(instantToLocalDateTime(message)).build();
    }

    private static LocalDateTime instantToLocalDateTime(Message message) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault());
    }
}
