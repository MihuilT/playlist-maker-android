package com.myapp.playlistmaker.data.network

import com.myapp.playlistmaker.data.database.PlaylistMakerDatabase
import com.myapp.playlistmaker.data.database.entity.PlaylistEntity
import com.myapp.playlistmaker.data.database.mapper.PlaylistEntityMapper.toEntity
import com.myapp.playlistmaker.data.database.mapper.PlaylistEntityMapper.toPlaylist
import com.myapp.playlistmaker.data.database.mapper.TrackEntityMapper.toEntity
import com.myapp.playlistmaker.data.database.mapper.TrackEntityMapper.toTrack
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.domain.PlaylistRepository
import com.myapp.playlistmaker.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class PlaylistsRepositoryImpl(
    database: PlaylistMakerDatabase
) : PlaylistRepository {
    private val playlistDao = database.playlistDao()
    private val trackDao = database.trackDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistDao.getPlaylistById(playlistId)
            .combine(trackDao.getTracksByPlaylistId(playlistId)) { playlistEntity, trackEntities ->
                playlistEntity?.let { entity ->
                    entity.toPlaylist(
                        trackEntities.map { trackEntity -> trackEntity.toTrack() }
                    )
                }
            }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return combine(
            playlistDao.getAllPlaylists(),
            trackDao.getAllTracks()
        ) { playlistEntities, trackEntities ->

            playlistEntities.map { playlist ->

                val trackList = trackEntities
                    .filter { it.playlistId == playlist.id }
                    .map { it.toTrack() }

                playlist.toPlaylist(trackList)
            }
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?) {
        val playlist = Playlist(
            name = name,
            description = description,
            coverImageUri = coverImageUri,
            trackList = emptyList()
        )
        playlistDao.insertPlaylist(
            playlist.toEntity()
        )
    }

    override suspend fun removeTrackFromPlaylist(track: Track) {
        if (track.favorite) {
            trackDao.insertTrack(
                track.copy(playlistId = 0).toEntity()
            )
        } else {
            trackDao.removeTrackFromPlaylist(track.id, track.playlistId)

            val newTrack = track.copy(playlistId = 0)
            trackDao.insertTrack(newTrack.toEntity())
            trackDao.deleteTrack(track.id)
        }
    }
    override suspend fun deletePlaylist(playlistId: Long) {
        // Удаляем плейлист и корректно обрабатываем связанные треки.
        val tracksInPlaylist = trackDao.getTracksByPlaylistId(playlistId).first()

        tracksInPlaylist.forEach { trackEntity ->
            if (trackEntity.favorite) {
                // Избранные треки сохраняем в базе, просто отвязываем от плейлиста.
                trackDao.insertTrack(trackEntity.copy(playlistId = 0))
            } else {
                // Неизбранные треки можно удалить целиком (по текущей модели один трек принадлежит одному плейлисту).
                trackDao.deleteTrack(trackEntity.id)
            }
        }

        playlistDao.deletePlaylistById(playlistId)
    }

    override suspend fun createPlaylistWithTracks(name: String, description: String, tracks: List<Track>) {
        val playlistId = playlistDao.insertPlaylist(
            PlaylistEntity(name = name, description = description)
        )
        tracks.forEach { track ->
            // если трек уже сохранен (например, в избранном), признак избранного не теряем
            val existing = trackDao.getTrackById(track.id)
            trackDao.insertTrack(
                track.copy(
                    favorite = existing?.favorite ?: track.favorite,
                    playlistId = playlistId
                ).toEntity()
            )
        }
    }
}
