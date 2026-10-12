package com.myapp.playlistmaker.ui.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.domain.Track
import com.myapp.playlistmaker.domain.SearchQuery
import com.myapp.playlistmaker.ui.theme.PrimaryBlue
import com.myapp.playlistmaker.ui.theme.PlaylistMakerTheme
import com.myapp.playlistmaker.ui.theme.White
import com.myapp.playlistmaker.ui.viewmodel.SearchUiState
import com.myapp.playlistmaker.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textFontSize: Int = 18,
) {
    val shape = RoundedCornerShape(6.dp)

    Box(
        modifier = modifier
            .height(49.dp)
            .clip(shape)
            .background(PlaylistMakerAppTheme.colors.background)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(modifier = Modifier.weight(1f)) {

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = textFontSize.sp,
                        color = PlaylistMakerAppTheme.colors.textPrimary,
                        lineHeight = textFontSize.sp
                    ),
                    cursorBrush = SolidColor(PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            onFocusChanged(state.isFocused)
                        },
                    decorationBox = { inner ->
                        if (value.isEmpty()) {
                            Text(
                                text = placeholderText,
                                fontSize = 16.sp,
                                color = PlaylistMakerAppTheme.colors.textSecondary
                            )
                        }
                        inner()
                    }
                )
            }

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}
@Composable
fun SearchHistoryList(
    historyList: List<SearchQuery>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = PlaylistMakerAppTheme.colors.textSecondary.copy(alpha = 0.2f)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            items(historyList) { historyItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(historyItem.query) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.glyph_17mzkky),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = PlaylistMakerAppTheme.colors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = historyItem.query,
                        color = PlaylistMakerAppTheme.colors.textPrimary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
@Composable
fun NetworkErrorPlaceholder(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.no_internet),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.network_problem_title),
            color = PlaylistMakerAppTheme.colors.textPrimary,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.network_problem_message),
            color = PlaylistMakerAppTheme.colors.textPrimary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(PrimaryBlue)
                .clickable { onRetry() }
                .padding(horizontal = 28.dp, vertical = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.action_refresh),
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun NothingFoundPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.notrack),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.search_nothing_found),
            color = PlaylistMakerAppTheme.colors.textPrimary,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SearchScreenPreview() {
    PlaylistMakerTheme {
        SearchScreen(
            searchViewModel = viewModel(),
            onBackClick = {},
            onTrackClick = {}
        )
    }
}

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val uiState by searchViewModel.uiState.collectAsState()

    var historyList by remember { mutableStateOf<List<SearchQuery>>(emptyList()) }
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState) {
        if (uiState is SearchUiState.Success) {
            historyList = searchViewModel.loadHistory()
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

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.search_title),
                    color = PlaylistMakerAppTheme.colors.textPrimary ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PlaylistMakerAppTheme.colors.background)
            ) {

                SearchField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        searchViewModel.onQueryChange(newText)
                    },
                    placeholderText = stringResource(R.string.search_placeholder),
                    focusRequester = focusRequester,
                    onFocusChanged = { focused ->
                        isFocused = focused
                        if (focused && text.isEmpty()) {
                            coroutineScope.launch {
                                historyList = searchViewModel.loadHistory()
                            }
                        }
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                if (text.isNotEmpty()) {
                                    focusManager.clearFocus()
                                    searchViewModel.search(text)
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = PlaylistMakerAppTheme.colors.textSecondary
                            )
                        }
                    },
                    trailingIcon = if (text.isNotEmpty()) {
                        {
                            IconButton(
                                onClick = {
                                    text = ""
                                    searchViewModel.clearSearch()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = PlaylistMakerAppTheme.colors.textSecondary
                                )
                            }
                        }
                    } else null
                )

                if (isFocused && text.isEmpty() && historyList.isNotEmpty()) {
                    SearchHistoryList(
                        historyList = historyList,
                        onClick = { word ->
                            text = word
                            focusManager.clearFocus()
                            searchViewModel.search(word)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        when (uiState) {

            is SearchUiState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (text.isEmpty())
                            stringResource(R.string.search_hint)
                        else
                            stringResource(R.string.search_press_button),
                        color = PlaylistMakerAppTheme.colors.textSecondary
                    )
                }
            }

            is SearchUiState.Searching -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchUiState.Success -> {
                val tracks = (uiState as SearchUiState.Success).tracks

                if (tracks.isEmpty()) {
                    NothingFoundPlaceholder()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tracks) { track ->
                            TrackItem(
                                track = track,
                                onClick = { selectedTrack ->
                                    searchViewModel.saveQueryToHistory(text)
                                    onTrackClick(selectedTrack)
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

            is SearchUiState.Fail -> {
                NetworkErrorPlaceholder(
                    onRetry = {
                        if (text.isNotEmpty()) {
                            searchViewModel.search(text)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    onClick: (Track) -> Unit
) {
    Card(
        onClick = { onClick(track) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
