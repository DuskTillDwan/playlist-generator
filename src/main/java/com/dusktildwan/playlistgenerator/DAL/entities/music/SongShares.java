package com.dusktildwan.playlistgenerator.DAL.entities.music;

import com.dusktildwan.playlistgenerator.DAL.entities.users.ChatMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "song_shares",
        schema = "music",
        uniqueConstraints = @UniqueConstraint(columnNames = {"song_id", "user_id", "shared_at"}))
public class SongShares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ChatMember user;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "shared_at", nullable = false)
    private LocalDateTime sharedAt;
}
