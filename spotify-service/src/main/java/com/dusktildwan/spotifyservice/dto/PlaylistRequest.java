package com.dusktildwan.spotifyservice.dto;

import lombok.Data;

@Data
public class PlaylistRequest {
    private String playlistName;
    private boolean createIfNotExists = false; // Default to false for safety
    private String description = ""; // Optional description for new playlists
    private boolean isPublic = true;
}
