package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.AlarmClock
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log

data class AppLaunchResult(
    val isAppCommand: Boolean,
    val appName: String? = null,
    val isLaunched: Boolean = false,
    val feedbackMessage: String = ""
)

object AppLauncherHelper {

    private const val TAG = "AppLauncherHelper"

    /**
     * Known package mappings for major system and popular applications.
     */
    private val KNOWN_SYSTEM_APPS = mapOf(
        "youtube" to listOf("com.google.android.youtube"),
        "whatsapp" to listOf("com.whatsapp", "com.whatsapp.w4b"),
        "instagram" to listOf("com.instagram.android"),
        "facebook" to listOf("com.facebook.katana", "com.facebook.lite"),
        "chrome" to listOf("com.android.chrome"),
        "browser" to listOf("com.android.chrome", "org.mozilla.firefox", "com.opera.browser", "com.microsoft.emmx"),
        "camera" to listOf("com.google.android.GoogleCamera", "com.android.camera", "com.sec.android.app.camera"),
        "calculator" to listOf("com.google.android.calculator", "com.android.calculator2", "com.sec.android.app.popupcalculator"),
        "settings" to listOf("com.android.settings"),
        "play store" to listOf("com.android.vending"),
        "spotify" to listOf("com.spotify.music"),
        "maps" to listOf("com.google.android.apps.maps"),
        "gmail" to listOf("com.google.android.gm"),
        "photos" to listOf("com.google.android.apps.photos", "com.google.android.gallery3d", "com.sec.android.gallery3d"),
        "gallery" to listOf("com.google.android.apps.photos", "com.google.android.gallery3d", "com.sec.android.gallery3d", "com.android.gallery3d"),
        "clock" to listOf("com.google.android.deskclock", "com.sec.android.app.clockpackage"),
        "phone" to listOf("com.google.android.dialer", "com.android.dialer", "com.samsung.android.dialer"),
        "messages" to listOf("com.google.android.apps.messaging", "com.android.mms", "com.samsung.android.messaging"),
        "contacts" to listOf("com.google.android.contacts", "com.android.contacts", "com.samsung.android.app.contacts"),
        "telegram" to listOf("org.telegram.messenger", "org.thunderdog.challegram"),
        "twitter" to listOf("com.twitter.android"),
        "netflix" to listOf("com.netflix.mediaclient"),
        "amazon" to listOf("in.amazon.mShop.android.shopping", "com.amazon.mShop.android.shopping"),
        "flipkart" to listOf("com.flipkart.android"),
        "phonepe" to listOf("com.phonepe.app"),
        "paytm" to listOf("net.one97.paytm"),
        "gpay" to listOf("com.google.android.apps.nbu.paisa.user"),
        "snapchat" to listOf("com.snapchat.android")
    )

    /**
     * Web / Deep link fallbacks ensuring services like YouTube, Maps, etc. ALWAYS open
     * even on emulators or devices where the dedicated APK is missing.
     */
    private val WEB_FALLBACK_URLS = mapOf(
        "youtube" to "https://www.youtube.com",
        "chrome" to "https://www.google.com",
        "browser" to "https://www.google.com",
        "whatsapp" to "https://api.whatsapp.com",
        "instagram" to "https://www.instagram.com",
        "facebook" to "https://www.facebook.com",
        "twitter" to "https://www.x.com",
        "spotify" to "https://open.spotify.com",
        "telegram" to "https://t.me",
        "maps" to "https://maps.google.com",
        "play store" to "https://play.google.com/store",
        "gmail" to "https://mail.google.com",
        "netflix" to "https://www.netflix.com",
        "amazon" to "https://www.amazon.in",
        "flipkart" to "https://www.flipkart.com",
        "gpay" to "https://pay.google.com"
    )

