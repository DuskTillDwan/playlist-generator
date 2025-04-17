package com.dusktildwan.playlistgenerator.scraper;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class YouTubeLinkParser {

    private static final Pattern YOUTUBE_SHORT_URL_PATTERN =
            Pattern.compile("https://youtu.be/([a-zA-Z0-9_-]+)(\\?si=[^&]*)?");

    /**
     * Extracts the video ID from a YouTube shortened URL.
     *
     * @param youtubeLink the shortened YouTube link (https://youtu.be/<video_id>)
     * @return the video ID or null if the link is invalid.
     */
    public static String extractVideoId(String youtubeLink) {
        Matcher matcher = YOUTUBE_SHORT_URL_PATTERN.matcher(youtubeLink);
        if (matcher.matches()) {
            return matcher.group(1); // Extract the video ID from the first capturing group
        }
        return null; // Return null if the link doesn't match the pattern
    }
}
