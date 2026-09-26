package com.myapp.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String? = null)

    suspend fun removeTrackFromPlaylist(track: Track)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun createPlaylistWithTracks(name: String, description: String, tracks: List<Track>)
}