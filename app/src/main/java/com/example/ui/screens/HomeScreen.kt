package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.AppSettings
import com.example.ui.MainViewModel
import com.example.ui.components.FestivalParticlesOverlay
import com.example.ui.components.PujaSoundSynthesizer
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToBooking: () -> Unit,
    onNavigateToVolunteer: () -> Unit,
    onNavigateToEmergency: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // States
    val settings by viewModel.appSettings.collectAsState(initial = null)
    val notifications by viewModel.allNotifications.collectAsState(initial = emptyList())

    var showTrackingDialog by remember { mutableStateOf(false) }
    var trackingInputNumber by remember { mutableStateOf("") }
    var trackingResultText by remember { mutableStateOf<String?>(null) }
    var trackingStatusType by remember { mutableStateOf("") } // "PENDING", "APPROVED", "REJECTED"
    var trackingCredentialsId by remember { mutableStateOf("") }
    var trackingCredentialsPassword by remember { mutableStateOf("") }

    // Live Soundboard Vibrational animations states
    var bellRotation by remember { mutableStateOf(0f) }
    var shankhScale by remember { mutableStateOf(1f) }
    var dhakDisplacement by remember { mutableStateOf(0f) }

    // Continuous sound rotational state animations
    val bellAnim = animateFloatAsState(
        targetValue = bellRotation,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium),
        finishedListener = { bellRotation = 0f },
        label = "bell"
    )
    val shankhAnim = animateFloatAsState(
        targetValue = shankhScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
        finishedListener = { shankhScale = 1f },
        label = "shankh"
    )
    val dhakAnim = animateFloatAsState(
        targetValue = dhakDisplacement,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh),
        finishedListener = { dhakDisplacement = 0f },
        label = "dhak"
    )

    // Countdown calculation to Maha Saptami: October 17, 2026, 08:00 AM
    var countdownText by remember { mutableStateOf("Loading Holy Countdown...") }
    LaunchedEffect(Unit) {
        while (true) {
            val targetTime = GregorianCalendar(2026, Calendar.OCTOBER, 17, 8, 0, 0).timeInMillis
            val diff = targetTime - System.currentTimeMillis()
            if (diff > 0) {
                val days = diff / (1000 * 60 * 60 * 24)
                val hours = (diff / (1000 * 60 * 60)) % 24
                val minutes = (diff / (1000 * 60)) % 60
                val seconds = (diff / 1000) % 60
                countdownText = "$days Days : $hours Hrs : $minutes Min : $seconds Sec"
            } else {
                countdownText = "✨ PROUD CELEBRATIONS HAVE BEGUN! ✨"
            }
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
    ) {
        // Overlay light flower falling visuals
        FestivalParticlesOverlay(particleType = "flowers")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Ticker Marquee Alert at the top
            notifications.firstOrNull()?.let { activeNotice ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CrimsonRed)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🚨 BROADCAST: ",
                        fontWeight = FontWeight.Bold,
                        color = ShimmeringGold,
                        fontSize = 11.sp
                    )
                    Text(
                        text = activeNotice.message,
                        color = Color.White,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // High aesthetic header, custom branding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Banner background
                val bannerUrl = settings?.committeeBannerUri ?: "durga_puja_banner_luxurious"
                if (bannerUrl.startsWith("http")) {
                    AsyncImage(
                        model = bannerUrl,
                        contentDescription = "Committee Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Luxurious Fallback artistic gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(CrimsonRed, DeepCharcoal)
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🚩 শ্রী শ্রী দুর্গা পূজা ২০২৬ 🚩",
                                style = MaterialTheme.typography.titleLarge,
                                color = ShimmeringGold,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "SHREE SHREE DURGA PUJA COMMITTEE",
                                fontSize = 11.sp,
                                letterSpacing = 2.sp,
                                color = SacredIvory.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Header gradient shadow Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(Color.Transparent, DeepCharcoal)
                            )
                        )
                )

                // Committiee Logo Float Circle layout
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(54.dp)
                        .background(ShimmeringGold, shape = CircleShape)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = "🌺",
                        fontSize = 28.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Cinematic Countdown Module
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF23171B)),
                border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔱 MAHA SAPTAMI COUNTDOWN 🔱",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShimmeringGold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = countdownText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- THE SENSORY PUJO SOUNDBOARD ---
            Text(
                text = "🔔 Sacred Puja Soundboard",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = ShimmeringGold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Bell button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            bellRotation = if (bellRotation == 15f) -15f else 15f
                            coroutineScope.launch {
                                PujaSoundSynthesizer.playBellSound(context)
                            }
                        }
                        .graphicsLayer(rotationZ = bellAnim.value),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF33141A)),
                    border = BorderStroke(1.dp, TempleSaffron.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔔", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ring Bell", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ShimmeringGold)
                    }
                }

                // Shankh conch Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            shankhScale = 1.25f
                            coroutineScope.launch {
                                PujaSoundSynthesizer.playShankhSound(context)
                            }
                        }
                        .graphicsLayer(scaleX = shankhAnim.value, scaleY = shankhAnim.value),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF332014)),
                    border = BorderStroke(1.dp, TempleSaffron.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🐚", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Blow Shankh", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ShimmeringGold)
                    }
                }

                // Dhak Drum Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            dhakDisplacement = if (dhakDisplacement == 8f) -8f else 8f
                            coroutineScope.launch {
                                PujaSoundSynthesizer.playDhakBeat(context)
                            }
                        }
                        .graphicsLayer(translationX = dhakAnim.value),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F3B1A)),
                    border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🥁", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Beat Dhak", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = ShimmeringGold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- TRACK REGISTRATION BUTTON (Mandatory Large Display Button) ---
            Button(
                onClick = {
                    trackingInputNumber = ""
                    trackingResultText = null
                    showTrackingDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                border = BorderStroke(1.3.dp, ShimmeringGold)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ShimmeringGold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "TRACK YOUR REGISTRATION",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = ShimmeringGold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- COMMITTEE LEADER CORNER ---
            Text(
                text = "🎖️ Executive Puja Board",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = ShimmeringGold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Chairman card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1417)),
                    border = BorderStroke(1.dp, TempleSaffron.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ShimmeringGold)
                                .padding(1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (settings?.chairmanName == "Ayush Tiwari") "🤵" else "👴",
                                fontSize = 34.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = settings?.chairmanName ?: "Ayush Tiwari",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = settings?.chairmanDesignation ?: "Puja Committee Chairman",
                            fontSize = 10.sp,
                            color = ShimmeringGold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Vice Chairman Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1417)),
                    border = BorderStroke(1.dp, TempleSaffron.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ShimmeringGold)
                                .padding(1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👩", fontSize = 34.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = settings?.viceChairmanName ?: "Shri Somnath Chatterjee",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = settings?.viceChairmanDesignation ?: "Puja Committee Vice Chairman",
                            fontSize = 10.sp,
                            color = ShimmeringGold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- HIGH ACTION GRID TILES shortcuts ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Book Puja / Prasad
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToBooking() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF281F1A)),
                    border = BorderStroke(1.2.dp, ShimmeringGold)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✨", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Rituals Booking", fontWeight = FontWeight.Bold, color = ShimmeringGold, fontSize = 13.sp)
                            Text("Puja and Prasad online", fontSize = 10.sp, color = SacredIvory.copy(alpha = 0.7f))
                        }
                    }
                }

                // Register Volunteer Shortcut
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToVolunteer() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1C16)),
                    border = BorderStroke(1.2.dp, ShimmeringGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔱", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Onboard Sewak", fontWeight = FontWeight.Bold, color = ShimmeringGold, fontSize = 13.sp)
                            Text("Apply as a Sewak", fontSize = 10.sp, color = SacredIvory.copy(alpha = 0.7f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- VIBRANT PANDALS & LIVE CROWD DETECTOR GUIDE ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗺️ Dynamic Pandal Tracker (Live Status)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ShimmeringGold
                )
                Text(
                    text = "Emergency 🆘",
                    fontSize = 11.sp,
                    color = CrimsonRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToEmergency() }
                )
            }

            val mockPandals = listOf(
                PandalTrackingItem("Central Town Park Ahiritola", "950m", "High 🔥", TempleSaffron, "Ahiritola lighting displays and grand traditional idol"),
                PandalTrackingItem("Sreebhumi Sporting Club", "3.2 km", "Extreme 🚨", CrimsonRed, "Replica gold temple & security alerts running"),
                PandalTrackingItem("College Square Puja", "1.4 km", "Medium ⚖️", Color(0xFFFFD700), "Scenic lake lights background and peaceful queues"),
                PandalTrackingItem("Maddox Square Ground", "4.8 km", "Low 🍀", Color(0xFF4CAF50), "Open assembly zone with grass seating and delicious food stalls")
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            ) {
                items(mockPandals) { pandal ->
                    Card(
                        modifier = Modifier
                            .padding(6.dp)
                            .width(230.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1417)),
                        border = BorderStroke(1.dp, pandal.badgeColor.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pandal.distance,
                                    fontSize = 11.sp,
                                    color = TempleSaffron,
                                    fontWeight = FontWeight.Bold
                                )

                                Box(
                                    modifier = Modifier
                                        .background(pandal.badgeColor.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Crowd: ${pandal.crowdCount}",
                                        fontSize = 10.sp,
                                        color = pandal.badgeColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = pandal.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = pandal.desc,
                                fontSize = 10.sp,
                                color = SacredIvory.copy(alpha = 0.7f),
                                maxLines = 2,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // --- THE MASSIVE DIALOG PANEL FOR VOLUNTEER REGISTRATION TRACKING ---
        if (showTrackingDialog) {
            AlertDialog(
                onDismissRequest = { showTrackingDialog = false },
                title = {
                    Text(
                        text = "🔍 Tracker Portal sewak",
                        color = ShimmeringGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Enter tracking code (e.g. DPUJAVOL2026-YOUR_CODE_NUMBER):",
                            color = SacredIvory.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = trackingInputNumber,
                            onValueChange = { trackingInputNumber = it },
                            placeholder = { Text("DPUJAVOL2026...", color = SacredIvory.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SacredIvory,
                                unfocusedTextColor = SacredIvory,
                                focusedBorderColor = ShimmeringGold,
                                unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        trackingResultText?.let { result ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = when (trackingStatusType) {
                                        "APPROVED" -> Color(0xFF003D1B)
                                        "REJECTED" -> Color(0xFF4C0F0F)
                                        else -> Color(0xFF332007)
                                    }
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    when (trackingStatusType) {
                                        "APPROVED" -> Color.Green
                                        "REJECTED" -> Color.Red
                                        else -> Color.Yellow
                                    }
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = result,
                                        fontWeight = FontWeight.Bold,
                                        color = SacredIvory,
                                        fontSize = 13.sp
                                    )

                                    if (trackingStatusType == "APPROVED") {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "🔑 SEWAK SECURE ACCESS DETAILS:",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = ShimmeringGold
                                        )
                                        Text(text = "ID: $trackingCredentialsId", fontSize = 12.sp, color = Color.White)
                                        Text(text = "PASSWORD: $trackingCredentialsPassword", fontSize = 12.sp, color = Color.White)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Note down credentials safely to access Sewak portals.",
                                            fontSize = 9.sp,
                                            color = SacredIvory.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        onClick = {
                            if (trackingInputNumber.isBlank()) {
                                trackingResultText = "Please enter a valid tracking number!"
                                trackingStatusType = "ERROR"
                                return@Button
                            }
                            coroutineScope.launch {
                                val application = viewModel.trackVolunteerApplication(trackingInputNumber.trim())
                                if (application != null) {
                                    trackingStatusType = application.status
                                    when (application.status) {
                                        "PENDING" -> {
                                            trackingResultText = "⏳ STATUS: PENDING\n\nYour application has been received and is waiting for puja committee review. Please check back soon."
                                        }
                                        "APPROVED" -> {
                                            trackingResultText = "🎉 STATUS: APPROVED\n\nPranam! The committee has approved your dedication to volunteer!"
                                            trackingCredentialsId = application.volunteerId ?: ""
                                            trackingCredentialsPassword = application.volunteerPassword ?: ""
                                        }
                                        "REJECTED" -> {
                                            trackingResultText = "❌ STATUS: REJECTED\n\nYou are rejected. Please apply again."
                                        }
                                    }
                                } else {
                                    trackingStatusType = "NOT_FOUND"
                                    trackingResultText = "🚫 INVALID TRACKING NUMBER\n\nNo sewak profile matched this tracking code. Check the format and numbers properly!"
                                }
                            }
                        }
                    ) {
                        Text("Track Now", color = ShimmeringGold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTrackingDialog = false }) {
                        Text("Dismiss", color = SacredIvory.copy(alpha = 0.6f))
                    }
                },
                containerColor = Color(0xFF1E1417)
            )
        }
    }
}

data class PandalTrackingItem(
    val name: String,
    val distance: String,
    val crowdCount: String,
    val badgeColor: Color,
    val desc: String
)
