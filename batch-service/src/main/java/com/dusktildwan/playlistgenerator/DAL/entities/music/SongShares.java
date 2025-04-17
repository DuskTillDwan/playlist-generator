package com.dusktildwan.playlistgenerator.DAL.entities.music;

import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "song_shares",
        schema = "music"
//        uniqueConstraints = @UniqueConstraint(columnNames = {"song_id", "user_id", "shared_at"})
)
public class SongShares {

    @EmbeddedId
    private SongShareId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private ChatMember user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;
}
