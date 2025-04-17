package com.dusktildwan.spotifyservice.controllers;

import com.dusktildwan.spotifyservice.services.SpotifyAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class AuthController {

    private final SpotifyAuthService spotifyAuthService;

    public AuthController(SpotifyAuthService spotifyAuthService) {
        this.spotifyAuthService = spotifyAuthService;
    }
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect(spotifyAuthService.authTokenUrl());
    }

    @GetMapping("/callback")
    public String handleRedirect(@RequestParam("code") String code) {
        // Now exchange the authorization code for an access token
        try {
            spotifyAuthService.exchangeCodeForToken(code);
            return ("Token received: "+ spotifyAuthService.getAccessToken());  // Or redirect to another page or handle it as needed
        } catch (Exception e) {
            return "Error exchanging code for access token: " + e.getMessage();
        }
    }

}


