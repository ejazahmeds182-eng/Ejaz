package com.example.ui.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import kotlinx.coroutines.launch
import kotlin.random.Random

data class GemSymbol(
    val name: String,
    val icon: String,
    val multiplier: Double,
    val color: Color
)

@Composable
fun FortuneGemsScreen(
    userBalance: Double,
    onBack: () -> Unit,
    onWinRecorded: (winAmount: Double, betAmount: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var betAmount by remember { mutableDoubleStateOf(50.0) }
    var isSpinning by remember { mutableStateOf(false) }
    var lastWinMessage by remember { mutableStateOf<String?>(null) }
    var lastWinAmount by remember { mutableDoubleStateOf(0.0) }

    val symbols = remember {
        listOf(
            GemSymbol("Crown Wild", "👑", 20.0, Color(0xFFFFD700)),
            GemSymbol("Ruby", "💎", 10.0, Color(0xFFE74C3C)),
            GemSymbol("Sapphire", "🔷", 6.0, Color(0xFF3498DB)),
            GemSymbol("Emerald", "🟢", 4.0, Color(0xFF2ECC71)),
            GemSymbol("Gold Coin", "🪙", 2.5, Color(0xFFF39C12)),
            GemSymbol("Amethyst", "🔮", 1.5, Color(0xFF9B59B6))
        )
    }

    val reel1 = remember { mutableStateListOf(symbols[0], symbols[1], symbols[2]) }
    val reel2 = remember { mutableStateListOf(symbols[1], symbols[0], symbols[3]) }
    val reel3 = remember { mutableStateListOf(symbols[2], symbols[4], symbols[0]) }
    // 4th Special Multiplier Reel (classic JILI Fortune Gems feature!)
    var specialMultiplier by remember { mutableStateOf("5X") }

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
                    text = "Fortune Gems 500",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
                Text(
                    text = " by JILI",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

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

        Spacer(modifier = Modifier.height(10.dp))

        // Golden Temple Slot Frame
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF332211), Color(0xFF1E140B), Color(0xFF28190B))
                    )
                )
                .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Jackpot Header
                Text(
                    text = "★ GRAND JACKPOT 500X ★",
                    color = GoldPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3 Reels + 1 Multiplier Reel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SlotReelColumn(symbols = reel1, isSpinning = isSpinning)
                    SlotReelColumn(symbols = reel2, isSpinning = isSpinning)
                    SlotReelColumn(symbols = reel3, isSpinning = isSpinning)

                    // Special Multiplier Reel
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(66.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF4A1212))
                            .border(1.5.dp, Color(0xFFFF5252), RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp)
                    ) {
                        Text("BONUS", color = Color(0xFFFF8A80), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = specialMultiplier,
                            color = Color(0xFFFFD700),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Win Notification / Status
        if (lastWinMessage != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldPrimary.copy(alpha = 0.2f))
                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = lastWinMessage!!,
                    color = TextGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Betting Options & Spin Control
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurface)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Bet:", color = TextSecondary, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(20.0, 50.0, 100.0, 500.0).forEach { amount ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (betAmount == amount) GoldPrimary else DarkCard)
                                    .clickable { if (!isSpinning) betAmount = amount }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "Rs ${amount.toInt()}",
                                    color = if (betAmount == amount) DarkBackground else TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (isSpinning || userBalance < betAmount) return@Button
                        isSpinning = true
                        lastWinMessage = null

                        coroutineScope.launch {
                            // Animate spinning reels
                            repeat(8) {
                                delay(120)
                                reel1[0] = symbols.random()
                                reel1[1] = symbols.random()
                                reel1[2] = symbols.random()

                                reel2[0] = symbols.random()
                                reel2[1] = symbols.random()
                                reel2[2] = symbols.random()

                                reel3[0] = symbols.random()
                                reel3[1] = symbols.random()
                                reel3[2] = symbols.random()
                            }

                            val multipliers = listOf("1X", "2X", "3X", "5X", "10X", "15X")
                            specialMultiplier = multipliers.random()
                            val multNum = specialMultiplier.replace("X", "").toDoubleOrNull() ?: 1.0

                            // Calculate payout on center payline
                            val center1 = reel1[1]
                            val center2 = reel2[1]
                            val center3 = reel3[1]

                            val isWin = (center1 == center2 && center2 == center3) ||
                                    (center1.name == "Crown Wild" || center2.name == "Crown Wild" || center3.name == "Crown Wild") ||
                                    (Random.nextDouble() < 0.45) // good fun payout rate for simulator

                            if (isWin) {
                                val symbolWin = if (center1 == center2 && center2 == center3) center1.multiplier else (2.0 + Random.nextDouble() * 5.0)
                                val totalWin = betAmount * symbolWin * multNum
                                lastWinAmount = totalWin
                                lastWinMessage = "🎉 MEGA WIN: Rs ${"%.2f".format(totalWin)}! ($specialMultiplier)"
                                onWinRecorded(totalWin, betAmount)
                            } else {
                                lastWinMessage = "Try again! Match 3 gems or hit Crown Wilds!"
                                onWinRecorded(0.0, betAmount)
                            }

                            isSpinning = false
                        }
                    },
                    enabled = !isSpinning && userBalance >= betAmount,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("spin_slot_button")
                ) {
                    Text(
                        text = if (isSpinning) "SPINNING REELS..." else "SPIN (Rs ${betAmount.toInt()})",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SlotReelColumn(
    symbols: List<GemSymbol>,
    isSpinning: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkCard)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp))
            .padding(vertical = 6.dp)
    ) {
        symbols.forEachIndexed { index, symbol ->
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (index == 1) GoldPrimary.copy(alpha = 0.2f) else Color.Transparent)
                    .border(
                        width = if (index == 1) 1.dp else 0.dp,
                        color = if (index == 1) GoldPrimary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol.icon,
                    fontSize = 28.sp
                )
            }
        }
    }
}
