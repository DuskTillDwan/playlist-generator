package com.dusktildwan.playlistgenerator.DAL.entities.music;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "music")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "platform", nullable = false)
    private Platform platform;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String externalId; // Playlist ID from Spotify/YouTube/SoundCloud

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();


}
