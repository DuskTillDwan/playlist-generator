package com.dusktildwan.playlistgenerator.DAL.entities.users;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistCollaboratorId implements Serializable {
    private Long playlistId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistCollaboratorId that = (PlaylistCollaboratorId) o;
        return Objects.equals(playlistId, that.playlistId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, userId);
    }
}

