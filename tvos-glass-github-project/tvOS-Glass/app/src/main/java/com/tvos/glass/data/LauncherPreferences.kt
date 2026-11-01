package com.tvos.glass.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "tvos_glass_settings")

class LauncherPreferences(private val context: Context) {

    companion object {
        val KEY_CLOCK_24H = booleanPreferencesKey("clock_24h")
        val KEY_CLOCK_STYLE = intPreferencesKey("clock_style")
        val KEY_WALLPAPER_BRIGHTNESS = floatPreferencesKey("wallpaper_brightness")
        val KEY_WALLPAPER_DIM = floatPreferencesKey("wallpaper_dim")
        val KEY_FAVORITES = stringSetPreferencesKey("favorite_packages")
    }

    val is24HourClock: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_CLOCK_24H] ?: false
    }

    val clockStyle: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_CLOCK_STYLE] ?: 0
    }

    val wallpaperBrightness: Flow<Float> = context.dataStore.data.map { prefs ->
        prefs[KEY_WALLPAPER_BRIGHTNESS] ?: 1.0f
    }

    val wallpaperDim: Flow<Float> = context.dataStore.data.map { prefs ->
        prefs[KEY_WALLPAPER_DIM] ?: 0.35f
    }

    val favoriteApps: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_FAVORITES] ?: setOf(
            "com.google.android.youtube.tv",
            "com.netflix.ninja",
            "com.amazon.amazonvideo.livingroom"
        )
    }

    suspend fun set24HourClock(enabled: Boolean) {
        context.dataStore.edit { it[KEY_CLOCK_24H] = enabled }
    }

    suspend fun setClockStyle(style: Int) {
        context.dataStore.edit { it[KEY_CLOCK_STYLE] = style }
    }

    suspend fun setWallpaperBrightness(brightness: Float) {
        context.dataStore.edit { it[KEY_WALLPAPER_BRIGHTNESS] = brightness }
    }

    suspend fun setWallpaperDim(dim: Float) {
        context.dataStore.edit { it[KEY_WALLPAPER_DIM] = dim }
    }

    suspend fun toggleFavorite(packageName: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_FAVORITES] ?: emptySet()
            if (current.contains(packageName)) {
                prefs[KEY_FAVORITES] = current - packageName
            } else {
                prefs[KEY_FAVORITES] = current + packageName
            }
        }
    }
}
