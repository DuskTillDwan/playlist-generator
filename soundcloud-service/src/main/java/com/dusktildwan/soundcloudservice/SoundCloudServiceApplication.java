package com.dusktildwan.soundcloudservice;

import com.dusktildwan.soundcloudservice.config.JpaConfig;
import com.dusktildwan.soundcloudservice.config.SoundCloudConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@SpringBootApplication
@Import(JpaConfig.class)
public class SoundCloudServiceApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SoundCloudServiceApplication.class, args);
        openSoundCloudLoginPage(applicationContext.getBean(SoundCloudConfig.class));
    }

    private static void openSoundCloudLoginPage(SoundCloudConfig soundCloudConfig) {

        String loginUrl = soundCloudConfig.getLoginUrl();

        // Open in default browser
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(loginUrl));
            } else {
                System.out.println("Desktop browsing not supported. Open this URL manually: " + loginUrl);
            }
        } catch (IOException | URISyntaxException e) {
            log.error("Unable to open URL {} with following error:", loginUrl, e);
        }
    }
}
