package com.myapp.playlistmaker.domain

data class SearchQuery(
    val query: String,
    var count: Int = 1
)