package com.dusktildwan.spotifyservice.controllers;

import com.dusktildwan.spotifyservice.dto.PlaylistRequest;
import com.dusktildwan.spotifyservice.dto.SpotifyPlaylist;
import com.dusktildwan.spotifyservice.services.SpotifyPlaylistService;
import com.dusktildwan.spotifyservice.services.SpotifySongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/spotify")
@RequiredArgsConstructor
public class SpotifySongController {

    private final SpotifySongService spotifySongService;
    private final SpotifyPlaylistService spotifyPlaylistService;

    @PutMapping("/add-song")
    public ResponseEntity<String> addSongToPlaylist(@RequestParam String playlistId, @RequestParam String songId) {
        try {
            HttpStatusCode response = spotifySongService.addSongToPlaylist(playlistId, songId);
            return new ResponseEntity<>("Song added successfully: " + response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add song: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/load-songs")
    public ResponseEntity<String> loadSongsToPlaylistById(@RequestParam String playlistId) {
        try {
            HttpStatusCode response = spotifySongService.loadSpotifySongInDatabaseToPlaylist(playlistId);
            return new ResponseEntity<>("Songs added successfully: " + response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add songs: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/load-songs-by-name")
    public ResponseEntity<String> loadSongsToPlaylistByName(@RequestBody PlaylistRequest request) {
        try {
            Optional<SpotifyPlaylist> playlist = spotifyPlaylistService.findPlaylistByName(request.getPlaylistName());
            
            SpotifyPlaylist targetPlaylist;
            
            if (playlist.isEmpty()) {
                if (request.isCreateIfNotExists()) {
                    // Create new playlist
                    targetPlaylist = spotifyPlaylistService.createPlaylist(
                        request.getPlaylistName(), 
                        request.getDescription(), 
                        request.isPublic()
                    );
                    log.info("Created new playlist: {}", targetPlaylist.getName());
                } else {
                    return new ResponseEntity<>(
                        "No matching playlist found for: " + request.getPlaylistName() + 
                        ". Set 'createIfNotExists' to true to auto-create.", 
                        HttpStatus.NOT_FOUND
                    );
                }
            } else {
                targetPlaylist = playlist.get();
                log.info("Found existing playlist: {}", targetPlaylist.getName());
            }
            
            HttpStatusCode response = spotifySongService.loadSpotifySongInDatabaseToPlaylist(targetPlaylist.getId());
            
            return new ResponseEntity<>(
                String.format("Songs added successfully to playlist '%s': %s", targetPlaylist.getName(), response), 
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add songs: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/playlists")
    public ResponseEntity<?> getVybeCheckPlaylists() {
        try {
            return new ResponseEntity<>(spotifyPlaylistService.getUserPlaylists(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch playlists: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/playlists")
    public ResponseEntity<String> createPlaylist(@RequestBody PlaylistRequest request) {
        try {
            SpotifyPlaylist newPlaylist = spotifyPlaylistService.createPlaylist(
                request.getPlaylistName(),
                request.getDescription(),
                request.isPublic()
            );
            
            return new ResponseEntity<>(
                String.format("Playlist '%s' created successfully with ID: %s", 
                    newPlaylist.getName(), newPlaylist.getId()), 
                HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create playlist: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


