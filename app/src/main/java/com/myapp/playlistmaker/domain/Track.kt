package com.myapp.playlistmaker.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String? = "",
    var favorite: Boolean = false,
    var playlistId: Long = 0,
    val collectionName: String = "",
    val primaryGenreName: String = "",
    val releaseDate: String = "",
    val trackTimeMillis: Long = 0
) : Parcelable
