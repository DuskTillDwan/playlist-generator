package com.dusktildwan.spotifyservice.controllers;

import com.dusktildwan.spotifyservice.services.SpotifyApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyApiService spotifyApiService;

    @PutMapping("/add-song")
    public ResponseEntity<String> addSongToPlaylist(@RequestParam String playlistId, @RequestParam String songId) {
        try {
            HttpStatusCode response = spotifyApiService.addSongToPlaylist(playlistId, songId);
            return new ResponseEntity<>("Song added successfully: " + response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add song: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/load-songs")
    public ResponseEntity<String> addSongToPlaylist(@RequestParam String playlistId) {
        try {
            HttpStatusCode response = spotifyApiService.loadSpotifySongInDatabaseToPlaylist(playlistId);
            return new ResponseEntity<>("Songs added successfully: " + response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add songs: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


