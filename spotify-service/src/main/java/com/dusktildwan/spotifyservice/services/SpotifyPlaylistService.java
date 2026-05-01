package com.dusktildwan.spotifyservice.services;

import com.dusktildwan.spotifyservice.dto.SpotifyPlaylist;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyPlaylistService {

    private final SpotifyAuthService spotifyAuthService;
    private final RestTemplate restTemplate;
    
    private static final String PLAYLIST_PREFIX = "Vybe Check:";
    private static final double MINIMUM_SIMILARITY_THRESHOLD = 0.6;

    public List<SpotifyPlaylist> getUserPlaylists() {
        String accessToken = spotifyAuthService.getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("Access token is missing.");
        }

        List<SpotifyPlaylist> allPlaylists = new ArrayList<>();
        String url = "https://api.spotify.com/v1/me/playlists?limit=50";
        
        while (url != null) {
            ResponseEntity<String> response = makeSpotifyRequest(url, accessToken);
            assert response.getBody() != null;
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
            
            JsonArray items = jsonResponse.getAsJsonArray("items");
            for (JsonElement item : items) {
                JsonObject playlist = item.getAsJsonObject();
                
                String playlistName = playlist.get("name").getAsString();
                
                // Filter for "Vybe Check:" prefix
                if (playlistName.startsWith(PLAYLIST_PREFIX)) {
                    SpotifyPlaylist spotifyPlaylist = SpotifyPlaylist.builder()
                            .id(playlist.get("id").getAsString())
                            .name(playlistName)
                            .description(playlist.has("description") && !playlist.get("description").isJsonNull() 
                                ? playlist.get("description").getAsString() : "")
                            .collaborative(playlist.get("collaborative").getAsBoolean())
                            .publicPlaylist(playlist.get("public").getAsBoolean())
                            .build();
                    
                    allPlaylists.add(spotifyPlaylist);
                }
            }
            
            // Check for next page
            url = jsonResponse.has("next") && !jsonResponse.get("next").isJsonNull() 
                ? jsonResponse.get("next").getAsString() : null;
        }
        
        log.info("Found {} Vybe Check playlists", allPlaylists.size());
        return allPlaylists;
    }

    public Optional<SpotifyPlaylist> findPlaylistByName(String searchName) {
        List<SpotifyPlaylist> playlists = getUserPlaylists();
        
        // Remove "Vybe Check:" prefix from search if present for comparison
        String cleanSearchName = searchName.startsWith(PLAYLIST_PREFIX) 
            ? searchName.substring(PLAYLIST_PREFIX.length()).trim()
            : searchName;
        
        return playlists.stream()
                .filter(playlist -> {
                    // Remove prefix from playlist name for comparison
                    String cleanPlaylistName = playlist.getName().substring(PLAYLIST_PREFIX.length()).trim();
                    double similarity = SpotifyPlaylist.calculateSimilarity(cleanPlaylistName.toLowerCase(), cleanSearchName.toLowerCase());
                    log.debug("Comparing '{}' with '{}', similarity: {}", cleanPlaylistName, cleanSearchName, similarity);
                    return similarity >= MINIMUM_SIMILARITY_THRESHOLD;
                })
                .max((p1, p2) -> {
                    String clean1 = p1.getName().substring(PLAYLIST_PREFIX.length()).trim();
                    String clean2 = p2.getName().substring(PLAYLIST_PREFIX.length()).trim();
                    double sim1 = SpotifyPlaylist.calculateSimilarity(clean1.toLowerCase(), cleanSearchName.toLowerCase());
                    double sim2 = SpotifyPlaylist.calculateSimilarity(clean2.toLowerCase(), cleanSearchName.toLowerCase());
                    return Double.compare(sim1, sim2);
                });
    }

    public SpotifyPlaylist createPlaylist(String playlistName, String description, boolean isPublic) {
        String accessToken = spotifyAuthService.getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("Access token is missing.");
        }

        // Ensure the playlist name has the "Vybe Check:" prefix
        String fullPlaylistName = playlistName.startsWith(PLAYLIST_PREFIX) 
            ? playlistName 
            : PLAYLIST_PREFIX + " " + playlistName;

        // Get user ID first
        String userId = getCurrentUserId(accessToken);
        
        // Create playlist
        String url = "https://api.spotify.com/v1/users/" + userId + "/playlists";
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", fullPlaylistName);
        requestBody.addProperty("description", description.isEmpty() 
            ? "Generated by Vybe Check Playlist Generator" 
            : description);
        requestBody.addProperty("public", isPublic);
        requestBody.addProperty("collaborative", false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to create playlist: " + response.getBody());
        }

        assert response.getBody() != null;
        JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
        
        SpotifyPlaylist newPlaylist = SpotifyPlaylist.builder()
                .id(jsonResponse.get("id").getAsString())
                .name(jsonResponse.get("name").getAsString())
                .description(jsonResponse.has("description") && !jsonResponse.get("description").isJsonNull() 
                    ? jsonResponse.get("description").getAsString() : "")
                .collaborative(jsonResponse.get("collaborative").getAsBoolean())
                .publicPlaylist(jsonResponse.get("public").getAsBoolean())
                .build();

        log.info("Created new playlist: {} with ID: {}", newPlaylist.getName(), newPlaylist.getId());
        return newPlaylist;
    }

    private String getCurrentUserId(String accessToken) {
        String url = "https://api.spotify.com/v1/me";
        ResponseEntity<String> response = makeSpotifyRequest(url, accessToken);
        assert response.getBody() != null;
        JsonObject userInfo = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return userInfo.get("id").getAsString();
    }

    private ResponseEntity<String> makeSpotifyRequest(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }
}
