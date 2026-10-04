package com.myapp.playlistmaker.data.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String?,
    val favorite: Boolean = false,
    val playlistId: Long = 0,
    val collectionName: String = "",
    val primaryGenreName: String = "",
    val releaseDate: String = "",
    val trackTimeMillis: Long = 0
)
