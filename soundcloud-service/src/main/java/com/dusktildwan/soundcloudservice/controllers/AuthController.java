package com.dusktildwan.soundcloudservice.controllers;

import com.dusktildwan.soundcloudservice.services.SoundCloudAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/soundcloud")
public class AuthController {

    private final SoundCloudAuthService soundCloudAuthService;

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect(soundCloudAuthService.authTokenUrl());
    }

    @GetMapping("/callback")
    public String handleRedirect(@RequestParam("code") String code) {
        // Now exchange the authorization code for an access token
        try {
            soundCloudAuthService.exchangeCodeForToken(code);
            return ("Token received: "+ soundCloudAuthService.getAccessToken());  // Or redirect to another page or handle it as needed
        } catch (Exception e) {
            return "Error exchanging code for access token: " + e.getMessage();
        }
    }

}


