package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.GalleryMemory
import com.example.ui.MainViewModel
import com.example.ui.theme.*

// Mock online image URLs for default high quality traditional photos
val PRESET_MEMORIES_PHOTOS = listOf(
    "https://images.unsplash.com/photo-1545641203-7d6cf291b3f5?auto=format&fit=crop&w=800&q=80" to "Maha Shasthi Pandal Lighting",
    "https://images.unsplash.com/photo-1561054708-3abbeb355701?auto=format&fit=crop&w=800&q=80" to "Maha Ashtami Anjali Gathering",
    "https://images.unsplash.com/photo-1562157873-818bc0726f68?auto=format&fit=crop&w=800&q=80" to "Traditional Dhunuchi Dance Competition",
    "https://images.unsplash.com/photo-1605371924599-2c03b1bcc53d?auto=format&fit=crop&w=800&q=80" to "Maha Dashami Sindoor Smearing"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: MainViewModel
) {
    val memories by viewModel.allMemories.collectAsState(initial = emptyList())
    var captionInput by remember { mutableStateOf("") }
    var selectedPresetPhotoCode by remember { mutableStateOf(PRESET_MEMORIES_PHOTOS[0].first) }
    var showUploadMemoryDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Leave screen padding of M3 Navigation elements
        ) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .padding(16.dp)
                    .windowInsetsPadding(WindowInsets.statusBars),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎬 Pujo Reels & Memories",
                    fontWeight = FontWeight.Black,
                    color = ShimmeringGold,
                    fontSize = 18.sp
                )

                IconButton(
                    onClick = {
                        captionInput = ""
                        showUploadMemoryDialog = true
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = ShimmeringGold, containerColor = CrimsonRed)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Post")
                }
            }

            if (memories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎬", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No Memories Uploaded Yet",
                            fontWeight = FontWeight.Bold,
                            color = SacredIvory.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                // Vertical reels list feed
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(memories) { memory ->
                        GalleryMemoryCard(memory = memory, onLike = { viewModel.likeMemory(memory.id) })
                    }
                }
            }
        }

        if (showUploadMemoryDialog) {
            AlertDialog(
                onDismissRequest = { showUploadMemoryDialog = false },
                title = {
                    Text(
                        text = "📤 Share Pujo Memory",
                        color = ShimmeringGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Describe your spiritual feeling or pandal photo:",
                            fontSize = 12.sp,
                            color = SacredIvory.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = captionInput,
                            onValueChange = { captionInput = it },
                            placeholder = { Text("Enter a traditional caption...", color = SacredIvory.copy(alpha = 0.3f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = SacredIvory,
                                unfocusedTextColor = SacredIvory,
                                focusedBorderColor = ShimmeringGold,
                                unfocusedBorderColor = SacredIvory.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Select Traditional Post Visual Mockup",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShimmeringGold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(PRESET_MEMORIES_PHOTOS) { (url, label) ->
                                val isSelected = selectedPresetPhotoCode == url
                                Card(
                                    modifier = Modifier
                                        .size(74.dp)
                                        .clickable { selectedPresetPhotoCode = url }
                                        .border(2.dp, if (isSelected) ShimmeringGold else Color.Transparent, RoundedCornerShape(8.dp)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        AsyncImage(
                                            model = url,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.5f))
                                        )
                                        Text(
                                            text = label.take(12) + "..",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .padding(2.dp),
                                            textAlign = TextAlign.Center
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
                            if (captionInput.isNotBlank()) {
                                viewModel.uploadMemory(captionInput, selectedPresetPhotoCode)
                                showUploadMemoryDialog = false
                            }
                        }
                    ) {
                        Text("Post Memory", color = ShimmeringGold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUploadMemoryDialog = false }) {
                        Text("Cancel", color = SacredIvory.copy(alpha = 0.6f))
                    }
                },
                containerColor = DarkSurface
            )
        }
    }
}

@Composable
fun GalleryMemoryCard(
    memory: GalleryMemory,
    onLike: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, TempleSaffron.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // User row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF331F21), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👑", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = memory.uploaderName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Shared during Durga Puja Festive 2026",
                        fontSize = 9.sp,
                        color = SacredIvory.copy(alpha = 0.5f)
                    )
                }
            }

            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                AsyncImage(
                    model = memory.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Bottom translucent details overlay caption
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = memory.caption,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Interaction buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLike() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = CrimsonRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${memory.likes} hearts",
                        fontSize = 12.sp,
                        color = ShimmeringGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = SacredIvory.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
