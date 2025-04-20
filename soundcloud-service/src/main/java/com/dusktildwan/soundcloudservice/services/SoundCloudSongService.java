package com.dusktildwan.soundcloudservice.services;

import com.dusktildwan.common.DAL.entities.music.*;
import com.dusktildwan.common.DAL.repositories.music.PlatformRepository;
import com.dusktildwan.common.DAL.repositories.music.PlaylistRepository;
import com.dusktildwan.common.DAL.repositories.music.PlaylistSongsRepository;
import com.dusktildwan.common.DAL.repositories.music.SongRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SoundCloudSongService {

    private final SoundCloudAuthService soundCloudAuthService;

    private final SongRepository songRepository;

    private final PlaylistRepository playlistRepository;

    private final PlaylistSongsRepository playlistSongsRepository;

    private final PlatformRepository platformRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public HttpStatusCode addSongToPlaylist(String playlistId, String songUrl) throws RuntimeException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        String accessToken = soundCloudAuthService.getAccessToken();

        if (accessToken == null) {
            throw new RuntimeException("Access token is missing.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{ \"uris\": [\"" + buildSongUri(songUrl) + "\"] }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getStatusCode();
    }

    public HttpStatusCode loadSpotifySongInDatabaseToPlaylist(String playlistId) throws RuntimeException {
        Platform SPOTIFY = getSpotifyPLatform();
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        String accessToken = soundCloudAuthService.getAccessToken();

        //get all the songs
        if (accessToken == null) {
            throw new RuntimeException("Access token is missing.");
        }

        List<Song> listOfSongs = songRepository.findAllByPlatformId(SPOTIFY);
        List<String> allUris = getAllUris(listOfSongs);

        // Create or fetch the playlist
        Playlist mainPlaylist = playlistRepository
                .findByExternalIdAndPlatform(playlistId, SPOTIFY)
                .orElseGet(() -> playlistRepository.saveAndFlush(
                        Playlist.builder()
                                .name("Test Playlist")
                                .externalId(playlistId)
                                .platform(SPOTIFY)
                                .build())
                );

        log.info("Using Playlist: {}", mainPlaylist.getName());

        // Link songs to the playlist in the database
        for (Song song : listOfSongs) {
            PlaylistSongId psId = new PlaylistSongId(mainPlaylist.getId(), song.getId());
            if (!playlistSongsRepository.existsById(psId)) {
                PlaylistSong newPlaylistSong = PlaylistSong.builder()
                        .playlistSongId(psId)
                        .playlist(mainPlaylist)
                        .song(song)
                        .build();
                playlistSongsRepository.save(newPlaylistSong);
                log.info("Added new PlaylistSong: {}", song.getUrl());

                // Only add URI if it's not already processed
                String uri = buildSongUri(song.getUrl());
                if (!uri.equals("Not a Track")) {
                    allUris.add(uri);
                }
            } else {
                log.info("Skipped already-added song: {} for playlist {}", song.getUrl(), mainPlaylist.getName());
            }
        }

        // Set headers for Spotify API request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Batch and send songs in chunks of 100
        HttpStatusCode finalStatus = HttpStatus.OK;
        List<List<String>> batches = partition(allUris);


        for (int i = 0; i < batches.size(); i++) {
            List<String> batch = batches.get(i);
            JsonObject body = new JsonObject();
            JsonArray uriArray = new JsonArray();

            for (String uri : batch) {
                uriArray.add(uri);
            }

            body.add("uris", uriArray);
            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            finalStatus = response.getStatusCode();

            if (!finalStatus.is2xxSuccessful()) {
                log.warn("Batch {} failed: {}", i + 1, response.getBody());
            } else {
                log.info("Batch {} uploaded successfully", i + 1);
            }
        }

        return finalStatus;
    }

    private List<String> getAllUris(List<Song> listOfSongs) {
        List<String> allUris = new ArrayList<>();
        for (Song song : listOfSongs) {
            String uri = buildSongUri(song.getUrl());
            if (!uri.equals("Not a Track")) {
                allUris.add(uri);
            }
        }
        return allUris;
    }

    private String buildSongUri(String songUrl) {
        try {
            URI songUri = new URI(songUrl);
            String path = songUri.getPath();
            if (!path.startsWith("/track/")) {
                return "Not a Track";
            }
            String trackId = path.split("/")[2];// Extracts the track ID
            log.info("spotify:track:{}", trackId);
            return "spotify:track:" + trackId;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Platform getSpotifyPLatform(){
        return platformRepository.getReferenceById(1L);
    }

    // Helper method to partition a list into chunks
    private static <T> List<List<T>> partition(List<T> list) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 100) { //spotify accepts
            partitions.add(list.subList(i, Math.min(i + 100, list.size())));
        }
        return partitions;
    }

}

