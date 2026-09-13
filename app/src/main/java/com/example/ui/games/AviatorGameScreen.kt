package com.example.ui.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GreenPositive
import com.example.ui.theme.RedAccent
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

enum class AviatorState {
    WAITING, FLYING, CRASHED
}

@Composable
fun AviatorGameScreen(
    userBalance: Double,
    onBack: () -> Unit,
    onWinRecorded: (winAmount: Double, betAmount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var gameState by remember { mutableStateOf(AviatorState.WAITING) }
    var currentMultiplier by remember { mutableDoubleStateOf(1.00) }
    var crashPoint by remember { mutableDoubleStateOf(2.50) }
    var betAmount by remember { mutableDoubleStateOf(100.0) }
    var hasBetInRound by remember { mutableStateOf(false) }
    var hasCashedOut by remember { mutableStateOf(false) }
    var cashOutMultiplier by remember { mutableDoubleStateOf(0.0) }
    var countdownSeconds by remember { mutableFloatStateOf(4.0f) }

    val history = remember {
        mutableStateListOf(2.14, 1.35, 14.82, 1.10, 4.25, 1.88, 7.30, 2.05)
    }

    // Flight Game Loop
    LaunchedEffect(gameState) {
        when (gameState) {
            AviatorState.WAITING -> {
                hasCashedOut = false
                currentMultiplier = 1.00
                countdownSeconds = 4.0f
                while (countdownSeconds > 0) {
                    delay(100)
                    countdownSeconds -= 0.1f
                }
                // Generate crash point with realistic distribution
                val rand = Random.nextDouble()
                crashPoint = when {
                    rand < 0.35 -> 1.10 + Random.nextDouble() * 0.70 // 1.10x - 1.80x
                    rand < 0.70 -> 1.80 + Random.nextDouble() * 2.20 // 1.80x - 4.00x
                    rand < 0.90 -> 4.00 + Random.nextDouble() * 6.00 // 4.00x - 10.0x
                    else -> 10.00 + Random.nextDouble() * 35.00      // 10.0x - 45.0x
                }
                gameState = AviatorState.FLYING
            }

            AviatorState.FLYING -> {
                val stepInterval = 60L
                while (currentMultiplier < crashPoint) {
                    delay(stepInterval)
                    val speed = if (currentMultiplier < 2.0) 0.03 else if (currentMultiplier < 5.0) 0.07 else 0.15
                    currentMultiplier += speed
                }
                currentMultiplier = crashPoint
                gameState = AviatorState.CRASHED
                history.add(0, (Math.round(crashPoint * 100.0) / 100.0))
                if (history.size > 15) history.removeLast()
                // If player bet and didn't cash out
                if (hasBetInRound && !hasCashedOut) {
                    onWinRecorded(0.0, betAmount)
                }
                hasBetInRound = false
            }

            AviatorState.CRASHED -> {
                delay(3000)
                gameState = AviatorState.WAITING
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
    ) {
        // Game Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldPrimary
                    )
                }
                Text(
                    text = "Aviator",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = RedAccent
                )
                Text(
                    text = " by SPRIBE",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // In-game balance indicator
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Rs ${"%.2f".format(userBalance)}",
                    color = TextGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Multipliers History Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(history) { mult ->
                val pillColor = when {
                    mult < 2.0 -> Color(0xFF3498DB)
                    mult < 5.0 -> Color(0xFF9B59B6)
                    else -> GoldPrimary
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(pillColor.copy(alpha = 0.2f))
                        .border(1.dp, pillColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${"%.2f".format(mult)}x",
                        color = pillColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Main Aviator Flight Canvas Screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1F0E0E), Color(0xFF0F0B09), Color(0xFF140D0B))
                    )
                )
                .border(1.5.dp, DarkCardBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Coordinate Grid & Flying Curve Canvas
            val flightProgress = if (gameState == AviatorState.FLYING) {
                ((currentMultiplier - 1.0) / 4.0).coerceIn(0.0, 1.0).toFloat()
            } else if (gameState == AviatorState.CRASHED) 1.0f else 0f

            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Grid lines
                val gridColor = Color(0x1AFFFFFF)
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                drawLine(gridColor, Offset(0f, h * 0.25f), Offset(w, h * 0.25f), pathEffect = pathEffect)
                drawLine(gridColor, Offset(0f, h * 0.50f), Offset(w, h * 0.50f), pathEffect = pathEffect)
                drawLine(gridColor, Offset(0f, h * 0.75f), Offset(w, h * 0.75f), pathEffect = pathEffect)
                drawLine(gridColor, Offset(w * 0.25f, 0f), Offset(w * 0.25f, h), pathEffect = pathEffect)
                drawLine(gridColor, Offset(w * 0.50f, 0f), Offset(w * 0.50f, h), pathEffect = pathEffect)
                drawLine(gridColor, Offset(w * 0.75f, 0f), Offset(w * 0.75f, h), pathEffect = pathEffect)

                // Flight Curve
                if (gameState == AviatorState.FLYING || gameState == AviatorState.CRASHED) {
                    val startX = 40f
                    val startY = h - 40f
                    val endX = startX + (w - 120f) * flightProgress
                    val endY = startY - (h - 120f) * flightProgress

                    val path = Path().apply {
                        moveTo(startX, startY)
                        cubicTo(
                            startX + (endX - startX) * 0.5f, startY,
                            startX + (endX - startX) * 0.8f, endY + (startY - endY) * 0.2f,
                            endX, endY
                        )
                    }

                    // Fill under curve
                    val fillPath = Path().apply {
                        addPath(path)
                        lineTo(endX, startY)
                        lineTo(startX, startY)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(RedAccent.copy(alpha = 0.35f), Color.Transparent),
                            startY = endY,
                            endY = startY
                        )
                    )

                    drawPath(
                        path = path,
                        color = RedAccent,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Draw Red Plane indicator
                    drawCircle(
                        color = Color.White,
                        radius = 8.dp.toPx(),
                        center = Offset(endX, endY)
                    )
                    drawCircle(
                        color = RedAccent,
                        radius = 6.dp.toPx(),
                        center = Offset(endX, endY)
                    )
                }
            }

            // Multiplier Center Display or Waiting / Flew Away
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (gameState) {
                    AviatorState.WAITING -> {
                        Text(
                            text = "WAITING FOR NEXT ROUND",
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${"%.1f".format(countdownSeconds.coerceAtLeast(0f))}s",
                            color = GoldPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 36.sp
                        )
                    }

                    AviatorState.FLYING -> {
                        Text(
                            text = "${"%.2f".format(currentMultiplier)}x",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 52.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }

                    AviatorState.CRASHED -> {
                        Text(
                            text = "FLEW AWAY!",
                            color = RedAccent,
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${"%.2f".format(crashPoint)}x",
                            color = RedAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp
                        )
                    }
                }

                // Win Splash if cashed out
                if (hasCashedOut) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(GreenPositive.copy(alpha = 0.9f))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "WON Rs ${"%.2f".format(betAmount * cashOutMultiplier)}!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Betting & Cash-out Control Panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                // Quick preset bets row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(50.0, 100.0, 500.0, 1000.0, 5000.0).forEach { preset ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (betAmount == preset) GoldPrimary else DarkCard)
                                .clickable {
                                    if (gameState == AviatorState.WAITING || !hasBetInRound) {
                                        betAmount = preset
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${preset.toInt()}",
                                color = if (betAmount == preset) DarkBackground else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Big Action Button: BET or CASH OUT
                if (gameState == AviatorState.FLYING && hasBetInRound && !hasCashedOut) {
                    val currentWin = betAmount * currentMultiplier
                    Button(
                        onClick = {
                            hasCashedOut = true
                            cashOutMultiplier = currentMultiplier
                            onWinRecorded(currentWin, betAmount)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF9800)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("cashout_button")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "CASH OUT",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                            Text(
                                text = "Rs ${"%.2f".format(currentWin)} (${"%.2f".format(currentMultiplier)}x)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                } else if (hasBetInRound && gameState == AviatorState.WAITING) {
                    Button(
                        onClick = { hasBetInRound = false },
                        colors = ButtonDefaults.buttonColors(containerColor = RedAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "CANCEL BET (Rs ${betAmount.toInt()})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            if (userBalance >= betAmount) {
                                hasBetInRound = true
                            }
                        },
                        enabled = (gameState == AviatorState.WAITING || gameState == AviatorState.CRASHED) && userBalance >= betAmount,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPositive,
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("bet_button")
                    ) {
                        Text(
                            text = if (hasBetInRound) "BET PLACED (Rs ${betAmount.toInt()})" else "BET Rs ${betAmount.toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
