package com.myapp.playlistmaker.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.ui.theme.ErrorRed
import com.myapp.playlistmaker.ui.theme.PrimaryBlue
import com.myapp.playlistmaker.ui.viewmodel.TrackDetailsViewModel
import kotlinx.coroutines.launch
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsContent(
    track: Track,
    playlists: List<Playlist>,
    onAddToPlaylist: (Long) -> Unit,
    onBackClick: () -> Unit,
    trackDetailsViewModel: TrackDetailsViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.action_add_to_playlist),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlaylistMakerAppTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (playlists.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_playlists),
                        fontSize = 18.sp,
                        color = PlaylistMakerAppTheme.colors.textSecondary
                    )
                } else {
                    playlists.forEach { playlist ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAddToPlaylist(playlist.id)
                                    isSheetOpen = false
                                    scope.launch { sheetState.hide() }
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.glyph_1rxsg6s),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = PlaylistMakerAppTheme.colors.textSecondary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                playlist.name,
                                fontSize = 18.sp,
                                color = PlaylistMakerAppTheme.colors.textPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaylistMakerAppTheme.colors.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(PlaylistMakerAppTheme.colors.surface),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.glyph_18tp5ei),
                        contentDescription = stringResource(R.string.action_back),
                        tint = PlaylistMakerAppTheme.colors.textPrimary
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .fillMaxHeight(0.48f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = track.image,
                    placeholder = painterResource(id = R.drawable.glyph_1rxsg6s),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = track.trackName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlaylistMakerAppTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = track.artistName,
                    fontSize = 16.sp,
                    color = PlaylistMakerAppTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            isSheetOpen = true
                            scope.launch { sheetState.show() }
                        },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    if (track.playlistId != 0L)
                                        PrimaryBlue
                                    else Color.Gray
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.glyph_1b005pl),
                                contentDescription = stringResource(R.string.action_add_to_playlist),
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { trackDetailsViewModel.toggleFavorite() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        val icon = if (track.favorite)
                            R.drawable.glyph_u2ehtp
                        else
                            R.drawable.glyph_6tc0n2
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    if (track.favorite)
                                        ErrorRed
                                    else Color.Gray
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = stringResource(R.string.action_add_to_favorites),
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.duration_label),
                        fontSize = 16.sp,
                        color = PlaylistMakerAppTheme.colors.textSecondary
                    )

                    Text(
                        text = track.trackTime,
                        fontSize = 16.sp,
                        color = PlaylistMakerAppTheme.colors.textPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@Composable
fun LoadingPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaylistMakerAppTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    trackDetailsViewModel: TrackDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by trackDetailsViewModel.uiState.collectAsState()

    when (uiState) {
        TrackDetailsViewModel.TrackDetailsUiState.Loading -> {
            LoadingPlaceholder()
        }

        is TrackDetailsViewModel.TrackDetailsUiState.Content -> {
            val content = uiState as TrackDetailsViewModel.TrackDetailsUiState.Content
            TrackDetailsContent(
                track = content.track,
                playlists = content.playlists,
                onAddToPlaylist = { playlistId ->
                    trackDetailsViewModel.addToPlaylist(playlistId)
                },
                onBackClick = onBackClick,
                trackDetailsViewModel = trackDetailsViewModel
            )
        }

        is TrackDetailsViewModel.TrackDetailsUiState.Error -> {
            TrackErrorPlaceholder(
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
fun TrackErrorPlaceholder(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.error_track_not_found))
        }
    }
}
