package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val settings by viewModel.appSettings.collectAsState(initial = null)

    val emergencyContacts = listOf(
        EmergencyContactItem("🏥 HOLY AMBULANCE WING", settings?.emergencyAmbulance ?: "102", "Instant emergency dispatch and medical trauma tents near pandals"),
        EmergencyContactItem("👮 LOCAL POLICE ASSIST", settings?.emergencyPolice ?: "100", "Pandal law patrol squads and instant crowd management assistance"),
        EmergencyContactItem("🚒 FIRE DEFENSE DEPT", settings?.emergencyFire ?: "101", "Fire hazard prevention and inspection squads"),
        EmergencyContactItem("🛡️ WOMEN HELPLINE CORE", settings?.emergencyWomen ?: "1091", "24/7 designated women protection services"),
        EmergencyContactItem("🌺 PUJA COMMITTEE DESK", settings?.emergencyCommittee ?: "9335937461", "Direct communication line with the executive office core")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepCharcoal, Color(0xFFFAF2E6))
                )
            )
            .padding(18.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back toolbar
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

        // SOS Header icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(CrimsonRed, shape = CircleShape)
                .border(2.dp, CrimsonRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🆘", fontSize = 34.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "SOS EMERGENCY SYSTEM",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = CrimsonRed,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Access live dispatch help desks instantly. Tapping dial links will safe-launch the device phone dialer pad.",
            fontSize = 11.sp,
            color = SacredIvory.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Column listing emergency contacts
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            emergencyContacts.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { launchDialer(context, item.number) },
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { launchDialer(context, item.number) },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White, containerColor = CrimsonRed)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call")
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Black,
                                color = ShimmeringGold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = item.number,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = item.subtitle,
                                fontSize = 9.sp,
                                color = SacredIvory.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun launchDialer(context: Context, number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

data class EmergencyContactItem(
    val title: String,
    val number: String,
    val subtitle: String
)
