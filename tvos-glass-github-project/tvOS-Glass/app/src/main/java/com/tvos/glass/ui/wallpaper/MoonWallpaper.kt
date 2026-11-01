package com.tvos.glass.ui.wallpaper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.tvos.glass.ui.theme.OledBackground
import kotlin.random.Random

@Composable
fun MoonWallpaper(
    dimAlpha: Float = 0.35f,
    brightness: Float = 1.0f
) {
    val stars = remember {
        val rand = Random(42)
        List(140) {
            Triple(rand.nextFloat(), rand.nextFloat(), rand.nextFloat() * 1.8f + 0.4f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OledBackground)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            for (star in stars) {
                drawCircle(
                    color = Color.White.copy(alpha = (star.third / 2.5f) * brightness),
                    radius = star.third,
                    center = Offset(star.first * width, star.second * height)
                )
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x1F223252),
                        Color(0x0A09101E),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.75f, height * 0.38f),
                    radius = width * 0.55f
                ),
                radius = width * 0.55f,
                center = Offset(width * 0.75f, height * 0.38f)
            )

            val moonCenter = Offset(width * 0.76f, height * 0.38f)
            val moonRadius = height * 0.28f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x35CFE2FE),
                        Color(0x1593C5FD),
                        Color(0x0538BDF8),
                        Color.Transparent
                    ),
                    center = moonCenter,
                    radius = moonRadius * 2.1f
                ),
                radius = moonRadius * 2.1f,
                center = moonCenter
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF1F5F9),
                        Color(0xFFE2E8F0),
                        Color(0xFFCBD5E1),
                        Color(0xFF64748B),
                        Color(0xFF1E293B)
                    ),
                    center = Offset(moonCenter.x - moonRadius * 0.25f, moonCenter.y - moonRadius * 0.25f),
                    radius = moonRadius
                ),
                radius = moonRadius,
                center = moonCenter
            )

            val craters = listOf(
                Triple(-0.2f, -0.1f, 0.22f),
                Triple(0.25f, -0.3f, 0.16f),
                Triple(0.1f, 0.2f, 0.28f),
                Triple(-0.35f, 0.25f, 0.14f),
                Triple(0.35f, 0.15f, 0.19f)
            )

            for (crater in craters) {
                val cCenter = Offset(
                    moonCenter.x + crater.first * moonRadius,
                    moonCenter.y + crater.second * moonRadius
                )
                val cRad = crater.third * moonRadius

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x33334155),
                            Color(0x221E293B),
                            Color.Transparent
                        ),
                        center = cCenter,
                        radius = cRad
                    ),
                    radius = cRad,
                    center = cCenter
                )
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0x40030407),
                        Color(0xE6030407)
                    ),
                    center = Offset(moonCenter.x - moonRadius * 0.6f, moonCenter.y - moonRadius * 0.3f),
                    radius = moonRadius * 1.35f
                ),
                radius = moonRadius,
                center = moonCenter
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = dimAlpha))
        )
    }
}
