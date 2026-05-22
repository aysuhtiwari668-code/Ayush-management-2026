package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.DeepCharcoal
import com.example.ui.theme.ShimmeringGold
import com.example.ui.theme.DarkSurface

enum class AppScreen {
    LOGIN,
    MAIN,
    BOOKINGS,
    VOLUNTEER,
    EMERGENCY,
    ADMIN
}

enum class MainTab {
    DASHBOARD,
    AI_CHAT,
    REELS_GALLERY
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }
                var activeMainTab by remember { mutableStateOf(MainTab.DASHBOARD) }

                // Simple state backstack navigation
                val screenBackstack = remember { mutableStateListOf<AppScreen>() }

                val navigateTo: (AppScreen) -> Unit = { target ->
                    screenBackstack.add(currentScreen)
                    currentScreen = target
                }

                val navigateBack: () -> Unit = {
                    if (screenBackstack.isNotEmpty()) {
                        currentScreen = screenBackstack.removeAt(screenBackstack.lastIndex)
                    } else {
                        currentScreen = AppScreen.LOGIN
                    }
                }

                // Handle system native back pressed
                BackHandler(enabled = currentScreen != AppScreen.LOGIN) {
                    navigateBack()
                }

                // Global Scaffold wrapper
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DeepCharcoal
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (currentScreen == AppScreen.MAIN) 56.dp else 0.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "screen_flow"
                        ) { screen ->
                            when (screen) {
                                AppScreen.LOGIN -> {
                                    LoginScreen(
                                        viewModel = viewModel,
                                        onLoginSuccess = {
                                            currentScreen = AppScreen.MAIN
                                            activeMainTab = MainTab.DASHBOARD
                                            screenBackstack.clear()
                                        }
                                    )
                                }

                                AppScreen.MAIN -> {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        when (activeMainTab) {
                                            MainTab.DASHBOARD -> {
                                                HomeScreen(
                                                    viewModel = viewModel,
                                                    onNavigateToBooking = { navigateTo(AppScreen.BOOKINGS) },
                                                    onNavigateToVolunteer = { navigateTo(AppScreen.VOLUNTEER) },
                                                    onNavigateToEmergency = { navigateTo(AppScreen.EMERGENCY) }
                                                )
                                            }

                                            MainTab.AI_CHAT -> {
                                                ChatbotScreen(viewModel = viewModel)
                                            }

                                            MainTab.REELS_GALLERY -> {
                                                GalleryScreen(viewModel = viewModel)
                                            }
                                        }

                                        // Admin floating panel trigger if authenticated as super chairman
                                        if (viewModel.isAdmin) {
                                            FloatingActionButton(
                                                onClick = { navigateTo(AppScreen.ADMIN) },
                                                containerColor = CrimsonRed,
                                                contentColor = ShimmeringGold,
                                                shape = CircleShape,
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(16.dp)
                                            ) {
                                                Text("⚙️", fontSize = 20.sp)
                                            }
                                        }

                                        // General Logout floating trigger
                                        FloatingActionButton(
                                            onClick = {
                                                viewModel.logout()
                                                currentScreen = AppScreen.LOGIN
                                                screenBackstack.clear()
                                            },
                                            containerColor = Color(0xFF2C2C2C),
                                            contentColor = Color.White,
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(16.dp)
                                                .size(44.dp)
                                        ) {
                                            Text("🚪", fontSize = 16.sp)
                                        }
                                    }
                                }

                                AppScreen.BOOKINGS -> {
                                    BookingsScreen(
                                        viewModel = viewModel,
                                        onNavigateBack = navigateBack
                                    )
                                }

                                AppScreen.VOLUNTEER -> {
                                    VolunteerScreen(
                                        viewModel = viewModel,
                                        onNavigateBack = navigateBack
                                    )
                                }

                                AppScreen.EMERGENCY -> {
                                    EmergencyScreen(
                                        viewModel = viewModel,
                                        onNavigateBack = navigateBack
                                    )
                                }

                                AppScreen.ADMIN -> {
                                    AdminScreen(
                                        viewModel = viewModel,
                                        onNavigateBack = navigateBack
                                    )
                                }
                            }
                        }
                    }

                    // Render bottom navigation bar exclusively on MAIN screen root
                    if (currentScreen == AppScreen.MAIN) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            NavigationBar(
                                containerColor = DarkSurface,
                                modifier = Modifier.height(64.dp)
                            ) {
                                NavigationBarItem(
                                    selected = activeMainTab == MainTab.DASHBOARD,
                                    onClick = { activeMainTab = MainTab.DASHBOARD },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = if (activeMainTab == MainTab.DASHBOARD) ShimmeringGold else Color.Gray) },
                                    label = { Text("Home", fontSize = 10.sp, color = if (activeMainTab == MainTab.DASHBOARD) CrimsonRed else Color.Gray) },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = CrimsonRed,
                                        selectedIconColor = ShimmeringGold,
                                        selectedTextColor = CrimsonRed,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )

                                NavigationBarItem(
                                    selected = activeMainTab == MainTab.AI_CHAT,
                                    onClick = { activeMainTab = MainTab.AI_CHAT },
                                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Celestial AI", tint = if (activeMainTab == MainTab.AI_CHAT) ShimmeringGold else Color.Gray) },
                                    label = { Text("Ma Assistant", fontSize = 10.sp, color = if (activeMainTab == MainTab.AI_CHAT) CrimsonRed else Color.Gray) },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = CrimsonRed,
                                        selectedIconColor = ShimmeringGold,
                                        selectedTextColor = CrimsonRed,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )

                                NavigationBarItem(
                                    selected = activeMainTab == MainTab.REELS_GALLERY,
                                    onClick = { activeMainTab = MainTab.REELS_GALLERY },
                                    icon = { Icon(Icons.Default.List, contentDescription = "Memories", tint = if (activeMainTab == MainTab.REELS_GALLERY) ShimmeringGold else Color.Gray) },
                                    label = { Text("Reels", fontSize = 10.sp, color = if (activeMainTab == MainTab.REELS_GALLERY) CrimsonRed else Color.Gray) },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = CrimsonRed,
                                        selectedIconColor = ShimmeringGold,
                                        selectedTextColor = CrimsonRed,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
