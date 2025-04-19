package com.dusktildwan.common.DAL.entities.music;

import com.dusktildwan.common.DAL.entities.users.ChatMember;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "song_shares",
        schema = "music")
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
