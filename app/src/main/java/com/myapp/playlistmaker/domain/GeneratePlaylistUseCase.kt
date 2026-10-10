package com.myapp.playlistmaker.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class GeneratePlaylistUseCase(
    private val trackRepository: TrackRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(request: PlaylistRequest): List<Track> = coroutineScope {
        // A1: предпочтения пользователя из профиля и избранного
        val profile = profileRepository.observeProfile().first()
        val favoriteArtists = trackRepository.getFavoriteTracks().first()
            .map { it.artistName.lowercase() }
            .toSet()

        // A2: отбор треков по жанрам и настроению
        val queries = request.genres.flatMap { genre ->
            request.mood.keywords.map { keyword -> "${genre.apiName} $keyword" }
        }
        val candidates = queries
            .map { query -> async { trackRepository.searchTracks(query) } }
            .awaitAll()
            .flatten()
            .distinctBy { it.id }
            .filter { track ->
                request.genres.any { track.primaryGenreName.contains(it.apiName, ignoreCase = true) }
            }

        // алгоритм рекомендаций: оценка каждого трека
        fun score(track: Track): Int {
            var points = 0
            if (track.artistName.lowercase() in favoriteArtists) points += 2
            if (profile.favoriteGenres.any {
                    track.primaryGenreName.contains(it.apiName, ignoreCase = true)
                }) points += 1
            return points
        }

        // A3 и A4: сортировка, ограничение длины, итоговый порядок
        candidates
            .sortedByDescending { score(it) }
            .take(request.size)
            .sortedWith(request.sortRule.comparator)
    }
}
