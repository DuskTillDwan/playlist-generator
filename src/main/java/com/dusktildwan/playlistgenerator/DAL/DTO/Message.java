package com.dusktildwan.playlistgenerator.DAL.DTO;

import com.google.gson.annotations.SerializedName;

public record Message(@SerializedName("sender_name") String senderName,
                      @SerializedName("timestamp_ms") String timestampMS,
                      String content,
                      SharedSong share ) {

}
