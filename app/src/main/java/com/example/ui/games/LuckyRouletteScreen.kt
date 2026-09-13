package com.example.ui.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class RoulettePrize(
    val label: String,
    val amount: Double,
    val color: Color
)

@Composable
fun LuckyRouletteScreen(
    userBalance: Double,
    onBack: () -> Unit,
    onPrizeWon: (prizeAmount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var lastWonPrize by remember { mutableStateOf<RoulettePrize?>(null) }
    val rotationAnim = remember { Animatable(0f) }

    val prizes = remember {
        listOf(
            RoulettePrize("Rs 1,500", 1500.0, Color(0xFFE5A93C)),
            RoulettePrize("Rs 50", 50.0, Color(0xFF2C3E50)),
            RoulettePrize("Rs 500", 500.0, Color(0xFFE74C3C)),
            RoulettePrize("Rs 100", 100.0, Color(0xFF27AE60)),
            RoulettePrize("Rs 3,000", 3000.0, Color(0xFF8E44AD)),
            RoulettePrize("Rs 20", 20.0, Color(0xFF34495E)),
            RoulettePrize("Rs 2,000", 2000.0, Color(0xFFD35400)),
            RoulettePrize("JACKPOT\n40K", 40000.0, Color(0xFFFFD700))
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GoldPrimary
                )
            }

            Text(
                text = "Lucky Roulette",
                color = GoldPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

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

        Text(
            text = "Spin & Win Daily Prizes Up To Rs 40,000!",
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Roulette Canvas Wheel
        Box(
            modifier = Modifier
                .size(310.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Spinning Wheel Disc
            Canvas(
                modifier = Modifier
                    .size(290.dp)
                    .rotate(rotationAnim.value)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2 - 4.dp.toPx()
                val segmentAngle = 360f / prizes.size

                prizes.forEachIndexed { index, prize ->
                    val startAngle = index * segmentAngle - 90f - (segmentAngle / 2f)
                    drawArc(
                        color = prize.color,
                        startAngle = startAngle,
                        sweepAngle = segmentAngle,
                        useCenter = true,
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                        topLeft = Offset(center.x - radius, center.y - radius)
                    )

                    // Draw separator lines
                    val lineAngleRad = Math.toRadians((startAngle).toDouble())
                    val lineEnd = Offset(
                        center.x + (radius * cos(lineAngleRad)).toFloat(),
                        center.y + (radius * sin(lineAngleRad)).toFloat()
                    )
                    drawLine(
                        color = Color(0xFFFFE082),
                        start = center,
                        end = lineEnd,
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Outer Gold Ring
                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = radius,
                    style = Stroke(width = 6.dp.toPx())
                )
            }

            // Top Pointer Needle
            Canvas(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopCenter)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, size.height)
                    lineTo(size.width * 0.15f, 0f)
                    lineTo(size.width * 0.85f, 0f)
                    close()
                }
                drawPath(path = path, color = Color(0xFFFF2A2A))
                drawPath(path = path, color = Color.White, style = Stroke(width = 1.5.dp.toPx()))
            }

            // Center Spin Button
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFFFDF7A), GoldPrimary, Color(0xFFB87914))
                        )
                    )
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isSpinning) "..." else "SPIN",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Win Banner
        if (lastWonPrize != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldPrimary.copy(alpha = 0.2f))
                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "🎉 Congratulations! You won ${lastWonPrize?.label}!",
                    color = TextGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Spin Action Button
        Button(
            onClick = {
                if (isSpinning) return@Button
                isSpinning = true
                lastWonPrize = null

                coroutineScope.launch {
                    val prizeIndex = Random.nextInt(prizes.size)
                    val segmentAngle = 360f / prizes.size
                    // Calculate target rotation with multiple revolutions
                    val extraRounds = 5 + Random.nextInt(3)
                    val targetAngle = (extraRounds * 360f) + (360f - (prizeIndex * segmentAngle))

                    rotationAnim.snapTo(rotationAnim.value % 360f)
                    rotationAnim.animateTo(
                        targetValue = targetAngle,
                        animationSpec = tween(
                            durationMillis = 3500,
                            easing = CubicBezierEasing(0.1f, 0.9f, 0.2f, 1f)
                        )
                    )

                    val won = prizes[prizeIndex]
                    lastWonPrize = won
                    isSpinning = false
                    onPrizeWon(won.amount)
                }
            },
            enabled = !isSpinning,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(52.dp)
                .testTag("spin_roulette_button")
        ) {
            Text(
                text = if (isSpinning) "Spinning..." else "LUCKY SPIN (FREE)",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}
