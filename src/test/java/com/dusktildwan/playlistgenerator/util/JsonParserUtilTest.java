package com.dusktildwan.playlistgenerator.util;

import com.dusktildwan.playlistgenerator.DAL.DTO.FacebookChat;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserUtilTest {

    @Test
    void parseFacebookChat_parsesMultipartFile() throws IOException {
    Path path = Paths.get("src/test/java/resources/test_chat.json");
    byte[] jsonBytes = Files.readAllBytes(path);

    MockMultipartFile mockFile = new MockMultipartFile(
            "file",
            "test_chat.json",
            "application/json",
            jsonBytes
    );
        // Parse the JSON file using JsonParserUtil
        FacebookChat facebookChat = JsonParserUtil.parseFacebookChat(mockFile);

        // Assertions to verify the parsed data
        assertNotNull(facebookChat);
        assertEquals(3, facebookChat.participants().size());
        assertEquals("Adam Savage", facebookChat.participants().getFirst().name());
        assertEquals("Halo Infinite", facebookChat.participants().get(1).name());
        assertEquals("Larry Chapman", facebookChat.participants().get(2).name());

        assertEquals(2, facebookChat.messages().size());
        assertEquals("Halo Infinite", facebookChat.messages().getFirst().senderName());
        assertEquals("https://open.spotify.com/track/2uvE4L5ZsYKpv8hbK4TIOt?si=9UoPAaOXSAOlVUbZrlkMEA&context=spotify%3Aalbum%3A0SzoksypeognxYJJOJEYip",
                facebookChat.messages().getFirst().share().link());
    }

    @Test
    void testParseFacebookChat_InvalidJson_ThrowsException() {
        // Invalid JSON
        String invalidJson = "{ \"participants\": [ { \"name\": \"Adam Savage\" }], \"messages\": [ }";

        // Create a mock MultipartFile with invalid JSON content
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test_chat.json",
                "application/json",
                invalidJson.getBytes()
        );

        // Expect JsonSyntaxException due to invalid JSON
        assertThrows(JsonSyntaxException.class, () -> JsonParserUtil.parseFacebookChat(mockFile));
    }

}
