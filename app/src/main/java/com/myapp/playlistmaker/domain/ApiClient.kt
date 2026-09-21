package com.myapp.playlistmaker.domain

import com.myapp.playlistmaker.data.dto.ApiResponse

interface ApiClient {
    suspend fun execute(request: Any): ApiResponse
}