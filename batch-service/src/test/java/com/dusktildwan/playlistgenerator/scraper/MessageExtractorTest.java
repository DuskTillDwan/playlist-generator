package com.dusktildwan.playlistgenerator.scraper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageExtractorTest {

    @Test
    public void testExtractSongLinks() {
        String message = "Hey, check this song! https://open.spotify.com/track/12345 and also this https://soundcloud.com/artist/song";

        List<String> links = MessageExtractor.extractLinks(message);

        assertEquals(2, links.size());
        assertEquals("https://open.spotify.com/track/12345", links.get(0));
        assertEquals("https://soundcloud.com/artist/song", links.get(1));

    }

    @Test
    public void testNoLinksInMessage() {
        String message = "Just chatting, no links here!";
        List<String> links = MessageExtractor.extractLinks(message);

        assertTrue(links.isEmpty());
    }

    @Test
    public void testExtractYouTubeLinks() {
        String message = "Watch this! https://www.youtube.com/watch?v=abcdef";
        List<String> links = MessageExtractor.extractLinks(message);

        assertEquals(1, links.size());
        assertEquals("https://www.youtube.com/watch?v=abcdef", links.getFirst());
    }
}