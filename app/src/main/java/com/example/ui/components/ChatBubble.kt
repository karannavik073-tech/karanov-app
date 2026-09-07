package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatMessage
import com.example.ui.theme.DarkSleekBorder
import com.example.ui.theme.DarkSleekSurface
import com.example.ui.theme.DarkSleekTextPrimary
import com.example.ui.theme.DarkSleekTextSecondary
import com.example.ui.theme.DarkSleekUserBubble
import com.example.ui.theme.DarkSleekUserText
import com.example.ui.theme.GeminiBlue
import com.example.ui.theme.GeminiCyan
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBorderLight
import com.example.ui.theme.SleekGreen
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary
import com.example.ui.theme.SleekUserBubble
import com.example.ui.theme.SleekUserText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatBubble(
    message: ChatMessage,
    isPlayingAudio: Boolean,
    onPlayAudio: () -> Unit,
    onStopAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isUser = message.isUser
    val isDark = isSystemInDarkTheme()

    val timeString = rememberFormattedTime(message.timestamp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            // Karanov AI Avatar Icon - Sleek mini badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(GeminiBlue, GeminiPurple)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Karanov AI",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 330.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Sleek Bubble Body
            val bubbleShape = if (isUser) {
                RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
            } else {
                RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
            }

            val bubbleBgColor = if (isUser) {
                if (isDark) DarkSleekUserBubble else SleekUserBubble
            } else {
                if (isDark) DarkSleekSurface else SleekSurface
            }

            val bubbleTextColor = if (isUser) {
                if (isDark) DarkSleekUserText else SleekUserText
            } else {
                if (isDark) DarkSleekTextPrimary else SleekTextPrimary
            }

            val bubbleBorder = if (!isUser) {
                BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight)
            } else null

            Surface(
                shape = bubbleShape,
                color = bubbleBgColor,
                border = bubbleBorder,
                shadowElevation = 1.dp,
                modifier = Modifier.shadow(1.dp, bubbleShape)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Response Header for AI messages
                    if (!isUser) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SleekBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "K",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "RESPONSE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            )

                            if (message.isRealTimeGrounded) {
                                Spacer(modifier = Modifier.weight(1f))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isDark) DarkSleekBorder else SleekSlate100)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(SleekGreen)
                                    )
                                    Text(
                                        text = "LIVE DATA",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary
                                    )
                                }
                            }
                        }
                    }

                    // Text Content
                    FormattedMessageText(
                        text = message.text,
                        textColor = bubbleTextColor
                    )

                    // Real-Time Grounding Sources if present
                    if (!isUser) {
                        val sources = message.getGroundingSources()
                        if (sources.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            GroundingSourcesView(
                                sources = sources,
                                searchQueries = message.searchQueries
                            )
                        }
                    }
                }
            }

            // Footer row with timestamp and AI actions
            Row(
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (isDark) DarkSleekTextSecondary else SleekTextSecondary).copy(alpha = 0.8f),
                    fontSize = 10.sp
                )

                if (!isUser) {
                    // Text-To-Speech Play/Stop Action Button
                    val audioColor by animateColorAsState(
                        targetValue = if (isPlayingAudio) GeminiCyan else (if (isDark) DarkSleekTextSecondary else SleekTextSecondary),
                        label = "audioColor"
                    )

                    IconButton(
                        onClick = {
                            if (isPlayingAudio) onStopAudio() else onPlayAudio()
                        },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = if (isPlayingAudio) "Stop Reading" else "Read Aloud",
                            tint = audioColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if (isPlayingAudio) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Playing Audio",
                            tint = GeminiCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Copy button
                    IconButton(
                        onClick = {
                            copyToClipboard(context, message.text)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            tint = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            AnimatedKLogo(
                size = 32.dp,
                elevation = 1.dp
            )
        }
    }
}

@Composable
private fun FormattedMessageText(text: String, textColor: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            lineHeight = 22.sp,
            letterSpacing = 0.2.sp
        ),
        color = textColor
    )
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Karanov AI Message", text)
    clipboard.setPrimaryClip(clip)
}

@Composable
private fun rememberFormattedTime(timestamp: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
}
