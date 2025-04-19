package com.dusktildwan.spotifyservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spotify.client")
public class SpotifyConfig{
    private String id;
    private String secret;
    private String refreshToken;
    private String redirectUrl;
    private String tokenUrl;
    private String loginUrl;

}

