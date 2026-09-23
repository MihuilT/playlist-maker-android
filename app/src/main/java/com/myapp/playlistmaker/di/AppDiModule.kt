package com.myapp.playlistmaker.di

import com.myapp.playlistmaker.data.database.PlaylistMakerDatabase
import com.myapp.playlistmaker.data.network.PlaylistsRepositoryImpl
import com.myapp.playlistmaker.data.network.ProfileRepositoryImpl
import com.myapp.playlistmaker.data.network.RetrofitITunesNetworkClient
import com.myapp.playlistmaker.data.network.SearchHistoryRepositoryImpl
import com.myapp.playlistmaker.data.network.TracksRepositoryImpl
import com.myapp.playlistmaker.data.preferences.SearchHistoryStorage
import com.myapp.playlistmaker.data.preferences.searchHistoryDataStore
import com.myapp.playlistmaker.data.preferences.ThemeSettingsRepositoryImpl
import com.myapp.playlistmaker.data.preferences.ThemeSettingsStorage
import com.myapp.playlistmaker.data.preferences.themeSettingsDataStore
import com.myapp.playlistmaker.domain.ThemeSettingsRepository
import com.myapp.playlistmaker.ui.viewmodel.ThemeViewModel
import com.myapp.playlistmaker.domain.ApiClient
import com.myapp.playlistmaker.domain.GeneratePlaylistUseCase
import com.myapp.playlistmaker.domain.ProfileRepository
import com.myapp.playlistmaker.domain.PlaylistRepository
import com.myapp.playlistmaker.domain.SearchHistoryRepository
import com.myapp.playlistmaker.domain.TrackRepository
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.ui.viewmodel.FavoritesViewModel
import com.myapp.playlistmaker.ui.viewmodel.GeneratePlaylistViewModel
import com.myapp.playlistmaker.ui.viewmodel.ProfileViewModel
import com.myapp.playlistmaker.ui.viewmodel.NewPlaylistViewModel
import com.myapp.playlistmaker.ui.viewmodel.PlaylistDetailsViewModel
import com.myapp.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.myapp.playlistmaker.ui.viewmodel.SearchViewModel
import com.myapp.playlistmaker.ui.viewmodel.TrackDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appDiModule = module {

    single<ApiClient> { RetrofitITunesNetworkClient() }

    single { PlaylistMakerDatabase.getInstance(androidContext()) }

    single<TrackRepository> {
        TracksRepositoryImpl(
            networkClient = get(),
            database = get()
        )
    }

    single<PlaylistRepository> {
        PlaylistsRepositoryImpl(
            database = get()
        )
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            database = get()
        )
    }

    // сценарий подбора плейлиста: новый объект при каждом запросе
    factory {
        GeneratePlaylistUseCase(
            trackRepository = get(),
            profileRepository = get()
        )
    }

    single {
        SearchHistoryStorage(
            dataStore = androidContext().searchHistoryDataStore
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            searchHistoryStorage = get()
        )
    }

    // Theme settings (DataStore)
    single {
        ThemeSettingsStorage(
            dataStore = androidContext().themeSettingsDataStore
        )
    }

    single<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(
            themeSettingsStorage = get()
        )
    }

    viewModel { PlaylistsViewModel(playlistRepository = get()) }

    viewModel { (playlistId: Long) ->
        PlaylistDetailsViewModel(playlistId = playlistId, playlistRepository = get())
    }

    viewModel {
        SearchViewModel(trackRepository = get(), searchHistoryRepository = get())
    }

    viewModel { (track: Track) ->
        TrackDetailsViewModel(track = track, trackRepository = get(), playlistRepository = get())
    }

    viewModel {
        FavoritesViewModel(trackRepository = get())
    }

    viewModel {
        NewPlaylistViewModel(playlistRepository = get())
    }

    viewModel {
        ThemeViewModel(themeSettingsRepository = get())
    }

    viewModel {
        GeneratePlaylistViewModel(
            generatePlaylist = get(),
            playlistRepository = get(),
            profileRepository = get()
        )
    }

    viewModel {
        ProfileViewModel(
            profileRepository = get()
        )
    }
}