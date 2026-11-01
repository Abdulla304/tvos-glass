package com.tvos.glass.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tvos.glass.ui.glass.GlassSurface
import com.tvos.glass.ui.theme.TextPrimary
import com.tvos.glass.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GlassClock(
    modifier: Modifier = Modifier,
    is24Hour: Boolean = false,
    useCapsule: Boolean = true
) {
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000L)
        }
    }

    val timePattern = if (is24Hour) "HH:mm" else "h:mm"
    val timeFormatter = remember(is24Hour) { SimpleDateFormat(timePattern, Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        if (useCapsule) {
            GlassSurface(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0x331E293B),
                contentElevation = 4.dp
            ) {
                Text(
                    text = dateFormatter.format(currentTime).uppercase(Locale.getDefault()),
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.6.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        } else {
            Text(
                text = dateFormatter.format(currentTime).uppercase(Locale.getDefault()),
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.4.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = timeFormatter.format(currentTime),
            color = TextPrimary,
            fontSize = 78.sp,
            fontWeight = FontWeight.ExtraLight,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-2).sp
        )
    }
}
