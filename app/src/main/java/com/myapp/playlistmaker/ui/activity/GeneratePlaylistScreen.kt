package com.myapp.playlistmaker.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.domain.Genre
import com.myapp.playlistmaker.domain.Mood
import com.myapp.playlistmaker.domain.SortRule
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
import com.myapp.playlistmaker.ui.theme.PrimaryBlue
import com.myapp.playlistmaker.ui.theme.White
import com.myapp.playlistmaker.ui.viewmodel.GeneratePlaylistViewModel

// Экран автоматического подбора плейлиста по жанрам и настроению
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeneratePlaylistScreen(
    viewModel: GeneratePlaylistViewModel,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val description = stringResource(R.string.generate_description)

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaylistMakerAppTheme.colors.surface)
    ) {
        ScreenHeader(
            title = stringResource(R.string.generate_title),
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle(stringResource(R.string.generate_genres))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Genre.entries.forEach { genre ->
                            FilterChip(
                                selected = genre in state.genres,
                                onClick = { viewModel.toggleGenre(genre) },
                                label = { Text(genre.title) }
                            )
                        }
                    }

                    SectionTitle(stringResource(R.string.generate_mood))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Mood.entries.forEach { mood ->
                            FilterChip(
                                selected = mood == state.mood,
                                onClick = { viewModel.onMoodChange(mood) },
                                label = { Text(mood.title) }
                            )
                        }
                    }

                    SectionTitle(stringResource(R.string.generate_size))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(10, 20, 30).forEach { size ->
                            FilterChip(
                                selected = size == state.size,
                                onClick = { viewModel.onSizeChange(size) },
                                label = { Text(size.toString()) }
                            )
                        }
                    }

                    SectionTitle(stringResource(R.string.generate_sort))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SortRule.entries.forEach { rule ->
                            FilterChip(
                                selected = rule == state.sortRule,
                                onClick = { viewModel.onSortChange(rule) },
                                label = { Text(rule.title) }
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.generate() },
                        enabled = state.genres.isNotEmpty() && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(48.dp)
                    ) {
                        Text(stringResource(R.string.action_generate))
                    }

                    if (state.genres.isEmpty()) {
                        Text(
                            text = stringResource(R.string.generate_select_genre),
                            color = PlaylistMakerAppTheme.colors.textSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            when {
                state.isLoading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.isError -> item {
                    Text(
                        text = stringResource(R.string.generate_error),
                        color = PlaylistMakerAppTheme.colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                state.tracks?.isEmpty() == true -> item {
                    Text(
                        text = stringResource(R.string.generate_empty),
                        color = PlaylistMakerAppTheme.colors.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }

                else -> items(state.tracks.orEmpty()) { track ->
                    TrackItem(track = track, onClick = {})
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = PlaylistMakerAppTheme.colors.background
                    )
                }
            }
        }

        if (!state.tracks.isNullOrEmpty()) {
            Button(
                onClick = { viewModel.save(description) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.action_save_playlist))
            }
        }
    }
}
