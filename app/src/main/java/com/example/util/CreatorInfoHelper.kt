package com.example.util

object CreatorInfoHelper {

    const val CREATOR_NAME = "करण नाविक (Karan Navik)"
    const val CREATOR_ADDRESS = "अंगुली, खुटहन, जौनपुर, उत्तर प्रदेश (Anguli, Khuthan, Jaunpur, Uttar Pradesh)"

    /**
     * Checks if the user is asking about the creator of Karanov AI or his address.
     * Returns a direct and definitive response if detected.
     */
    fun checkCreatorQuery(input: String, languageCode: String): String? {
        val lower = input.lowercase().trim().replace(Regex("[?.!,:;]"), "")
        val isHindi = languageCode == "hi" || languageCode == "hinglish" || input.any { it in '\u0900'..'\u097F' }

        val askingWhoMade = isAskingCreator(lower)
        val askingAddress = isAskingAddress(lower)

        if (askingWhoMade && askingAddress) {
            return if (isHindi) {
                "मेरे को **$CREATOR_NAME** ने बनाया है और वह **$CREATOR_ADDRESS** के रहने वाले हैं। 🌟"
            } else {
                "I was created by **Karan Navik**, and he resides in **Anguli, Khuthan, Jaunpur, Uttar Pradesh**. 🌟"
            }
        } else if (askingWhoMade) {
            return if (isHindi) {
                "मेरे को **$CREATOR_NAME** ने बनाया है। मैं उनका एआई असिस्टेंट (Karanov AI) हूँ। 🤖✨"
            } else {
                "I was created by **Karan Navik**. I am Karanov AI, his personal intelligent AI assistant! 🤖✨"
            }
        } else if (askingAddress) {
            return if (isHindi) {
                "करण नाविक (Karan Navik) जी **$CREATOR_ADDRESS** के रहने वाले हैं। 📍"
            } else {
                "Karan Navik lives in **Anguli, Khuthan, Jaunpur, Uttar Pradesh**. 📍"
            }
        }

        return null
    }

    private fun isAskingCreator(lower: String): Boolean {
        val creatorKeywords = listOf(
            "kisne banaya",
            "kisane banaya",
            "kisne banaya hai",
            "kisane banaya hai",
            "kisme banaya",
            "kaun banaya",
            "kon banaya",
            "who made you",
            "who created you",
            "who is your creator",
            "who built you",
            "who developed you",
            "who is your developer",
            "tumhe kisne",
            "tumko kisne",
            "karanov kisne banaya",
            "creator kaun hai",
            "developer kaun hai",
            "karan navik kaun",
            "who is karan navik"
        )
        return creatorKeywords.any { lower.contains(it) }
    }

    private fun isAskingAddress(lower: String): Boolean {
        val addressKeywords = listOf(
            "kaha rahane wale",
            "kahan rahane wale",
            "kahan ke rahane wale",
            "kaha ke rahane wale",
            "kahan rehte",
            "kaha rehte",
            "kahan rahte",
            "kaha rahte",
            "unka address",
            "wo kahan ke",
            "wo kaha ke",
            "address kya",
            "pata kya",
            "ghar kahan",
            "karan navik kahan",
            "where does he live",
            "where is he from",
            "creator address",
            "developer address",
            "creator location",
            "his address",
            "unka pata",
            "unka ghar"
        )
        return addressKeywords.any { lower.contains(it) }
    }
}
