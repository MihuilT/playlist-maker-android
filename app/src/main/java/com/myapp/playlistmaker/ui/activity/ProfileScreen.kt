package com.myapp.playlistmaker.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.domain.Genre
import com.myapp.playlistmaker.domain.Mood
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
import com.myapp.playlistmaker.ui.theme.PrimaryBlue
import com.myapp.playlistmaker.ui.theme.White
import com.myapp.playlistmaker.ui.viewmodel.ProfileViewModel

// Экран профиля пользователя: имя, аватар, любимые жанры и настроение по умолчанию
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val profile by viewModel.draft.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()

    val context = LocalContext.current
    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) { }
            viewModel.onAvatarChange(it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaylistMakerAppTheme.colors.surface)
    ) {
        ScreenHeader(
            title = stringResource(R.string.profile_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(PlaylistMakerAppTheme.colors.background)
                    .clickable { avatarPicker.launch(arrayOf("image/*")) },
                contentAlignment = Alignment.Center
            ) {
                val avatar = profile.avatarUri
                if (avatar != null) {
                    AsyncImage(
                        model = avatar.toUri(),
                        contentDescription = stringResource(R.string.profile_avatar),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = stringResource(R.string.profile_avatar),
                        modifier = Modifier.size(64.dp),
                        tint = PlaylistMakerAppTheme.colors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = profile.name,
                onValueChange = viewModel::onNameChange,
                label = { Text(stringResource(R.string.profile_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            SectionTitle(stringResource(R.string.profile_favorite_genres))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Genre.entries.forEach { genre ->
                    FilterChip(
                        selected = genre in profile.favoriteGenres,
                        onClick = { viewModel.toggleGenre(genre) },
                        label = { Text(genre.title) }
                    )
                }
            }

            SectionTitle(stringResource(R.string.profile_default_mood))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Mood.entries.forEach { mood ->
                    FilterChip(
                        selected = mood == profile.defaultMood,
                        onClick = { viewModel.onMoodChange(mood) },
                        label = { Text(mood.title) }
                    )
                }
            }

            Button(
                onClick = { viewModel.save() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.action_save))
            }

            if (isSaved) {
                Text(
                    text = stringResource(R.string.profile_saved),
                    color = PlaylistMakerAppTheme.colors.textSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}
