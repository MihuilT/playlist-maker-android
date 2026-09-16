package com.myapp.playlistmaker.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.myapp.playlistmaker.R
import com.myapp.playlistmaker.ui.theme.PlaylistMakerTheme
import com.myapp.playlistmaker.ui.theme.PlaylistMakerAppTheme
private fun shareApp(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message))
    }
    val chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.settings_share_app))
    context.startActivity(chooserIntent)
}
private fun openUserAgreement(context: Context) {
    val agreementUri = context.getString(R.string.offer_url).toUri()
    val browserIntent = Intent(Intent.ACTION_VIEW, agreementUri)

    try {
        context.startActivity(browserIntent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            R.string.toast_browser_not_found,
            Toast.LENGTH_SHORT
        ).show()
    }
}


@Composable
fun SettingsItem(
    text: String,
    showCustomSwitch: Boolean = false,
    showIcon: Boolean = false,
    showArrow: Boolean = false,
    iconResId: Int = 0,
    switchState: Boolean = false,
    onSwitchChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(PlaylistMakerAppTheme.colors.surface)
            .clickable { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = PlaylistMakerAppTheme.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            when {
                showCustomSwitch -> {
                    ThemeSwitch(
                        checked = switchState,
                        onCheckedChange = onSwitchChange ?: {}
                    )
                }
                showIcon -> {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = PlaylistMakerAppTheme.colors.textSecondary
                    )
                }
                showArrow -> {
                    Icon(
                        painter = painterResource(id = R.drawable.glyph_7vi5zo),
                        contentDescription = stringResource(R.string.content_desc_arrow),
                        modifier = Modifier.size(24.dp),
                        tint = PlaylistMakerAppTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}


@Composable
fun ThemeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbPosition by animateDpAsState(
        targetValue = if (checked) 21.dp else 0.dp,
        animationSpec = tween(durationMillis = 200)
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) PlaylistMakerAppTheme.colors.switchTrackEnabled  else PlaylistMakerAppTheme.colors.switchTrackDisabled,
        animationSpec = tween(durationMillis = 200)
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) PlaylistMakerAppTheme.colors.switchThumbEnabled else PlaylistMakerAppTheme.colors.switchThumbDisabled,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = modifier
            .width(35.dp)
            .height(24.dp)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(35.dp)
                .height(14.dp)
                .background(trackColor, RoundedCornerShape(7.dp))
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = thumbPosition)
                .background(thumbColor, CircleShape)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SettingsScreenPreview() {
    PlaylistMakerTheme {
        SettingsScreen(
            onBackClick = {},
            isDarkThemeEnabled = false,
            onDarkThemeToggle = { _ -> }
        )
    }
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    isDarkThemeEnabled: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit
) {
    val context = LocalContext.current

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
                    text = stringResource(R.string.settings_title),
                    color = PlaylistMakerAppTheme.colors.textPrimary ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PlaylistMakerAppTheme.colors.surface)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            SettingsItem(
                text = stringResource(R.string.settings_dark_theme),
                showCustomSwitch = true,
                switchState = isDarkThemeEnabled,
                onSwitchChange = onDarkThemeToggle
            )

            SettingsItem(
                text = stringResource(R.string.settings_share_app),
                showIcon = true,
                iconResId = R.drawable.glyph_1if889w,
                onClick = {
                    shareApp(context)
                }
            )

            SettingsItem(
                text = stringResource(R.string.settings_email_developers),
                showIcon = true,
                iconResId = R.drawable.glyph_gd12cc,
                onClick = {
                    writeToDevelopers(context)
                }
            )

            SettingsItem(
                text = stringResource(R.string.settings_user_agreement),
                showArrow = true,
                onClick = {
                    openUserAgreement(context)
                }
            )
        }
    }
}

private fun writeToDevelopers(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_email_subject))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_email_body))
    }

    try {
        context.startActivity(emailIntent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            R.string.toast_email_app_not_found,
            Toast.LENGTH_SHORT
        ).show()
    }
}
