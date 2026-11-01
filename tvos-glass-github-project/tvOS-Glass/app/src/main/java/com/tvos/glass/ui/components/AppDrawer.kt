package com.tvos.glass.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.tvos.glass.data.AppModel
import com.tvos.glass.ui.glass.GlassCard
import com.tvos.glass.ui.glass.GlassSurface
import com.tvos.glass.ui.theme.GlassBackgroundElevated
import com.tvos.glass.ui.theme.TextPrimary
import com.tvos.glass.ui.theme.TextSecondary

@Composable
fun AppDrawer(
    apps: List<AppModel>,
    onAppClick: (AppModel) -> Unit,
    onToggleFavorite: (AppModel) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        GlassSurface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(32.dp),
            backgroundColor = GlassBackgroundElevated,
            contentElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Application Library",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${apps.size} Installed Applications",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(apps, key = { it.packageName }) { app ->
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            focusScale = 1.12f,
                            onClick = { onAppClick(app) }
                        ) { isFocused ->
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    app.icon?.let { drawable ->
                                        val bitmap = remember(app.packageName) {
                                            drawable.toBitmap(width = 96, height = 96).asImageBitmap()
                                        }
                                        Image(
                                            bitmap = bitmap,
                                            contentDescription = app.label,
                                            modifier = Modifier
                                                .size(52.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = app.label,
                                        color = if (isFocused) TextPrimary else TextSecondary,
                                        fontSize = 13.sp,
                                        fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (app.isFavorite) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Favorite",
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
