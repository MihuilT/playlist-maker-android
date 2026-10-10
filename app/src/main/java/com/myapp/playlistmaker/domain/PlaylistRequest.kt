package com.myapp.playlistmaker.domain

enum class Genre(val title: String, val apiName: String) {
    POP("Поп", "Pop"),
    ROCK("Рок", "Rock"),
    HIP_HOP("Хип-хоп", "Hip-Hop"),
    ELECTRONIC("Электроника", "Electronic"),
    ALTERNATIVE("Альтернатива", "Alternative"),
    JAZZ("Джаз", "Jazz"),
    CLASSICAL("Классика", "Classical")
}

enum class Mood(val title: String, val keywords: List<String>) {
    ENERGETIC("Энергичное", listOf("dance", "workout")),
    CALM("Спокойное", listOf("chill", "acoustic")),
    FOCUS("Для концентрации", listOf("instrumental", "piano")),
    SAD("Грустное", listOf("ballad", "sad"))
}

data class PlaylistRequest(
    val genres: Set<Genre>,
    val mood: Mood,
    val size: Int = 20,
    val sortRule: SortRule = SortRule.DEFAULT
)