    /**
     * Multilingual & Phonetic Speech Normalization mappings (Hindi, Hinglish, English spoken variants).
     */
    private val PHONETIC_APP_ALIASES = mapOf(
        "you tube" to "youtube",
        "u tube" to "youtube",
        "utube" to "youtube",
        "yt" to "youtube",
        "यूट्यूब" to "youtube",
        "युटुब" to "youtube",
        "यू ट्यूब" to "youtube",
        "whats app" to "whatsapp",
        "व्हाट्सएप" to "whatsapp",
        "व्हाट्सऐप" to "whatsapp",
        "वाट्सएप" to "whatsapp",
        "व्हाट्स अप" to "whatsapp",
        "insta" to "instagram",
        "insta gram" to "instagram",
        "इंस्टाग्राम" to "instagram",
        "इंस्टा" to "instagram",
        "face book" to "facebook",
        "fb" to "facebook",
        "फेसबुक" to "facebook",
        "फेसबूक" to "facebook",
        "play store" to "play store",
        "playstore" to "play store",
        "google play" to "play store",
        "प्ले स्टोर" to "play store",
        "प्लेस्टोर" to "play store",
        "google maps" to "maps",
        "google map" to "maps",
        "मैप" to "maps",
        "मैप्स" to "maps",
        "गूगल मैप" to "maps",
        "नक्शा" to "maps",
        "google chrome" to "chrome",
        "गूगल क्रोम" to "chrome",
        "क्रोम" to "chrome",
        "ब्राउज़र" to "chrome",
        "ब्राउजर" to "chrome",
        "google pay" to "gpay",
        "g pay" to "gpay",
        "गूगल पे" to "gpay",
        "paytm" to "paytm",
        "पेटीएम" to "paytm",
        "phonepe" to "phonepe",
        "phone pe" to "phonepe",
        "फ़ोनपे" to "phonepe",
        "फोन पे" to "phonepe",
        "flipkart" to "flipkart",
        "फ्लिपकार्ट" to "flipkart",
        "amazon" to "amazon",
        "अमेज़न" to "amazon",
        "अमेजन" to "amazon",
        "netflix" to "netflix",
        "नेटफ्लिक्स" to "netflix",
        "spotify" to "spotify",
        "स्पॉटिफ़ाई" to "spotify",
        "स्पॉटीफाई" to "spotify",
        "म्यूजिक" to "spotify",
        "गाना" to "spotify",
        "camera" to "camera",
        "कैमरा" to "camera",
        "कैमरे" to "camera",
        "calculator" to "calculator",
        "कैलकुलेटर" to "calculator",
        "settings" to "settings",
        "सेटिंग" to "settings",
        "सेटिंग्स" to "settings",
        "gallery" to "gallery",
        "photos" to "photos",
        "गैलरी" to "gallery",
        "फोटो" to "photos",
        "फ़ोटो" to "photos",
        "तस्वीरें" to "photos",
        "phone" to "phone",
        "dialer" to "phone",
        "call" to "phone",
        "फ़ोन" to "phone",
        "फोन" to "phone",
        "कॉल" to "phone",
        "डायलर" to "phone",
        "messages" to "messages",
        "message" to "messages",
        "sms" to "messages",
        "मैसेज" to "messages",
        "संदेश" to "messages",
        "एसएमएस" to "messages",
        "clock" to "clock",
        "alarm" to "clock",
        "घड़ी" to "clock",
        "अलार्म" to "clock",
        "gmail" to "gmail",
        "mail" to "gmail",
        "email" to "gmail",
        "जीमेल" to "gmail",
        "ईमेल" to "gmail",
        "contacts" to "contacts",
        "कॉन्टैक्ट्स" to "contacts",
        "संपर्क" to "contacts",
        "telegram" to "telegram",
        "टेलीग्राम" to "telegram",
        "twitter" to "twitter",
        "x" to "twitter",
        "ट्विटर" to "twitter",
        "snapchat" to "snapchat",
        "स्नैपचैट" to "snapchat"
    )

