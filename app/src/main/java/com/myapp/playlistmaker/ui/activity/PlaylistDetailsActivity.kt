package com.myapp.playlistmaker.ui.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.ui.viewmodel.PlaylistDetailsViewModel
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PlaylistDetailsScreen(
    playlistDetailsViewModel: PlaylistDetailsViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlistDetails by playlistDetailsViewModel.playlistDetails.collectAsState()

    val playlistDeleted by playlistDetailsViewModel.playlistDeleted.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(playlistDeleted) {
        if (playlistDeleted) onBackClick()
    }

    if (playlistDetails == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.loading))
        }
        return
    }

    val trackList = playlistDetails!!.trackList
    val totalMinutes = playlistDetailsViewModel.totalMinutes(trackList)
    val countText = pluralStringResource(R.plurals.tracks_count, trackList.size, trackList.size)

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val coverHeight = screenHeight * 0.40f

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

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = playlistDetails!!.name,
                    color = PlaylistMakerAppTheme.colors.textPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.action_delete_playlist),
                        tint = PlaylistMakerAppTheme.colors.textPrimary
                    )
                }
}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(coverHeight)
                .clip(RoundedCornerShape(16.dp))
        ) {
            AsyncImage(
                model = playlistDetails!!.coverImageUri,
                contentDescription = playlistDetails!!.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Text(
                text = playlistDetails!!.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PlaylistMakerAppTheme.colors.textPrimary
            )

            if (playlistDetails!!.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = playlistDetails!!.description,
                    fontSize = 15.sp,
                    color = PlaylistMakerAppTheme.colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(R.string.playlist_duration_and_count, totalMinutes, countText),
                color = PlaylistMakerAppTheme.colors.textPrimary,
                fontSize = 15.sp
            )
        }

        if (trackList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.playlist_no_tracks), color = PlaylistMakerAppTheme.colors.textSecondary)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(trackList) { track ->
                    PlaylistTrackItem(
                        track = track,
                        onClick = { onTrackClick(track) },
                        onLongClick = { playlistDetailsViewModel.removeTrack(track) }
                    )
                }
            }
        }
    }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = stringResource(R.string.delete_playlist_title)) },
                text = { Text(text = stringResource(R.string.delete_playlist_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            playlistDetailsViewModel.deletePlaylist()
                        }
                    ) {
                        Text(text = stringResource(R.string.action_delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(text = stringResource(R.string.action_cancel))
                    }
                }
            )
        }

}

@Composable
fun PlaylistTrackItem(
    track: Track,
    onClick: (Track) -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp)
            .combinedClickable(
                onClick = { onClick(track) },
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = PlaylistMakerAppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = track.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.glyph_1rxsg6s),
                    error = painterResource(id = R.drawable.glyph_1rxsg6s)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = track.trackName,
                        fontSize = 16.sp,
                        color = PlaylistMakerAppTheme.colors.textPrimary,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    val bullet = stringResource(R.string.bullet_separator)
                    Text(
                        text = buildAnnotatedString {
                            append(track.artistName)
                            append(bullet)
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(track.trackTime)
                            }
                        },
                        fontSize = 14.sp,
                        color = PlaylistMakerAppTheme.colors.textSecondary,
                        maxLines = 1
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.glyph_7vi5zo),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = PlaylistMakerAppTheme.colors.textSecondary
            )
        }
    }
}