package com.myapp.playlistmaker.data.network

import com.myapp.playlistmaker.data.database.PlaylistMakerDatabase
import com.myapp.playlistmaker.data.database.entity.ProfileEntity
import com.myapp.playlistmaker.domain.Genre
import com.myapp.playlistmaker.domain.Mood
import com.myapp.playlistmaker.domain.Profile
import com.myapp.playlistmaker.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    database: PlaylistMakerDatabase
) : ProfileRepository {
    private val profileDao = database.profileDao()

    // если профиль еще не заполнен, возвращаем профиль по умолчанию
    override fun observeProfile(): Flow<Profile> =
        profileDao.observeProfile().map { entity -> entity?.toProfile() ?: Profile() }

    override suspend fun saveProfile(profile: Profile) {
        profileDao.saveProfile(profile.toEntity())
    }

    private fun ProfileEntity.toProfile(): Profile {
        val genreNames = favoriteGenres.split(",").filter { it.isNotBlank() }.toSet()
        return Profile(
            name = name,
            avatarUri = avatarUri,
            favoriteGenres = Genre.entries.filter { it.name in genreNames }.toSet(),
            defaultMood = Mood.entries.firstOrNull { it.name == defaultMood } ?: Mood.ENERGETIC
        )
    }

    private fun Profile.toEntity(): ProfileEntity =
        ProfileEntity(
            name = name,
            avatarUri = avatarUri,
            favoriteGenres = favoriteGenres.joinToString(",") { it.name },
            defaultMood = defaultMood.name
        )
}
