package com.dusktildwan.playlistgenerator.util;

import com.dusktildwan.playlistgenerator.DAL.DTO.FacebookChat;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;

public class JsonParserUtil {
    private static final Gson GSON = new Gson();

    public static FacebookChat parseFacebookChat(MultipartFile file) throws IOException{
        try(JsonReader reader = new JsonReader(new InputStreamReader(file.getInputStream()))){
            return GSON.fromJson(reader, FacebookChat.class);
        }
    }
}
