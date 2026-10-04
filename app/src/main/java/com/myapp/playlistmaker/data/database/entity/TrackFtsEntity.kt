package com.myapp.playlistmaker.data.database.entity

import androidx.room.Entity
import androidx.room.Fts4

// Полнотекстовый индекс по сохраненным трекам (поисковый индекс на диаграмме DFD)
@Fts4(contentEntity = TrackEntity::class)
@Entity(tableName = "tracks_fts")
data class TrackFtsEntity(
    val trackName: String,
    val artistName: String,
    val collectionName: String
)
