package com.dusktildwan.playlistgenerator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class MessengerLinkParserTest {

    @Test
    void testExtractUrlFromMessengerLink() {
        // Given: a Messenger link with a valid Spotify URL in the 'u' parameter
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g";

        // When: the URL is extracted from the Messenger link
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);

        // Then: the extracted URL should be the correct URL
        assertNotNull(extractedUrl);
        assertEquals("https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34", extractedUrl);
    }

    @Test
    void testDecodeUrl() {
        // Given: a URL encoded Spotify link
        String encodedUrl = "https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34";

        // When: the URL is decoded
        String decodedUrl = MessengerLinkParser.decodeUrl(encodedUrl);

        // Then: the decoded URL should match the original Spotify URL
        assertEquals("https://open.spotify.com/track/6pTtDlxT35UY4qi8yzrJFM?si=dab0bad787124c34", decodedUrl);
    }

    @Test
    void testFullLinkParsing() {
        // Given: a full Messenger link with an encoded Spotify URL
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g";

        // When: the URL is extracted and decoded
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);
        String decodedUrl = MessengerLinkParser.decodeUrl(extractedUrl);

        // Then: the decoded URL should be the correct Spotify link
        assertEquals("https://open.spotify.com/track/6pTtDlxT35UY4qi8yzrJFM?si=dab0bad787124c34", decodedUrl);
    }

}