    /**
     * Checks if user message or voice transcription is a command to open or launch an app.
     * Directly launches the requested app and returns descriptive feedback.
     */
    fun checkAndLaunchApp(
        context: Context,
        rawInput: String,
        languageCode: String
    ): AppLaunchResult {
        val extractedAppName = extractAppName(rawInput) ?: return AppLaunchResult(isAppCommand = false)

        val isHindi = languageCode == "hi" || languageCode == "hinglish" || rawInput.any { it in '\u0900'..'\u097F' }

        val normalizedKey = normalizeAppToken(extractedAppName)

        // 1. First attempt: Standard / Direct system and popular app handler (YouTube, Camera, etc.)
        var launched = tryLaunchKnownApp(context, normalizedKey)

        // 2. Second attempt: Dynamically search all installed applications on the device
        if (!launched) {
            launched = tryLaunchInstalledApp(context, extractedAppName) ||
                    (normalizedKey != extractedAppName.lowercase() && tryLaunchInstalledApp(context, normalizedKey))
        }

        // 3. Third attempt: Open via Web/Deep link fallback if applicable (e.g. YouTube, Maps)
        if (!launched) {
            val fallbackUrl = WEB_FALLBACK_URLS[normalizedKey]
            if (fallbackUrl != null) {
                launched = tryOpenUri(context, Uri.parse(fallbackUrl))
            }
        }

        val displayName = getAppDisplayName(extractedAppName, normalizedKey)

        return if (launched) {
            val message = if (isHindi) {
                "$displayName खोला जा रहा है... 🚀"
            } else {
                "Opening $displayName... 🚀"
            }
            AppLaunchResult(
                isAppCommand = true,
                appName = displayName,
                isLaunched = true,
                feedbackMessage = message
            )
        } else {
            // If the app is truly not found on the device, open Play Store directly to find & install it
            val playStoreOpened = tryOpenPlayStore(context, displayName)
            val message = if (isHindi) {
                if (playStoreOpened) {
                    "'$displayName' आपके डिवाइस में नहीं मिला। इसे इंस्टॉल करने के लिए Google Play Store खोला जा रहा है... 📲"
                } else {
                    "आपके फ़ोन में '$displayName' ऐप नहीं मिला। कृपया सुनिश्चित करें कि यह इंस्टॉल है।"
                }
            } else {
                if (playStoreOpened) {
                    "'$displayName' was not found on your device. Opening Google Play Store to install it... 📲"
                } else {
                    "'$displayName' was not found on your device. Please make sure it is installed."
                }
            }
            AppLaunchResult(
                isAppCommand = true,
                appName = displayName,
                isLaunched = playStoreOpened,
                feedbackMessage = message
            )
        }
    }

