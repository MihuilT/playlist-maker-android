package com.myapp.playlistmaker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.ui.navigation.PlaylistMakerNavHost
import com.myapp.playlistmaker.ui.theme.PlaylistMakerTheme
import com.myapp.playlistmaker.ui.viewmodel.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
import com.myapp.playlistmaker.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = koinViewModel()
            val isDarkThemeEnabled by themeViewModel.isDarkThemeEnabled.collectAsState()

            PlaylistMakerTheme(darkTheme = isDarkThemeEnabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PlaylistMakerAppTheme.colors.background
                ) {
                    PlaylistMakerNavHost()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuItem(
    text: String,
    iconResId: Int = 0,
    onClick: () -> Unit,
    imageVector: ImageVector? = null
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = PlaylistMakerAppTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (imageVector != null) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = PlaylistMakerAppTheme.colors.textPrimary
                    )
                } else {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = PlaylistMakerAppTheme.colors.textPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = text,
                    color = PlaylistMakerAppTheme.colors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                )
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
fun MainScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(PlaylistMakerAppTheme.colors.primary),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(PlaylistMakerAppTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                MainMenuItem(
                    text = stringResource(R.string.menu_search),
                    iconResId = android.R.drawable.ic_menu_search,
                    onClick = onSearchClick
                )

                MainMenuItem(
                    text = stringResource(R.string.menu_playlists),
                    iconResId = R.drawable.glyph_1wjjykr,
                    onClick = onPlaylistsClick
                )

                MainMenuItem(
                    text = stringResource(R.string.menu_favorites),
                    iconResId = R.drawable.glyph_6tc0n2,
                    onClick = onFavoritesClick
                )

                MainMenuItem(
                    text = stringResource(R.string.menu_profile),
                    imageVector = Icons.Filled.Person,
                    onClick = onProfileClick
                )

                MainMenuItem(
                    text = stringResource(R.string.menu_settings),
                    iconResId = R.drawable.glyph_koqj83,
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MainScreenPreview() {
    PlaylistMakerTheme {
        MainScreen(
            onSearchClick = {},
            onSettingsClick = {},
            onPlaylistsClick = {},
            onFavoritesClick = {},
            onProfileClick = {}
        )
    }
}
