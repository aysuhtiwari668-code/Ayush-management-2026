package com.example.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Random

data class ChatMessage(
    val sender: String, // "user" or "assistant"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(db)

    // User Session State
    var currentUser: User? by mutableStateOf(null)
        private set
    
    var isAdmin: Boolean by mutableStateOf(false)
        private set

    // Observable flows
    val allApplications: StateFlow<List<VolunteerApplication>> = repository.allApplicationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPrasadBookings: StateFlow<List<PrasadBooking>> = repository.allPrasadBookingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPujaBookings: StateFlow<List<PujaBooking>> = repository.allPujaBookingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMemories: StateFlow<List<GalleryMemory>> = repository.allMemoriesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<Notification>> = repository.allNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appSettings: StateFlow<AppSettings?> = repository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Chat State
    private val _chatHistory = mutableStateListOf<ChatMessage>()
    val chatHistory: List<ChatMessage> get() = _chatHistory
    var isChatLoading: Boolean by mutableStateOf(false)

    init {
        // Add a primary warm greeting message in Chatbot
        _chatHistory.add(
            ChatMessage("assistant", "Shubho Sharodiya! 🌺 I am Maa Durga's spiritual assistant. Ask me anything about the holy rituals, dates, Sandhi Puja, Dhunuchi dance, or Prasad booking!")
        )
    }

    // --- Authentication Actions ---

    fun loginUser(mobile: String, pin: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (mobile == "AYSUHTIWARI668" && pin == "AYUSH668") {
                isAdmin = true
                currentUser = User("admin", "Chairman Ayush", "AYUSH668", "Varanasi, India", "avatar_traditional_male")
                onSuccess()
                return@launch
            }

            val user = repository.getUserByMobile(mobile)
            if (user != null && user.password == pin) {
                currentUser = user
                isAdmin = false
                onSuccess()
            } else {
                onError("Invalid mobile number or password pin!")
            }
        }
    }

    fun registerUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existing = repository.getUserByMobile(user.mobile)
            if (existing != null) {
                onError("Account with this mobile number already exists!")
                return@launch
            }
            repository.insertUser(user)
            currentUser = user
            isAdmin = false
            onSuccess()
        }
    }

    fun logout() {
        currentUser = null
        isAdmin = false
    }

    // --- Volunteer Application Actions ---

    fun applyAsVolunteer(
        name: String,
        mobile: String,
        address: String,
        age: Int,
        photo: String,
        aadhaar: String,
        category: String,
        onSubmitted: (String) -> Unit
    ) {
        viewModelScope.launch {
            val rand = Random()
            val suffix = 1000 + rand.nextInt(9000)
            val trackingNum = "DPUJAVOL2026-$suffix"

            val app = VolunteerApplication(
                trackingNumber = trackingNum,
                fullName = name,
                mobile = mobile,
                address = address,
                age = age,
                photoUri = photo,
                aadhaar = aadhaar,
                workCategory = category,
                status = "PENDING"
            )
            repository.insertApplication(app)

            // Trigger instant Admin notification
            repository.insertNotification(
                Notification(
                    title = "New Volunteer Submitted!",
                    message = "$name applied for category $category. Click detail to approve/reject."
                )
            )

            onSubmitted(trackingNum)
        }
    }

    suspend fun trackVolunteerApplication(trackingNum: String): VolunteerApplication? {
        return repository.getApplicationByTracking(trackingNum)
    }

    fun approveVolunteer(trackingNum: String) {
        viewModelScope.launch {
            val app = repository.getApplicationByTracking(trackingNum) ?: return@launch
            val rand = Random()
            val vId = "PUJA26VOL" + (1000 + rand.nextInt(9000))
            val vPwd = "PASS" + (100 + rand.nextInt(900))

            repository.updateVolunteerStatus(trackingNum, "APPROVED", vId, vPwd)
            repository.insertNotification(
                Notification(
                    title = "Volunteer Application Approved!",
                    message = "Congratulations ${app.fullName}! You are assigned as a Puja Volunteer. ID: $vId"
                )
            )
        }
    }

    fun rejectVolunteer(trackingNum: String) {
        viewModelScope.launch {
            val app = repository.getApplicationByTracking(trackingNum) ?: return@launch
            repository.updateVolunteerStatus(trackingNum, "REJECTED", null, null)
            repository.insertNotification(
                Notification(
                    title = "Volunteer Application Rejected",
                    message = "Application of ${app.fullName} has been rejected."
                )
            )
        }
    }

    // --- Bookings Actions ---

    fun bookPrasad(userMobile: String, quantity: Int, prasadType: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val rand = Random()
            val trackingNum = "Bhog-${1000 + rand.nextInt(9000)}"
            val booking = PrasadBooking(
                trackingNumber = trackingNum,
                userMobile = userMobile,
                quantity = quantity,
                prasadType = prasadType,
                status = "ACCEPTED"
            )
            repository.insertPrasadBooking(booking)
            onComplete(trackingNum)
        }
    }

    fun bookPuja(userMobile: String, devoteeName: String, pujaType: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val rand = Random()
            // Generate unique 11-digit Ticket number starting with 2026: 202645 + 5 random digits
            val randDigits = 10000 + rand.nextInt(90000)
            val ticketNum = "202645$randDigits"

            val booking = PujaBooking(
                ticketNumber = ticketNum,
                userMobile = userMobile,
                pujaType = pujaType,
                devoteeName = devoteeName,
                status = "CONFIRMED"
            )
            repository.insertPujaBooking(booking)
            onComplete(ticketNum)
        }
    }

    // --- Gallery Actions ---

    fun uploadMemory(caption: String, presetImageUri: String) {
        viewModelScope.launch {
            val uploaderName = currentUser?.name ?: "Anonymous Devotee"
            val memory = GalleryMemory(
                uploaderName = uploaderName,
                caption = caption,
                imageUri = presetImageUri
            )
            repository.insertMemory(memory)
        }
    }

    fun likeMemory(id: Int) {
        viewModelScope.launch {
            repository.incrementLike(id)
        }
    }

    // --- AppSettings Admin Control Actions ---

    fun updateAppSettings(
        chairmanName: String,
        chairmanPhoto: String,
        chairmanDesig: String,
        viceChairmanName: String,
        viceChairmanPhoto: String,
        viceChairmanDesig: String,
        logoUri: String,
        bannerUri: String,
        posterUri: String,
        upi: String,
        qrCode: String,
        policeNum: String,
        ambulanceNum: String,
        fireNum: String,
        womenNum: String,
        committeeNum: String
    ) {
        viewModelScope.launch {
            val current = repository.getSettingsDirect() ?: AppSettings()
            val updated = current.copy(
                chairmanName = chairmanName,
                chairmanPhotoUri = chairmanPhoto,
                chairmanDesignation = chairmanDesig,
                viceChairmanName = viceChairmanName,
                viceChairmanPhotoUri = viceChairmanPhoto,
                viceChairmanDesignation = viceChairmanDesig,
                officialLogoUri = logoUri,
                committeeBannerUri = bannerUri,
                festivalPosterUri = posterUri,
                upiId = upi,
                qrCodeUri = qrCode,
                emergencyPolice = policeNum,
                emergencyAmbulance = ambulanceNum,
                emergencyFire = fireNum,
                emergencyWomen = womenNum,
                emergencyCommittee = committeeNum
            )
            repository.saveSettings(updated)
            repository.insertNotification(
                Notification(
                    title = "System Setup Updated",
                    message = "Puja settings (payment QR, contacts, and layout banners) updated by administrator."
                )
            )
        }
    }

    // Push Broadcast Notification as Admin
    fun broadcastNotification(title: String, message: String) {
        viewModelScope.launch {
            repository.insertNotification(
                Notification(title = title, message = message)
            )
        }
    }

    // --- Interactive AI Chat Bot Actions ---

    fun askChatbot(prompt: String) {
        if (prompt.isBlank()) return
        
        val userMsg = ChatMessage("user", prompt)
        _chatHistory.add(userMsg)
        
        isChatLoading = true
        viewModelScope.launch {
            val responseText = repository.askGemini(prompt)
            _chatHistory.add(ChatMessage("assistant", responseText))
            isChatLoading = false
        }
    }
}

// Utility class to hold local helper item mappings
private fun <T> mutableStateListOf() = androidx.compose.runtime.mutableStateListOf<T>()
