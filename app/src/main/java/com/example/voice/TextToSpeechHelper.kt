package com.example.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TextToSpeechHelper(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _currentPlayingMessageId = MutableStateFlow<String?>(null)
    val currentPlayingMessageId: StateFlow<String?> = _currentPlayingMessageId.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _currentPlayingMessageId.value = utteranceId
                }

                override fun onDone(utteranceId: String?) {
                    if (_currentPlayingMessageId.value == utteranceId) {
                        _currentPlayingMessageId.value = null
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    if (_currentPlayingMessageId.value == utteranceId) {
                        _currentPlayingMessageId.value = null
                    }
                }
            })
        }
    }

    fun speak(messageId: String, text: String, languageCode: String) {
        if (!isInitialized || tts == null) return

        // If clicking the same message that's currently playing, stop it
        if (_currentPlayingMessageId.value == messageId) {
            stop()
            return
        }

        // Clean markdown symbols for natural TTS speech
        val cleanedText = cleanMarkdownForSpeech(text)

        // Set locale based on language code
        val targetLocale = when (languageCode) {
            "hi" -> Locale.forLanguageTag("hi-IN")
            "hinglish" -> Locale.forLanguageTag("en-IN")
            "es" -> Locale.forLanguageTag("es-ES")
            "fr" -> Locale.FRENCH
            "de" -> Locale.GERMAN
            "ja" -> Locale.JAPANESE
            "bn" -> Locale.forLanguageTag("bn-IN")
            "ta" -> Locale.forLanguageTag("ta-IN")
            "ar" -> Locale.forLanguageTag("ar-SA")
            else -> Locale.US
        }

        try {
            val result = tts?.setLanguage(targetLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
        } catch (e: Exception) {
            tts?.setLanguage(Locale.US)
        }

        tts?.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, messageId)
        _currentPlayingMessageId.value = messageId
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            // ignore
        }
        _currentPlayingMessageId.value = null
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            // ignore
        }
        tts = null
        isInitialized = false
        _currentPlayingMessageId.value = null
    }

    private fun cleanMarkdownForSpeech(markdown: String): String {
        return markdown
            .replace(Regex("```[a-zA-Z]*\\n[\\s\\S]*?\\n```"), " Code block omitted. ")
            .replace(Regex("`[^`]+`"), "")
            .replace(Regex("\\[([^\\]]+)\\]\\([^\\)]+\\)"), "$1")
            .replace(Regex("[*#_~>]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
