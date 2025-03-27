package com.dusktildwan.playlistgenerator.scraper;

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
    void testExtractSpotifyUrlFromMessengerLink() {
        // Given: a Messenger link with a valid Spotify URL in the 'u' parameter
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g";

        // When: the URL is extracted from the Messenger link
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);

        // Then: the extracted URL should be the correct URL
        assertNotNull(extractedUrl);
        assertEquals("https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34", extractedUrl);
    }

    @Test
    void testDecodeSpotifyUrl() {
        // Given: a URL encoded Spotify link
        String encodedUrl = "https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34";

        // When: the URL is decoded
        String decodedUrl = MessengerLinkParser.decodeUrl(encodedUrl);

        // Then: the decoded URL should match the original Spotify URL
        assertEquals("https://open.spotify.com/track/6pTtDlxT35UY4qi8yzrJFM?si=dab0bad787124c34", decodedUrl);
    }

    @Test
    void testFullSpotifyLinkParsing() {
        // Given: a full Messenger link with an encoded Spotify URL
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fopen.spotify.com%2Ftrack%2F6pTtDlxT35UY4qi8yzrJFM%3Fsi%3Ddab0bad787124c34&h=AT0V7TsE-iqaaqscrGA0wrlr_JL7exwytUEH8YNTddXE81OasgCcTDgVfxc4qgNFdhrBnCTcW-RIl1nVsdqFtkCfBB42xSqjgdJdjR_KrY8whR6ixwZPhIQUcJkCmHW3T0e91g";

        // When: the URL is extracted and decoded
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);
        String decodedUrl = MessengerLinkParser.decodeUrl(extractedUrl);

        // Then: the decoded URL should be the correct Spotify link
        assertEquals("https://open.spotify.com/track/6pTtDlxT35UY4qi8yzrJFM?si=dab0bad787124c34", decodedUrl);
    }

    @Test
    void testExtractSoundCloudUrlFromMessengerLink() {
        // Given: a Messenger link with a valid SoundCloud URL in the 'u' parameter
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fsoundcloud.com%2Flynyofficial%2Fbasscannon%3Fsi%3D569fd6a5006e4e27b93f3fcf8fd58f04%26utm_source%3Dclipboard%26utm_medium%3Dtext%26utm_campaign%3Dsocial_sharing&h=AT1LkJ68J0ZPwNzVTMTbOm7kjGZcWzisKAHw5T5m9a_1h7_Ydy5sivuepcUbEHKqxW9FCy6YyegcyYNAv3VEiCHn04boj-QG3A1-uwAh9Sg_conp3N_8qjQs2-qF-Jji9dOhUyPKG5VJv3M";

        // When: the URL is extracted from the Messenger link
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);

        // Then: the extracted URL should be the correct URL
        assertNotNull(extractedUrl);
        assertEquals("https%3A%2F%2Fsoundcloud.com%2Flynyofficial%2Fbasscannon%3Fsi%3D569fd6a5006e4e27b93f3fcf8fd58f04%26utm_source%3Dclipboard%26utm_medium%3Dtext%26utm_campaign%3Dsocial_sharing", extractedUrl);
    }

    @Test
    void testDecodeSoundCloudUrl() {
        // Given: a URL encoded SoundCloud link
        String encodedUrl = "https%3A%2F%2Fsoundcloud.com%2Flynyofficial%2Fbasscannon%3Fsi%3D569fd6a5006e4e27b93f3fcf8fd58f04%26utm_source%3Dclipboard%26utm_medium%3Dtext%26utm_campaign%3Dsocial_sharing";

        // When: the URL is decoded
        String decodedUrl = MessengerLinkParser.decodeUrl(encodedUrl);

        // Then: the decoded URL should match the original SoundCloud URL
        assertEquals("https://soundcloud.com/lynyofficial/basscannon?si=569fd6a5006e4e27b93f3fcf8fd58f04&utm_source=clipboard&utm_medium=text&utm_campaign=social_sharing", decodedUrl);
    }

    @Test
    void testFullSoundCloudLinkParsing() {
        // Given: a full Messenger link with an encoded SoundCloud URL
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fsoundcloud.com%2Flynyofficial%2Fbasscannon%3Fsi%3D569fd6a5006e4e27b93f3fcf8fd58f04%26utm_source%3Dclipboard%26utm_medium%3Dtext%26utm_campaign%3Dsocial_sharing&h=AT1LkJ68J0ZPwNzVTMTbOm7kjGZcWzisKAHw5T5m9a_1h7_Ydy5sivuepcUbEHKqxW9FCy6YyegcyYNAv3VEiCHn04boj-QG3A1-uwAh9Sg_conp3N_8qjQs2-qF-Jji9dOhUyPKG5VJv3M";

        // When: the URL is extracted and decoded
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);
        String decodedUrl = MessengerLinkParser.decodeUrl(extractedUrl);

        // Then: the decoded URL should be the correct Spotify link
        assertEquals("https://soundcloud.com/lynyofficial/basscannon?si=569fd6a5006e4e27b93f3fcf8fd58f04&utm_source=clipboard&utm_medium=text&utm_campaign=social_sharing", decodedUrl);
    }


    @Test
    void testExtractYouTubeUrlFromMessengerLink() {
        // Given: a Messenger link with a valid YouTube URL in the 'u' parameter
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DXp8e6juEgsc&h=AT2pZ7tF8IBYHkW0O-Ln7jydOA7h1oC59ixwm9dbzX3XWtnPyjw-IJbA5_8WfPwENj4h1qJrIy1a-XlIUZs0DJbUFiJojtMxWzpZZgzGL0OMsG35klIgQ91B-DskIqkcXTk8gQ8U1rNhwbM";

        // When: the URL is extracted from the Messenger link
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);

        // Then: the extracted URL should be the correct URL
        assertNotNull(extractedUrl);
        assertEquals("https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DXp8e6juEgsc", extractedUrl);
    }

    @Test
    void testDecodeYouTubeUrl() {
        // Given: a URL encoded YouTube link
        String encodedUrl = "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DXp8e6juEgsc&h=AT2pZ7tF8IBYHkW0O-Ln7jydOA7h1oC59ixwm9dbzX3XWtnPyjw-IJbA5_8WfPwENj4h1qJrIy1a-XlIUZs0DJbUFiJojtMxWzpZZgzGL0OMsG35klIgQ91B-DskIqkcXTk8gQ8U1rNhwbM";

        // When: the URL is decoded
        String decodedUrl = MessengerLinkParser.decodeUrl(encodedUrl);

        // Then: the decoded URL should match the original YouTube URL
        assertEquals("https://www.youtube.com/watch?v=Xp8e6juEgsc&h=AT2pZ7tF8IBYHkW0O-Ln7jydOA7h1oC59ixwm9dbzX3XWtnPyjw-IJbA5_8WfPwENj4h1qJrIy1a-XlIUZs0DJbUFiJojtMxWzpZZgzGL0OMsG35klIgQ91B-DskIqkcXTk8gQ8U1rNhwbM", decodedUrl);
    }

    @Test
    void testFullYouTubeLinkParsing() {
        // Given: a full Messenger link with an encoded YouTube URL
        String messengerLink = "https://l.messenger.com/l.php?u=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DXp8e6juEgsc&h=AT2pZ7tF8IBYHkW0O-Ln7jydOA7h1oC59ixwm9dbzX3XWtnPyjw-IJbA5_8WfPwENj4h1qJrIy1a-XlIUZs0DJbUFiJojtMxWzpZZgzGL0OMsG35klIgQ91B-DskIqkcXTk8gQ8U1rNhwbM";

        // When: the URL is extracted and decoded
        String extractedUrl = MessengerLinkParser.extractUrlFromMessengerLink(messengerLink);
        String decodedUrl = MessengerLinkParser.decodeUrl(extractedUrl);

        // Then: the decoded URL should be the correct Spotify link
        assertEquals("https://www.youtube.com/watch?v=Xp8e6juEgsc", decodedUrl);
    }

}