package com.dusktildwan.batch.util;

import org.junit.jupiter.api.Test;

import static com.dusktildwan.batch.util.PlatformDetector.detectPlatform;
import static org.junit.jupiter.api.Assertions.*;

class PlatformDetectorTest {

    @Test
    void detectsSpotifyFromSpotifyComUrl() {
        assertEquals("SPOTIFY", detectPlatform("https://open.spotify.com/track/123"));
    }

    @Test
    void detectsSpotifyFromSpotifyLinkUrl() {
        assertEquals("SPOTIFY", detectPlatform("https://spotify.link/some-id"));
    }

    @Test
    void detectsYoutubeFromYoutubeComUrl() {
        assertEquals("YOUTUBE", detectPlatform("https://www.youtube.com/watch?v=abc"));
    }

    @Test
    void detectsYoutubeFromYoutubeUrl() {
        assertEquals("YOUTUBE", detectPlatform("https://youtu.be/xyz"));
    }

    @Test
    void detectsSoundCloudFromUrl() {
        assertEquals("SOUNDCLOUD", detectPlatform("https://soundcloud.com/artist/track"));
    }

    @Test
    void returnsUnknownForUnsupportedUrl() {
        assertEquals("UNKNOWN", detectPlatform("https://example.com/whatever"));
    }

}