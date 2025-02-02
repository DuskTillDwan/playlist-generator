package com.dusktildwan.playlistgenerator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class MessageExtractorTest {

    @Autowired
    private MessageExtractor messageExtractor;

    @Test
    public void testExtractSongLinks() {
        String message = "Hey, check this song! https://open.spotify.com/track/12345 and also this https://soundcloud.com/artist/song";

        List<String> links = messageExtractor.extractLinks(message);

        assertEquals(2, links.size());
        assertEquals("https://open.spotify.com/track/12345", links.get(0));
        assertEquals("https://soundcloud.com/artist/song", links.get(1));

    }

    @Test
    public void testNoLinksInMessage() {
        String message = "Just chatting, no links here!";
        List<String> links = messageExtractor.extractLinks(message);

        assertTrue(links.isEmpty());
    }

    @Test
    public void testExtractYouTubeLinks() {
        String message = "Watch this! https://www.youtube.com/watch?v=abcdef";
        List<String> links = messageExtractor.extractLinks(message);

        assertEquals(1, links.size());
        assertEquals("https://www.youtube.com/watch?v=abcdef", links.getFirst());
    }

    @Test
    public void testExtractYouTubeShareLinks() {
        String message = "Watch this! https://youtu.be/AkT-jsmbmbc?si=8PKQiGCZ1ojMSJtE";
        List<String> links = messageExtractor.extractLinks(message);

        assertEquals(1, links.size());
        assertEquals("https://youtu.be/AkT-jsmbmbc?si=8PKQiGCZ1ojMSJtE", links.getFirst());
    }

    @Test
    public void testExtractSpotifySharedLinks() {
        String message = "Check out this song on Spotify: https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g";
        List<String> links = messageExtractor.extractLinks(message);

        assertEquals(1, links.size());
        assertEquals("Check out this song on Spotify: https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g", links.getFirst());
    }
}