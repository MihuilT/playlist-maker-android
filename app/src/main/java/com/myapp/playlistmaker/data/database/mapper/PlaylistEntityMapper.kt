package com.myapp.playlistmaker.data.database.mapper

import com.myapp.playlistmaker.data.database.entity.PlaylistEntity
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.domain.Track

object PlaylistEntityMapper {
    fun PlaylistEntity.toPlaylist(tracks: List<Track> = emptyList()): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            coverImageUri = this.coverImageUri,
            trackList = tracks
        )
    }

    fun Playlist.toEntity(): PlaylistEntity {
        return PlaylistEntity(
            id = this.id,
            name = this.name,
            description = this.description,
            coverImageUri = this.coverImageUri
        )
    }
}