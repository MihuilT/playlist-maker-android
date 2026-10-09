package com.myapp.playlistmaker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.playlistmaker.domain.Genre
import com.myapp.playlistmaker.domain.Mood
import com.myapp.playlistmaker.domain.Profile
import com.myapp.playlistmaker.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // редактируемая копия профиля, в базу попадает по кнопке «Сохранить»
    private val _draft = MutableStateFlow(Profile())
    val draft: StateFlow<Profile> = _draft.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    init {
        viewModelScope.launch {
            _draft.value = profileRepository.observeProfile().first()
        }
    }

    fun onNameChange(name: String) = edit { it.copy(name = name) }

    fun onAvatarChange(uri: String) = edit { it.copy(avatarUri = uri) }

    fun onMoodChange(mood: Mood) = edit { it.copy(defaultMood = mood) }

    fun toggleGenre(genre: Genre) = edit { profile ->
        val genres = if (genre in profile.favoriteGenres) {
            profile.favoriteGenres - genre
        } else {
            profile.favoriteGenres + genre
        }
        profile.copy(favoriteGenres = genres)
    }

    fun save() {
        viewModelScope.launch {
            profileRepository.saveProfile(_draft.value)
            _isSaved.value = true
        }
    }

    private fun edit(change: (Profile) -> Profile) {
        _draft.update(change)
        _isSaved.value = false
    }
}
