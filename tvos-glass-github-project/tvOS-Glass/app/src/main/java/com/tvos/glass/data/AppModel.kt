package com.tvos.glass.data

import android.graphics.drawable.Drawable

data class AppModel(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable?,
    val isFavorite: Boolean = false,
    val lastLaunchTime: Long = 0L
)
