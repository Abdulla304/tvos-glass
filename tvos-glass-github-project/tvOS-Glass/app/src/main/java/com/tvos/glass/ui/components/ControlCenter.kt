package com.tvos.glass.ui.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun ControlCenter(
    onDismissRequest: () -> Unit,
    onOpenSettingsSheet: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        GlassSurface(
            modifier = Modifier
                .width(420.dp)
                .fillMaxHeight()
                .padding(24.dp),
            shape = RoundedCornerShape(32.dp),
            backgroundColor = GlassBackgroundElevated,
            contentElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Control Center",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Press BACK to close",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ControlTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Wifi,
                        label = "Wi-Fi Network",
                        onClick = { safeStartActivity(context, Intent(Settings.ACTION_WIFI_SETTINGS)) }
                    )
                    ControlTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Bluetooth,
                        label = "Bluetooth",
                        onClick = { safeStartActivity(context, Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ControlTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.VolumeUp,
                        label = "Audio / Sound",
                        onClick = { safeStartActivity(context, Intent(Settings.ACTION_SOUND_SETTINGS)) }
                    )
                    ControlTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Tv,
                        label = "Display",
                        onClick = { safeStartActivity(context, Intent(Settings.ACTION_DISPLAY_SETTINGS)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ControlTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Tune,
                        label = "Glass Settings",
                        onClick = {
                            onDismissRequest()
                            onOpenSettingsSheet()
                        }
                    )
                    ControlTile(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Settings,
                        label = "Android TV OS",
                        onClick = { safeStartActivity(context, Intent(Settings.ACTION_SETTINGS)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlTile(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0x381E293B),
        focusScale = 1.08f,
        onClick = onClick
    ) { isFocused ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isFocused) GlassAccentCyan else Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = if (isFocused) TextPrimary else TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun safeStartActivity(context: Context, intent: Intent) {
    try {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val fallback = Intent(Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallback)
        } catch (_: Exception) {}
    }
}
