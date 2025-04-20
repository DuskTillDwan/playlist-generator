package com.dusktildwan.batch.controllers;

import com.dusktildwan.batch.services.UploadService;
import com.dusktildwan.batch.util.JsonParserUtil;
import com.dusktildwan.common.DAL.DTO.FacebookChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<HttpStatusCode> uploadJsonFile(@RequestParam("file") MultipartFile jsonFile) throws IOException {
        if(jsonFile.isEmpty()) {
            log.error("FILE IS EMPTY");
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!"application/json".equals(jsonFile.getContentType())) {
            log.error("FILE IS NOT JSON: {}", jsonFile.getContentType());
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        FacebookChat facebookChat = JsonParserUtil.parseFacebookChat(jsonFile);

        uploadService.processParticipants(facebookChat);
        try {
            uploadService.processMessages(facebookChat);
        } catch(RuntimeException e){
            log.error("Something went wrong saving songs to the database");
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