    /**
     * Extracts the target app name from diverse English, Hindi, and Hinglish speech/text inputs.
     * Supports:
     * - "open youtube", "open youTube", "open you tube", "youtube kholo"
     * - "youtube open karo", "youtube chalao", "chalao youtube", "kholo youtube"
     * - "direct youtube open kar do", "karan open youtube", "can you open youtube"
     * - "mujhe youtube open karna hai", "youtube khul jana chahiye"
     * - Direct single app names like "youtube"
     */
    fun extractAppName(input: String): String? {
        val raw = input.trim()
        if (raw.isBlank()) return null

        val text = raw.replace(Regex("[?.!,:;~`'\"।]+$"), "").trim()
        val lower = text.lowercase().trim()

        // 1. Single known direct app check (e.g. user just said "youtube" or "camera")
        val normalizedSingle = normalizeAppToken(lower)
        if (isKnownDirectApp(normalizedSingle)) {
            return normalizedSingle
        }

        // 2. Prefix patterns (e.g. "open youtube", "kholo camera", "chalao calculator", "launch chrome")
        val prefixRegex = Regex(
            "^(?:please\\s+|plz\\s+|kripya\\s+|zara\\s+|can you\\s+|could you\\s+|bhai\\s+|karan\\s+|hey\\s+|hello\\s+|direct\\s+|ab\\s+)*" +
                    "(?:open|launch|start|run|kholo|khol|kholna|kholiye|chalao|chala|chalana|chalu karo|chalu kar|start karo|start kar|shuru karo|open karo|open kar|open kijiye)\\s+" +
                    "(?:the\\s+|my\\s+|apna\\s+|mera\\s+)?(?:app\\s+|application\\s+|ऐप\\s+)?(.+)",
            RegexOption.IGNORE_CASE
        )
        prefixRegex.find(text)?.let { match ->
            val candidate = cleanExtractedCandidate(match.groupValues[1])
            if (candidate.isNotBlank()) return candidate
        }

        // Hindi Prefix Regex (e.g. "खोलो कैमरा", "ओपन करो यूट्यूब", "चलाओ व्हाट्सएप")
        val hindiPrefixRegex = Regex(
            "^(?:कृपया\\s+|जरा\\s+|भाई\\s+|डायरेक्ट\\s+)*" +
                    "(?:खोलो|खोल दो|खोलिये|खोलिए|चलाओ|चला दो|शुरू करो|चालू करो|ओपन करो|लांच करो|स्टार्ट करो)\\s+" +
                    "(?:ऐप\\s+|एप्लिकेशन\\s+)?(.+)",
            RegexOption.IGNORE_CASE
        )
        hindiPrefixRegex.find(text)?.let { match ->
            val candidate = cleanExtractedCandidate(match.groupValues[1])
            if (candidate.isNotBlank()) return candidate
        }

        // 3. Suffix patterns (e.g. "youtube open karo", "youtube open", "youtube kholo", "camera chalao", "youtube khul jana chahiye")
        val suffixRegex = Regex(
            "^(?:please\\s+|plz\\s+|kripya\\s+|zara\\s+|bhai\\s+|karan\\s+|direct\\s+)*" +
                    "(.+?)\\s+" +
                    "(?:ko\\s+|ka\\s+|ki\\s+)?(?:app\\s+|application\\s+|ऐप\\s+)?" +
                    "(?:open|open karo|open kar|open kijiye|open kar do|kholo|khol do|kholna|kholiye|khol de|khol|chalao|chala do|chala de|chalu karo|chalu kar do|start karo|start kar|shuru karo|khulna chahiye|khul jana chahiye)$",
            RegexOption.IGNORE_CASE
        )
        suffixRegex.find(text)?.let { match ->
            val candidate = cleanExtractedCandidate(match.groupValues[1])
            if (candidate.isNotBlank()) return candidate
        }

        // Hindi Suffix Regex (e.g. "यूट्यूब खोलो", "व्हाट्सएप ओपन करो", "कैमरा चलाओ", "कैलकुलेटर खोल दो", "खुल जाना चाहिए")
        val hindiSuffixRegex = Regex(
            "^(.+?)\\s+(?:को\\s+)?(?:ऐप\\s+|एप्लिकेशन\\s+)?" +
                    "(?:खोलो|खोल दो|खोलिये|खोलिए|चलाओ|चला दो|ओपन करो|चालू करो|शुरू करो|खुलना चाहिए|खुल जाना चाहिए)$",
            RegexOption.IGNORE_CASE
        )
        hindiSuffixRegex.find(text)?.let { match ->
            val candidate = cleanExtractedCandidate(match.groupValues[1])
            if (candidate.isNotBlank()) return candidate
        }

        // 4. Infix intention patterns (e.g. "mujhe youtube open karna hai", "i want to open youtube")
        val infixRegex = Regex(
            "^(?:mujhe|humko|i want to|main|mera)\\s+(.+?)\\s+(?:app\\s+)?(?:open karna hai|kholna hai|chalana hai|dekhna hai|start karna hai)",
            RegexOption.IGNORE_CASE
        )
        infixRegex.find(text)?.let { match ->
            val candidate = cleanExtractedCandidate(match.groupValues[1])
            if (candidate.isNotBlank()) return candidate
        }

        return null
    }

    private fun cleanExtractedCandidate(raw: String): String {
        var candidate = raw.trim()
        val fillerTokens = listOf(
            "please", "plz", "kripya", "zara", "bhai", "ji", "yaar",
            "app", "apps", "application", "the", "ko", "bhi", "na", "to", "hi", "now",
            "कृपया", "जरा", "भाई", "जी", "ऐप", "एप्लिकेशन", "को", "भी", "ना", "तो"
        )
        for (f in fillerTokens) {
            if (candidate.startsWith("$f ", ignoreCase = true)) {
                candidate = candidate.substring(f.length).trim()
            }
            if (candidate.endsWith(" $f", ignoreCase = true)) {
                candidate = candidate.substring(0, candidate.length - f.length).trim()
            }
        }
        return candidate.trim()
    }

