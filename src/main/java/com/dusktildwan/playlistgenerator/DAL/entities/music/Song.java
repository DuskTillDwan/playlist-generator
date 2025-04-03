package com.dusktildwan.playlistgenerator.DAL.entities.music;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "music")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "platform_id", nullable = false) //fk to platforms
    private Platform platformId;

    @Column
    private String title;

    @Column(nullable = false)
    private String url;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
