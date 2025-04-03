 package com.dusktildwan.playlistgenerator.DAL.repositories.music;

 import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
 import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShares;
 import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
 import com.dusktildwan.playlistgenerator.DAL.repositories.users.ChatMemberRepository;
 import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 import org.springframework.dao.DataIntegrityViolationException;
 import org.springframework.test.context.jdbc.Sql;

 import java.time.Instant;
 import java.time.LocalDateTime;
 import java.time.ZoneId;
 import java.util.Optional;

 import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
 import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
@Sql("/scripts/test-db.sql")
public class SongSharesRepositoryTest {
    @Autowired
    SongSharesRepository songSharesRepository;

    @Autowired
    ChatMemberRepository chatMemberRepository;

    @Autowired
    SongRepository songRepository;

    Message message = new Message("Bruce Banner",
            1740223403518L,
            "This is content",
            new SharedSong("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt"));

    Platform spotify = Platform.builder().id(1L).name("SPOTIFY").build();

    Song song = Song.builder().platformId(spotify).url("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt").build();

    ChatMember chatMember = ChatMember.builder().name("Bruce Banner").build();

    SongShares songShares = SongShares.builder()
            .song(song)
            .user(chatMember)
            .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
            .build();

    @Test
    void savesSongShare_savesSuccessfully(){

        songRepository.saveAndFlush(song);
        chatMemberRepository.saveAndFlush(chatMember);

        SongShares actualSongShare = songSharesRepository.save(songShares);

        assertThat(actualSongShare).isEqualTo(songShares);
    }

    @Test
    void savesSongShare_savesDuplicateShare_throwsDataIntegrityViolation(){

        songRepository.saveAndFlush(song);

        chatMemberRepository.saveAndFlush(chatMember);

        songSharesRepository.saveAndFlush(songShares);

        assertThat(songSharesRepository.findById(1L)).isEqualTo(Optional.of(songShares));

        //create duplicate share, so Hibernate doesn't reuse the same entity
        SongShares duplicateSongShare = SongShares.builder()
                .song(song)
                .user(chatMember)
                .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
                .build();


        assertThatThrownBy(() -> songSharesRepository.saveAndFlush(duplicateSongShare))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void savedSongShare_findsById(){

        songRepository.saveAndFlush(song);
        chatMemberRepository.saveAndFlush(chatMember);

        songSharesRepository.save(songShares);

        Optional<SongShares> retrievedShare = songSharesRepository.findById(1L);
        assertThat(retrievedShare).isPresent();
        assertThat(retrievedShare.get().getSong().getUrl()).isEqualTo(songShares.getSong().getUrl());
    }

    @Test
    void savedSongShare_findByInvalidID_returnsEmptyOptional(){
        assertThat(songSharesRepository.findById(2L)).isEqualTo(Optional.empty());
    }
}
