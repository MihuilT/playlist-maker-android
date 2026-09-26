package com.myapp.playlistmaker.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.domain.Playlist
import com.myapp.playlistmaker.ui.theme.DarkGrey
import com.myapp.playlistmaker.ui.theme.PrimaryBlue
import com.myapp.playlistmaker.ui.viewmodel.PlaylistsViewModel
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
@Composable
fun PlaylistItem(playlist: Playlist, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 20.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp)
        ) {
            if (playlist.coverImageUri != null) {
                AsyncImage(
                    model = playlist.coverImageUri.toUri(),
                    contentDescription = playlist.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.glyph_e9h9),
                    contentDescription = playlist.name,
                    tint = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(playlist.name, fontSize = 17.sp)

            val count = playlist.trackList.size
            Text(
                text = pluralStringResource(R.plurals.tracks_count, count, count),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PlaylistsScreen(
    onBackClick: () -> Unit,
    onCreatePlaylistClick: () -> Unit,
    onGenerateClick: () -> Unit,
    navigateToPlaylist: (Int) -> Unit,
    playlistsViewModel: PlaylistsViewModel
) {
    val playlists by playlistsViewModel.playlists.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaylistMakerAppTheme.colors.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = stringResource(R.string.playlists_title),
                        color = PlaylistMakerAppTheme.colors.textPrimary ,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(onClick = onGenerateClick) {
                        Text(
                            text = stringResource(R.string.action_generate),
                            color = PrimaryBlue,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (playlists.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.playlists_empty_title), color = PlaylistMakerAppTheme.colors.textSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(playlists) { playlist ->
                        PlaylistItem(playlist) {
                            navigateToPlaylist(playlist.id.toInt())
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onCreatePlaylistClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = DarkGrey,
            contentColor = PlaylistMakerAppTheme.colors.surface,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

