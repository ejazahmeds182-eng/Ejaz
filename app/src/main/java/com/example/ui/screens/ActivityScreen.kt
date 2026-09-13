package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyCheckInDay
import com.example.model.DailyMission
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GreenPositive
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ActivityScreen(
    checkInDays: List<DailyCheckInDay>,
    missions: List<DailyMission>,
    onClaimCheckIn: (day: Int) -> Unit,
    onClaimMission: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
    ) {
        // Daily Check-In Calendar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Daily Check-In Rewards", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Continuous login gives up to Rs 10,000", color = TextSecondary, fontSize = 11.sp)
                        }
                        Text("📅", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(checkInDays) { checkIn ->
                            CheckInDayCard(checkIn = checkIn, onClaim = { onClaimCheckIn(checkIn.day) })
                        }
                    }
                }
            }
        }

        // Daily Challenge Quests Header
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Daily Mission Quests", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Refresh in 08:32:10", color = TextGold, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Mission Items
        items(missions) { mission ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(0.5.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(mission.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(mission.description, color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { mission.progressFraction },
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = GoldPrimary,
                                trackColor = DarkBackground
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${mission.currentProgress}/${mission.targetProgress}",
                                color = TextGold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "+Rs ${mission.reward.toInt()}",
                            color = GreenPositive,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        if (mission.isClaimed) {
                            Text("Claimed", color = Color.Gray, fontSize = 11.sp)
                        } else {
                            Button(
                                onClick = { onClaimMission(mission.id) },
                                enabled = mission.isCompleted,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldPrimary,
                                    disabledContainerColor = Color(0xFF38291A)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp).testTag("claim_mission_${mission.id}")
                            ) {
                                Text(
                                    text = if (mission.isCompleted) "CLAIM" else "IN PROGRESS",
                                    color = if (mission.isCompleted) Color.Black else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckInDayCard(
    checkIn: DailyCheckInDay,
    onClaim: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (checkIn.isCurrent) Brush.verticalGradient(listOf(Color(0xFF4A3416), DarkCard))
                else Brush.verticalGradient(listOf(DarkCard, DarkSurface))
            )
            .border(
                width = if (checkIn.isCurrent) 1.5.dp else 0.5.dp,
                color = if (checkIn.isCurrent) GoldPrimary else DarkCardBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = checkIn.isCurrent && !checkIn.isClaimed, onClick = onClaim)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Day ${checkIn.day}", color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("🪙", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Rs ${checkIn.reward.toInt()}", color = TextGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            if (checkIn.isClaimed) {
                Text("✓ Done", color = GreenPositive, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            } else if (checkIn.isCurrent) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(GoldPrimary)
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text("CLAIM", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Text("Lock", color = Color.Gray, fontSize = 9.sp)
            }
        }
    }
}
