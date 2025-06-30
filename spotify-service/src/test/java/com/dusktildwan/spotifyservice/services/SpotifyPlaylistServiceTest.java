package com.dusktildwan.spotifyservice.services;

import com.dusktildwan.spotifyservice.dto.SpotifyPlaylist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotifyPlaylistServiceTest {

    @Mock
    private SpotifyAuthService spotifyAuthService;
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private SpotifyPlaylistService spotifyPlaylistService;

    @Test
    void testSimilarityCalculation() {
        double exactMatch = SpotifyPlaylist.calculateSimilarity("test", "test");
        assertThat(exactMatch).isEqualTo(1.0);
    }

    @Test
    void testPartialMatch() {
        double partialMatch = SpotifyPlaylist.calculateSimilarity("testing", "test");
        assertThat(partialMatch).isGreaterThan(0.5);
    }

    @Test
    void testNoMatch() {
        double noMatch = SpotifyPlaylist.calculateSimilarity("completely different", "test");
        assertThat(noMatch).isLessThan(0.5);
    }

    @Test
    void testFindPlaylistByName_MockResponse() {
        // Mock the auth service
        when(spotifyAuthService.getAccessToken()).thenReturn("mock-token");
        
        // Mock the Spotify API response for getUserPlaylists call
        String mockResponse = """
            {
                "items": [
                    {
                        "id": "123",
                        "name": "Vybe Check: My Awesome Playlist",
                        "description": "Test playlist",
                        "collaborative": false,
                        "public": true
                    },
                    {
                        "id": "456",
                        "name": "Vybe Check: Another Playlist",
                        "description": "Another test",
                        "collaborative": false,
                        "public": false
                    }
                ],
                "next": null
            }
            """;
        
        // Mock the specific URL that getUserPlaylists() calls
        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/me/playlists?limit=50"), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));
        
        // Test finding a playlist
        Optional<SpotifyPlaylist> result = spotifyPlaylistService.findPlaylistByName("My Awesome Playlist");
        
        // Debug: Print the result to see what we got
        System.out.println("Result present: " + result.isPresent());
        result.ifPresent(spotifyPlaylist -> System.out.println("Found playlist: " + spotifyPlaylist.getName()));
        
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Vybe Check: My Awesome Playlist");
        assertThat(result.get().getId()).isEqualTo("123");
    }

    @Test
    void testFindPlaylistByName_NoMatch() {
        // Mock the auth service
        when(spotifyAuthService.getAccessToken()).thenReturn("mock-token");
        
        // Mock response with no matching playlists
        String mockResponse = """
            {
                "items": [
                    {
                        "id": "123",
                        "name": "Vybe Check: My Awesome Playlist",
                        "description": "Test playlist",
                        "collaborative": false,
                        "public": true
                    }
                ],
                "next": null
            }
            """;
        
        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/me/playlists?limit=50"), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));
        
        // Test finding a playlist that doesn't match
        Optional<SpotifyPlaylist> result = spotifyPlaylistService.findPlaylistByName("Completely Different Name");
        
        assertThat(result).isEmpty();
    }

    @Test
    void testFindPlaylistByName_ExactMatch() {
        // Mock the auth service
        when(spotifyAuthService.getAccessToken()).thenReturn("mock-token");
        
        String mockResponse = """
            {
                "items": [
                    {
                        "id": "123",
                        "name": "Vybe Check: My Awesome Playlist",
                        "description": "Test playlist",
                        "collaborative": false,
                        "public": true
                    }
                ],
                "next": null
            }
            """;
        
        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/me/playlists?limit=50"), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));
        
        // Test exact match (including prefix)
        Optional<SpotifyPlaylist> result = spotifyPlaylistService.findPlaylistByName("Vybe Check: My Awesome Playlist");
        
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Vybe Check: My Awesome Playlist");
        assertThat(result.get().getId()).isEqualTo("123");
    }

    @Test
    void testFindPlaylistByName_PartialMatch() {
        // Mock the auth service
        when(spotifyAuthService.getAccessToken()).thenReturn("mock-token");
        
        String mockResponse = """
            {
                "items": [
                    {
                        "id": "123",
                        "name": "Vybe Check: My Awesome Playlist",
                        "description": "Test playlist",
                        "collaborative": false,
                        "public": true
                    }
                ],
                "next": null
            }
            """;
        
        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/me/playlists?limit=50"), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));
        
        // Test partial match
        Optional<SpotifyPlaylist> result = spotifyPlaylistService.findPlaylistByName("awesome playlist");
        
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Vybe Check: My Awesome Playlist");
    }
}
