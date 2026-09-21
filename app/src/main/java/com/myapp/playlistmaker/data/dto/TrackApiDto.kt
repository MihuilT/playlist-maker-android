package com.myapp.playlistmaker.data.dto

data class TrackApiDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val previewUrl: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val primaryGenreName: String?,
    val releaseDate: String?
)
