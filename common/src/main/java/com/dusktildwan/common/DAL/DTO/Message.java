package com.dusktildwan.common.DAL.DTO;

import com.google.gson.annotations.SerializedName;

public record Message(@SerializedName("sender_name") String senderName,
                      @SerializedName("timestamp_ms") Long timestampMS,
                      String content,
                      SharedSong share ) {

}
