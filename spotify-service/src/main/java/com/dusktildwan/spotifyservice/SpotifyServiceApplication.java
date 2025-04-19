package com.dusktildwan.spotifyservice;

import com.dusktildwan.spotifyservice.config.JpaConfig;
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
public class SpotifyServiceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SpotifyServiceApplication.class, args);
        openSpotifyLoginPage(applicationContext.getBean(SpotifyConfig.class));
	}

	private static void openSpotifyLoginPage(SpotifyConfig spotifyConfig) {

		String loginUrl = spotifyConfig.getLoginUrl();

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
