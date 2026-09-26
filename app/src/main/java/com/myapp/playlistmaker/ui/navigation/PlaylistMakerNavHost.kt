package com.myapp.playlistmaker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.ui.activity.NewPlaylistScreen
import com.myapp.playlistmaker.ui.activity.FavoritesScreen
import com.myapp.playlistmaker.ui.activity.PlaylistDetailsScreen
import com.myapp.playlistmaker.ui.activity.MainScreen
import com.myapp.playlistmaker.ui.activity.PlaylistsScreen
import com.myapp.playlistmaker.ui.activity.SearchScreen
import com.myapp.playlistmaker.ui.activity.SettingsScreen
import com.myapp.playlistmaker.ui.activity.TrackDetailsScreen
import com.myapp.playlistmaker.ui.activity.GeneratePlaylistScreen
import com.myapp.playlistmaker.ui.activity.ProfileScreen
import com.myapp.playlistmaker.ui.viewmodel.FavoritesViewModel
import com.myapp.playlistmaker.ui.viewmodel.GeneratePlaylistViewModel
import com.myapp.playlistmaker.ui.viewmodel.ProfileViewModel
import com.myapp.playlistmaker.ui.viewmodel.NewPlaylistViewModel
import com.myapp.playlistmaker.ui.viewmodel.PlaylistDetailsViewModel
import com.myapp.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.myapp.playlistmaker.ui.viewmodel.SearchViewModel
import com.myapp.playlistmaker.ui.viewmodel.TrackDetailsViewModel
import com.myapp.playlistmaker.ui.viewmodel.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Playlists : Screen("playlists")
    object Favorites : Screen("favorites")
    object CreatePlaylist : Screen("create_playlist")
    object TrackDetails : Screen("track_details")
    object PlaylistDetails : Screen("playlist/{id}")
    object Profile : Screen("profile")
    object GeneratePlaylist : Screen("generate_playlist")
}


@Composable
fun PlaylistMakerNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onPlaylistsClick = { navController.navigate(Screen.Playlists.route) },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = koinViewModel()

            SearchScreen(
                searchViewModel = searchViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("track", track)

                    navController.navigate(Screen.TrackDetails.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            val themeViewModel: ThemeViewModel = koinViewModel()
            val isDarkThemeEnabled by themeViewModel.isDarkThemeEnabled.collectAsState()

            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                isDarkThemeEnabled = isDarkThemeEnabled,
                onDarkThemeToggle = themeViewModel::setDarkThemeEnabled
            )
        }

        composable(Screen.Playlists.route) {
            val playlistsViewModel: PlaylistsViewModel = koinViewModel()

            PlaylistsScreen(
                playlistsViewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() },
                onCreatePlaylistClick = { navController.navigate(Screen.CreatePlaylist.route) },
                onGenerateClick = { navController.navigate(Screen.GeneratePlaylist.route) },
                navigateToPlaylist = { id -> navController.navigate("playlist/$id") }
            )
        }

        composable(
            route = Screen.PlaylistDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("id") ?: 0L

            val playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel(
                parameters = { parametersOf(playlistId) }
            )

            PlaylistDetailsScreen(
                playlistDetailsViewModel = playlistDetailsViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("track", track)
                    navController.navigate(Screen.TrackDetails.route)
                }
            )
        }

        composable(Screen.Favorites.route) {
            val favoritesViewModel: FavoritesViewModel = koinViewModel()

            FavoritesScreen(
                favoritesViewModel = favoritesViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("track", track)
                    navController.navigate(Screen.TrackDetails.route)
                }
            )
        }

        composable(Screen.CreatePlaylist.route) { backStackEntry ->
            val newPlaylistViewModel: NewPlaylistViewModel = koinViewModel()

            NewPlaylistScreen(
                newPlaylistViewModel = newPlaylistViewModel,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { name, desc, coverUri ->
                    newPlaylistViewModel.createPlaylist(name, desc, coverUri)
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.GeneratePlaylist.route) {
            val generatePlaylistViewModel: GeneratePlaylistViewModel = koinViewModel()

            GeneratePlaylistScreen(
                viewModel = generatePlaylistViewModel,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = koinViewModel()

            ProfileScreen(
                viewModel = profileViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.TrackDetails.route) { backStackEntry ->
            val track = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Track>("track")


            val trackDetailsViewModel: TrackDetailsViewModel = koinViewModel(
                parameters = { parametersOf(track) }
            )

            TrackDetailsScreen(
                trackDetailsViewModel = trackDetailsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}