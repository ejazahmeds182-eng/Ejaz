package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RedBadge
import com.example.ui.theme.TextMuted
import com.example.viewmodel.ScreenTab

@Composable
fun BottomNavBar(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                label = "Home",
                icon = Icons.Default.Home,
                isSelected = currentTab == ScreenTab.HOME,
                badge = null,
                onClick = { onTabSelected(ScreenTab.HOME) },
                testTag = "tab_home"
            )

            NavBarItem(
                label = "Deposit",
                icon = Icons.Default.AccountBalanceWallet,
                isSelected = currentTab == ScreenTab.DEPOSIT,
                badge = null,
                onClick = { onTabSelected(ScreenTab.DEPOSIT) },
                testTag = "tab_deposit"
            )

            NavBarItem(
                label = "Agency",
                icon = Icons.Default.MonetizationOn,
                isSelected = currentTab == ScreenTab.AGENCY,
                badge = "7",
                isSpecialGold = true,
                onClick = { onTabSelected(ScreenTab.AGENCY) },
                testTag = "tab_agency"
            )

            NavBarItem(
                label = "Activity",
                icon = Icons.Default.CardGiftcard,
                isSelected = currentTab == ScreenTab.ACTIVITY,
                badge = "7",
                onClick = { onTabSelected(ScreenTab.ACTIVITY) },
                testTag = "tab_activity"
            )

            NavBarItem(
                label = "Me",
                icon = Icons.Default.Person,
                isSelected = currentTab == ScreenTab.ME,
                badge = "11",
                onClick = { onTabSelected(ScreenTab.ME) },
                testTag = "tab_me"
            )
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badge: String?,
    isSpecialGold: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    val activeColor = GoldPrimary
    val inactiveColor = TextMuted

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected || isSpecialGold) activeColor else inactiveColor,
                modifier = Modifier.size(26.dp)
            )

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 10.dp, y = (-4).dp)
                        .clip(CircleShape)
                        .background(RedBadge)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = label,
            color = if (isSelected) activeColor else inactiveColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
