package com.myapp.playlistmaker.domain

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImageUri: String? = null,
    var trackList: List<Track>
)