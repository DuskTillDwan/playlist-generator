 package com.dusktildwan.playlistgenerator.DAL.repositories.music;

 import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
 import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShareId;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.SongShares;
 import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
 import com.dusktildwan.playlistgenerator.DAL.repositories.users.ChatMemberRepository;
 import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
 import jakarta.persistence.EntityManager;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 import org.springframework.test.context.jdbc.Sql;

 import java.time.Instant;
 import java.time.LocalDateTime;
 import java.time.ZoneId;
 import java.util.Optional;

 import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @Autowired
    EntityManager entityManager;

    Message message = new Message("Bruce Banner",
            1740223403518L,
            "This is content",
            new SharedSong("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt"));

    Platform spotify = Platform.builder().id(1L).name("SPOTIFY").build();

    Song song = Song.builder().platformId(spotify).url("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt").build();

    ChatMember chatMember = ChatMember.builder().name("Bruce Banner").build();

    @Test
    void savesSongShare_savesSuccessfully(){
        songRepository.saveAndFlush(song);
        chatMemberRepository.saveAndFlush(chatMember);

        SongShareId songShareId = SongShareId.builder()
                .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
                .userId(chatMember.getId())
                .songId(song.getId()).build();

        SongShares songShares = SongShares.builder()
                .id(songShareId)
                .song(song)
                .user(chatMember)
                .build();

        SongShares actual = songSharesRepository.saveAndFlush(songShares);

        assertThat(actual.getSong()).isEqualTo(songShares.getSong());
        assertThat(actual.getId().getSharedAt()).isEqualTo(songShares.getId().getSharedAt());
        assertThat(actual.getId().getSongId()).isEqualTo(songShares.getId().getSongId());
        assertThat(actual.getId().getUserId()).isEqualTo(songShares.getId().getUserId());
    }

    @Test
    void savesSongShare_savesDuplicateShare_doesNotSave(){

        songRepository.saveAndFlush(song);
        chatMemberRepository.saveAndFlush(chatMember);

        SongShareId songShareId = SongShareId.builder()
                .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
                .userId(chatMember.getId())
                .songId(song.getId()).build();

        SongShares songShares = SongShares.builder()
                .id(songShareId)
                .song(song)
                .user(chatMember)
                .build();

        songSharesRepository.saveAndFlush(songShares);

        Optional<SongShares> retrieved = songSharesRepository.findById(songShareId);
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getId()).isEqualTo(songShares.getId());

        long countBefore = songSharesRepository.count();

        entityManager.clear();

        //create duplicate share, so Hibernate doesn't reuse the same entity
        SongShares duplicateSongShare = SongShares.builder()
                .id(new SongShareId(song.getId(),
                        chatMember.getId(),
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault())))
                .song(song)
                .user(chatMember)
                .build();

        long countAfter = songSharesRepository.count();
        songSharesRepository.saveAndFlush(duplicateSongShare);
        assertThat(countAfter).isEqualTo(countBefore);
    }

    @Test
    void savedSongShare_findsById(){

        songRepository.saveAndFlush(song);
        chatMemberRepository.saveAndFlush(chatMember);

        SongShareId songShareId = SongShareId.builder()
                .sharedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.timestampMS()), ZoneId.systemDefault()))
                .userId(chatMember.getId())
                .songId(song.getId()).build();

        SongShares songShares = SongShares.builder()
                .id(songShareId)
                .song(song)
                .user(chatMember)
                .build();

        songSharesRepository.save(songShares);

        Optional<SongShares> retrieved = songSharesRepository.findById(songShareId);
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getId()).isEqualTo(songShares.getId());
        assertThat(retrieved.get().getSong().getUrl()).isEqualTo(song.getUrl());
        assertThat(retrieved.get().getUser().getId()).isEqualTo(chatMember.getId());
    }

    @Test
    void savedSongShare_findByInvalidID_returnsEmptyOptional(){
        SongShareId songShareId = new SongShareId();
        assertThat(songSharesRepository.findById(songShareId)).isEqualTo(Optional.empty());
    }
}
