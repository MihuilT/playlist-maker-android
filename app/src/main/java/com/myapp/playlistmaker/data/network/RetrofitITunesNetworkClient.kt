package com.myapp.playlistmaker.data.network

import com.myapp.playlistmaker.data.dto.ApiResponse
import com.myapp.playlistmaker.data.dto.TrackSearchRequest
import com.myapp.playlistmaker.domain.ApiClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitITunesNetworkClient : ApiClient {

    private val retrofitClient = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApi = retrofitClient.create(ITunesApi::class.java)

    override suspend fun execute(request: Any): ApiResponse {
        return try {
            when (request) {
                is TrackSearchRequest -> {
                    val apiResponse = itunesApi.searchTracks(request.query)
                    apiResponse.resultCode = 200
                    apiResponse
                }

                else -> ApiResponse().apply { resultCode = 400 }
            }
        } catch (_: IOException) {
            ApiResponse().apply {
                resultCode = -1
            }
        } catch (_: Exception) {
            ApiResponse().apply {
                resultCode = -2
            }
        }
    }
}
