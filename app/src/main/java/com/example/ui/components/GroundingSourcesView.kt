package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GroundingSource
import com.example.ui.theme.DarkSleekBorder
import com.example.ui.theme.DarkSleekSurface
import com.example.ui.theme.DarkSleekSurfaceVariant
import com.example.ui.theme.DarkSleekTextSecondary
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBorderLight
import com.example.ui.theme.SleekGreen
import com.example.ui.theme.SleekSlate50
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GroundingSourcesView(
    sources: List<GroundingSource>,
    searchQueries: String,
    modifier: Modifier = Modifier
) {
    if (sources.isEmpty() && searchQueries.isBlank()) return

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    val cardBg = if (isDark) DarkSleekSurfaceVariant else SleekSlate50
    val cardBorder = if (isDark) DarkSleekBorder else SleekBorderLight
    val pillBg = if (isDark) DarkSleekSurface else SleekSurface

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = cardBg,
        border = BorderStroke(1.dp, cardBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(SleekGreen)
                    )

                    Text(
                        text = "LIVE DATA: REAL-TIME GROUNDING (${sources.size})",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        fontSize = 10.sp,
                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Expanded view
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (searchQueries.isNotBlank()) {
                        Text(
                            text = "Searched: $searchQueries",
                            style = MaterialTheme.typography.bodySmall,
                            color = (if (isDark) DarkSleekTextSecondary else SleekTextSecondary).copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        sources.forEach { source ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(pillBg)
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source.uri))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // ignore invalid uri
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = source.title.ifBlank { "Source" },
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = SleekBlue,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(end = 2.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Open Source Link",
                                    tint = SleekBlue,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
