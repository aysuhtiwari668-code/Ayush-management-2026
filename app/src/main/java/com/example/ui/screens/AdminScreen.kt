package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppSettings
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val applications by viewModel.allApplications.collectAsState(initial = emptyList())
    val prasadBookings by viewModel.allPrasadBookings.collectAsState(initial = emptyList())
    val pujaBookings by viewModel.allPujaBookings.collectAsState(initial = emptyList())
    val settings by viewModel.appSettings.collectAsState(initial = null)

    var activeAdminTab by remember { mutableStateOf("volunteers") } // "volunteers", "bookings", "settings", "notifications"

    // Settings panel state
    var chairmanName by remember { mutableStateOf("") }
    var chairmanDesig by remember { mutableStateOf("") }
    var viceChairmanName by remember { mutableStateOf("") }
    var viceChairmanDesig by remember { mutableStateOf("") }
    var logoUriText by remember { mutableStateOf("") }
    var bannerUriText by remember { mutableStateOf("") }
    var posterUriText by remember { mutableStateOf("") }
    var upiIdText by remember { mutableStateOf("") }
    var qrCodeText by remember { mutableStateOf("") }
    var policeContact by remember { mutableStateOf("") }
    var ambulanceContact by remember { mutableStateOf("") }
    var fireContact by remember { mutableStateOf("") }
    var womenContact by remember { mutableStateOf("") }
    var committeeContact by remember { mutableStateOf("") }

    // Broadcast panel state
    var broadcastTitle by remember { mutableStateOf("") }
    var broadcastMessage by remember { mutableStateOf("") }
    var broadcastSuccessMsg by remember { mutableStateOf<String?>(null) }
    var settingsSuccessMsg by remember { mutableStateOf<String?>(null) }

    // Sync input states once settings are loaded
    LaunchedEffect(settings) {
        settings?.let { s ->
            chairmanName = s.chairmanName
            chairmanDesig = s.chairmanDesignation
            viceChairmanName = s.viceChairmanName
            viceChairmanDesig = s.viceChairmanDesignation
            logoUriText = s.officialLogoUri
            bannerUriText = s.committeeBannerUri
            posterUriText = s.festivalPosterUri
            upiIdText = s.upiId
            qrCodeText = s.qrCodeUri
            policeContact = s.emergencyPolice
            ambulanceContact = s.emergencyAmbulance
            fireContact = s.emergencyFire
            womenContact = s.emergencyWomen
            committeeContact = s.emergencyCommittee
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Dynamic admin back header toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "⚙️ Admin Control Station",
                    fontWeight = FontWeight.Black,
                    color = ShimmeringGold,
                    fontSize = 16.sp
                )
                Text(text = "Primary Administrator: Ayush Tiwari", fontSize = 9.sp, color = SacredIvory.copy(alpha = 0.5f))
            }

            Button(
                onClick = onNavigateBack,
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                border = BorderStroke(1.dp, ShimmeringGold)
            ) {
                Text("Exit Admin", color = ShimmeringGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Subtabs scrolling selector row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .background(DarkSurface)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                "volunteers" to "🤝 Approval Sewaks",
                "bookings" to "📊 Live Bookings",
                "settings" to "📝 Update System Params",
                "notifications" to "📢 Push Broadcasts"
            )
            tabs.forEach { (key, title) ->
                val isActive = activeAdminTab == key
                Box(
                    modifier = Modifier
                        .background(if (isActive) CrimsonRed else DeepCharcoal, shape = RoundedCornerShape(8.dp))
                        .clickable { activeAdminTab = key }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .border(1.dp, if (isActive) ShimmeringGold else Color.Transparent, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        color = if (isActive) ShimmeringGold else SacredIvory,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Active layout contents
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(14.dp)
        ) {
            when (activeAdminTab) {
                "volunteers" -> {
                    val pendingApps = applications.filter { it.status == "PENDING" }
                    if (pendingApps.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🤝", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No Pending Sewak Applications",
                                    fontWeight = FontWeight.Bold,
                                    color = SacredIvory.copy(alpha = 0.5f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(pendingApps) { app ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                    border = BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.4f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = app.fullName,
                                                fontWeight = FontWeight.Bold,
                                                color = SacredIvory,
                                                fontSize = 14.sp
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(CrimsonRed.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                              ) {
                                                Text(app.workCategory, fontSize = 9.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "Mobile: ${app.mobile}", fontSize = 11.sp, color = SacredIvory.copy(alpha = 0.7f))
                                        Text(text = "Age: ${app.age} • Address: ${app.address}", fontSize = 11.sp, color = SacredIvory.copy(alpha = 0.7f))
                                        if (app.aadhaar.isNotBlank()) {
                                            Text(text = "Aadhaar: ${app.aadhaar}", fontSize = 11.sp, color = SacredIvory.copy(alpha = 0.7f))
                                        }
                                        Text(text = "Tracking ID: ${app.trackingNumber}", fontSize = 11.sp, color = TempleSaffron, fontWeight = FontWeight.Bold)

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Button(
                                                onClick = { viewModel.rejectVolunteer(app.trackingNumber) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEB)),
                                                modifier = Modifier.padding(end = 8.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Reject", tint = CrimsonRed, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Reject", color = CrimsonRed, fontSize = 11.sp)
                                            }

                                            Button(
                                                onClick = { viewModel.approveVolunteer(app.trackingNumber) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD1FAE5))
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = "Approve", tint = Color(0xFF065F46), modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Approve", color = Color(0xFF065F46), fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "bookings" -> {
                    // List Prasad Bookings and Puja tickets
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Online Ritual Bookings Logbook",
                            fontWeight = FontWeight.Bold,
                            color = ShimmeringGold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Showing Prasad and Puja bookings processed, syncing under admin payment codes.",
                            fontSize = 10.sp,
                            color = SacredIvory.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        TabRow(
                            selectedTabIndex = 0,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = DarkSurface
                        ) {
                            // Sub panel view toggle representation
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                Text("🍛 PRE-BOOKED PRASAD PACKETS (${prasadBookings.size}):", fontSize = 12.sp, color = ShimmeringGold, fontWeight = FontWeight.Bold)
                            }
                            items(prasadBookings) { bhog ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = bhog.prasadType, color = SacredIvory, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            Text(text = "Caller ID: ${bhog.userMobile} • Qty: ${bhog.quantity}", fontSize = 10.sp, color = SacredIvory.copy(alpha = 0.7f))
                                        }
                                        Text(text = bhog.trackingNumber, color = TempleSaffron, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text("🔱 PUJA SANCOV RITUALS TICKETS (${pujaBookings.size}):", fontSize = 12.sp, color = ShimmeringGold, fontWeight = FontWeight.Bold)
                            }
                            items(pujaBookings) { booking ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = booking.pujaType, color = SacredIvory, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            Text(text = "Chant: ${booking.devoteeName} • Caller: ${booking.userMobile}", fontSize = 10.sp, color = SacredIvory.copy(alpha = 0.7f))
                                        }
                                        Text(text = booking.ticketNumber, color = TempleSaffron, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }
                    }
                }

                "settings" -> {
                    // Update QR config and details
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Central Committee Setup Console",
                            fontWeight = FontWeight.Bold,
                            color = ShimmeringGold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        settingsSuccessMsg?.let { msg ->
                            Text(text = msg, color = Color.Green, fontSize = 13.sp, modifier = Modifier.padding(vertical = 4.dp))
                        }

                        // UPI edits
                        OutlinedTextField(
                            value = upiIdText,
                            onValueChange = { upiIdText = it },
                            label = { Text("Payment UPI ID (Everywhere synced)", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // QR URI code
                        OutlinedTextField(
                            value = qrCodeText,
                            onValueChange = { qrCodeText = it },
                            label = { Text("UPI QR URI Payment protocol", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("👑 CHAIRMAN CONFIG:", fontSize = 11.sp, color = ShimmeringGold, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = chairmanName,
                            onValueChange = { chairmanName = it },
                            label = { Text("Chairman Full Name", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = chairmanDesig,
                            onValueChange = { chairmanDesig = it },
                            label = { Text("Chairman Designation Title", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("🎖️ VICE-CHAIRMAN CONFIG:", fontSize = 11.sp, color = ShimmeringGold, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = viceChairmanName,
                            onValueChange = { viceChairmanName = it },
                            label = { Text("Vice-Chairman Full Name", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = viceChairmanDesig,
                            onValueChange = { viceChairmanDesig = it },
                            label = { Text("Vice-Chairman Designation State", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("🆘 LIVE EMERGENCY DIRECTORY NUMBERS:", fontSize = 11.sp, color = ShimmeringGold, fontWeight = FontWeight.Bold)

                        OutlinedTextField(value = policeContact, onValueChange = { policeContact = it }, label = { Text("Police") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(value = ambulanceContact, onValueChange = { ambulanceContact = it }, label = { Text("Ambulance") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(value = fireContact, onValueChange = { fireContact = it }, label = { Text("Fire Department") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(value = womenContact, onValueChange = { womenContact = it }, label = { Text("Women Protection Line") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(value = committeeContact, onValueChange = { committeeContact = it }, label = { Text("Committee Line") }, modifier = Modifier.fillMaxWidth())

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.updateAppSettings(
                                    chairmanName = chairmanName,
                                    chairmanPhoto = "avatar_traditional_male",
                                    chairmanDesig = chairmanDesig,
                                    viceChairmanName = viceChairmanName,
                                    viceChairmanPhoto = "avatar_traditional_female",
                                    viceChairmanDesig = viceChairmanDesig,
                                    logoUri = logoUriText,
                                    bannerUri = bannerUriText,
                                    posterUri = posterUriText,
                                    upi = upiIdText,
                                    qrCode = qrCodeText,
                                    policeNum = policeContact,
                                    ambulanceNum = ambulanceContact,
                                    fireNum = fireContact,
                                    womenNum = womenContact,
                                    committeeNum = committeeContact
                                )
                                settingsSuccessMsg = "✅ Database settings updated successfully!"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SAVE OVERALL SETTINGS", color = ShimmeringGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                "notifications" -> {
                    // Send notification to broadcast
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Broadcast Alerts Center",
                            fontWeight = FontWeight.Bold,
                            color = ShimmeringGold,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Send alert notification warnings that crop up dynamically on devotee Home marquee lines.",
                            fontSize = 11.sp,
                            color = SacredIvory.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        broadcastSuccessMsg?.let { msg ->
                            Text(text = msg, color = Color.Green, fontSize = 13.sp, modifier = Modifier.padding(vertical = 4.dp))
                        }

                        OutlinedTextField(
                            value = broadcastTitle,
                            onValueChange = { broadcastTitle = it },
                            placeholder = { Text("Alert Topic (e.g., Maha Saptami Aarti Stream)", color = SacredIvory.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = broadcastMessage,
                            onValueChange = { broadcastMessage = it },
                            placeholder = { Text("Type full alert context details here...", color = SacredIvory.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = SacredIvory, unfocusedTextColor = SacredIvory, focusedBorderColor = ShimmeringGold),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (broadcastTitle.isNotBlank() && broadcastMessage.isNotBlank()) {
                                    viewModel.broadcastNotification(broadcastTitle, broadcastMessage)
                                    broadcastSuccessMsg = "✅ Notification broadcasted globally!"
                                    broadcastTitle = ""
                                    broadcastMessage = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SEND INSTANT ALERT BROADCAST", color = ShimmeringGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
