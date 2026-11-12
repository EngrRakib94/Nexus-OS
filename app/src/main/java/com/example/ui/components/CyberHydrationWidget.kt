package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun CyberHydrationWidget(
    currentMl: Int,
    targetMl: Int,
    onDrinkWater: (Int) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fraction = (currentMl.toFloat() / targetMl.coerceAtLeast(1)).coerceIn(0f, 1.5f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "water_fill_anim"
    )

    // Animated water wave ripple
    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    val deficiencyMl = (targetMl - currentMl).coerceAtLeast(0)
    val isGoalAchieved = currentMl >= targetMl

    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = if (isGoalAchieved) NeonGreen else NeonCyan,
        backgroundColor = CyberSurface
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonCyan.copy(alpha = 0.15f))
                        .border(1.dp, NeonCyan, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Water",
                        tint = NeonCyan,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "HYDRATION TELEMETRY & WATER TRACKER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            letterSpacing = 0.8.sp
                        )
                    )
                    Text(
                        text = "DAILY QUOTA: ${targetMl} ML (~10 GLASSES) // BIOMETRIC HEALTH",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                        color = TextMuted
                    )
                }
            }

            CyberBadge(
                text = if (isGoalAchieved) "GOAL FULFILLED" else "DEFICIENCY: ${deficiencyMl}ML",
                color = if (isGoalAchieved) NeonGreen else NeonAmber
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Center Animated Fluid Chamber & Deficiency Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberBackgroundSecondary)
                .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Futuristic Animated Cyber Water Capsule
            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF08121E))
                    .border(1.5.dp, if (isGoalAchieved) NeonGreen else NeonCyan, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Wave Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val waterLevelY = height - (height * animatedFraction.coerceIn(0f, 1f))

                    clipRect(0f, 0f, width, height) {
                        val path = Path()
                        path.moveTo(0f, height)
                        path.lineTo(0f, waterLevelY)

                        val waveAmplitude = 4.dp.toPx()
                        val waveLength = width

                        var x = 0f
                        while (x <= width) {
                            val y = waterLevelY + waveAmplitude * sin((x / waveLength) * 2 * Math.PI.toFloat() + waveOffset)
                            path.lineTo(x, y)
                            x += 4f
                        }

                        path.lineTo(width, height)
                        path.close()

                        // Fluid Gradient
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = if (isGoalAchieved) {
                                    listOf(NeonGreen.copy(alpha = 0.8f), Color(0xFF047857))
                                } else {
                                    listOf(NeonCyan.copy(alpha = 0.85f), Color(0xFF0284C7))
                                }
                            )
                        )
                    }

                    // Measurement Markings on the glass capsule
                    val marks = 4
                    for (i in 1..marks) {
                        val markY = height * (i.toFloat() / (marks + 1))
                        drawLine(
                            color = Color(0x66FFFFFF),
                            start = Offset(4.dp.toPx(), markY),
                            end = Offset(12.dp.toPx(), markY),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }

                // Center percentage text overlay
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(animatedFraction * 100).toInt()}%",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${currentMl}ml",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Deficiency & Health Advice Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isGoalAchieved) "HYDRATION EQUILIBRIUM ACHIEVED" else "BIOLOGICAL DEFICIENCY DETECTED",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (isGoalAchieved) NeonGreen else NeonAmber
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isGoalAchieved) {
                        "আপনার শরীর প্রয়োজনীয় পানির চাহিদা পুরোপুরি পূরণ করেছে। মেটাবলিজম ও এনার্জি লেভেল সর্বোচ্চ বজায় থাকবে।"
                    } else {
                        "শরীরের পানির ঘাটতি রয়েছে $deficiencyMl মিলিলিটার। সুস্থতা ও ফোকাস বজায় রাখতে এখনই পর্যাপ্ত পানি পান করুন।"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = TextSecondary,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { animatedFraction.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isGoalAchieved) NeonGreen else NeonCyan,
                    trackColor = CyberBorder
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0 ml", fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
                    Text("${targetMl / 2} ml", fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
                    Text("${targetMl} ml", fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = NeonCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Drinking Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { onDrinkWater(250) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan.copy(alpha = 0.15f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(vertical = 7.dp)
            ) {
                Icon(Icons.Default.LocalDrink, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("+250 ML (১ গ্লাস)", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onDrinkWater(500) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen.copy(alpha = 0.15f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(vertical = 7.dp)
            ) {
                Icon(Icons.Default.Water, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("+500 ML (১ বোতল)", fontFamily = FontFamily.Monospace, fontSize = 9.sp, color = NeonGreen, fontWeight = FontWeight.Bold)
            }

            IconButton(
                onClick = onReset,
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(CyberSurfaceElevated)
                    .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset Hydration", tint = TextMuted, modifier = Modifier.size(16.dp))
            }
        }
    }
}
