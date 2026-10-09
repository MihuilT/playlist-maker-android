package com.myapp.playlistmaker.domain

data class Profile(
    val name: String = "",
    val avatarUri: String? = null,
    val favoriteGenres: Set<Genre> = emptySet(),
    val defaultMood: Mood = Mood.ENERGETIC
)
