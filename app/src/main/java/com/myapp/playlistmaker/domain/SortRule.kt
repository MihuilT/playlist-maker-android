package com.myapp.playlistmaker.domain

// Правила сортировки подобранного плейлиста (стрелка управления на диаграмме IDEF0)
enum class SortRule(val title: String, val comparator: Comparator<Track>) {
    DEFAULT("Как есть", Comparator { _, _ -> 0 }),
    NAME("По названию", compareBy<Track> { it.trackName.lowercase() }),
    ARTIST("По исполнителю", compareBy<Track> { it.artistName.lowercase() }),
    DURATION("По длительности", compareBy<Track> { it.trackTimeMillis }),
    NEWEST("Сначала новые", compareByDescending<Track> { it.releaseDate })
}
