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
@IdClass(PlaylistCollaboratorId.class)
public class PlaylistCollaborator {
    @Id
    @Column(name = "playlist_id")
    private Long playlistId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private ChatMember chatMember;

}
