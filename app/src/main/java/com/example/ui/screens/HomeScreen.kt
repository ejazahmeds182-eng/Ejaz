package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.GameItem
import com.example.ui.components.AnnouncementTicker
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.GreenPositive
import com.example.ui.theme.RedAccent
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    games: List<GameItem>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onGameClick: (String) -> Unit,
    onRouletteClick: () -> Unit,
    onInviteClick: () -> Unit,
    onVipClick: () -> Unit,
    onCustomerServiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Hots", "Favorite", "JILI", "PG", "INOUT", "JDB")

    val filteredGames = when (selectedCategory) {
        "Hots" -> games.filter { it.isHot }
        "Favorite" -> games.filter { it.isFavorite }
        else -> games.filter { it.category == selectedCategory || it.provider == selectedCategory }
    }.ifEmpty { games }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Promo Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF4A3414), Color(0xFF26190B))
                            )
                        )
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                        .clickable { onInviteClick() }
                        .height(130.dp)
                ) {
                    // Try to show generated banner, fallback to composed layout
                    Image(
                        painter = painterResource(id = R.drawable.banner_hero),
                        contentDescription = "Invite Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay with glow text
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xCC1A1005), Color(0x33000000))
                                )
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "INVITE 1 FRIEND",
                            color = Color(0xFFFFEB3B),
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "GET 3 REWARDS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFD35400))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "TOTAL PRIZE POOL RS 3,000",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 3 Quick Action Banners
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Lucky Roulette Get 1500
                    QuickBannerCard(
                        title = "Lucky Roulette",
                        subtitle = "Get 1500",
                        accentColor = Color(0xFFE67E22),
                        icon = "🎡",
                        onClick = onRouletteClick,
                        modifier = Modifier.weight(1f),
                        tag = "quick_roulette"
                    )

                    // Invite Friends Rs 3000
                    QuickBannerCard(
                        title = "Invite Friends",
                        subtitle = "Reward Rs 3000",
                        accentColor = Color(0xFFF39C12),
                        icon = "🎁",
                        onClick = onInviteClick,
                        modifier = Modifier.weight(1f),
                        tag = "quick_invite"
                    )
                }
            }

            // Third VIP Banner Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF38260F), Color(0xFF26190A))
                            )
                        )
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                        .clickable { onVipClick() }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("quick_vip")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👑", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Become a VIP and enjoy benefits",
                                    color = TextGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Up to Rs 5,000,000",
                                    color = Color(0xFFFFD54F),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Go",
                            tint = GoldPrimary
                        )
                    }
                }
            }

            // Announcement Ticker
            item {
                AnnouncementTicker(onTickerClick = onRouletteClick)
            }

            // Game Category Tabs
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFF3D2714) else DarkSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) GoldPrimary else DarkCardBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onCategorySelected(category) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("cat_$category"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (category == "Hots") {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = RedAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                } else if (category == "Favorite") {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = category,
                                    color = if (isSelected) TextGold else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Section Header: "Hots (34) More Game Hall >"
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = RedAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedCategory,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkCard)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${filteredGames.size}",
                                color = GoldPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* no-op */ }
                    ) {
                        Text(
                            text = "Game Hall",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "More",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Games Grid
            items(filteredGames.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GameCard(
                        game = pair[0],
                        onClick = { onGameClick(pair[0].id) },
                        modifier = Modifier.weight(1f)
                    )

                    if (pair.size > 1) {
                        GameCard(
                            game = pair[1],
                            onClick = { onGameClick(pair[1].id) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Floating Customer Service Headset Button at bottom left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 72.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(GoldAccent, GoldPrimary, Color(0xFF8C5D0B))
                    )
                )
                .border(2.dp, Color(0xFFFFE082), CircleShape)
                .clickable { onCustomerServiceClick() }
                .testTag("floating_support_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Headphones,
                contentDescription = "Support",
                tint = Color.Black,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun QuickBannerCard(
    title: String,
    subtitle: String,
    accentColor: Color,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF382813), Color(0xFF21170A))
                )
            )
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(10.dp)
            .testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Text(text = icon, fontSize = 26.sp)
        }
    }
}

@Composable
fun GameCard(
    game: GameItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("game_card_${game.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(game.accentColor).copy(alpha = 0.25f),
                            Color(0xFF1B140E)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Game Visual Artwork Representation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val emojiIcon = when (game.iconType) {
                    "plane" -> "✈️"
                    "gems" -> "👑"
                    "money" -> "💵"
                    "cowboy" -> "🤠"
                    "piggy" -> "🐷"
                    "box" -> "🎁"
                    "bomb" -> "🐔"
                    else -> "🎰"
                }

                Text(
                    text = emojiIcon,
                    fontSize = 42.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = game.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (game.multiplierText.isNotEmpty()) {
                    Text(
                        text = game.multiplierText,
                        color = Color(game.accentColor),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Top Provider Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x88000000))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = game.provider,
                    color = TextGold,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Top Right Favorite Heart
            Icon(
                imageVector = if (game.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (game.isFavorite) RedAccent else Color(0x66FFFFFF),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(16.dp)
            )
        }
    }
}
