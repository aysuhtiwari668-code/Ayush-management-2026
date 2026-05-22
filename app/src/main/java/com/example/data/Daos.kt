package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE mobile = :mobile")
    suspend fun getUserByMobile(mobile: String): User?

    @Query("SELECT * FROM users")
    fun getAllAsFlow(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}

@Dao
interface VolunteerDao {
    @Query("SELECT * FROM volunteer_applications WHERE trackingNumber = :trackingNum")
    suspend fun getApplicationByTracking(trackingNum: String): VolunteerApplication?

    @Query("SELECT * FROM volunteer_applications WHERE mobile = :mobile")
    suspend fun getApplicationByMobile(mobile: String): VolunteerApplication?

    @Query("SELECT * FROM volunteer_applications ORDER BY trackingNumber DESC")
    fun getAllApplicationsFlow(): Flow<List<VolunteerApplication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(app: VolunteerApplication)

    @Query("UPDATE volunteer_applications SET status = :status, volunteerId = :vId, volunteerPassword = :vPwd WHERE trackingNumber = :trackingNum")
    suspend fun updateStatus(trackingNum: String, status: String, vId: String?, vPwd: String?)
}

@Dao
interface PrasadBookingDao {
    @Query("SELECT * FROM prasad_bookings WHERE trackingNumber = :trackingNum")
    suspend fun getPrasadByTracking(trackingNum: String): PrasadBooking?

    @Query("SELECT * FROM prasad_bookings WHERE userMobile = :mobile ORDER BY bookingDate DESC")
    fun getPrasadByUserFlow(mobile: String): Flow<List<PrasadBooking>>

    @Query("SELECT * FROM prasad_bookings ORDER BY bookingDate DESC")
    fun getAllBookingsFlow(): Flow<List<PrasadBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrasadBooking(booking: PrasadBooking)
}

@Dao
interface PujaBookingDao {
    @Query("SELECT * FROM puja_bookings WHERE ticketNumber = :ticketNum")
    suspend fun getPujaByTicket(ticketNum: String): PujaBooking?

    @Query("SELECT * FROM puja_bookings WHERE userMobile = :mobile ORDER BY bookingDate DESC")
    fun getPujaByUserFlow(mobile: String): Flow<List<PujaBooking>>

    @Query("SELECT * FROM puja_bookings ORDER BY bookingDate DESC")
    fun getAllBookingsFlow(): Flow<List<PujaBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPujaBooking(booking: PujaBooking)
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_memories ORDER BY timestamp DESC")
    fun getAllAsFlow(): Flow<List<GalleryMemory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: GalleryMemory)

    @Query("UPDATE gallery_memories SET likes = likes + 1 WHERE id = :id")
    suspend fun incrementLike(id: Int)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllAsFlow(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettingsFlow(): Flow<AppSettings?>

    @Query("SELECT * FROM app_settings WHERE id = 1")
    suspend fun getSettingsDirect(): AppSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: AppSettings)
}
