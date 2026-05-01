package com.dusktildwan.common.DAL.entities.users;

import com.dusktildwan.common.DAL.entities.music.Playlist;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PlaylistCollaborator {
    @EmbeddedId
    PlaylistCollaboratorId playlistCollaboratorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private ChatMember chatMember;

}
