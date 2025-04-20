package com.dusktildwan.soundcloudservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "soundcloud.client")
public class SoundCloudConfig {
    private String id;
    private String secret;
    private String refreshToken;
    private String redirectUrl;
    private String tokenUrl;
    private String loginUrl;

}

