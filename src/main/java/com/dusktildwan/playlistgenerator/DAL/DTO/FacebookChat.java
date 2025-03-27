package com.dusktildwan.playlistgenerator.DAL.DTO;

import java.util.List;

public record FacebookChat(List<Participant> participants, List<Message> messages) {
}
