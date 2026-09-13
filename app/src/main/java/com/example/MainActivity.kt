package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopHeaderBar
import com.example.ui.games.AviatorGameScreen
import com.example.ui.games.FortuneGemsScreen
import com.example.ui.games.LuckyRouletteScreen
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.AgencyScreen
import com.example.ui.screens.CustomerServiceDialog
import com.example.ui.screens.DepositScreen
import com.example.ui.screens.EditPasswordDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MeScreen
import com.example.ui.screens.StatisticsDialog
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ActiveGameModal
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                JJRSApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun JJRSApp(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.toastMessage) {
        state.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // When a full game or dialog modal is active
        when (state.activeModal) {
            ActiveGameModal.AVIATOR -> {
                AviatorGameScreen(
                    userBalance = state.balance,
                    onBack = { viewModel.closeModal() },
                    onWinRecorded = { win, bet ->
                        viewModel.recordGameWin(win, bet, "Aviator")
                    }
                )
            }

            ActiveGameModal.LUCKY_ROULETTE -> {
                LuckyRouletteScreen(
                    userBalance = state.balance,
                    onBack = { viewModel.closeModal() },
                    onPrizeWon = { prize ->
                        viewModel.deposit(prize, "Lucky Roulette", "System Bonus")
                    }
                )
            }

            ActiveGameModal.FORTUNE_GEMS -> {
                FortuneGemsScreen(
                    userBalance = state.balance,
                    onBack = { viewModel.closeModal() },
                    onWinRecorded = { win, bet ->
                        viewModel.recordGameWin(win, bet, "Fortune Gems")
                    }
                )
            }

            ActiveGameModal.CUSTOMER_SERVICE -> {
                CustomerServiceDialog(
                    messages = state.chatMessages,
                    onSendMessage = { text -> viewModel.sendChatMessage(text) },
                    onBack = { viewModel.closeModal() }
                )
            }

            ActiveGameModal.STATISTICS -> {
                StatisticsDialog(
                    stats = state.stats,
                    userId = state.userId,
                    onBack = { viewModel.closeModal() }
                )
            }

            ActiveGameModal.EDIT_PASSWORD -> {
                EditPasswordDialog(
                    onBack = { viewModel.closeModal() },
                    onPasswordUpdated = {
                        viewModel.closeModal()
                        Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            ActiveGameModal.NONE -> {
                // Standard app layout with Header and Bottom Navigation
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkBackground,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    topBar = {
                        TopHeaderBar(
                            balance = state.balance,
                            onMenuClick = { viewModel.openModal(ActiveGameModal.CUSTOMER_SERVICE) },
                            onDepositClick = { viewModel.selectTab(ScreenTab.DEPOSIT) },
                            modifier = Modifier.statusBarsPadding()
                        )
                    },
                    bottomBar = {
                        BottomNavBar(
                            currentTab = state.currentTab,
                            onTabSelected = { tab -> viewModel.selectTab(tab) }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = state.currentTab,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "tab_content"
                        ) { tab ->
                            when (tab) {
                                ScreenTab.HOME -> {
                                    HomeScreen(
                                        games = state.games,
                                        selectedCategory = state.selectedCategory,
                                        onCategorySelected = { cat -> viewModel.selectCategory(cat) },
                                        onGameClick = { gameId -> viewModel.openGame(gameId) },
                                        onRouletteClick = { viewModel.openModal(ActiveGameModal.LUCKY_ROULETTE) },
                                        onInviteClick = { viewModel.selectTab(ScreenTab.AGENCY) },
                                        onVipClick = { viewModel.selectTab(ScreenTab.ME) },
                                        onCustomerServiceClick = { viewModel.openModal(ActiveGameModal.CUSTOMER_SERVICE) }
                                    )
                                }

                                ScreenTab.DEPOSIT -> {
                                    DepositScreen(
                                        currentBalance = state.balance,
                                        onDepositSubmitted = { amount, method, account ->
                                            viewModel.deposit(amount, method, account)
                                        }
                                    )
                                }

                                ScreenTab.AGENCY -> {
                                    AgencyScreen(
                                        claimableCommission = state.claimableCommission,
                                        todayTurnover = state.todayTeamTurnover,
                                        teamCount = state.teamMembersCount,
                                        inviteCode = "JJRS-${state.userId}",
                                        onClaimCommission = { viewModel.claimAgencyCommission() }
                                    )
                                }

                                ScreenTab.ACTIVITY -> {
                                    ActivityScreen(
                                        checkInDays = state.checkInDays,
                                        missions = state.missions,
                                        onClaimCheckIn = { day -> viewModel.claimDailyCheckIn(day) },
                                        onClaimMission = { id -> viewModel.claimMission(id) }
                                    )
                                }

                                ScreenTab.ME -> {
                                    MeScreen(
                                        balance = state.balance,
                                        userId = state.userId,
                                        username = state.username,
                                        vipLevel = state.vipLevel,
                                        vipNextLevel = state.vipNextLevel,
                                        vipProgress = state.vipProgress,
                                        vipTarget = state.vipTarget,
                                        onWithdrawClick = { viewModel.selectTab(ScreenTab.DEPOSIT) },
                                        onDepositClick = { viewModel.selectTab(ScreenTab.DEPOSIT) },
                                        onInviteClick = { viewModel.selectTab(ScreenTab.AGENCY) },
                                        onStatisticsClick = { viewModel.openModal(ActiveGameModal.STATISTICS) },
                                        onCustomerServiceClick = { viewModel.openModal(ActiveGameModal.CUSTOMER_SERVICE) },
                                        onEditPasswordClick = { viewModel.openModal(ActiveGameModal.EDIT_PASSWORD) },
                                        onLanguageClick = {
                                            Toast.makeText(context, "Language: English", Toast.LENGTH_SHORT).show()
                                        },
                                        onLogoutClick = {
                                            Toast.makeText(context, "Account switch / logout requested", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Preserve for GreetingScreenshotTest backward compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("JJRS") }
}