    private fun isKnownDirectApp(normalized: String): Boolean {
        return KNOWN_SYSTEM_APPS.containsKey(normalized) || WEB_FALLBACK_URLS.containsKey(normalized)
    }

    fun normalizeAppToken(name: String): String {
        val clean = name.lowercase().trim()
        // Check direct alias dictionary
        PHONETIC_APP_ALIASES[clean]?.let { return it }

        // Remove multiple spaces or hyphen
        val noSpace = clean.replace(Regex("[\\s_\\-]+"), " ")
        PHONETIC_APP_ALIASES[noSpace]?.let { return it }

        val condensed = clean.replace(Regex("[\\s_\\-]+"), "")
        PHONETIC_APP_ALIASES[condensed]?.let { return it }

        return clean
    }

    private fun tryLaunchKnownApp(context: Context, key: String): Boolean {
        // Special system intents
        when (key) {
            "camera" -> {
                if (tryStartIntent(context, Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)) ||
                    tryStartIntent(context, Intent(MediaStore.ACTION_IMAGE_CAPTURE))) {
                    return true
                }
            }
            "settings" -> {
                if (tryStartIntent(context, Intent(Settings.ACTION_SETTINGS))) {
                    return true
                }
            }
            "phone" -> {
                if (tryStartIntent(context, Intent(Intent.ACTION_DIAL))) {
                    return true
                }
            }
            "clock" -> {
                if (tryStartIntent(context, Intent(AlarmClock.ACTION_SHOW_ALARMS))) {
                    return true
                }
            }
            "calculator" -> {
                try {
                    val calcIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_CALCULATOR)
                    if (tryStartIntent(context, calcIntent)) return true
                } catch (ignored: Exception) {}
            }
            "gallery", "photos" -> {
                try {
                    val galleryIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_GALLERY)
                    if (tryStartIntent(context, galleryIntent)) return true
                } catch (ignored: Exception) {}
            }
            "messages" -> {
                try {
                    val msgIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MESSAGING)
                    if (tryStartIntent(context, msgIntent)) return true
                } catch (ignored: Exception) {}
            }
            "chrome", "browser" -> {
                try {
                    val browserIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
                    if (tryStartIntent(context, browserIntent)) return true
                } catch (ignored: Exception) {}
            }
            "youtube" -> {
                // First check package launch
                val packages = KNOWN_SYSTEM_APPS["youtube"] ?: emptyList()
                for (pkg in packages) {
                    if (launchPackage(context, pkg)) return true
                }
                // Deep link intent: automatically handled by YouTube app or browser
                if (tryOpenUri(context, Uri.parse("vnd.youtube:"))) return true
                if (tryOpenUri(context, Uri.parse("https://www.youtube.com"))) return true
            }
        }

        // Package lookup
        val packages = KNOWN_SYSTEM_APPS[key] ?: emptyList()
        for (pkg in packages) {
            if (launchPackage(context, pkg)) {
                return true
            }
        }
        return false
    }

    /**
     * Dynamically searches all installed applications on the device matching the label or package name.
     */
    fun tryLaunchInstalledApp(context: Context, queryName: String): Boolean {
        try {
            val pm: PackageManager = context.packageManager
            val launcherIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = pm.queryIntentActivities(launcherIntent, 0)
            val cleanQuery = queryName.lowercase().trim()
            val strippedQuery = cleanQuery.replace(Regex("[\\s_\\-.]"), "")

            if (resolveInfos.isEmpty()) {
                Log.w(TAG, "No launcher activities returned for query '$queryName'")
            }

            // 1. Exact match by label or stripped label
            for (info in resolveInfos) {
                val appLabel = info.loadLabel(pm).toString().lowercase().trim()
                val strippedLabel = appLabel.replace(Regex("[\\s_\\-.]"), "")
                val pkg = info.activityInfo.packageName.lowercase()
                val strippedPkg = pkg.substringAfterLast('.').replace(Regex("[\\s_\\-.]"), "")

                if (appLabel == cleanQuery || strippedLabel == strippedQuery || strippedPkg == strippedQuery) {
                    if (launchActivity(context, info.activityInfo.packageName, info.activityInfo.name)) {
                        return true
                    }
                }
            }

            // 2. Starts with match
            for (info in resolveInfos) {
                val appLabel = info.loadLabel(pm).toString().lowercase().trim()
                val strippedLabel = appLabel.replace(Regex("[\\s_\\-.]"), "")
                if (appLabel.startsWith(cleanQuery) || strippedLabel.startsWith(strippedQuery)) {
                    if (launchActivity(context, info.activityInfo.packageName, info.activityInfo.name)) {
                        return true
                    }
                }
            }

            // 3. Substring match on label or package
            for (info in resolveInfos) {
                val appLabel = info.loadLabel(pm).toString().lowercase().trim()
                val pkgName = info.activityInfo.packageName.lowercase()
                val strippedLabel = appLabel.replace(Regex("[\\s_\\-.]"), "")
                if (appLabel.contains(cleanQuery) || cleanQuery.contains(appLabel) ||
                    strippedLabel.contains(strippedQuery) || pkgName.contains(strippedQuery)) {
                    if (launchActivity(context, info.activityInfo.packageName, info.activityInfo.name)) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error querying installed apps: ${e.message}")
        }
        return false
    }

    private fun launchActivity(context: Context, packageName: String, className: String? = null): Boolean {
        return try {
            val pm = context.packageManager
            val intent = if (className != null) {
                Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    setClassName(packageName, className)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                }
            } else {
                pm.getLaunchIntentForPackage(packageName)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                }
            }
            if (intent != null) {
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching activity for $packageName: ${e.message}")
            false
        }
    }

    private fun launchPackage(context: Context, packageName: String): Boolean {
        return try {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                context.startActivity(intent)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching package $packageName: ${e.message}")
            false
        }
    }

    private fun tryOpenUri(context: Context, uri: Uri): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error opening URI $uri: ${e.message}")
            false
        }
    }

    private fun tryOpenPlayStore(context: Context, appName: String): Boolean {
        return try {
            val marketUri = Uri.parse("market://search?q=${Uri.encode(appName)}&c=apps")
            if (tryOpenUri(context, marketUri)) return true
            val webPlayStoreUri = Uri.parse("https://play.google.com/store/search?q=${Uri.encode(appName)}&c=apps")
            tryOpenUri(context, webPlayStoreUri)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening Play Store for $appName: ${e.message}")
            false
        }
    }

    private fun tryStartIntent(context: Context, intent: Intent): Boolean {
        return try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error starting intent: ${e.message}")
            false
        }
    }

    private fun getAppDisplayName(extracted: String, normalizedKey: String): String {
        return when (normalizedKey) {
            "youtube" -> "YouTube"
            "whatsapp" -> "WhatsApp"
            "instagram" -> "Instagram"
            "facebook" -> "Facebook"
            "chrome" -> "Chrome"
            "camera" -> "Camera"
            "calculator" -> "Calculator"
            "settings" -> "Settings"
            "photos", "gallery" -> "Photos & Gallery"
            "maps" -> "Google Maps"
            "gmail" -> "Gmail"
            "spotify" -> "Spotify"
            "telegram" -> "Telegram"
            "play store" -> "Google Play Store"
            "clock" -> "Clock"
            "phone" -> "Phone"
            "messages" -> "Messages"
            "contacts" -> "Contacts"
            "twitter" -> "X (Twitter)"
            "netflix" -> "Netflix"
            "amazon" -> "Amazon"
            "flipkart" -> "Flipkart"
            "paytm" -> "Paytm"
            "phonepe" -> "PhonePe"
            "gpay" -> "Google Pay"
            "snapchat" -> "Snapchat"
            else -> extracted.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
        }
    }
}

