package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val mobile: String,
    val name: String,
    val password: String,
    val address: String,
    val profilePhotoUri: String
)

@Entity(tableName = "volunteer_applications")
data class VolunteerApplication(
    @PrimaryKey val trackingNumber: String,
    val fullName: String,
    val mobile: String,
    val address: String,
    val age: Int,
    val photoUri: String,
    val aadhaar: String,
    val workCategory: String,
    val status: String, // "PENDING", "APPROVED", "REJECTED"
    val volunteerId: String? = null,
    val volunteerPassword: String? = null
)

@Entity(tableName = "prasad_bookings")
data class PrasadBooking(
    @PrimaryKey val trackingNumber: String,
    val userMobile: String,
    val quantity: Int,
    val prasadType: String,
    val bookingDate: Long = System.currentTimeMillis(),
    val status: String // "PENDING", "ACCEPTED", "READY", "DELIVERED"
)

@Entity(tableName = "puja_bookings")
data class PujaBooking(
    @PrimaryKey val ticketNumber: String, // 11 digits
    val userMobile: String,
    val pujaType: String,
    val devoteeName: String,
    val bookingDate: Long = System.currentTimeMillis(),
    val status: String // "CONFIRMED"
)

@Entity(tableName = "gallery_memories")
data class GalleryMemory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uploaderName: String,
    val caption: String,
    val imageUri: String, // Base64 or Preset resource representation
    val likes: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey val id: Int = 1,
    val chairmanName: String = "Ayush Tiwari",
    val chairmanPhotoUri: String = "avatar_traditional_male",
    val chairmanDesignation: String = "Puja Committee Chairman",
    val viceChairmanName: String = "Shri Somnath Chatterjee",
    val viceChairmanPhotoUri: String = "avatar_traditional_female",
    val viceChairmanDesignation: String = "Puja Committee Vice Chairman",
    val officialLogoUri: String = "logo_pujo_traditional",
    val committeeBannerUri: String = "banner_pujo_festive",
    val festivalPosterUri: String = "poster_pujo_cinematic",
    val qrCodeUri: String = "upi_qr_mock", // QR description/URI
    val upiId: String = "aysuhtiwari668@ybl",
    val emergencyPolice: String = "100",
    val emergencyAmbulance: String = "102",
    val emergencyFire: String = "101",
    val emergencyWomen: String = "1091",
    val emergencyCommittee: String = "9335937461"
)
