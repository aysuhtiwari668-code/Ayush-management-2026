package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ChatMessage
import com.example.ui.MainViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    viewModel: MainViewModel
) {
    val chatHistory = viewModel.chatHistory
    val isChatLoading = viewModel.isChatLoading
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    var textInput by remember { mutableStateOf("") }
    var speakingMessageText by remember { mutableStateOf<String?>(null) }

    // Avatar pulsing animation for the AI celestial messenger
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val avatarPulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Voice readout wave animation (fades heights of vertical bars dynamically)
    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 4.dp.value,
        targetValue = 28.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w1"
    )
    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 8.dp.value,
        targetValue = 36.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w2"
    )
    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 2.dp.value,
        targetValue = 20.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(330, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w3"
    )

    // Scroll chat list to bottom whenever new message is typed
    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            scrollState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Safety margin for modern bottoms bars
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .padding(16.dp)
                    .windowInsetsPadding(WindowInsets.statusBars),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pulsing Divine Avatar
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .graphicsLayer(scaleX = avatarPulseScale, scaleY = avatarPulseScale)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFFFFEA7A).copy(alpha = 0.35f), Color.Transparent)
                                ),
                                shape = CircleShape
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CrimsonRed)
                            .border(1.dp, ShimmeringGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🪷", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Ma Durga AI Assistant",
                        fontWeight = FontWeight.Bold,
                        color = ShimmeringGold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Spiritual Companion • Powered by Gemini",
                        fontSize = 9.sp,
                        color = Color.Green
                    )
                }
            }

            // Message dialogue column list
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding( horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
            ) {
                items(chatHistory) { msg ->
                    val isAssistant = msg.sender == "assistant"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isAssistant) Arrangement.Start else Arrangement.End
                    ) {
                        Card(
                            modifier = Modifier.widthIn(max = 280.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isAssistant) 2.dp else 16.dp,
                                bottomEnd = if (isAssistant) 16.dp else 2.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isAssistant) DarkSurface else CrimsonRed
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isAssistant) TempleSaffron.copy(alpha = 0.3f) else ShimmeringGold.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = msg.text,
                                    fontSize = 13.sp,
                                    color = if (isAssistant) SacredIvory else Color.White,
                                    lineHeight = 17.sp
                                )

                                if (isAssistant) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    // Visual Speaker sound wave simulator triggers on press!
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable {
                                                speakingMessageText = if (speakingMessageText == msg.text) null else msg.text
                                            }
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Read out Loud",
                                            tint = ShimmeringGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (speakingMessageText == msg.text) "Simulating read..." else "Simulate Reading Loud",
                                            fontSize = 9.sp,
                                            color = ShimmeringGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (isChatLoading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(DarkSurface, shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "⚡ Receiving heavenly answers...",
                                    fontSize = 11.sp,
                                    color = ShimmeringGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Simulated active speaking wave pane
            speakingMessageText?.let { currentText ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF171012)),
                    border = BorderStroke(1.dp, CrimsonRed)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🔊 CHATBOT VOICE RESPONSE SIMULATION",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = ShimmeringGold
                            )
                            Text(
                                text = currentText.take(65) + "...",
                                fontSize = 11.sp,
                                color = SacredIvory.copy(alpha = 0.8f),
                                maxLines = 1
                            )
                        }

                        // Simulated haptic audio bars jumping
                        Row(
                            modifier = Modifier.width(36.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(width = 4.dp, height = waveHeight1.dp).background(CrimsonRed))
                            Box(modifier = Modifier.size(width = 4.dp, height = waveHeight2.dp).background(ShimmeringGold))
                            Box(modifier = Modifier.size(width = 4.dp, height = waveHeight3.dp).background(TempleSaffron))
                        }
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Ask about Ashtami, Sandhi Puja, Bhog...", color = SacredIvory.copy(alpha = 0.3f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SacredIvory,
                        unfocusedTextColor = SacredIvory,
                        focusedBorderColor = ShimmeringGold,
                        unfocusedBorderColor = SacredIvory.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            viewModel.askChatbot(textInput)
                            textInput = ""
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = DeepCharcoal, containerColor = ShimmeringGold)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send Message")
                }
            }
        }
    }
}
