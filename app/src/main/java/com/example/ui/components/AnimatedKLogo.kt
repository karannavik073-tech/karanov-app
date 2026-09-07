package com.example.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Modern animated "K" logo for Karanov AI with continuous multi-color animation.
 * Features a smoothly revolving rainbow/vibrant gradient spectrum and animated pulsing border.
 */
@Composable
fun AnimatedKLogo(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    showBorder: Boolean = true,
    elevation: Dp = 2.dp
) {
    val isDark = isSystemInDarkTheme()
    val infiniteTransition = rememberInfiniteTransition(label = "k_logo_animation")

    // Smooth continuous rotation of the vibrant color spectrum
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spectrum_rotation"
    )

    // Dynamic cycling border colors
    val animatedBorderColor1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF38BDF8), // Cyan
        targetValue = Color(0xFFEC4899), // Pink
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_c1"
    )

    val animatedBorderColor2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF818CF8), // Indigo
        targetValue = Color(0xFF34D399), // Emerald
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_c2"
    )

    // Rich spectrum of vibrant colors
    val colorSpectrum = listOf(
        Color(0xFF2563EB), // Sleek Royal Blue
        Color(0xFF7C3AED), // Vivid Purple
        Color(0xFFEC4899), // Neon Pink
        Color(0xFFF97316), // Warm Orange
        Color(0xFF10B981), // Emerald Green
        Color(0xFF06B6D4), // Cyan
        Color(0xFF4F46E5), // Indigo
        Color(0xFF2563EB)  // Back to Royal Blue for seamless loop
    )

    val fontSize = (size.value * 0.52f).sp

    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = elevation,
                shape = CircleShape,
                ambientColor = animatedBorderColor1.copy(alpha = 0.4f),
                spotColor = animatedBorderColor2.copy(alpha = 0.5f)
            )
            .then(
                if (showBorder) {
                    Modifier.border(
                        BorderStroke(
                            width = if (size > 60.dp) 2.5.dp else 1.5.dp,
                            brush = Brush.sweepGradient(
                                listOf(animatedBorderColor1, animatedBorderColor2, animatedBorderColor1)
                            )
                        ),
                        shape = CircleShape
                    )
                } else Modifier
            )
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Rotating multi-color animated background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotationAngle)
                .background(
                    brush = Brush.sweepGradient(colorSpectrum)
                )
        )

        // Subtle dark/light glass overlay for depth and contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDark) Color.Black.copy(alpha = 0.15f)
                    else Color.White.copy(alpha = 0.05f)
                )
        )

        // Upright Bold "K" Letter (does NOT rotate, remains crystal clear & readable)
        Text(
            text = "K",
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = (size.value * 0.03f).dp)
        )
    }
}
