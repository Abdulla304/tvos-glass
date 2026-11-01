package com.tvos.glass.ui.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tvos.glass.ui.theme.*

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = GlassBackgroundSmoky,
    isFocused: Boolean = false,
    contentElevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val animatedBorderAlpha by animateFloatAsState(
        targetValue = if (isFocused) 0.85f else 0.18f,
        animationSpec = tween(durationMillis = 220),
        label = "borderAlpha"
    )

    val animatedGlowAlpha by animateFloatAsState(
        targetValue = if (isFocused) 0.35f else 0.0f,
        animationSpec = tween(durationMillis = 220),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = if (isFocused) contentElevation * 2 else contentElevation,
                shape = shape,
                clip = false,
                ambientColor = if (isFocused) GlassAccentCyan else Color.Black,
                spotColor = if (isFocused) GlassAccentCyan else Color.Black
            )
            .clip(shape)
            .background(backgroundColor)
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GlassHighlightTop,
                            Color.Transparent,
                            GlassHighlightBottom
                        )
                    )
                )
                if (animatedGlowAlpha > 0f) {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GlassFocusGlow.copy(alpha = animatedGlowAlpha),
                                Color.Transparent
                            ),
                            radius = size.maxDimension / 1.2f
                        )
                    )
                }
            }
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = animatedBorderAlpha),
                        Color.White.copy(alpha = animatedBorderAlpha * 0.3f)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}
