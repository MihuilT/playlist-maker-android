package com.myapp.playlistmaker.ui.activity

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
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
import com.myapp.playlistmaker.ui.theme.PlaylistMakerTheme
import com.myapp.playlistmaker.ui.viewmodel.FavoritesViewModel
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
@Composable
fun FavoriteTrackItem(
    track: Track,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ),
        colors = CardDefaults.cardColors(
            containerColor = PlaylistMakerAppTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
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

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val favoriteTracks by favoritesViewModel.favoriteTracks.collectAsState()
    val query by favoritesViewModel.query.collectAsState()
    val focusRequester = remember { FocusRequester() }

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
                    text = stringResource(R.string.favorites_title),
                    color = PlaylistMakerAppTheme.colors.textPrimary ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // поиск по своей коллекции работает без интернета (полнотекстовый индекс Room)
        SearchField(
            value = query,
            onValueChange = favoritesViewModel::onQueryChange,
            placeholderText = stringResource(R.string.favorites_search_placeholder),
            focusRequester = focusRequester,
            onFocusChanged = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = PlaylistMakerAppTheme.colors.textSecondary
                )
            },
            trailingIcon = if (query.isNotEmpty()) {
                {
                    IconButton(
                        onClick = { favoritesViewModel.onQueryChange("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.action_clear),
                            tint = PlaylistMakerAppTheme.colors.textSecondary
                        )
                    }
                }
            } else null
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (favoriteTracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (query.isNotBlank()) {
                        stringResource(R.string.search_nothing_found)
                    } else {
                        stringResource(R.string.favorites_empty_hint)
                    },
                    color = PlaylistMakerAppTheme.colors.textSecondary,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(favoriteTracks) { track ->
                    FavoriteTrackItem(
                        track = track,
                        onClick = { onTrackClick(track) },
                        onLongClick = {
                            favoritesViewModel.toggleFavorite(track, false)
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = PlaylistMakerAppTheme.colors.background
                    )
                }
            }
        }
    }
}

