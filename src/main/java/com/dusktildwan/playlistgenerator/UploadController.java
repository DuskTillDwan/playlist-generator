 package com.dusktildwan.playlistgenerator;

 import com.dusktildwan.playlistgenerator.DAL.DTO.FacebookChat;
 import com.dusktildwan.playlistgenerator.util.JsonParserUtil;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.http.HttpStatusCode;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;
 import org.springframework.web.multipart.MultipartFile;

 import java.io.IOException;

@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public HttpStatusCode uploadJsonFile(@RequestParam("file") MultipartFile jsonFile) throws IOException {
        if(jsonFile == null) {
            log.info("file is empty???");
            return  HttpStatusCode.valueOf(405);
        }
        FacebookChat fileStructure = JsonParserUtil.parseFacebookChat(jsonFile);
        log.info("Read file structure as : {}", fileStructure.toString());
        return HttpStatusCode.valueOf(201);
    }
}
