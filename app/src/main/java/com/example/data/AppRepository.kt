package com.example.data

import com.example.BuildConfig
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.Part
import com.example.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(private val db: AppDatabase) {

    // DAOs
    private val userDao = db.userDao()
    private val volunteerDao = db.volunteerDao()
    private val prasadDao = db.prasadBookingDao()
    private val pujaDao = db.pujaBookingDao()
    private val galleryDao = db.galleryDao()
    private val notificationDao = db.notificationDao()
    private val settingsDao = db.settingsDao()

    // Users
    suspend fun getUserByMobile(mobile: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByMobile(mobile)
    }

    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    // Volunteers
    val allApplicationsFlow: Flow<List<VolunteerApplication>> = volunteerDao.getAllApplicationsFlow()

    suspend fun getApplicationByTracking(trackingNum: String): VolunteerApplication? = withContext(Dispatchers.IO) {
        volunteerDao.getApplicationByTracking(trackingNum)
    }

    suspend fun getApplicationByMobile(mobile: String): VolunteerApplication? = withContext(Dispatchers.IO) {
        volunteerDao.getApplicationByMobile(mobile)
    }

    suspend fun insertApplication(app: VolunteerApplication) = withContext(Dispatchers.IO) {
        volunteerDao.insertApplication(app)
    }

    suspend fun updateVolunteerStatus(trackingNum: String, status: String, vId: String?, vPwd: String?) = withContext(Dispatchers.IO) {
        volunteerDao.updateStatus(trackingNum, status, vId, vPwd)
    }

    // Prasad Bookings
    fun getPrasadByUserFlow(mobile: String): Flow<List<PrasadBooking>> = prasadDao.getPrasadByUserFlow(mobile)
    val allPrasadBookingsFlow: Flow<List<PrasadBooking>> = prasadDao.getAllBookingsFlow()

    suspend fun insertPrasadBooking(booking: PrasadBooking) = withContext(Dispatchers.IO) {
        prasadDao.insertPrasadBooking(booking)
    }

    // Puja Bookings
    fun getPujaByUserFlow(mobile: String): Flow<List<PujaBooking>> = pujaDao.getPujaByUserFlow(mobile)
    val allPujaBookingsFlow: Flow<List<PujaBooking>> = pujaDao.getAllBookingsFlow()

    suspend fun insertPujaBooking(booking: PujaBooking) = withContext(Dispatchers.IO) {
        pujaDao.insertPujaBooking(booking)
    }

    // Gallery
    val allMemoriesFlow: Flow<List<GalleryMemory>> = galleryDao.getAllAsFlow()

    suspend fun insertMemory(memory: GalleryMemory) = withContext(Dispatchers.IO) {
        galleryDao.insertMemory(memory)
    }

    suspend fun incrementLike(id: Int) = withContext(Dispatchers.IO) {
        galleryDao.incrementLike(id)
    }

    // Notifications
    val allNotificationsFlow: Flow<List<Notification>> = notificationDao.getAllAsFlow()

    suspend fun insertNotification(notification: Notification) = withContext(Dispatchers.IO) {
        notificationDao.insertNotification(notification)
    }

    // Settings
    val settingsFlow: Flow<AppSettings?> = settingsDao.getSettingsFlow()
    suspend fun getSettingsDirect(): AppSettings? = withContext(Dispatchers.IO) {
        settingsDao.getSettingsDirect()
    }

    suspend fun saveSettings(settings: AppSettings) = withContext(Dispatchers.IO) {
        settingsDao.saveSettings(settings)
    }

    // Gemini Chatbot Integration
    suspend fun askGemini(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "GEMINI_API_KEY") {
            return@withContext getOfflineSpiritualAnswer(prompt)
        }

        val request = GenerateContentRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            ),
            systemInstruction = Content(
                parts = listOf(
                    Part(
                        text = "You are Maa Durga's divine celestial assistant. " +
                                "Answer in a deeply spiritual, welcoming, and culturally rich manner. " +
                                "Use elegant English mixed with warm Bengali phrases (like 'Jai Maa Durga!', 'Shubho Sharodiya!'). " +
                                "Explain the deep spiritual significance of Durga Puja (e.g. Saptami, Ashtami Sandhi Puja, Dhunuchi dance, Maha Dashami Sindoor Khela) " +
                                "in high-vibe festival vocabulary. Keep responses concise, beautiful, and inspiring."
                    )
                )
            )
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Maa Durga blesses you. (No response received, please check connection)."
        } catch (e: Exception) {
            // Log or fallback
            getOfflineSpiritualAnswer(prompt)
        }
    }

    private fun getOfflineSpiritualAnswer(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("sandhi") || lower.contains("sandhipuja") -> {
                "✨ *Sandhi Puja* is the most sacred juncture of Durga Puja! It occurs during the last 24 minutes of Maha Ashtami and first 24 minutes of Maha Navami. It marks the exact moment Maha Durga manifested her cosmic weapons to defeat the demon Mahishasura. 108 golden clay lamps are lit, accompanied by rhythmic dhak drumming! Jai Maa Durga!"
            }
            lower.contains("dhunuchi") || lower.contains("dance") -> {
                "🔥 *Dhunuchi Naach* is a spectacular ritual performed on Maha Navami during Aarti! Devotees balance clay pots ('Dhunuchi') filled with burning coconut husks and powdered camphor. This energetic dance, done to the hypnotic beats of standard Dhak drums, is a spiritual surrender and expression of ecstasy. Shubho Sharodiya!"
            }
            lower.contains("sindoor") || lower.contains("khela") || lower.contains("dashami") -> {
                "🌸 *Sindoor Khela* occurs on Vijayadashami! Before bidding farewell to the Divine Mother, married women adorn Her with vermilion (sindoor) and then smear it on each other. It symbolizes marital bliss, sweet sisterhood, and celebrates Mother Durga's temporary return to Mt. Kailash with Her children."
            }
            lower.contains("ashtami") || lower.contains("pushpanjali") -> {
                "🙌 *Maha Ashtami* is the peak day of rituals! Devotees keep a fast till they offer *Pushpanjali* (flower offerings) to the Mother Durga while the priest chants primary Sanskrit slokas. This is followed by Kumari Puja and the auspicious transition into Sandhi Puja. Shubho Saptami and Ashtami!"
            }
            else -> {
                "🌺 *Jai Maa Durga!* Durga Puja is the celebration of the triumph of good over evil. The Mother Goddess descends with her children Ganesha, Kartikeya, Lakshmi, and Saraswati to bless humanity. How can I guide your spiritual path on this majestic festival, dear devotee? Ask me about Saptami, Ashtami Sandhi Puja, Dhunuchi Naach, or Sindoor Khela!"
            }
        }
    }
}
