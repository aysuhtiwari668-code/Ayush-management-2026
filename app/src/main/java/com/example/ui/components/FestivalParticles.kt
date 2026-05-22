package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin

// Spark / Flower Particle model
data class Particle(
    var x: Float,
    var y: Float,
    val vx: Float,
    var vy: Float,
    val size: Float,
    val color: Color,
    var alpha: Float = 1.0f,
    var rotation: Float = 0f,
    val rotationSpeed: Float = 0f,
    val maxLife: Float = 1.0f,
    var life: Float = 1.0f
)

@Composable
fun FestivalParticlesOverlay(
    modifier: Modifier = Modifier,
    particleType: String = "flowers" // "flowers", "fireworks", "diya_glow", "smoke"
) {
    val random = remember { Random() }
    val particles = remember { mutableStateListOf<Particle>() }
    
    // Ticker to tick particle life and physics
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val ticker by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tick"
    )

    // Trigger update logic on ticker changes
    LaunchedEffect(ticker) {
        // Update existing particles
        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()
            p.x += p.vx
            p.y += p.vy
            p.life -= 0.012f
            p.alpha = (p.life / p.maxLife).coerceIn(0f, 1f)
            p.rotation += p.rotationSpeed

            if (particleType == "flowers") {
                p.vy += 0.02f // gravity
            }

            if (p.life <= 0f) {
                iterator.remove()
            }
        }

        // Spawn new ones up to limit
        if (particles.size < 60) {
            when (particleType) {
                "flowers" -> {
                    // Spawn at the top
                    if (random.nextInt(100) < 15) {
                        particles.add(
                            Particle(
                                x = random.nextFloat() * 1080f,
                                y = -50f,
                                vx = (random.nextFloat() - 0.5f) * 2f,
                                vy = 1.5f + random.nextFloat() * 3f,
                                size = 12f + random.nextFloat() * 18f,
                                color = if (random.nextBoolean()) Color(0xFFFF9933) else Color(0xFFFFCC00), // Marigold colors
                                rotationSpeed = (random.nextFloat() - 0.5f) * 5f,
                                maxLife = 4.0f,
                                life = 4.0f
                            )
                        )
                    }
                }
                "fireworks" -> {
                    // Periodically launch from bottom and explode near top center
                    if (random.nextInt(100) < 5) {
                        val centerX = 200f + random.nextFloat() * 800f
                        val centerY = 150f + random.nextFloat() * 400f
                        val fireworkColor = when (random.nextInt(4)) {
                            0 -> Color(0xFFFFD700) // Gold
                            1 -> Color(0xFFFF3333) // Sindoor Red
                            2 -> Color(0xFFFF8C00) // Saffron
                            else -> Color(0xFF64FFDA) // Glowing turquoise
                        }
                        // Create 25 burst particles
                        for (i in 0 until 24) {
                            val angle = (i * (360f / 24f)) * (Math.PI / 180f)
                            val speed = 2f + random.nextFloat() * 5f
                            particles.add(
                                Particle(
                                    x = centerX,
                                    y = centerY,
                                    vx = (cos(angle) * speed).toFloat(),
                                    vy = (sin(angle) * speed).toFloat(),
                                    size = 5f + random.nextFloat() * 6f,
                                    color = fireworkColor,
                                    maxLife = 1.5f,
                                    life = 1.5f
                                )
                            )
                        }
                    }
                }
                "diya_glow" -> {
                    // Particles representing heat waves ascending slowly
                    if (random.nextInt(100) < 12) {
                        particles.add(
                            Particle(
                                x = random.nextFloat() * 1080f,
                                y = 800f + random.nextFloat() * 200f,
                                vx = (random.nextFloat() - 0.5f) * 0.8f,
                                vy = -(1.0f + random.nextFloat() * 1.5f),
                                size = 8f + random.nextFloat() * 12f,
                                color = Color(0xFFFFC107).copy(alpha = 0.6f),
                                maxLife = 2f,
                                life = 2f
                            )
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                if (particleType == "fireworks") {
                    detectTapGestures { offset ->
                        // Burst custom fireworks on user tap! Extremely cool!
                        val fireworkColor = when (random.nextInt(3)) {
                            0 -> Color(0xFFFFD700)
                            1 -> Color(0xFFFF3F3F)
                            else -> Color(0xFFFF9F00)
                        }
                        for (i in 0 until 30) {
                            val angle = (i * (360f / 30f)) * (Math.PI / 180f)
                            val speed = 3f + random.nextFloat() * 6f
                            particles.add(
                                Particle(
                                    x = offset.x,
                                    y = offset.y,
                                    vx = (cos(angle) * speed).toFloat(),
                                    vy = (sin(angle) * speed).toFloat(),
                                    size = 6f + random.nextFloat() * 8f,
                                    color = fireworkColor,
                                    maxLife = 1.8f,
                                    life = 1.8f
                                )
                            )
                        }
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->
                val adjustedX = (p.x / 1080f) * size.width
                val adjustedY = if (p.y < 0) p.y else (p.y / 1000f) * size.height
                
                when (particleType) {
                    "flowers" -> {
                        drawFlower(
                            center = Offset(adjustedX, adjustedY),
                            radius = p.size,
                            color = p.color.copy(alpha = p.alpha),
                            rotation = p.rotation
                        )
                    }
                    else -> {
                        drawCircle(
                            color = p.color.copy(alpha = p.alpha),
                            radius = p.size,
                            center = Offset(adjustedX, adjustedY)
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawFlower(
    center: Offset,
    radius: Float,
    color: Color,
    rotation: Float
) {
    // Draw 5 flower petals and a gold center
    val petalCount = 5
    val pRadius = radius * 0.61f
    
    // Rotate canvas context temporarily or compute rotated petals manually
    for (i in 0 until petalCount) {
        val angle = (rotation + (i * (360f / petalCount))) * (Math.PI / 180f)
        val pX = center.x + (cos(angle) * (radius * 0.7f)).toFloat()
        val pY = center.y + (sin(angle) * (radius * 0.7f)).toFloat()
        
        drawCircle(
            color = color,
            radius = pRadius,
            center = Offset(pX, pY)
        )
    }
    // Draw golden center
    drawCircle(
        color = Color(0xFFE5A823).copy(alpha = color.alpha),
        radius = radius * 0.45f,
        center = center
    )
}
