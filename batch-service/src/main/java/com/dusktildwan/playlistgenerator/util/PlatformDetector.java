package com.dusktildwan.playlistgenerator.util;


public class PlatformDetector {
    public static String detectPlatform(String url) {
        if (url.contains("spotify.com") || url.contains("spotify.link")) {
            return PlatformType.SPOTIFY.name();
        } else if (url.contains("youtube.com") || url.contains("youtu.be")) {
            return PlatformType.YOUTUBE.name();
        } else if (url.contains("soundcloud.com")) {
            return PlatformType.SOUNDCLOUD.name();
        }
        return PlatformType.UNKNOWN.name();
    }
}

