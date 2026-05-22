package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

val WORK_CATEGORIES = listOf(
    "Pandal Management",
    "Prasad Distribution",
    "Crowd Control Patrol",
    "VIP Guest Assistance",
    "Medical Relief Wing"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolunteerScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    // Inputs
    var nameInput by remember { mutableStateOf("") }
    var mobileInput by remember { mutableStateOf("") }
    var addressInput by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("") }
    var aadhaarInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(WORK_CATEGORIES[0]) }
    var selectedPhotoParam by remember { mutableStateOf("avatar_holy_trishul") }

    // UI state
    var isSubmitted by remember { mutableStateOf(false) }
    var generatedTrackingNumber by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepCharcoal, Color(0xFFFAF2E6)) // Warm organic cream tones
                )
            )
            .padding(18.dp)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simple elegant back line
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🔱 Sewak Seva Onboarding 🔱",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ShimmeringGold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Deem yourself worthy of the Divine Mother's service. Apply to assist our crowd and pandal operations.",
            fontSize = 11.sp,
            color = SacredIvory.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isSubmitted) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Fill Sewak Application Details",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    validationError?.let { err ->
                        Text(
                            text = err,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Name
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Full Name", color = SacredIvory.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SacredIvory,
                            unfocusedTextColor = SacredIvory,
                            focusedBorderColor = ShimmeringGold,
                            unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mobile
                    OutlinedTextField(
                        value = mobileInput,
                        onValueChange = { mobileInput = it },
                        label = { Text("Mobile Phone Number", color = SacredIvory.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SacredIvory,
                            unfocusedTextColor = SacredIvory,
                            focusedBorderColor = ShimmeringGold,
                            unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Address
                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("Address details", color = SacredIvory.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SacredIvory,
                            unfocusedTextColor = SacredIvory,
                            focusedBorderColor = ShimmeringGold,
                            unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Age
                        OutlinedTextField(
                            value = ageInput,
                            onValueChange = { ageInput = it },
                            label = { Text("Age", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SacredIvory,
                                unfocusedTextColor = SacredIvory,
                                focusedBorderColor = ShimmeringGold,
                                unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        // Aadhaar
                        OutlinedTextField(
                            value = aadhaarInput,
                            onValueChange = { aadhaarInput = it },
                            label = { Text("Aadhaar (Optional)", color = SacredIvory.copy(alpha = 0.6f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SacredIvory,
                                unfocusedTextColor = SacredIvory,
                                focusedBorderColor = ShimmeringGold,
                                unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Preferred Seva Category",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ShimmeringGold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Work categories chips selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        WORK_CATEGORIES.forEach { category ->
                            val isSelected = selectedCategory == category
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) CrimsonRed else Color(0xFF2B1F22),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .border(
                                        1.dp,
                                        if (isSelected) ShimmeringGold else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Text(
                                    text = category,
                                    fontSize = 11.sp,
                                    color = if (isSelected) ShimmeringGold else SacredIvory,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Select Sewak ID Avatar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ShimmeringGold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Row of preset avatars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PRESET_AVATARS.forEach { (avatarCode, _) ->
                            val isSelected = selectedPhotoParam == avatarCode
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ShimmeringGold else Color(0xFF332225))
                                    .clickable { selectedPhotoParam = avatarCode }
                                    .padding(1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(DarkSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (avatarCode) {
                                            "avatar_traditional_male" -> "🤵"
                                            "avatar_traditional_female" -> "👩"
                                            "avatar_flower_diya" -> "🕯️"
                                            "avatar_holy_trishul" -> "🔱"
                                            else -> "🪷"
                                        },
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Button(
                        onClick = {
                            validationError = null
                            if (nameInput.isBlank() || mobileInput.isBlank() || addressInput.isBlank() || ageInput.isBlank()) {
                                validationError = "Please fill in all required fields (Name, Phone, Address, Age)."
                                return@Button
                            }
                            val parsedAge = ageInput.toIntOrNull()
                            if (parsedAge == null || parsedAge <= 0) {
                                validationError = "Please enter a valid numeric age!"
                                return@Button
                            }

                            viewModel.applyAsVolunteer(
                                name = nameInput,
                                mobile = mobileInput,
                                address = addressInput,
                                age = parsedAge,
                                photo = selectedPhotoParam,
                                aadhaar = aadhaarInput,
                                category = selectedCategory,
                                onSubmitted = { code ->
                                    generatedTrackingNumber = code
                                    isSubmitted = true
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        border = BorderStroke(1.dp, ShimmeringGold)
                    ) {
                        Text(
                            "SUBMIT SEWA APPLICATION",
                            fontWeight = FontWeight.Bold,
                            color = ShimmeringGold
                        )
                    }
                }
            }
        } else {
            // Submitted Success Mode display card representing generated tracking ID
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                border = BorderStroke(2.dp, ShimmeringGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 Sewa Submission Successful!",
                        fontWeight = FontWeight.Bold,
                        color = Color.Green,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Your application was registered securely under the divine oversight of the Durga Puja Committee.",
                        color = SacredIvory.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "🎫 SEWAK TRACKING NUMBER:",
                        fontSize = 11.sp,
                        color = ShimmeringGold,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF331D21)),
                        border = BorderStroke(1.dp, CrimsonRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = generatedTrackingNumber,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TempleSaffron,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Important Note: Save this tracking number carefully. Tapping 'Track Your Registration' on the Home Screen can verify if you are APPROVED by the Admin Committee. If APPROVED, a Volunteer login ID/Password is given.",
                        fontSize = 11.sp,
                        color = SacredIvory.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { isSubmitted = false; nameInput = ""; mobileInput = ""; ageInput = ""; aadhaarInput = "" },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Apply Another Sewak", color = ShimmeringGold, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
