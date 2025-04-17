package com.dusktildwan.playlistgenerator.DAL.entities.music;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "playlist_song", schema = "music")
@IdClass(PlaylistSongId.class)
public class PlaylistSongs {
    @Id
    @Column(name = "playlist_id")
    private Long playlistId;

    @Id
    @Column(name = "song_id")
    private Long songId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", insertable = false, updatable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "song_id", insertable = false, updatable = false)
    private Song song;

}
