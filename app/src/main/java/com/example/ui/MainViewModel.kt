package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.local.AppPreferences
import com.example.data.local.ChatDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.ChatSession
import com.example.data.model.Language
import com.example.data.model.SupportedLanguages
import com.example.data.remote.GeminiApiService
import com.example.util.AppLauncherHelper
import com.example.util.CreatorInfoHelper
import com.example.voice.TextToSpeechHelper
import com.example.voice.VoiceRecognitionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = AppPreferences(application)
    private val database = ChatDatabase.getInstance(application)
    private val chatDao = database.chatDao()
    private val geminiApi = GeminiApiService()

    // State flows
    private val _currentLanguage = MutableStateFlow(SupportedLanguages.getByCode(prefs.selectedLanguageCode))
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _isRealTimeSearchEnabled = MutableStateFlow(prefs.isRealTimeSearchEnabled)
    val isRealTimeSearchEnabled: StateFlow<Boolean> = _isRealTimeSearchEnabled.asStateFlow()

    private val _currentSessionId = MutableStateFlow<String?>(null)
    val currentSessionId: StateFlow<String?> = _currentSessionId.asStateFlow()

    val sessions: StateFlow<List<ChatSession>> = chatDao.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<ChatMessage>> = _currentSessionId.flatMapLatest { sessionId ->
        if (sessionId != null) {
            chatDao.getMessagesForSession(sessionId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    // Voice recognition states
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _rmsDb = MutableStateFlow(0f)
    val rmsDb: StateFlow<Float> = _rmsDb.asStateFlow()

    private val _speechPartialText = MutableStateFlow("")
    val speechPartialText: StateFlow<String> = _speechPartialText.asStateFlow()

    private val _customApiKey = MutableStateFlow(prefs.customApiKey)
    val customApiKey: StateFlow<String> = _customApiKey.asStateFlow()

    // UI Dialog & Sheet states
    val showApiKeyDialog = MutableStateFlow(false)
    val showLanguageDialog = MutableStateFlow(false)
    val showVoiceDialog = MutableStateFlow(false)
    val showHistoryDrawer = MutableStateFlow(false)

    // Text to Speech
    private val ttsHelper = TextToSpeechHelper(application)
    val currentlyPlayingAudioId: StateFlow<String?> = ttsHelper.currentPlayingMessageId

    // Voice Recognizer
    private var voiceHelper: VoiceRecognitionHelper? = null

    init {
        initVoiceHelper()
        loadOrCreateInitialSession()
    }

    private fun initVoiceHelper() {
        voiceHelper = VoiceRecognitionHelper(
            context = getApplication(),
            onListeningState = { listening ->
                _isListening.value = listening
                if (!listening) {
                    _rmsDb.value = 0f
                }
            },
            onRmsChanged = { rms ->
                _rmsDb.value = rms
            },
            onPartialResult = { partial ->
                _speechPartialText.value = partial
            },
            onFinalResult = { text ->
                _speechPartialText.value = ""
                showVoiceDialog.value = false
                if (text.isNotBlank()) {
                    sendMessage(text, triggeredByVoice = true)
                }
            },
            onError = { _ ->
                _isListening.value = false
                _rmsDb.value = 0f
            }
        )
    }

    private fun loadOrCreateInitialSession() {
        viewModelScope.launch {
            val savedSessionId = prefs.currentSessionId
            if (savedSessionId != null && chatDao.getSessionById(savedSessionId) != null) {
                _currentSessionId.value = savedSessionId
            } else {
                createNewSession()
            }
        }
    }

    fun createNewSession() {
        viewModelScope.launch {
            val newSession = ChatSession(
                id = UUID.randomUUID().toString(),
                title = "New Chat",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            chatDao.insertSession(newSession)
            _currentSessionId.value = newSession.id
            prefs.currentSessionId = newSession.id
        }
    }

    fun selectSession(sessionId: String) {
        _currentSessionId.value = sessionId
        prefs.currentSessionId = sessionId
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            chatDao.deleteMessagesForSession(sessionId)
            chatDao.deleteSession(sessionId)
            if (_currentSessionId.value == sessionId) {
                createNewSession()
            }
        }
    }

    fun clearAllChats() {
        viewModelScope.launch {
            chatDao.clearAllMessages()
            chatDao.clearAllSessions()
            createNewSession()
        }
    }

    fun setLanguage(language: Language) {
        _currentLanguage.value = language
        prefs.selectedLanguageCode = language.code
    }

    fun toggleRealTimeSearch() {
        val newState = !_isRealTimeSearchEnabled.value
        _isRealTimeSearchEnabled.value = newState
        prefs.isRealTimeSearchEnabled = newState
    }

    fun saveCustomApiKey(key: String) {
        _customApiKey.value = key
        prefs.customApiKey = key
    }

    fun startVoiceRecognition() {
        showVoiceDialog.value = true
        _speechPartialText.value = ""
        voiceHelper?.startListening(_currentLanguage.value.speechLocale)
    }

    fun stopVoiceRecognition() {
        voiceHelper?.stopListening()
        showVoiceDialog.value = false
    }

    fun playOrStopTts(message: ChatMessage) {
        ttsHelper.speak(message.id, message.text, message.languageCode)
    }

    fun stopTts() {
        ttsHelper.stop()
    }

    fun sendMessage(userText: String, triggeredByVoice: Boolean = false) {
        val trimmed = userText.trim()
        if (trimmed.isBlank() || _isGenerating.value) return

        val sessionId = _currentSessionId.value ?: return
        val currentLang = _currentLanguage.value
        val realTimeEnabled = _isRealTimeSearchEnabled.value

        viewModelScope.launch {
            // Save User Message
            val userMessage = ChatMessage(
                sessionId = sessionId,
                sender = "USER",
                text = trimmed,
                languageCode = currentLang.code
            )
            chatDao.insertMessage(userMessage)

            // Update session title if first message
            val session = chatDao.getSessionById(sessionId)
            if (session != null && session.title == "New Chat") {
                val newTitle = if (trimmed.length > 28) trimmed.take(28) + "..." else trimmed
                chatDao.updateSession(session.copy(title = newTitle, updatedAt = System.currentTimeMillis()))
            } else if (session != null) {
                chatDao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
            }

            // 1. Check if user commanded to open/launch an installed app
            val appLaunchResult = AppLauncherHelper.checkAndLaunchApp(
                context = getApplication(),
                rawInput = trimmed,
                languageCode = currentLang.code
            )

            if (appLaunchResult.isAppCommand) {
                val aiMessage = ChatMessage(
                    sessionId = sessionId,
                    sender = "AI",
                    text = appLaunchResult.feedbackMessage,
                    languageCode = currentLang.code,
                    isRealTimeGrounded = false
                )
                chatDao.insertMessage(aiMessage)

                if (triggeredByVoice || prefs.autoSpeakResponses) {
                    ttsHelper.speak(aiMessage.id, aiMessage.text, currentLang.code)
                }
                return@launch
            }

            // 2. Check if user is asking who created Karanov AI or where Karan Navik lives / address
            val creatorAnswer = CreatorInfoHelper.checkCreatorQuery(
                input = trimmed,
                languageCode = currentLang.code
            )

            if (creatorAnswer != null) {
                val aiMessage = ChatMessage(
                    sessionId = sessionId,
                    sender = "AI",
                    text = creatorAnswer,
                    languageCode = currentLang.code,
                    isRealTimeGrounded = false
                )
                chatDao.insertMessage(aiMessage)

                if (triggeredByVoice && prefs.autoSpeakResponses) {
                    ttsHelper.speak(aiMessage.id, aiMessage.text, currentLang.code)
                }
                return@launch
            }

            _isGenerating.value = true

            // Resolve API key
            val resolvedApiKey = if (_customApiKey.value.isNotBlank()) {
                _customApiKey.value
            } else {
                BuildConfig.GEMINI_API_KEY
            }

            // Current message history for context
            val currentHistory = messages.value + userMessage

            // Call Gemini API with Real-Time Google Search Grounding
            val result = geminiApi.generateContent(
                apiKey = resolvedApiKey,
                messagesHistory = currentHistory,
                systemInstruction = currentLang.systemPromptDirective,
                enableRealTimeSearch = realTimeEnabled
            )

            _isGenerating.value = false

            // Save AI Response
            val aiMessage = ChatMessage(
                sessionId = sessionId,
                sender = "AI",
                text = result.text,
                rawSources = ChatMessage.formatSources(result.groundingSources),
                searchQueries = result.searchQueries.joinToString(", "),
                languageCode = currentLang.code,
                isRealTimeGrounded = result.isRealTimeGrounded
            )
            chatDao.insertMessage(aiMessage)

            // If user asked via voice and preferences allow auto-speak, read aloud the response
            if (triggeredByVoice && prefs.autoSpeakResponses && !result.isError) {
                ttsHelper.speak(aiMessage.id, aiMessage.text, currentLang.code)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceHelper?.destroy()
        ttsHelper.shutdown()
    }
}
