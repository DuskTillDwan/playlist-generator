package com.dusktildwan.playlistgenerator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class FacebookGroupChatScraperTest {

    @Autowired
    FacebookGroupChatScraper facebookGroupChatScraper;

    @Test
    void testScrapeAndExtractLinks() {
        // When: links are extracted from the scraped messages
        List<String> extractedLinks = facebookGroupChatScraper.scrapeAndExtractLinks();

        // Then: verify that the extracted links match the expected values
        assertEquals(3, extractedLinks.size());
        assertTrue(extractedLinks.contains("https://open.spotify.com/track/6pTtDlxT35UY4qi8yzrJFM?si=dab0bad787124c34"));
        assertTrue(extractedLinks.contains("https://youtu.be/oKmeO_QvAcs"));
        assertTrue(extractedLinks.contains("https://soundcloud.com/artist/song"));
        assertFalse(extractedLinks.contains("Random text with no links here"));
    }
}