package com.dusktildwan.batch.util;


public class PlatformDetector {
    public static PlatformType detectPlatform(String url) {
        if (url.contains("spotify.com") || url.contains("spotify.link")) {
            return PlatformType.SPOTIFY;
        } else if (url.contains("youtube.com") || url.contains("youtu.be")) {
            return PlatformType.YOUTUBE;
        } else if (url.contains("soundcloud.com")) {
            return PlatformType.SOUNDCLOUD;
        }
        return PlatformType.UNKNOWN;
    }
}

