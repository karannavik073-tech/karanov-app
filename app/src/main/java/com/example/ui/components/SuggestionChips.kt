package com.example.ui.components

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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuickPrompt
import com.example.ui.theme.DarkSleekBorder
import com.example.ui.theme.DarkSleekSurface
import com.example.ui.theme.DarkSleekTextPrimary
import com.example.ui.theme.DarkSleekTextSecondary
import com.example.ui.theme.GeminiCyan
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekGreen
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekTextPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuggestionChips(
    prompts: List<QuickPrompt>,
    onSelectPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val chipBg = if (isDark) DarkSleekSurface else SleekSurface
    val chipBorder = if (isDark) DarkSleekBorder else SleekBorder
    val chipTextColor = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
    val chipShape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prompts.forEach { item ->
                Surface(
                    shape = chipShape,
                    color = chipBg,
                    border = BorderStroke(1.dp, chipBorder),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .shadow(1.dp, chipShape)
                        .clip(chipShape)
                        .clickable { onSelectPrompt(item.prompt) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = item.icon, fontSize = 15.sp)

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = chipTextColor,
                            fontSize = 13.sp
                        )

                        if (item.isRealTime) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(SleekGreen)
                            )
                        }
                    }
                }
            }
        }
    }
}
