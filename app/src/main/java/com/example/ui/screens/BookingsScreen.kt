package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.FestivalParticlesOverlay
import com.example.ui.theme.*

val PUJA_TYPES = listOf(
    "Shree Maha Ashtami Sankalpa Pushpanjali",
    "Sandhi Puja 108 Diya Ritual Offering",
    "Navami Chandi Path Aarti Blessing",
    "Vijayadashami Shanti Kalyan Puja"
)

val PRASAD_TYPES = listOf(
    "Traditional Khichuri Bhog Box (Standard)",
    "Luxurious Saffron Payesh & Labra Plate (VIP)",
    "Chana-Narkel Pradapi Laddu Packet"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    var activeSubTab by remember { mutableStateOf("puja") } // "puja" or "prasad"

    // Inputs
    var devoteeNameInput by remember { mutableStateOf("") }
    var selectedPujaType by remember { mutableStateOf(PUJA_TYPES[0]) }

    var prasadQuantityInput by remember { mutableStateOf(1) }
    var selectedPrasadType by remember { mutableStateOf(PRASAD_TYPES[0]) }

    // Booking Output States
    var bookingModeActive by remember { mutableStateOf(true) } // true: inputs, false: ticket success
    var lastTicketNumber by remember { mutableStateOf("") }
    var lastTrackingNumber by remember { mutableStateOf("") }
    var successType by remember { mutableStateOf("") } // "PUJA" or "PRASAD"

    val settings by viewModel.appSettings.collectAsState(initial = null)
    val currentUser = viewModel.currentUser

    // Golden shine linear gradient animation
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    // Build the dynamic glowing golden brush with moving linear highlights
    val goldenShineBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE5A823), // Darker pujo gold
            Color(0xFFFFEA7A), // Extreme high brightness shining light
            Color(0xFFE5A823)
        ),
        start = Offset(shimmerOffset, shimmerOffset),
        end = Offset(shimmerOffset + 350f, shimmerOffset + 350f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
    ) {
        // Overlay gentle warm candle lights
        FestivalParticlesOverlay(particleType = "diya_glow")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.3f))
                ) {
                    Text("← Back Home", color = ShimmeringGold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (bookingModeActive) {
                // Choice selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurface, shape = RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (activeSubTab == "puja") CrimsonRed else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { activeSubTab = "puja" }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔱 BOOK RITUALS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (activeSubTab == "puja") ShimmeringGold else SacredIvory
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (activeSubTab == "prasad") CrimsonRed else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { activeSubTab = "prasad" }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🍛 BOOK PRASAD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (activeSubTab == "prasad") ShimmeringGold else SacredIvory
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (activeSubTab == "puja") {
                    // Puja Form Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Divine Puja Booking Desk",
                                fontWeight = FontWeight.Bold,
                                color = ShimmeringGold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Sankalpa Puja performed online under traditional rituals with named flower offerings.",
                                fontSize = 11.sp,
                                color = SacredIvory.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = devoteeNameInput,
                                onValueChange = { devoteeNameInput = it },
                                label = { Text("Devotee Chant Name", color = SacredIvory.copy(alpha = 0.6f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = SacredIvory,
                                    unfocusedTextColor = SacredIvory,
                                    focusedBorderColor = ShimmeringGold,
                                    unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                                ),
                                placeholder = { Text("e.g. Ayush Tiwari", color = SacredIvory.copy(alpha = 0.3f)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Select Traditional Ritual Type",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShimmeringGold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            PUJA_TYPES.forEach { puja ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedPujaType = puja }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPujaType == puja,
                                        onClick = { selectedPujaType = puja },
                                        colors = RadioButtonDefaults.colors(selectedColor = ShimmeringGold)
                                    )
                                    Text(
                                        text = puja,
                                        fontSize = 12.sp,
                                        color = SacredIvory,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (devoteeNameInput.isBlank()) {
                                        devoteeNameInput = currentUser?.name ?: "Devotee Ayush"
                                    }
                                    val mobile = currentUser?.mobile ?: "9335937461"
                                    viewModel.bookPuja(mobile, devoteeNameInput, selectedPujaType) { ticket ->
                                        lastTicketNumber = ticket
                                        successType = "PUJA"
                                        bookingModeActive = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                                border = BorderStroke(1.dp, ShimmeringGold)
                            ) {
                                Text("BOOK SANCTIFIED PUJA", fontWeight = FontWeight.Bold, color = ShimmeringGold)
                            }
                        }
                    }
                } else {
                    // Prasad form card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Sanctified Bhog Counter",
                                fontWeight = FontWeight.Bold,
                                color = ShimmeringGold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Pre-book Maha Prasad Bhog packets for hand-delivering or collection queues.",
                                fontSize = 11.sp,
                                color = SacredIvory.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Specify Bhog Quantity (Packets)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShimmeringGold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { if (prasadQuantityInput > 1) prasadQuantityInput-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed)
                                ) {
                                    Text("-", fontSize = 18.sp, color = ShimmeringGold)
                                }

                                Text(
                                    text = prasadQuantityInput.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = SacredIvory
                                )

                                Button(
                                    onClick = { prasadQuantityInput++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed)
                                ) {
                                    Text("+", fontSize = 18.sp, color = ShimmeringGold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Select Prasad Variety",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShimmeringGold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            PRASAD_TYPES.forEach { prasad ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedPrasadType = prasad }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPrasadType == prasad,
                                        onClick = { selectedPrasadType = prasad },
                                        colors = RadioButtonDefaults.colors(selectedColor = ShimmeringGold)
                                    )
                                    Text(
                                        text = prasad,
                                        fontSize = 12.sp,
                                        color = SacredIvory,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    val mobile = currentUser?.mobile ?: "9335937461"
                                    viewModel.bookPrasad(mobile, prasadQuantityInput, selectedPrasadType) { trackNum ->
                                        lastTrackingNumber = trackNum
                                        successType = "PRASAD"
                                        bookingModeActive = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                                border = BorderStroke(1.dp, ShimmeringGold)
                            ) {
                                Text("CONFIRM PRASAD PRE-BOOKING", fontWeight = FontWeight.Bold, color = ShimmeringGold)
                            }
                        }
                    }
                }
            } else {
                // THE PREMIUM SHINING GOLDEN TICKET PREVIEW ZONE!
                Text(
                    text = "🏆 Pujo Sanctum Golden Ticket 🏆",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = ShimmeringGold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // The Golden Ticket card wrapper
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = goldenShineBrush) // Applies Diagonal moving shimmer highlight!
                            .padding(2.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(DarkSurface) // Dark internal body
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🚩 MAA DURGA BLESSINGS 🚩",
                            color = ShimmeringGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "SHREE SHREE DURGA PUJA COMMITTEE",
                            color = SacredIvory.copy(alpha = 0.5f),
                            fontSize = 9.sp,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Render dynamic content fields
                        if (successType == "PUJA") {
                            Text(text = "RITUAL ENTRY PASS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = selectedPujaType, fontSize = 12.sp, color = ShimmeringGold, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Devotee Name: $devoteeNameInput", fontSize = 13.sp, color = Color.White)
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = "11-DIGIT TICKET NUMBER", fontSize = 10.sp, color = SacredIvory.copy(alpha = 0.5f))
                            Text(text = lastTicketNumber, fontSize = 18.sp, fontWeight = FontWeight.Black, color = TempleSaffron, letterSpacing = 2.sp)
                        } else {
                            Text(text = "MAHA PRASAD RECEIPT", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = selectedPrasadType, fontSize = 12.sp, color = ShimmeringGold, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Quantity Pre-booked: $prasadQuantityInput packets", fontSize = 13.sp, color = Color.White)
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = "BHOG TRACKING CODE", fontSize = 10.sp, color = SacredIvory.copy(alpha = 0.5f))
                            Text(text = lastTrackingNumber, fontSize = 20.sp, fontWeight = FontWeight.Black, color = TempleSaffron, letterSpacing = 2.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Custom drawn 2D Vector QR code block on canvas representing authenticity details
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Draw typical QR module corner guides
                                val qSize = size.width
                                val margin = 4f
                                val squareSize = qSize * 0.28f

                                // Top-Left Guide square
                                drawRect(
                                    color = Color.Black,
                                    topLeft = Offset(margin, margin),
                                    size = androidx.compose.ui.geometry.Size(squareSize, squareSize)
                                )
                                drawRect(
                                    color = Color.White,
                                    topLeft = Offset(margin + qSize * 0.05f, margin + qSize * 0.05f),
                                    size = androidx.compose.ui.geometry.Size(squareSize - qSize * 0.1f, squareSize - qSize * 0.1f)
                                )

                                // Top-Right Guide square
                                drawRect(
                                    color = Color.Black,
                                    topLeft = Offset(qSize - squareSize - margin, margin),
                                    size = androidx.compose.ui.geometry.Size(squareSize, squareSize)
                                )
                                drawRect(
                                    color = Color.White,
                                    topLeft = Offset(qSize - squareSize - margin + qSize * 0.05f, margin + qSize * 0.05f),
                                    size = androidx.compose.ui.geometry.Size(squareSize - qSize * 0.1f, squareSize - qSize * 0.1f)
                                )

                                // Bottom-Left Guide square
                                drawRect(
                                    color = Color.Black,
                                    topLeft = Offset(margin, qSize - squareSize - margin),
                                    size = androidx.compose.ui.geometry.Size(squareSize, squareSize)
                                )
                                drawRect(
                                    color = Color.White,
                                    topLeft = Offset(margin + qSize * 0.05f, qSize - squareSize - margin + qSize * 0.05f),
                                    size = androidx.compose.ui.geometry.Size(squareSize - qSize * 0.1f, squareSize - qSize * 0.1f)
                                )

                                // Draw some randomized grid noise inside simulating barcodes content
                                val randSeed = if (successType == "PUJA") lastTicketNumber.hashCode() else lastTrackingNumber.hashCode()
                                val r = java.util.Random(randSeed.toLong())
                                val dotSegments = 7
                                val cellSize = (qSize - (margin * 2)) / dotSegments
                                for (row in 1 until dotSegments - 1) {
                                    for (col in 1 until dotSegments - 1) {
                                        // Avoid guides quadrant overlapping
                                        if ((row < 3 && col < 3) || (row < 3 && col >= dotSegments - 3) || (row >= dotSegments - 3 && col < 3)) {
                                            continue
                                        }
                                        if (r.nextBoolean()) {
                                            drawRect(
                                                color = Color.Black,
                                                topLeft = Offset(margin + col * cellSize, margin + row * cellSize),
                                                size = androidx.compose.ui.geometry.Size(cellSize * 0.85f, cellSize * 0.85f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Dynamic Committee Payment target indicators
                        Text(
                            text = "Admin Payment Destination:",
                            fontSize = 10.sp,
                            color = SacredIvory.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "UPI: ${settings?.upiId ?: "aysuhtiwari668@ybl"}",
                            fontSize = 11.sp,
                            color = ShimmeringGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action controls
                Button(
                    onClick = { bookingModeActive = true; devoteeNameInput = "" },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book Another Ceremony", color = ShimmeringGold, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
