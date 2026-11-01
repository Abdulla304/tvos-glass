package com.tvos.glass.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.tvos.glass.data.AppModel
import com.tvos.glass.ui.glass.GlassCard
import com.tvos.glass.ui.glass.GlassSurface
import com.tvos.glass.ui.theme.GlassBackgroundSmoky
import com.tvos.glass.ui.theme.TextPrimary
import com.tvos.glass.ui.theme.TextSecondary

@Composable
fun GlassDock(
    modifier: Modifier = Modifier,
    apps: List<AppModel>,
    onAppClick: (AppModel) -> Unit,
    firstItemFocusRequester: FocusRequester = remember { FocusRequester() }
) {
    val listState = rememberLazyListState()

    GlassSurface(
        modifier = modifier.height(144.dp),
        shape = RoundedCornerShape(32.dp),
        backgroundColor = GlassBackgroundSmoky,
        contentElevation = 16.dp
    ) {
        if (apps.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite applications pinned to dock",
                    color = TextSecondary,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(apps, key = { _, item -> item.packageName }) { index, app ->
                    val requester = if (index == 0) firstItemFocusRequester else remember { FocusRequester() }

                    GlassCard(
                        modifier = Modifier
                            .width(130.dp)
                            .height(110.dp)
                            .focusRequester(requester),
                        focusScale = 1.15f,
                        onClick = { onAppClick(app) }
                    ) { isFocused ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
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
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = app.label,
                                color = if (isFocused) TextPrimary else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
