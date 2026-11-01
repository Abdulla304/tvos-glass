package com.tvos.glass.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tvos.glass.data.AppModel
import com.tvos.glass.data.AppRepository
import com.tvos.glass.data.LauncherPreferences
import com.tvos.glass.ui.components.*
import com.tvos.glass.ui.glass.GlassCard
import com.tvos.glass.ui.theme.GlassAccentCyan
import com.tvos.glass.ui.wallpaper.MoonWallpaper
import kotlinx.coroutines.launch

enum class ScreenOverlay {
    NONE,
    APP_DRAWER,
    CONTROL_CENTER,
    SETTINGS_SHEET
}

@Composable
fun HomeScreen(
    appRepository: AppRepository,
    preferences: LauncherPreferences
) {
    val coroutineScope = rememberCoroutineScope()

    val is24Hour by preferences.is24HourClock.collectAsState(initial = false)
    val clockStyle by preferences.clockStyle.collectAsState(initial = 1)
    val wallpaperDim by preferences.wallpaperDim.collectAsState(initial = 0.35f)
    val wallpaperBrightness by preferences.wallpaperBrightness.collectAsState(initial = 1.0f)
    val favoriteKeys by preferences.favoriteApps.collectAsState(initial = emptySet())

    var installedApps by remember { mutableStateOf<List<AppModel>>(emptyList()) }
    var activeOverlay by remember { mutableStateOf(ScreenOverlay.NONE) }
    val dockFocusRequester = remember { FocusRequester() }

    LaunchedEffect(favoriteKeys) {
        installedApps = appRepository.loadInstalledApps(favoriteKeys)
    }

    LaunchedEffect(installedApps) {
        if (installedApps.isNotEmpty() && activeOverlay == ScreenOverlay.NONE) {
            try {
                dockFocusRequester.requestFocus()
            } catch (_: Exception) {}
        }
    }

    BackHandler(enabled = activeOverlay != ScreenOverlay.NONE) {
        activeOverlay = ScreenOverlay.NONE
        try {
            dockFocusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MoonWallpaper(
            dimAlpha = wallpaperDim,
            brightness = wallpaperBrightness
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                GlassClock(
                    is24Hour = is24Hour,
                    useCapsule = (clockStyle == 1)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassWidgets()

                    GlassCard(
                        modifier = Modifier.size(46.dp),
                        shape = CircleShape,
                        backgroundColor = Color(0x381E293B),
                        focusScale = 1.15f,
                        onClick = {
                            activeOverlay = if (activeOverlay == ScreenOverlay.CONTROL_CENTER) {
                                ScreenOverlay.NONE
                            } else {
                                ScreenOverlay.CONTROL_CENTER
                            }
                        }
                    ) { isFocused ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Control Center",
                                tint = if (isFocused) GlassAccentCyan else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    GlassCard(
                        modifier = Modifier.size(46.dp),
                        shape = CircleShape,
                        backgroundColor = Color(0x381E293B),
                        focusScale = 1.15f,
                        onClick = {
                            activeOverlay = if (activeOverlay == ScreenOverlay.SETTINGS_SHEET) {
                                ScreenOverlay.NONE
                            } else {
                                ScreenOverlay.SETTINGS_SHEET
                            }
                        }
                    ) { isFocused ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = if (isFocused) GlassAccentCyan else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassCard(
                        modifier = Modifier
                            .width(170.dp)
                            .height(44.dp),
                        shape = CircleShape,
                        backgroundColor = Color(0x2E1E293B),
                        focusScale = 1.08f,
                        onClick = {
                            activeOverlay = if (activeOverlay == ScreenOverlay.APP_DRAWER) {
                                ScreenOverlay.NONE
                            } else {
                                ScreenOverlay.APP_DRAWER
                            }
                        }
                    ) { isFocused ->
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Apps,
                                contentDescription = "Apps",
                                tint = if (isFocused) GlassAccentCyan else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            androidx.compose.material3.Text(
                                text = "All Apps",
                                color = if (isFocused) Color.White else Color(0xFFA1A1AA),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                val favoriteApps = remember(installedApps, favoriteKeys) {
                    installedApps.filter { it.isFavorite }
                        .ifEmpty { installedApps.take(6) }
                }

                GlassDock(
                    modifier = Modifier.fillMaxWidth(),
                    apps = favoriteApps,
                    onAppClick = { app -> appRepository.launchApp(app) },
                    firstItemFocusRequester = dockFocusRequester
                )
            }
        }

        AnimatedVisibility(
            visible = activeOverlay == ScreenOverlay.APP_DRAWER,
            enter = fadeIn(tween(250)) + scaleIn(tween(250), initialScale = 0.94f),
            exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.94f)
        ) {
            AppDrawer(
                apps = installedApps,
                onAppClick = { app ->
                    activeOverlay = ScreenOverlay.NONE
                    appRepository.launchApp(app)
                },
                onToggleFavorite = { app ->
                    coroutineScope.launch {
                        preferences.toggleFavorite(app.packageName)
                    }
                }
            )
        }

        AnimatedVisibility(
            visible = activeOverlay == ScreenOverlay.CONTROL_CENTER,
            enter = slideInHorizontally(tween(280), initialOffsetX = { it }) + fadeIn(tween(280)),
            exit = slideOutHorizontally(tween(220), targetOffsetX = { it }) + fadeOut(tween(220))
        ) {
            ControlCenter(
                onDismissRequest = { activeOverlay = ScreenOverlay.NONE },
                onOpenSettingsSheet = { activeOverlay = ScreenOverlay.SETTINGS_SHEET }
            )
        }

        AnimatedVisibility(
            visible = activeOverlay == ScreenOverlay.SETTINGS_SHEET,
            enter = slideInHorizontally(tween(280), initialOffsetX = { it }) + fadeIn(tween(280)),
            exit = slideOutHorizontally(tween(220), targetOffsetX = { it }) + fadeOut(tween(220))
        ) {
            SettingsSheet(
                is24Hour = is24Hour,
                clockStyle = clockStyle,
                wallpaperDim = wallpaperDim,
                onToggle24Hour = { coroutineScope.launch { preferences.set24HourClock(it) } },
                onToggleClockStyle = { coroutineScope.launch { preferences.setClockStyle(it) } },
                onChangeWallpaperDim = { coroutineScope.launch { preferences.setWallpaperDim(it) } },
                onClose = { activeOverlay = ScreenOverlay.NONE }
            )
        }
    }
}
