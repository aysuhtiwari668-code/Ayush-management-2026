package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.User
import com.example.ui.MainViewModel
import com.example.ui.theme.*

// Pre-defined visual Bengali Avatar listings
val PRESET_AVATARS = listOf(
    "avatar_traditional_male" to "Traditional Dhoti Sabyasachi",
    "avatar_traditional_female" to "Red Saree Sanhita",
    "avatar_flower_diya" to "Sacred Diya Pooja",
    "avatar_holy_trishul" to "Maa Durga Trishul",
    "avatar_lotus" to "Celestial Golden Lotus"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    
    // Inputs Model
    var mobileInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var addressInput by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("avatar_holy_trishul") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepCharcoal, Color(0xFFFAF2E6)) // Warm organic cream tones
                )
            )
            .padding(24.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo / Icon header
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(Color(0xFFE5A823), shape = CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CrimsonRed, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌺", // Sacred flower
                    fontSize = 44.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DURGA PUJA 2026",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = ShimmeringGold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Festival Management Sanctum",
            style = MaterialTheme.typography.bodyMedium,
            color = SacredIvory.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Card displaying input details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkSurface.copy(alpha = 0.95f)
            ),
            border = BorderStroke(1.dp, ShimmeringGold.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isRegisterMode) "Devotee Registration" else "Devotee & Admin Login",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SacredIvory
                )

                Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let { msg ->
                    Text(
                        text = msg,
                        color = Color.Red,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                successMessage?.let { msg ->
                    Text(
                        text = msg,
                        color = ShimmeringGold,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (isRegisterMode) {
                    // Registration inputs
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
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ShimmeringGold) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = mobileInput,
                    onValueChange = { mobileInput = it },
                    label = { Text(if (isRegisterMode) "Mobile Number" else "Mobile Number or Admin ID", color = SacredIvory.copy(alpha = 0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SacredIvory,
                        unfocusedTextColor = SacredIvory,
                        focusedBorderColor = ShimmeringGold,
                        unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                    ),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ShimmeringGold) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password PIN", color = SacredIvory.copy(alpha = 0.6f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SacredIvory,
                        unfocusedTextColor = SacredIvory,
                        focusedBorderColor = ShimmeringGold,
                        unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                    ),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ShimmeringGold) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (isRegisterMode) {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("Home Address", color = SacredIvory.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = SacredIvory,
                            unfocusedTextColor = SacredIvory,
                            focusedBorderColor = ShimmeringGold,
                            unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Pujo Avatar Profile",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ShimmeringGold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row of preset avatars
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PRESET_AVATARS.forEach { (avatarCode, name) ->
                            val isSelected = selectedAvatar == avatarCode
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ShimmeringGold else Color(0xFF332225))
                                    .clickable { selectedAvatar = avatarCode }
                                    .padding(2.dp),
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
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        errorMessage = null
                        if (mobileInput.isBlank() || passwordInput.isBlank()) {
                            errorMessage = "Mobile / ID and Password cannot be empty!"
                            return@Button
                        }

                        if (isRegisterMode) {
                            if (nameInput.isBlank() || addressInput.isBlank()) {
                                errorMessage = "Name and Address details are required!"
                                return@Button
                            }

                            val newUser = User(
                                mobile = mobileInput,
                                name = nameInput,
                                password = passwordInput,
                                address = addressInput,
                                profilePhotoUri = selectedAvatar
                            )

                            viewModel.registerUser(newUser, onSuccess = {
                                successMessage = "Registered Successfully! Welcome."
                                isRegisterMode = false
                                onLoginSuccess()
                            }, onError = {
                                errorMessage = it
                            })
                        } else {
                            viewModel.loginUser(mobileInput, passwordInput, onSuccess = {
                                onLoginSuccess()
                            }, onError = {
                                errorMessage = it
                            })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonRed,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, ShimmeringGold)
                ) {
                    Text(
                        text = if (isRegisterMode) "Create Divine Profile" else "Access Holy Portal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ShimmeringGold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isRegisterMode) "Already have an account? Login" else "New devotee? Create Register Account",
                    color = ShimmeringGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable {
                            isRegisterMode = !isRegisterMode
                            errorMessage = null
                            successMessage = null
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}
