package com.dusktildwan.playlistgenerator.util;

import com.dusktildwan.playlistgenerator.DAL.DTO.FacebookChat;
import com.dusktildwan.playlistgenerator.DAL.DTO.Message;
import com.dusktildwan.playlistgenerator.DAL.DTO.Participant;
import com.dusktildwan.playlistgenerator.DAL.DTO.SharedSong;

import java.util.List;

public class TestDataFactory {

    public static Message createSpotifyMessage(String sender) {
        return new Message(sender,
                System.currentTimeMillis(),
                "Spotify message content",
                new SharedSong("https://open.spotify.com/track/123"));
    }

    public static Message createYouTubeMessage(String sender) {
        return new Message(sender,
                System.currentTimeMillis(),
                "YouTube message content",
                new SharedSong("https://www.youtube.com/watch?v=abc123"));
    }

    public static Message createSoundCloudMessage(String sender) {
        return new Message(sender,
                System.currentTimeMillis(),
                "SoundCloud message content",
                new SharedSong("https://on.soundcloud.com/xyz"));
    }

    public static Message createUnknownPlatformMessage(String sender) {
        return new Message(sender,
                System.currentTimeMillis(),
                "Unknown platform",
                new SharedSong("https://bad.link.com/song"));
    }

    public static Message createNullSharedSongMessage(String sender) {
        return new Message(sender,
                System.currentTimeMillis(),
                "No shared song",
                null);
    }

    public static Message createEmptyUrlMessage(String sender) {
        return new Message(sender,
                System.currentTimeMillis(),
                "Empty share link",
                new SharedSong(null));
    }

    public static Participant createParticipant(String name) {
        return new Participant(name);
    }

    public static FacebookChat createFacebookChat(List<Participant> participants, List<Message> messages) {
        return new FacebookChat(participants, messages);
    }
}
