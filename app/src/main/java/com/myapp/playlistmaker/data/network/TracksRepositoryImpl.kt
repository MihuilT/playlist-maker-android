package com.myapp.playlistmaker.data.network

import com.myapp.playlistmaker.data.database.PlaylistMakerDatabase
import com.myapp.playlistmaker.data.database.mapper.TrackEntityMapper.toEntity
import com.myapp.playlistmaker.data.database.mapper.TrackEntityMapper.toTrack
import com.myapp.playlistmaker.data.dto.TrackSearchRequest
import com.myapp.playlistmaker.data.dto.TrackSearchResponse
import com.myapp.playlistmaker.domain.ApiClient
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.domain.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: ApiClient,
    database: PlaylistMakerDatabase
) : TrackRepository {
    private val trackDao = database.trackDao()

    override suspend fun searchTracks(query: String): List<Track> {
        val networkResponse = networkClient.execute(TrackSearchRequest(query = query))

        return when (networkResponse.resultCode) {
            200 -> (networkResponse as TrackSearchResponse).results.map { trackDto ->
                Track(
                    id = trackDto.trackId,
                    trackName = trackDto.trackName,
                    artistName = trackDto.artistName,
                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(trackDto.trackTimeMillis),
                    image = trackDto.artworkUrl100?.replace("100x100bb", "512x512bb"),
                    collectionName = trackDto.collectionName.orEmpty(),
                    primaryGenreName = trackDto.primaryGenreName.orEmpty(),
                    releaseDate = trackDto.releaseDate.orEmpty().take(4),
                    trackTimeMillis = trackDto.trackTimeMillis
                )
            }

            -1 -> throw IOException("network_error")
            else -> throw IOException("server_error")
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return trackDao.getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { entity -> entity?.toTrack() }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return trackDao.getFavoriteTracks()
            .map { entities -> entities.map { it.toTrack() } }
    }

    override suspend fun insertSongToPlaylist(track: Track, playlistId: Long) {
        trackDao.insertTrack(
            track.copy(playlistId = playlistId).toEntity()
        )
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        trackDao.insertTrack(
            track.copy(favorite = isFavorite).toEntity()
        )
    }

    override suspend fun deleteIfNotFavoriteAndNotInPlaylist(track: Track) {
        if (!track.favorite && track.playlistId == 0L) {
            trackDao.deleteTrack(track.id)
        }
    }

    override fun searchInCollection(query: String): Flow<List<Track>> {
        // Из введенного текста делаем запрос для FTS: каждое слово ищется по началу ("imag* drag*")
        val ftsQuery = query.trim()
            .split(Regex("\\s+"))
            .map { word -> word.filter { it.isLetterOrDigit() } }
            .filter { it.isNotEmpty() }
            .joinToString(" ") { "$it*" }

        if (ftsQuery.isEmpty()) return flowOf(emptyList())

        return trackDao.searchInCollection(ftsQuery)
            .map { entities -> entities.map { it.toTrack() } }
    }
}