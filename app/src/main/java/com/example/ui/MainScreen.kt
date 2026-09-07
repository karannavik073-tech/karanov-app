package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.R
import com.example.ui.components.AnimatedKLogo
import com.example.ui.components.ApiKeyDialog
import com.example.ui.components.ChatBubble
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.components.SuggestionChips
import com.example.ui.components.VoiceInputDialog
import com.example.ui.theme.DarkSleekActionMidnight
import com.example.ui.theme.DarkSleekBackground
import com.example.ui.theme.DarkSleekBadgeBg
import com.example.ui.theme.DarkSleekBadgeText
import com.example.ui.theme.DarkSleekBorder
import com.example.ui.theme.DarkSleekOnActionMidnight
import com.example.ui.theme.DarkSleekSurface
import com.example.ui.theme.DarkSleekSurfaceVariant
import com.example.ui.theme.DarkSleekTextPrimary
import com.example.ui.theme.DarkSleekTextSecondary
import com.example.ui.theme.GeminiBlue
import com.example.ui.theme.GeminiCyan
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.SleekActionMidnight
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekBadgeBg
import com.example.ui.theme.SleekBadgeText
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderLight
import com.example.ui.theme.SleekGreen
import com.example.ui.theme.SleekOrange
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSlate200
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekSurfaceVariant
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isDark = isSystemInDarkTheme()

    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val isRealTimeSearchEnabled by viewModel.isRealTimeSearchEnabled.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val currentSessionId by viewModel.currentSessionId.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val rmsDb by viewModel.rmsDb.collectAsState()
    val speechPartialText by viewModel.speechPartialText.collectAsState()
    val customApiKey by viewModel.customApiKey.collectAsState()
    val currentlyPlayingAudioId by viewModel.currentlyPlayingAudioId.collectAsState()

    val showApiKeyDialog by viewModel.showApiKeyDialog.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val showVoiceDialog by viewModel.showVoiceDialog.collectAsState()
    var showProfileDialog by remember { mutableStateOf(false) }

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll to bottom on new messages
    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Permission launcher for Voice Recognition
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceRecognition()
        }
    }

    fun requestVoiceInput() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            viewModel.startVoiceRecognition()
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = if (isDark) DarkSleekSurface else SleekSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Drawer Header with User Profile Photo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                scope.launch { drawerState.close() }
                                showProfileDialog = true
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnimatedKLogo(
                            size = 42.dp,
                            elevation = 3.dp
                        )

                        Column {
                            Text(
                                text = "Jaykishan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                            )
                            Text(
                                text = "Karanov AI • Pro",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // New Chat Action
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) DarkSleekSurfaceVariant else SleekSlate100,
                        border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.createNewSession()
                                scope.launch { drawerState.close() }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Chat",
                                tint = SleekBlue
                            )
                            Text(
                                text = "New Conversation",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = if (isDark) DarkSleekBorder else SleekBorderLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Sessions List
                    Text(
                        text = "Recent Chats",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(sessions) { session ->
                            val isSelected = session.id == currentSessionId
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) {
                                            if (isDark) DarkSleekSurfaceVariant else SleekSlate200
                                        } else Color.Transparent
                                    )
                                    .clickable {
                                        viewModel.selectSession(session.id)
                                        scope.launch { drawerState.close() }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Forum,
                                        contentDescription = null,
                                        tint = if (isSelected) SleekBlue else (if (isDark) DarkSleekTextSecondary else SleekTextSecondary),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = session.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.deleteSession(session.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete chat",
                                        tint = (if (isDark) DarkSleekTextSecondary else SleekTextSecondary).copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Clear All Chats Button
                    if (sessions.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                viewModel.clearAllChats()
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Clear All History",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) DarkSleekBackground else SleekBackground)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
            containerColor = if (isDark) DarkSleekBackground else SleekBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Karanov AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isRealTimeSearchEnabled) SleekGreen else (if (isDark) DarkSleekTextSecondary else SleekTextSecondary))
                                )
                                Text(
                                    text = if (isRealTimeSearchEnabled) "REAL-TIME CONNECTED" else "FAST MODE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.8.sp,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        Box(modifier = Modifier.padding(start = 12.dp, end = 6.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = if (isDark) DarkSleekSurface else SleekSurface,
                                border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                                shadowElevation = 1.dp,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                            ) {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .testTag("drawer_menu_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open Chat History",
                                        tint = if (isDark) DarkSleekTextPrimary else SleekTextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        // Language Chip Button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDark) DarkSleekBadgeBg else SleekBadgeBg,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.showLanguageDialog.value = true }
                                .testTag("language_selector_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = currentLanguage.flag, fontSize = 12.sp)
                                Text(
                                    text = currentLanguage.nativeName.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    fontSize = 10.sp,
                                    color = if (isDark) DarkSleekBadgeText else SleekBadgeText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Settings Icon
                        Surface(
                            shape = CircleShape,
                            color = if (isDark) DarkSleekSurface else SleekSurface,
                            border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        ) {
                            IconButton(
                                onClick = { viewModel.showApiKeyDialog.value = true },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Gemini API Settings",
                                    tint = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // New Chat
                        Surface(
                            shape = CircleShape,
                            color = if (isDark) DarkSleekSurface else SleekSurface,
                            border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        ) {
                            IconButton(
                                onClick = { viewModel.createNewSession() },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("new_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Conversation",
                                    tint = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // User Profile K Logo Button in TopAppBar
                        AnimatedKLogo(
                            size = 36.dp,
                            elevation = 2.dp,
                            modifier = Modifier
                                .clickable { showProfileDialog = true }
                                .testTag("profile_avatar_button")
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isDark) DarkSleekBackground else SleekBackground
                    )
                )
            },
            bottomBar = {
                // Bottom Input & Controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isDark) DarkSleekBackground else SleekBackground)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Real-Time Search Toggle Badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isRealTimeSearchEnabled) {
                                if (isDark) DarkSleekSurfaceVariant else SleekSlate100
                            } else {
                                if (isDark) DarkSleekSurface else SleekSurface
                            },
                            border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { viewModel.toggleRealTimeSearch() }
                                .testTag("real_time_search_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isRealTimeSearchEnabled) SleekGreen else (if (isDark) DarkSleekTextSecondary else SleekTextSecondary))
                                )
                                Text(
                                    text = if (isRealTimeSearchEnabled) "LIVE WEB: CONNECTED" else "LIVE WEB: OFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = if (isRealTimeSearchEnabled) SleekBlue else (if (isDark) DarkSleekTextSecondary else SleekTextSecondary),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Text(
                            text = currentLanguage.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = (if (isDark) DarkSleekTextSecondary else SleekTextSecondary).copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }

                    // Input Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Voice Mic Button
                        val micTransition = rememberInfiniteTransition(label = "micPulse")
                        val micScale by micTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = if (isListening) 1.25f else 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(600, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "micScale"
                        )

                        Surface(
                            shape = CircleShape,
                            color = if (isListening) GeminiPurple else (if (isDark) DarkSleekSurface else SleekSurface),
                            border = if (!isListening) BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight) else null,
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .size(48.dp)
                                .scale(if (isListening) micScale else 1f)
                                .shadow(1.dp, CircleShape)
                                .clip(CircleShape)
                                .clickable { requestVoiceInput() }
                                .testTag("voice_input_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Input",
                                    tint = if (isListening) Color.White else (if (isDark) DarkSleekTextPrimary else SleekActionMidnight),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        // Text Field
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    text = currentLanguage.inputPlaceholder,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = (if (isDark) DarkSleekTextSecondary else SleekTextSecondary).copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .shadow(1.dp, RoundedCornerShape(24.dp))
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(24.dp),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = if (isDark) DarkSleekSurface else SleekSurface,
                                unfocusedContainerColor = if (isDark) DarkSleekSurface else SleekSurface,
                                focusedBorderColor = SleekBlue,
                                unfocusedBorderColor = if (isDark) DarkSleekBorder else SleekBorderLight,
                                focusedTextColor = if (isDark) DarkSleekTextPrimary else SleekTextPrimary,
                                unfocusedTextColor = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                            )
                        )

                        // Send Button
                        val canSend = inputText.isNotBlank() && !isGenerating
                        val sendBgColor by animateColorAsState(
                            targetValue = if (canSend) {
                                if (isDark) DarkSleekActionMidnight else SleekActionMidnight
                            } else {
                                if (isDark) DarkSleekSurfaceVariant else SleekSurfaceVariant
                            },
                            label = "sendBg"
                        )

                        Surface(
                            shape = CircleShape,
                            color = sendBgColor,
                            shadowElevation = if (canSend) 2.dp else 0.dp,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .clickable(enabled = canSend) {
                                    val textToSend = inputText
                                    inputText = ""
                                    viewModel.sendMessage(textToSend)
                                }
                                .testTag("send_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send Message",
                                    tint = if (canSend) {
                                        if (isDark) DarkSleekOnActionMidnight else Color.White
                                    } else {
                                        (if (isDark) DarkSleekTextSecondary else SleekTextSecondary).copy(alpha = 0.4f)
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) DarkSleekBackground else SleekBackground)
                    .padding(paddingValues)
            ) {
                if (messages.isEmpty()) {
                    // Empty Greeting View
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Sleek Hero Avatar & AI Sparkle Badge
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { showProfileDialog = true }
                                .testTag("hero_avatar_badge")
                        ) {
                            AnimatedKLogo(
                                size = 80.dp,
                                elevation = 6.dp
                            )

                            // Sleek Karanov Sparkle Mini Badge
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(GeminiBlue, GeminiPurple)
                                        )
                                    )
                                    .border(2.dp, if (isDark) DarkSleekBackground else SleekBackground, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Karanov AI",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = currentLanguage.greetingTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentLanguage.greetingSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Sleek Capability Indicator Badges
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(bottom = 24.dp)
                        ) {
                            // Multi-Language Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isDark) DarkSleekSurface else SleekSurface,
                                border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                                shadowElevation = 1.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(SleekBlue)
                                    )
                                    Text(
                                        text = "Multi-Language",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Voice Enabled Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isDark) DarkSleekSurface else SleekSurface,
                                border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                                shadowElevation = 1.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(SleekOrange)
                                    )
                                    Text(
                                        text = "Voice Enabled",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Live Web Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isDark) DarkSleekSurface else SleekSurface,
                                border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                                shadowElevation = 1.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(SleekGreen)
                                    )
                                    Text(
                                        text = "Real-Time Web",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Quick Prompts & Real-Time Suggestions
                        Text(
                            text = "Try asking / यह पूछ कर देखें:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        SuggestionChips(
                            prompts = currentLanguage.quickPrompts,
                            onSelectPrompt = { prompt ->
                                viewModel.sendMessage(prompt)
                            }
                        )
                    }
                } else {
                    // Message stream
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(messages, key = { it.id }) { message ->
                            ChatBubble(
                                message = message,
                                isPlayingAudio = currentlyPlayingAudioId == message.id,
                                onPlayAudio = { viewModel.playOrStopTts(message) },
                                onStopAudio = { viewModel.stopTts() }
                            )
                        }

                        // Thinking / Searching indicator
                        if (isGenerating) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
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
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isDark) DarkSleekSurface else SleekSurface,
                                        border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                                        shadowElevation = 1.dp
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            if (isRealTimeSearchEnabled) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(SleekGreen)
                                                )
                                                Text(
                                                    text = "Searching real-time web...",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = SleekBlue,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            } else {
                                                Text(
                                                    text = "Karanov AI is thinking...",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary
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
        }
    }

    // Dialogs
    if (showLanguageDialog) {
        LanguageSelectorDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { lang ->
                viewModel.setLanguage(lang)
            },
            onDismiss = { viewModel.showLanguageDialog.value = false }
        )
    }

    if (showApiKeyDialog) {
        ApiKeyDialog(
            customApiKey = customApiKey,
            onSaveKey = { key ->
                viewModel.saveCustomApiKey(key)
            },
            onDismiss = { viewModel.showApiKeyDialog.value = false }
        )
    }

    if (showVoiceDialog) {
        VoiceInputDialog(
            language = currentLanguage,
            partialText = speechPartialText,
            rmsDb = rmsDb,
            isListening = isListening,
            onDismiss = { viewModel.stopVoiceRecognition() },
            onSubmitText = { text ->
                viewModel.stopVoiceRecognition()
                viewModel.sendMessage(text, triggeredByVoice = true)
            }
        )
    }

    if (showProfileDialog) {
        Dialog(onDismissRequest = { showProfileDialog = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = if (isDark) DarkSleekSurface else SleekSurface,
                border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated K Logo with vibrant color animation and shadow
                    AnimatedKLogo(
                        size = 110.dp,
                        elevation = 8.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Jaykishan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "jaykishann597@gmail.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sleek Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDark) DarkSleekBadgeBg else SleekBadgeBg,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SleekGreen)
                                )
                                Text(
                                    text = "PRO USER",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = if (isDark) DarkSleekBadgeText else SleekBadgeText
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDark) DarkSleekSurfaceVariant else SleekSlate100,
                            border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight)
                        ) {
                            Text(
                                text = "Gemini 2.5 Flash",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Creator Attribution Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isDark) DarkSleekSurfaceVariant else SleekSlate100,
                        border = BorderStroke(1.dp, if (isDark) DarkSleekBorder else SleekBorderLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Crafted with ❤️ by Karan Navik",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) DarkSleekTextPrimary else SleekTextPrimary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "📍 Anguli, Khuthan, Jaunpur, Uttar Pradesh",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = if (isDark) DarkSleekTextSecondary else SleekTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { showProfileDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) DarkSleekActionMidnight else SleekActionMidnight,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Done",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
