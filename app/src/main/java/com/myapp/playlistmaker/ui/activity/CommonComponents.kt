package com.myapp.playlistmaker.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme

// Верхняя панель экрана: стрелка «Назад» и заголовок
@Composable
fun ScreenHeader(
    title: String,
    onBackClick: () -> Unit
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
                text = title,
                color = PlaylistMakerAppTheme.colors.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

// Подзаголовок группы параметров (жанры, настроение и т.п.)
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        color = PlaylistMakerAppTheme.colors.textPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}
