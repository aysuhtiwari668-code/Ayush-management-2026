package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Natural Tones Design Theme Palette (Warm Cream, Gold, and Royal Red)
val CrimsonRed = Color(0xFF9C1C1C)       // Deep elegant royal red (#9C1C1C)
val ShimmeringGold = Color(0xFFD4AF37)   // Traditional metallic gold (#D4AF37)
val TempleSaffron = Color(0xFFB45309)    // Warm saffron amber (#B45309)
val SacredIvory = Color(0xFF0F172A)      // Deep slate text for extreme contrast on cream (#0F172A)
val DeepCharcoal = Color(0xFFFCF8F2)     // Premium warm cream background (#FCF8F2)
val DarkSurface = Color(0xFFFFFFFF)      // Pure white card overlays
val BrightGoldAccent = Color(0xFFF3E5AB) // Delicate pale yellow/gold border (#F3E5AB)
val AmberGlow = Color(0xFFB45309)        // Accent amber indicators

// Light Tones (Applying Natural-Tones)
val LightPrimary = CrimsonRed
val LightSecondary = ShimmeringGold
val LightTertiary = TempleSaffron
val LightBackground = DeepCharcoal
val LightSurface = DarkSurface

// Dark Tones (Overridden with Natural-Tones to guarantee style consistency)
val DarkPrimary = CrimsonRed
val DarkSecondary = ShimmeringGold
val DarkTertiary = TempleSaffron
val DarkBackground = DeepCharcoal
