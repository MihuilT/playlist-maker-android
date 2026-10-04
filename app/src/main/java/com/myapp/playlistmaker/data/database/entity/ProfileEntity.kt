package com.myapp.playlistmaker.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: Long = 1L,      // профиль владельца устройства
    val name: String,
    val avatarUri: String?,
    val favoriteGenres: String,         // названия жанров через запятую
    val defaultMood: String
)
