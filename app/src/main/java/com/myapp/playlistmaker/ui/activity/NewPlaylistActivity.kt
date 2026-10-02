package com.myapp.playlistmaker.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.ui.theme.PrimaryBlue
import com.myapp.playlistmaker.ui.theme.PlaylistMakerTheme
import com.myapp.playlistmaker.ui.theme.White
import com.myapp.playlistmaker.ui.viewmodel.NewPlaylistViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.core.net.toUri
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlaylistScreen(
    newPlaylistViewModel: NewPlaylistViewModel,
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String?) -> Unit
) {
    val playlistName = remember { mutableStateOf("") }
    val playlistDescription = remember { mutableStateOf("") }
    val coverImageUri = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) { }

            coverImageUri.value = it.toString()
            newPlaylistViewModel.setCoverImageUri(it.toString())
        }
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch(arrayOf("image/*"))
        }
    }

    val isButtonEnabled = playlistName.value.isNotBlank()
    val isNameFieldFocused = remember { mutableStateOf(false) }

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
                    text = stringResource(R.string.playlist_new_title),
                    color = PlaylistMakerAppTheme.colors.textPrimary ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    // Фон должен зависеть от темы, иначе на тёмной теме элементы могут "пропасть".
                    .background(PlaylistMakerAppTheme.colors.surface)
                    .clickable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                        } else {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    imagePickerLauncher.launch(arrayOf("image/*"))
                                }
                                else -> {
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (coverImageUri.value != null) {
                    AsyncImage(
                        model = coverImageUri.value!!.toUri(),
                        contentDescription = stringResource(R.string.playlist_cover_desc),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Используем Icon + tint, чтобы иконка корректно отображалась в тёмной теме.
                    Icon(
                        painter = painterResource(id = R.drawable.glyph_e9h9),
                        contentDescription = stringResource(R.string.playlist_add_photo),
                        modifier = Modifier.size(100.dp),
                        tint = PlaylistMakerAppTheme.colors.textSecondary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = playlistName.value,
                    onValueChange = { playlistName.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.playlist_name_required),
                            color = if (isNameFieldFocused.value || playlistName.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.textSecondary,
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = if (playlistName.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.background,
                        focusedTextColor = PlaylistMakerAppTheme.colors.textPrimary,
                        unfocusedTextColor = PlaylistMakerAppTheme.colors.textPrimary,
                        cursorColor = PrimaryBlue,
                        focusedContainerColor = PlaylistMakerAppTheme.colors.surface,
                        unfocusedContainerColor = PlaylistMakerAppTheme.colors.surface,
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = if (playlistName.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.textSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = playlistDescription.value,
                    onValueChange = { playlistDescription.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.playlist_description_label),
                            color = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.textSecondary,
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.background,
                        unfocusedBorderColor = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.background,
                        focusedTextColor = PlaylistMakerAppTheme.colors.textPrimary,
                        unfocusedTextColor = PlaylistMakerAppTheme.colors.textPrimary,
                        cursorColor = PrimaryBlue,
                        focusedContainerColor = PlaylistMakerAppTheme.colors.surface,
                        unfocusedContainerColor = PlaylistMakerAppTheme.colors.surface,
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = if (playlistDescription.value.isNotEmpty()) PrimaryBlue else PlaylistMakerAppTheme.colors.textSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            color = if (isButtonEnabled) PrimaryBlue else PlaylistMakerAppTheme.colors.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isButtonEnabled) {
                                onSaveClick(playlistName.value, playlistDescription.value, coverImageUri.value)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.action_create),
                        color = if (isButtonEnabled) PlaylistMakerAppTheme.colors.surface else PlaylistMakerAppTheme.colors.textSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun NewPlaylistScreenPreview() {
    PlaylistMakerTheme {
        NewPlaylistScreen(
            newPlaylistViewModel = koinViewModel(),
            onBackClick = {},
            onSaveClick = { _, _, _ -> }
        )
    }
}