package com.dusktildwan.batch.controllers;

import com.dusktildwan.batch.services.UploadService;
import com.dusktildwan.batch.util.JsonParserUtil;
import com.dusktildwan.common.DAL.DTO.FacebookChat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.dusktildwan.batch.util.TestDataFactory.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({OutputCaptureExtension.class})
@ContextConfiguration(classes = { UploadController.class })
@WebMvcTest(UploadController.class)
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    UploadService uploadService;

    private MockMultipartFile mockValidFile;

    @BeforeEach
    void setup() throws IOException {
        Path path = Paths.get("src/test/resources/test_chat.json");
        byte[] jsonBytes = Files.readAllBytes(path);

        mockValidFile = new MockMultipartFile(
                "file",
                "test_chat.json",
                "application/json",
                jsonBytes
        );

    }

    @Test
    void uploadJsonFile_Returns400_WhenFileIsEmpty(CapturedOutput capturedOutput) throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.json", "application/json", new byte[0]);

        mockMvc.perform(multipart("/upload").file(emptyFile))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(uploadService);
        assertThat(capturedOutput.getOut()).contains("FILE IS EMPTY");
    }

    @Test
    void uploadInvalidFile_Returns400_FileIsInvalid(CapturedOutput capturedOutput) throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.txt", "application/text", new byte[1]);

        mockMvc.perform(multipart("/upload").file(emptyFile))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(uploadService);
        assertThat(capturedOutput.getOut()).contains("FILE IS NOT JSON");
    }

    @Test
    void uploadJsonFile_Returns201_WhenFileIsValid() throws Exception {
        FacebookChat dummyChat = createFacebookChat(
                List.of(createParticipant("Susan Storm"),
                        createParticipant("Mister Rodgers"),
                        createParticipant("Bruce Banner")),
                List.of(createValidSpotifyMessage("Bruce Banner"),
                        createSoundCloudMessage("Susan Storm"),
                        createYouTubeMessage("Mister Rodgers"),
                        createUnknownPlatformMessage("Louis Bard")));

        try (MockedStatic<JsonParserUtil> parserMock = Mockito.mockStatic(JsonParserUtil.class)) {
            parserMock.when(() -> JsonParserUtil.parseFacebookChat(any())).thenReturn(dummyChat);

            mockMvc.perform(multipart("/upload").file(mockValidFile))
                    .andExpect(status().isCreated());

            verify(uploadService).processParticipants(dummyChat);
            verify(uploadService).processMessages(dummyChat);
        }
    }

    @Test
    void uploadJsonFile_HandlesRuntimeExceptionGracefully() throws Exception {
        FacebookChat dummyChat = mock(FacebookChat.class);

        try (MockedStatic<JsonParserUtil> parserMock = Mockito.mockStatic(JsonParserUtil.class)) {
            parserMock.when(() -> JsonParserUtil.parseFacebookChat(any())).thenReturn(dummyChat);
            doThrow(new RuntimeException("fail")).when(uploadService).processMessages(any());

            mockMvc.perform(multipart("/upload").file(mockValidFile))
                    .andExpect(status().is5xxServerError());

            verify(uploadService).processParticipants(dummyChat);
            verify(uploadService).processMessages(dummyChat);
        }
    }
}