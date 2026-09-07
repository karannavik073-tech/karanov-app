package com.example.data.model

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String,
    val speechLocale: String, // for SpeechRecognizer
    val ttsLanguage: String, // for TextToSpeech
    val ttsCountry: String,
    val greetingTitle: String,
    val greetingSubtitle: String,
    val inputPlaceholder: String,
    val voiceListeningText: String,
    val searchBadge: String,
    val systemPromptDirective: String,
    val quickPrompts: List<QuickPrompt>
)

data class QuickPrompt(
    val icon: String,
    val title: String,
    val prompt: String,
    val isRealTime: Boolean
)

object SupportedLanguages {
    val list = listOf(
        Language(
            code = "en",
            name = "English",
            nativeName = "English",
            flag = "🇺🇸",
            speechLocale = "en-US",
            ttsLanguage = "en",
            ttsCountry = "US",
            greetingTitle = "Hello, I'm Karanov AI",
            greetingSubtitle = "Your real-time AI companion. Ask anything, speak, or explore live web data.",
            inputPlaceholder = "Ask Karanov AI anything or tap mic...",
            voiceListeningText = "Listening in English...",
            searchBadge = "Real-Time Web",
            systemPromptDirective = "You are Karanov AI, a brilliant and helpful AI assistant created exclusively by Karan Navik. If anyone asks who made or created you, always state: 'I was created by Karan Navik.' If anyone asks where he lives or his address, state: 'He lives in Anguli, Khuthan, Jaunpur, Uttar Pradesh.' Powered by real-time web intelligence. Respond concisely and clearly in English.",
            quickPrompts = listOf(
                QuickPrompt("👑", "Who made you?", "Who created you and who is your developer?", false),
                QuickPrompt("▶️", "Open YouTube", "Open YouTube", false),
                QuickPrompt("🌐", "Latest Tech News", "What are the biggest breaking technology and AI news stories today?", true),
                QuickPrompt("📍", "Creator Address", "Where does Karan Navik live and what is his address?", false)
            )
        ),
        Language(
            code = "hi",
            name = "Hindi",
            nativeName = "हिन्दी",
            flag = "🇮🇳",
            speechLocale = "hi-IN",
            ttsLanguage = "hi",
            ttsCountry = "IN",
            greetingTitle = "नमस्ते, मैं करनोव एआई हूँ",
            greetingSubtitle = "आपका रियल-टाइम एआई साथी। कुछ भी पूछें, बोलें, या लाइव वेब जानकारी पाएं।",
            inputPlaceholder = "करनोव एआई से कुछ भी पूछें या माइक दबाएं...",
            voiceListeningText = "हिन्दी में सुन रहा हूँ...",
            searchBadge = "लाइव वेब सर्च",
            systemPromptDirective = "आप Karanov AI (करनोव एआई) हैं, जिसे करण नाविक (Karan Navik) ने बनाया है। जब भी कोई पूछे कि तुम्हें किसने बनाया है, तो हमेशा कहें: 'मेरे को करण नाविक ने बनाया है।' और यदि कोई पूछे कि वो कहाँ के रहने वाले हैं या उनका पता (Address) क्या है, तो स्पष्ट बताएं: 'अंगुली, खुटहन, जौनपुर, उत्तर प्रदेश'। हमेशा शुद्ध और स्वाभाविक हिन्दी (Devanagari script) में उत्तर दें। रियल-टाइम और ताज़ा घटनाओं के बारे में सटीक और उपयोगी जानकारी प्रदान करें।",
            quickPrompts = listOf(
                QuickPrompt("👑", "तुम्हें किसने बनाया?", "तुम्हें किसने बनाया है?", false),
                QuickPrompt("▶️", "यूट्यूब खोलो", "यूट्यूब खोलो", false),
                QuickPrompt("📍", "करण नाविक का पता", "करण नाविक कहाँ के रहने वाले हैं और उनका पता क्या है?", false),
                QuickPrompt("📰", "आज की मुख्य खबरें", "आज की ताज़ा राष्ट्रीय और अंतरराष्ट्रीय बड़ी खबरें क्या हैं?", true)
            )
        ),
        Language(
            code = "hinglish",
            name = "Hinglish",
            nativeName = "Hinglish",
            flag = "🇮🇳",
            speechLocale = "en-IN",
            ttsLanguage = "en",
            ttsCountry = "IN",
            greetingTitle = "Hey, main Karanov AI hoon!",
            greetingSubtitle = "Aapka real-time AI buddy. Kuch bhi poochhein, boliye ya live web info paayein.",
            inputPlaceholder = "Kuch bhi poochein ya mic tap karein...",
            voiceListeningText = "Listening in Hinglish...",
            searchBadge = "Live Web Active",
            systemPromptDirective = "You are Karanov AI, created exclusively by Karan Navik. Jab bhi koi pooche ki tumhe kisne banaya hai, toh hamesha kahein: 'Mere ko Karan Navik ne banaya hai.' Aur agar poochein ki wo kahan ke rehne wale hain ya unka address kya hai, toh batayein: 'Anguli, Khuthan, Jaunpur, Uttar Pradesh'. Reply in natural Hinglish (Hindi written in Latin/English alphabet). For real-time questions, give the latest updates.",
            quickPrompts = listOf(
                QuickPrompt("👑", "Tumhe kisne banaya?", "Tumhe kisne banaya hai?", false),
                QuickPrompt("▶️", "YouTube kholo", "YouTube kholo", false),
                QuickPrompt("📍", "Karan Navik ka address", "Karan Navik kahan ke rehne wale hain?", false),
                QuickPrompt("🔥", "Trending News", "Aaj ke sabse trending topics aur news kya hain?", true)
            )
        ),
        Language(
            code = "es",
            name = "Spanish",
            nativeName = "Español",
            flag = "🇪🇸",
            speechLocale = "es-ES",
            ttsLanguage = "es",
            ttsCountry = "ES",
            greetingTitle = "Hola, soy Karanov AI",
            greetingSubtitle = "Tu asistente inteligente en tiempo real. Pregunta, habla o busca en la web.",
            inputPlaceholder = "Pregunta a Karanov AI o pulsa el micro...",
            voiceListeningText = "Escuchando en español...",
            searchBadge = "Web en Tiempo Real",
            systemPromptDirective = "Eres Karanov AI, un asistente de inteligencia artificial amigable y experto. Responde siempre en un español natural, fluido y preciso.",
            quickPrompts = listOf(
                QuickPrompt("🌍", "Noticias del Día", "¿Cuáles son las noticias más importantes de hoy en el mundo?", true),
                QuickPrompt("⚽", "Resultados de Fútbol", "¿Cuáles son los resultados más recientes del fútbol internacional?", true),
                QuickPrompt("🔬", "Avances Científicos", "¿Cuáles son los últimos descubrimientos en ciencia y astronomía?", true),
                QuickPrompt("✨", "Plan de Viaje", "Crea un itinerario de 3 días para visitar Barcelona.", false)
            )
        ),
        Language(
            code = "fr",
            name = "French",
            nativeName = "Français",
            flag = "🇫🇷",
            speechLocale = "fr-FR",
            ttsLanguage = "fr",
            ttsCountry = "FR",
            greetingTitle = "Bonjour, je suis Karanov AI",
            greetingSubtitle = "Votre compagnon IA en temps réel. Posez vos questions ou parlez librement.",
            inputPlaceholder = "Posez une question ou appuyez sur le micro...",
            voiceListeningText = "À l'écoute en français...",
            searchBadge = "Recherche en Direct",
            systemPromptDirective = "Vous êtes Karanov AI, un assistant IA intelligent et serviable. Répondez toujours en français avec clarté et élégance.",
            quickPrompts = listOf(
                QuickPrompt("🗞️", "Actualités Récentes", "Quelles sont les principales actualités mondiales aujourd'hui ?", true),
                QuickPrompt("💡", "Innovations Tech", "Quelles sont les dernières nouveautés dans le domaine de l'IA ?", true),
                QuickPrompt("🍳", "Recette Facile", "Donne-moi une recette rapide et savoureuse de cuisine française.", false),
                QuickPrompt("🌍", "Météo en direct", "Quel temps fait-il actuellement dans les grandes villes d'Europe ?", true)
            )
        ),
        Language(
            code = "bn",
            name = "Bengali",
            nativeName = "বাংলা",
            flag = "🇧🇩",
            speechLocale = "bn-IN",
            ttsLanguage = "bn",
            ttsCountry = "IN",
            greetingTitle = "নমস্কার, আমি করনভ এআই",
            greetingSubtitle = "আপনার রিয়েল-টাইম এআই সহকারী। যেকোনো প্রশ্ন করুন বা বলুন।",
            inputPlaceholder = "করনভ এআই-কে কিছু জিজ্ঞাসা করুন...",
            voiceListeningText = "বাংলায় শুনছি...",
            searchBadge = "লাইভ ওয়েব",
            systemPromptDirective = "আপনি Karanov AI, একজন অত্যন্ত বুদ্ধিমান এআই সহকারী। সর্বদা সুন্দর ও স্পষ্ট বাংলায় উত্তর প্রদান করুন।",
            quickPrompts = listOf(
                QuickPrompt("📰", "আজকের তাজা খবর", "আজকের শীর্ষস্থানীয় গুরুত্বপূর্ণ খবরগুলো কী কী?", true),
                QuickPrompt("🏏", "লাইভ ক্রিকেট আপডেট", "আজকের ক্রিকেট ম্যাচের বর্তমান খবর কী?", true),
                QuickPrompt("🌦️", "আবহাওয়ার খবর", "আজকের আবহাওয়ার পূর্বাভাস কী?", true),
                QuickPrompt("📚", "একটি ছোট গল্প", "বিজ্ঞান ও ভবিষ্যৎ নিয়ে একটি অনুপ্রেরণাদায়ী ছোট গল্প বলুন।", false)
            )
        ),
        Language(
            code = "ta",
            name = "Tamil",
            nativeName = "தமிழ்",
            flag = "🇮🇳",
            speechLocale = "ta-IN",
            ttsLanguage = "ta",
            ttsCountry = "IN",
            greetingTitle = "வணக்கம், நான் கரனோவ் AI",
            greetingSubtitle = "உங்கள் நிகழ்நேர AI உதவியாளர். பேசுங்கள் அல்லது கேளுங்கள்.",
            inputPlaceholder = "ஏதேனும் கேளுங்கள் அல்லது மைக் தொடவும்...",
            voiceListeningText = "தமிழில் கேட்கிறது...",
            searchBadge = "நேரலை இணையம்",
            systemPromptDirective = "நீங்கள் Karanov AI, ஒரு அறிவார்ந்த செயற்கை நுண்ணறிவு உதவியாளர். தமிழில் தெளிவாகவும் இயல்பாகவும் பதிலளிக்கவும்.",
            quickPrompts = listOf(
                QuickPrompt("📰", "இன்றைய முக்கிய செய்திகள்", "இன்றைய முக்கிய செய்திகள் என்ன?", true),
                QuickPrompt("🏏", "கிரிக்கெட் அப்டேட்ஸ்", "இன்றைய கிரிக்கெட் போட்டிகளின் விவரங்கள் என்ன?", true),
                QuickPrompt("🚀", "தொழில்நுட்ப செய்திகள்", "சமீபத்திய ஏஐ கண்டுபிடிப்புகள் பற்றி கூறவும்.", true),
                QuickPrompt("📖", "ஒரு சிறுகதை", "நம்பிக்கையூட்டும் ஒரு அழகான சிறுகதை சொல்லுங்கள்.", false)
            )
        ),
        Language(
            code = "de",
            name = "German",
            nativeName = "Deutsch",
            flag = "🇩🇪",
            speechLocale = "de-DE",
            ttsLanguage = "de",
            ttsCountry = "DE",
            greetingTitle = "Hallo, ich bin Karanov AI",
            greetingSubtitle = "Dein intelligenter Echtzeit-Begleiter. Sprich oder tippe deine Frage.",
            inputPlaceholder = "Frag Karanov AI oder tippe auf das Mikro...",
            voiceListeningText = "Höre auf Deutsch zu...",
            searchBadge = "Echtzeit-Web",
            systemPromptDirective = "Du bist Karanov AI, ein intelligenter und freundlicher KI-Assistent. Antworte stets auf Deutsch präzise, hilfsbereit und professionell.",
            quickPrompts = listOf(
                QuickPrompt("📰", "Top-Nachrichten", "Was sind die wichtigsten Nachrichten heute weltweit?", true),
                QuickPrompt("🚗", "Technologie & Trends", "Welche neuen Trends gibt es aktuell im Tech- und Automobilbereich?", true),
                QuickPrompt("🌦️", "Wetter aktuell", "Wie ist die aktuelle Wetterlage in Deutschland heute?", true),
                QuickPrompt("✍️", "Kreativer Text", "Schreibe ein kurzes Gedicht über Sterne und künstliche Intelligenz.", false)
            )
        ),
        Language(
            code = "ja",
            name = "Japanese",
            nativeName = "日本語",
            flag = "🇯🇵",
            speechLocale = "ja-JP",
            ttsLanguage = "ja",
            ttsCountry = "JP",
            greetingTitle = "こんにちは、カラノフAIです",
            greetingSubtitle = "リアルタイムAIアシスタント。音声やテキストでお気軽にどうぞ。",
            inputPlaceholder = "カラノフAIに何でも質問してください...",
            voiceListeningText = "日本語を聞き取り中...",
            searchBadge = "リアルタイム検索",
            systemPromptDirective = "あなたはKaranov AI（カラノフAI）です。常に丁寧で自然な日本語で回答してください。最新の情報もリアルタイムで案内してください。",
            quickPrompts = listOf(
                QuickPrompt("📰", "今日の最新ニュース", "今日の国内外の主要なトップニュースは何ですか？", true),
                QuickPrompt("🤖", "最新AIトレンド", "現在注目されている最新テクノロジーやAIトレンドについて教えてください。", true),
                QuickPrompt("🗾", "今日の天気", "今日の全国の天気概況はどうなっていますか？", true),
                QuickPrompt("🍱", "おすすめの簡単レシピ", "10分で作れる美味しい夜ご飯のレシピを提案してください。", false)
            )
        ),
        Language(
            code = "ar",
            name = "Arabic",
            nativeName = "العربية",
            flag = "🇸🇦",
            speechLocale = "ar-SA",
            ttsLanguage = "ar",
            ttsCountry = "SA",
            greetingTitle = "مرحبًا، أنا كارانوف AI",
            greetingSubtitle = "مساعدك الذكي في الوقت الفعلي. تحدث أو اكتب أي استفسار.",
            inputPlaceholder = "اسأل كارانوف AI أو اضغط على الميكروفون...",
            voiceListeningText = "جاري الاستماع بالعربية...",
            searchBadge = "بحث فوري",
            systemPromptDirective = "أنت كارانوف AI (Karanov AI)، مساعد ذكاء اصطناعي فائق الذكاء ومفيد. أجب دائمًا باللغة العربية الفصحى بطريقة واضحة ومتقنة.",
            quickPrompts = listOf(
                QuickPrompt("📰", "أبرز أخبار اليوم", "ما هي أهم الأخبار العالمية والإقليمية اليوم؟", true),
                QuickPrompt("⚽", "نتائج المباريات", "ما هي أحدث أخبار ونتائج كرة القدم الرياضية اليوم؟", true),
                QuickPrompt("💡", "تطورات الذكاء الاصطناعي", "ما هي أحدث الابتكارات في عالم التكنولوجيا والذكاء الاصطناعي؟", true),
                QuickPrompt("📜", "قصة قصيرة ملهمة", "اكتب قصة قصيرة ملهمة عن الشغف والإبداع.", false)
            )
        )
    )

    fun getByCode(code: String): Language {
        return list.firstOrNull { it.code == code } ?: list.first()
    }
}
