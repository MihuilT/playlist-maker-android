package com.myapp.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun searchTracks(query: String): List<Track>

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun insertSongToPlaylist(track: Track, playlistId: Long)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)

    suspend fun deleteIfNotFavoriteAndNotInPlaylist(track: Track)

    fun searchInCollection(query: String): Flow<List<Track>>
}