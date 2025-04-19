package com.dusktildwan.spotifyservice.services;

import com.dusktildwan.spotifyservice.SpotifyConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static java.net.http.HttpClient.newHttpClient;

@Slf4j
@Service
public class SpotifyAuthService {

    final SpotifyConfig spotifyConfig;

    @Getter
    private String accessToken;

    public SpotifyAuthService(SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }

    public void exchangeCodeForToken(String authorizationCode) throws IOException, InterruptedException {
        // Prepare the token request URL and parameters
        // Create HTTP client and send the request to exchange the code for an access token
        HttpResponse<String> response;
        try (HttpClient client = newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(spotifyConfig.getTokenUrl()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(buildBodyForExchange(spotifyConfig, authorizationCode)))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }

        if (response.statusCode() == 200) {
            // Extract access token from response (you can use JSON parsing libraries like Jackson)
            accessToken = extractAccessToken(response.body());
        } else {
            throw new IOException("Failed to exchange code for access token: " + response.body());
        }
    }

    public String authTokenUrl(){
        return buildAuthorizeUrl(spotifyConfig);
    }

    private String extractAccessToken(String responseBody) {
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
        return jsonResponse.get("access_token").getAsString();
    }

    private String buildBodyForExchange(SpotifyConfig spotifyConfig, String authorizationCode) {
        return "grant_type=authorization_code&" +
                "code=" + authorizationCode + "&" +
                "redirect_uri=" + URLEncoder.encode(spotifyConfig.getRedirectUrl(), StandardCharsets.UTF_8) + "&" +
                "client_id=" + spotifyConfig.getId() + "&" +
                "client_secret=" + spotifyConfig.getSecret();
    }

    private String buildAuthorizeUrl(SpotifyConfig spotifyConfig) {
        return "https://accounts.spotify.com/authorize?" +
                "client_id=" + spotifyConfig.getId() + "&" +
                "response_type=code&" +
                "redirect_uri=" + URLEncoder.encode(spotifyConfig.getRedirectUrl(), StandardCharsets.UTF_8) + "&" +
                "scope=user-library-read user-library-modify playlist-modify-public playlist-modify-private";
    }
}

