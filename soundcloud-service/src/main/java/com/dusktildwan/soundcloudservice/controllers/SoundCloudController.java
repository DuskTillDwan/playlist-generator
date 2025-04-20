package com.dusktildwan.soundcloudservice.controllers;

import com.dusktildwan.soundcloudservice.services.SoundCloudSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/soundcloud")
@RequiredArgsConstructor
public class SoundCloudController {

    private final SoundCloudSongService soundCloudSongService;

    @PutMapping("/add-song")
    public ResponseEntity<String> addSongToPlaylist(@RequestParam String playlistId, @RequestParam String songId) {
        try {
            HttpStatusCode response = soundCloudSongService.addSongToPlaylist(playlistId, songId);
            return new ResponseEntity<>("Song added successfully: " + response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add song: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/load-songs")
    public ResponseEntity<String> addSongToPlaylist(@RequestParam String playlistId) {
        try {
            HttpStatusCode response = soundCloudSongService.loadSpotifySongInDatabaseToPlaylist(playlistId);
            return new ResponseEntity<>("Songs added successfully: " + response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add songs: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


