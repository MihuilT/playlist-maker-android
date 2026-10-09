package com.myapp.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile>

    suspend fun saveProfile(profile: Profile)
}
