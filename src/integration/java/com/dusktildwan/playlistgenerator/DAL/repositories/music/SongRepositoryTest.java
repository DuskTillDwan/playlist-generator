 package com.dusktildwan.playlistgenerator.DAL.repositories.music;

 import com.dusktildwan.playlistgenerator.DAL.entities.music.Platform;
 import com.dusktildwan.playlistgenerator.DAL.entities.music.Song;
 import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 import org.springframework.dao.DataIntegrityViolationException;
 import org.springframework.test.context.jdbc.Sql;

 import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
 import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

 @DataJpaTest
 @AutoConfigureEmbeddedDatabase
 @Sql("/scripts/test-db.sql")
 public class SongRepositoryTest {
     @Autowired
     SongRepository songRepository;

     Platform spotify = Platform.builder().id(1L).name("SPOTIFY").build();

     Song song = Song.builder().platformId(spotify).url("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt").build();

     @Test
     void savesSong_Successfully(){
         Song savedSong = songRepository.saveAndFlush(song);

         assertThat(savedSong).isEqualTo(song);
     }

     @Test
     void savesSong_withNullUrl_throwsDataIntegrityViolation(){
         song.setUrl(null);

         assertThatThrownBy(() -> songRepository.saveAndFlush(song))
                 .isInstanceOf(DataIntegrityViolationException.class)
                 .hasMessageContaining("not-null property references a null or transient value");
     }

     @Test
     void findByURL_returnSong(){
        songRepository.saveAndFlush(song);

        assertThat(songRepository.findSongByUrl(song.getUrl())).isEqualTo(song);
     }

     @Test
     void findByURL_InvalidUrl_fails(){
        songRepository.saveAndFlush(song);

        assertThat(songRepository.findSongByUrl("sometrashurl.com/blahblahblah")).isEqualTo(null);
     }

     @Test
     void existsByUrl_forSavedSong_returnsTrue(){
        songRepository.saveAndFlush(song);

        assertThat(songRepository.existsByUrl(song.getUrl())).isTrue();
     }

     @Test
     void existsByUrl_whenUrlDoesNotExist_returnsFalse(){
        songRepository.saveAndFlush(song);

        assertThat(songRepository.existsByUrl("sometrashurl.com/blahblahblah")).isFalse();
     }

 }
