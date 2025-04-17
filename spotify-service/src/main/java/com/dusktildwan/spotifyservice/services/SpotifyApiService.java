package com.dusktildwan.spotifyservice.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class SpotifyApiService {

    private final SpotifyAuthService spotifyAuthService;
    private final RestTemplate restTemplate = new RestTemplate();

    public SpotifyApiService(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }

    public HttpStatusCode addSongToPlaylist(String playlistId, String songUrl) throws RuntimeException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        String accessToken = spotifyAuthService.getAccessToken();

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
    public HttpStatusCode addListOfSongsToPlaylist(String playlistId, List<String> listOfSongs) throws RuntimeException {
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        String accessToken = spotifyAuthService.getAccessToken();

        if (accessToken == null) {
            throw new RuntimeException("Access token is missing.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(buildBulkLoadRequest(listOfSongs), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getStatusCode();
    }

    private String buildSongUri(String songUrl) {
        try {
            URI songUri = new URI(songUrl);
            String path = songUri.getPath();
            if (!path.startsWith("/track/")) {
                return "Not a Track";
            }
            String trackId = path.split("/")[2]; // Extracts the track ID
            return "spotify:track:" + trackId;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private String buildBulkLoadRequest(List<String> songUriList){
        JsonObject body = new JsonObject();
        JsonArray list = new JsonArray();

        for(String song : songUriList){
            list.add(buildSongUri(song));
        }

        body.add("uris", list);
        return body.toString();
    }
}

