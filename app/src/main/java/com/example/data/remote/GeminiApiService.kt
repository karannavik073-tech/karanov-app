package com.example.data.remote

import com.example.data.model.ChatMessage
import com.example.data.model.GroundingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeminiResult(
    val text: String,
    val groundingSources: List<GroundingSource> = emptyList(),
    val searchQueries: List<String> = emptyList(),
    val isRealTimeGrounded: Boolean = false,
    val isError: Boolean = false
)

class GeminiApiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateContent(
        apiKey: String,
        messagesHistory: List<ChatMessage>,
        systemInstruction: String,
        enableRealTimeSearch: Boolean
    ): GeminiResult = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext GeminiResult(
                text = "⚠️ **Gemini API Key Required**\n\nPlease add your Gemini API Key in the AI Studio **Secrets** panel (GEMINI_API_KEY) or tap the ⚙️ Settings icon at top right to enter your key directly.",
                isError = true
            )
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            // Construct Request JSON
            val requestJson = JSONObject()

            // System instruction
            val systemObj = JSONObject()
            val sysParts = JSONArray()
            sysParts.put(JSONObject().put("text", systemInstruction))
            systemObj.put("parts", sysParts)
            requestJson.put("systemInstruction", systemObj)

            // Contents array
            val contentsArray = JSONArray()
            // Take recent context up to last 10 messages to stay within limits and fast
            val recentMessages = messagesHistory.takeLast(10)
            for (msg in recentMessages) {
                val contentObj = JSONObject()
                contentObj.put("role", if (msg.isUser) "user" else "model")
                val partsArray = JSONArray()
                partsArray.put(JSONObject().put("text", msg.text))
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
            }
            requestJson.put("contents", contentsArray)

            // Tools: Google Search for Real-Time Grounding
            if (enableRealTimeSearch) {
                val toolsArray = JSONArray()
                val googleSearchTool = JSONObject()
                googleSearchTool.put("googleSearch", JSONObject())
                toolsArray.put(googleSearchTool)
                requestJson.put("tools", toolsArray)
            }

            // Generation config
            val genConfig = JSONObject()
            genConfig.put("temperature", 0.7)
            genConfig.put("topP", 0.95)
            requestJson.put("generationConfig", genConfig)

            val body = requestJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorMsg = try {
                    val errJson = JSONObject(responseBody)
                    errJson.optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}"
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return@withContext GeminiResult(
                    text = "Error connecting to Karanov AI: $errorMsg",
                    isError = true
                )
            }

            // Parse response
            val root = JSONObject(responseBody)
            val candidates = root.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) {
                return@withContext GeminiResult(
                    text = "No response generated. Please try asking again.",
                    isError = true
                )
            }

            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            val textBuilder = StringBuilder()
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    if (part.has("text")) {
                        textBuilder.append(part.getString("text"))
                    }
                }
            }
            val resultText = textBuilder.toString()

            // Grounding metadata
            val sources = mutableListOf<GroundingSource>()
            val searchQueries = mutableListOf<String>()
            var isGrounded = false

            val groundingMetadata = firstCandidate.optJSONObject("groundingMetadata")
            if (groundingMetadata != null) {
                val queries = groundingMetadata.optJSONArray("webSearchQueries")
                if (queries != null) {
                    for (i in 0 until queries.length()) {
                        searchQueries.add(queries.getString(i))
                    }
                }

                val chunks = groundingMetadata.optJSONArray("groundingChunks")
                if (chunks != null) {
                    for (i in 0 until chunks.length()) {
                        val chunk = chunks.getJSONObject(i)
                        val web = chunk.optJSONObject("web")
                        if (web != null) {
                            val title = web.optString("title", "Web Source")
                            val uri = web.optString("uri", "")
                            if (uri.isNotBlank()) {
                                sources.add(GroundingSource(title = title, uri = uri))
                            }
                        }
                    }
                }

                if (sources.isNotEmpty() || searchQueries.isNotEmpty()) {
                    isGrounded = true
                }
            }

            GeminiResult(
                text = resultText.ifBlank { "Received response from Karanov AI." },
                groundingSources = sources,
                searchQueries = searchQueries,
                isRealTimeGrounded = isGrounded,
                isError = false
            )
        } catch (e: Exception) {
            GeminiResult(
                text = "Network or API error: ${e.localizedMessage ?: "Unknown error occurred"}",
                isError = true
            )
        }
    }
}
