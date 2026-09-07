package com.example

import com.example.util.CreatorInfoHelper
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCreatorQuery_whoMadeYou() {
        val answer = CreatorInfoHelper.checkCreatorQuery("tumhe kisne banaya hai", "hi")
        assertNotNull(answer)
        assertTrue(answer!!.contains("Karan Navik") || answer.contains("करण नाविक"))

        val answerHinglish = CreatorInfoHelper.checkCreatorQuery("who created you", "en")
        assertNotNull(answerHinglish)
        assertTrue(answerHinglish!!.contains("Karan Navik"))
    }

    @Test
    fun testCreatorQuery_address() {
        val answer = CreatorInfoHelper.checkCreatorQuery("wo kahan ke rahane wale hai", "hi")
        assertNotNull(answer)
        assertTrue(answer!!.contains("Anguli") || answer.contains("अंगुली"))
        assertTrue(answer.contains("Jaunpur") || answer.contains("जौनपुर"))

        val answer2 = CreatorInfoHelper.checkCreatorQuery("unka address kya hai", "hi")
        assertNotNull(answer2)
        assertTrue(answer2!!.contains("अंगुली, खुटहन, जौनपुर, उत्तर प्रदेश"))
    }

    @Test
    fun testCreatorQuery_bothWhoMadeAndAddress() {
        val answer = CreatorInfoHelper.checkCreatorQuery("tumhe kisne banaya hai aur unka address kya hai", "hi")
        assertNotNull(answer)
        assertTrue(answer!!.contains("करण नाविक"))
        assertTrue(answer.contains("अंगुली, खुटहन, जौनपुर, उत्तर प्रदेश"))
    }

    @Test
    fun testAppLauncherHelper_extractYouTube() {
        // Test user's exact query: "open youTube"
        val app1 = com.example.util.AppLauncherHelper.extractAppName("open youTube")
        assertNotNull(app1)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app1!!))

        // "open YouTube"
        val app2 = com.example.util.AppLauncherHelper.extractAppName("open YouTube")
        assertNotNull(app2)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app2!!))

        // "open you tube" (speech to text with spaces)
        val app3 = com.example.util.AppLauncherHelper.extractAppName("open you tube")
        assertNotNull(app3)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app3!!))

        // "youtube kholo"
        val app4 = com.example.util.AppLauncherHelper.extractAppName("youtube kholo")
        assertNotNull(app4)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app4!!))

        // "यूट्यूब खोलो"
        val app5 = com.example.util.AppLauncherHelper.extractAppName("यूट्यूब खोलो")
        assertNotNull(app5)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app5!!))

        // "direct youtube open karo"
        val app6 = com.example.util.AppLauncherHelper.extractAppName("direct youtube open karo")
        assertNotNull(app6)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app6!!))

        // "chalao youtube"
        val app7 = com.example.util.AppLauncherHelper.extractAppName("chalao youtube")
        assertNotNull(app7)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app7!!))

        // "mujhe youtube dekhna hai"
        val app8 = com.example.util.AppLauncherHelper.extractAppName("mujhe youtube dekhna hai")
        assertNotNull(app8)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app8!!))

        // "youtube khul jana chahiye"
        val app9 = com.example.util.AppLauncherHelper.extractAppName("youtube khul jana chahiye")
        assertNotNull(app9)
        assertEquals("youtube", com.example.util.AppLauncherHelper.normalizeAppToken(app9!!))

        // Arbitrary app: "open Instagram"
        val app10 = com.example.util.AppLauncherHelper.extractAppName("open Instagram")
        assertNotNull(app10)
        assertEquals("instagram", com.example.util.AppLauncherHelper.normalizeAppToken(app10!!))
    }
}
