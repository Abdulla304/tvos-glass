package com.tvos.glass.ui.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tvos.glass.ui.glass.GlassCard
import com.tvos.glass.ui.glass.GlassSurface
import com.tvos.glass.ui.theme.GlassAccentCyan
import com.tvos.glass.ui.theme.GlassBackgroundElevated
import com.tvos.glass.ui.theme.TextPrimary
import com.tvos.glass.ui.theme.TextSecondary

@Composable
fun SettingsSheet(
    is24Hour: Boolean,
    clockStyle: Int,
    wallpaperDim: Float,
    onToggle24Hour: (Boolean) -> Unit,
    onToggleClockStyle: (Int) -> Unit,
    onChangeWallpaperDim: (Float) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        GlassSurface(
            modifier = Modifier
                .width(460.dp)
                .fillMaxHeight()
                .padding(24.dp),
            shape = RoundedCornerShape(32.dp),
            backgroundColor = GlassBackgroundElevated,
            contentElevation = 28.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Launcher Preferences",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "BACK to exit",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                SettingsToggleTile(
                    title = "24-Hour Time",
                    subtitle = if (is24Hour) "Using military time format" else "Using 12-hour AM/PM format",
                    icon = Icons.Default.Schedule,
                    isActive = is24Hour,
                    onClick = { onToggle24Hour(!is24Hour) }
                )

                SettingsToggleTile(
                    title = "Glass Date Capsule",
                    subtitle = if (clockStyle == 1) "Capsule backdrop active" else "Minimal date typography",
                    icon = Icons.Default.Schedule,
                    isActive = clockStyle == 1,
                    onClick = { onToggleClockStyle(if (clockStyle == 1) 0 else 1) }
                )

                Text(
                    text = "LUNAR ATMOSPHERE DIM",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf("Bright" to 0.20f, "Balanced" to 0.35f, "Deep OLED" to 0.55f)
                    presets.forEach { (name, dimValue) ->
                        val selected = kotlin.math.abs(wallpaperDim - dimValue) < 0.05f
                        GlassCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = if (selected) Color(0x6638BDF8) else Color(0x221E293B),
                            focusScale = 1.05f,
                            onClick = { onChangeWallpaperDim(dimValue) }
                        ) { isFocused ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name,
                                    color = if (selected || isFocused) TextPrimary else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "SYSTEM INTEGRATION",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                SettingsToggleTile(
                    title = "Set as Default TV Launcher",
                    subtitle = "Open Android home selector settings",
                    icon = Icons.Default.Home,
                    isActive = false,
                    onClick = { openHomeSelectionSettings(context) }
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x261E293B),
        focusScale = 1.04f,
        onClick = onClick
    ) { isFocused ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isFocused) GlassAccentCyan else Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isFocused) TextPrimary else Color(0xFFE4E4E7),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Active",
                    tint = GlassAccentCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun openHomeSelectionSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val fallback = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallback)
        } catch (_: Exception) {}
    }
}
