package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        VolunteerApplication::class,
        PrasadBooking::class,
        PujaBooking::class,
        GalleryMemory::class,
        Notification::class,
        AppSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun volunteerDao(): VolunteerDao
    abstract fun prasadBookingDao(): PrasadBookingDao
    abstract fun pujaBookingDao(): PujaBookingDao
    abstract fun galleryDao(): GalleryDao
    abstract fun notificationDao(): NotificationDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "durga_puja_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            // Seed default AppSettings
            val defaultSettings = AppSettings(
                id = 1,
                chairmanName = "Ayush Tiwari",
                chairmanPhotoUri = "avatar_traditional_male",
                chairmanDesignation = "Puja Committee Chairman",
                viceChairmanName = "Shri Somnath Chatterjee",
                viceChairmanPhotoUri = "avatar_traditional_female",
                viceChairmanDesignation = "Puja Committee Vice Chairman",
                officialLogoUri = "logo_traditional_glow",
                committeeBannerUri = "durga_puja_banner_luxurious",
                festivalPosterUri = "durga_puja_poster_cinematic",
                qrCodeUri = "upi://pay?pa=aysuhtiwari668@ybl&pn=DurgaPujaCommittee&cu=INR&am=501",
                upiId = "aysuhtiwari668@ybl",
                emergencyPolice = "100",
                emergencyAmbulance = "102",
                emergencyFire = "101",
                emergencyWomen = "1091",
                emergencyCommittee = "9335937461"
            )
            db.settingsDao().saveSettings(defaultSettings)

            // Seed some notifications
            db.notificationDao().insertNotification(
                Notification(
                    title = "Hearty Welcome!",
                    message = "Warm greetings from your Puja committee. Dive into the holy atmosphere."
                )
            )
            db.notificationDao().insertNotification(
                Notification(
                    title = "Maha Saptami Aarti Stream",
                    message = "Maha Saptami direct stream starts tonight at 7:00 PM. Access Live Aarti."
                )
            )

            // Seed initial gallery memories
            db.galleryDao().insertMemory(
                GalleryMemory(
                    uploaderName = "Committee Admin",
                    caption = "Spectacular view of Maha Shasthi pandal! Lighting up the sky.",
                    imageUri = "https://images.unsplash.com/photo-1545641203-7d6cf291b3f5?auto=format&fit=crop&w=800&q=80",
                    likes = 1258
                )
            )
            db.galleryDao().insertMemory(
                GalleryMemory(
                    uploaderName = "Ayush",
                    caption = "Divine blessings of Maha Ashtami Pushpanjali ritual.",
                    imageUri = "https://images.unsplash.com/photo-1561054708-3abbeb355701?auto=format&fit=crop&w=800&q=80",
                    likes = 3450
                )
            )
        }
    }
}
