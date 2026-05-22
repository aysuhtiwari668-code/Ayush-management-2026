package com.example.ui.components

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sin

object PujaSoundSynthesizer {

    suspend fun playBellSound(context: Context?) = withContext(Dispatchers.Default) {
        triggerHaptic(context, 100)
        val sampleRate = 44100
        val duration = 0.6 // seconds
        val numSamples = (duration * sampleRate).toInt()
        val sample = DoubleArray(numSamples)
        val generatedSnd = ShortArray(numSamples)

        // High pitch tinkling chime bell: 1800Hz + 3600Hz, linear decay
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val fade = 1.0 - t / duration
            sample[i] = (sin(2 * Math.PI * 1800 * t) + 0.4 * sin(2 * Math.PI * 3600 * t)) * fade
            generatedSnd[i] = (sample[i] * 30000).toInt().toShort()
        }

        playAudio(generatedSnd, sampleRate)
    }

    suspend fun playShankhSound(context: Context?) = withContext(Dispatchers.Default) {
        triggerHaptic(context, 500)
        val sampleRate = 44100
        val duration = 2.0 // seconds
        val numSamples = (duration * sampleRate).toInt()
        val sample = DoubleArray(numSamples)
        val generatedSnd = ShortArray(numSamples)

        // Resonant sweeping conch horn: starts at 170Hz, sweeps to 210Hz with rich harmonics
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val currentFreq = 165.0 + (40.0 * (t / duration))
            val env = if (t < 0.4) {
                t / 0.4 // fade in
            } else if (t > duration - 0.6) {
                (duration - t) / 0.6 // fade out
            } else {
                1.0
            }
            // Fundamental + 2nd harmonic + 3rd harmonic for that authentic rough trumpeting texture
            sample[i] = (sin(2 * Math.PI * currentFreq * t) + 
                         0.5 * sin(2 * Math.PI * (2 * currentFreq) * t) +
                         0.25 * sin(2 * Math.PI * (3 * currentFreq) * t)) * env
            generatedSnd[i] = (sample[i] * 20000).toInt().toShort()
        }

        playAudio(generatedSnd, sampleRate)
    }

    suspend fun playDhakBeat(context: Context?) = withContext(Dispatchers.Default) {
        val sampleRate = 44100
        val duration = 1.5 // seconds
        val numSamples = (duration * sampleRate).toInt()
        val sample = DoubleArray(numSamples)
        val generatedSnd = ShortArray(numSamples)

        // Rhythmic traditional drumming loop: "Dha... Kur-Kur... Dha! Dha-Kur-Kur-Dha!"
        val beats = listOf(
            Beat(0.0, 70.0, 0.25, 1.2),     // Dha
            Beat(0.25, 110.0, 0.08, 0.4),   // Kur
            Beat(0.33, 110.0, 0.08, 0.4),   // Kur
            Beat(0.42, 70.0, 0.20, 1.0),    // Dha
            Beat(0.70, 70.0, 0.22, 1.1),    // Dha
            Beat(0.92, 110.0, 0.08, 0.4),   // Kur
            Beat(1.00, 110.0, 0.08, 0.4),   // Kur
            Beat(1.10, 70.0, 0.30, 1.3)     // Dha
        )

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            var sum = 0.0
            for (beat in beats) {
                if (t >= beat.start && t < beat.start + beat.duration) {
                    val dt = t - beat.start
                    val decay = 1.0 - (dt / beat.duration)
                    // Bass thud frequency
                    sum += sin(2 * Math.PI * beat.freq * dt) * decay * beat.volume
                }
            }
            sample[i] = sum.coerceIn(-1.0, 1.0)
            generatedSnd[i] = (sample[i] * 26000).toInt().toShort()
        }

        // Trigger a staggered vibration sequence matching the drums
        triggerCustomVibration(context)
        playAudio(generatedSnd, sampleRate)
    }

    private data class Beat(val start: Double, val freq: Double, val duration: Double, val volume: Double)

    private fun playAudio(shortArray: ShortArray, sampleRate: Int) {
        try {
            val minBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = maxOf(minBufSize, shortArray.size * 2)
            val audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STATIC
            )
            audioTrack.write(shortArray, 0, shortArray.size)
            audioTrack.play()

            Thread {
                try {
                    Thread.sleep((shortArray.size.toDouble() / sampleRate * 1000).toLong() + 200)
                    audioTrack.stop()
                    audioTrack.release()
                } catch (e: Exception) {}
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun triggerHaptic(context: Context?, duration: Long) {
        try {
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(duration)
            }
        } catch (e: Exception) {}
    }

    private fun triggerCustomVibration(context: Context?) {
        try {
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
            val pattern = longArrayOf(0, 100, 150, 40, 40, 40, 50, 80, 120, 40, 40, 40, 150)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                vibrator.vibrate(pattern, -1)
            }
        } catch (e: Exception) {}
    }
}
