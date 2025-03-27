package com.dusktildwan.playlistgenerator.DAL.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FacebookChat {
    List<Participant> participants;
    List<Message> messages;
}
