package com.dusktildwan.playlistgenerator.scraper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YouTubeLinkParserTest {

    @Test
    void testExtractVideoIdFromShortenedUrl() {
        // Given: a YouTube shortened URL
        String youtubeLink = "https://youtu.be/oKmeO_QvAcs?si=EX8aOkAhQ0XXbTny";

        // When: the video ID is extracted
        String videoId = YouTubeLinkParser.extractVideoId(youtubeLink);

        // Then: the extracted video ID should match the expected value
        assertNotNull(videoId);
        assertEquals("oKmeO_QvAcs", videoId);
    }

    @Test
    void testExtractVideoIdFromUrlWithoutSiParam() {
        // Given: a YouTube shortened URL without the 'si' query parameter
        String youtubeLink = "https://youtu.be/oKmeO_QvAcs";

        // When: the video ID is extracted
        String videoId = YouTubeLinkParser.extractVideoId(youtubeLink);

        // Then: the extracted video ID should match the expected value
        assertNotNull(videoId);
        assertEquals("oKmeO_QvAcs", videoId);
    }

    @Test
    void testInvalidYouTubeLink() {
        // Given: an invalid YouTube shortened URL
        String invalidLink = "https://youtube.com/oKmeO_QvAcs";

        // When: attempting to extract the video ID
        String videoId = YouTubeLinkParser.extractVideoId(invalidLink);

        // Then: the result should be null
        assertNull(videoId);
    }
}
