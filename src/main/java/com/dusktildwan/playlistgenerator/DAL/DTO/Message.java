package com.dusktildwan.playlistgenerator.DAL.DTO;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Message {

    @SerializedName("sender_name")
    private String senderName;

    @SerializedName("timestamp_ms")
    private Long timestampMS;

    private String content;

    private SharedSong share;

}
