package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ChatMessage
import com.example.model.DailyCheckInDay
import com.example.model.DailyMission
import com.example.model.GameItem
import com.example.model.WithdrawalRecord
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class ScreenTab {
    HOME, DEPOSIT, AGENCY, ACTIVITY, ME
}

enum class ActiveGameModal {
    NONE, AVIATOR, LUCKY_ROULETTE, FORTUNE_GEMS, CUSTOMER_SERVICE, STATISTICS, EDIT_PASSWORD
}

data class UserStats(
    val totalBet: Double = 33863.00,
    val totalWon: Double = 41250.00,
    val totalRounds: Int = 142,
    val highestWin: Double = 18450.00,
    val winRate: Int = 68
)

data class UiState(
    val currentTab: ScreenTab = ScreenTab.HOME,
    val activeModal: ActiveGameModal = ActiveGameModal.NONE,
    val balance: Double = 1250.31,
    val userId: String = "461738",
    val username: String = "923419666242",
    val vipLevel: Int = 5,
    val vipNextLevel: Int = 6,
    val vipProgress: Double = 33863.00,
    val vipTarget: Double = 50000.00,
    val selectedCategory: String = "Hots",
    val games: List<GameItem> = emptyList(),
    val checkInDays: List<DailyCheckInDay> = emptyList(),
    val missions: List<DailyMission> = emptyList(),
    val withdrawals: List<WithdrawalRecord> = emptyList(),
    val chatMessages: List<ChatMessage> = emptyList(),
    val teamMembersCount: Int = 18,
    val todayTeamTurnover: Double = 148500.00,
    val claimableCommission: Double = 4250.00,
    val stats: UserStats = UserStats(),
    val toastMessage: String? = null
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
        startLiveAnnouncementTicker()
    }

    private fun loadInitialData() {
        val initialGames = listOf(
            GameItem(
                id = "aviator_1",
                title = "Aviator",
                provider = "SPRIBE",
                category = "Hots",
                isHot = true,
                isFavorite = true,
                multiplierText = "x500.0",
                accentColor = 0xFFE74C3C,
                iconType = "plane"
            ),
            GameItem(
                id = "fortune_gems",
                title = "Fortune Gems 500",
                provider = "JILI",
                category = "Hots",
                isHot = true,
                isFavorite = true,
                multiplierText = "500X",
                accentColor = 0xFFFFD700,
                iconType = "gems"
            ),
            GameItem(
                id = "aviator_2",
                title = "Aviator 2",
                provider = "SPRIBE",
                category = "Hots",
                isHot = true,
                isFavorite = false,
                multiplierText = "x1000",
                accentColor = 0xFFFF5722,
                iconType = "plane"
            ),
            GameItem(
                id = "money_coming",
                title = "Money Coming",
                provider = "JILI",
                category = "Hots",
                isHot = true,
                isFavorite = true,
                multiplierText = "10,000X",
                accentColor = 0xFF2ECC71,
                iconType = "money"
            ),
            GameItem(
                id = "wild_bounty",
                title = "Wild Bounty Showdown",
                provider = "PG",
                category = "PG",
                isHot = true,
                isFavorite = false,
                multiplierText = "5,000X",
                accentColor = 0xFFE67E22,
                iconType = "cowboy"
            ),
            GameItem(
                id = "lucky_piggy",
                title = "Lucky Piggy",
                provider = "PG",
                category = "PG",
                isHot = true,
                isFavorite = false,
                multiplierText = "20,000X",
                accentColor = 0xFFF39C12,
                iconType = "piggy"
            ),
            GameItem(
                id = "labubu_box",
                title = "Labubu's Lucky Box",
                provider = "INOUT",
                category = "INOUT",
                isHot = false,
                isFavorite = false,
                multiplierText = "x200",
                accentColor = 0xFF8E44AD,
                iconType = "box"
            ),
            GameItem(
                id = "chicken_bomb",
                title = "Chicken Bomb 2000",
                provider = "JDB",
                category = "JDB",
                isHot = true,
                isFavorite = false,
                multiplierText = "2000X",
                accentColor = 0xFFE74C3C,
                iconType = "bomb"
            )
        )

        val checkIns = listOf(
            DailyCheckInDay(day = 1, reward = 50.0, isClaimed = true, isCurrent = false),
            DailyCheckInDay(day = 2, reward = 100.0, isClaimed = true, isCurrent = false),
            DailyCheckInDay(day = 3, reward = 250.0, isClaimed = false, isCurrent = true),
            DailyCheckInDay(day = 4, reward = 500.0, isClaimed = false, isCurrent = false),
            DailyCheckInDay(day = 5, reward = 1000.0, isClaimed = false, isCurrent = false),
            DailyCheckInDay(day = 6, reward = 2500.0, isClaimed = false, isCurrent = false),
            DailyCheckInDay(day = 7, reward = 10000.0, isClaimed = false, isCurrent = false)
        )

        val missionsList = listOf(
            DailyMission(
                id = "m1",
                title = "Fly & Cash Out",
                description = "Play 3 rounds in Aviator Crash",
                reward = 500.0,
                currentProgress = 1,
                targetProgress = 3
            ),
            DailyMission(
                id = "m2",
                title = "High Altitude Winner",
                description = "Cash out at 2.50x or higher",
                reward = 1500.0,
                currentProgress = 0,
                targetProgress = 1
            ),
            DailyMission(
                id = "m3",
                title = "Fortune Wheel Spin",
                description = "Spin the Lucky Roulette",
                reward = 1000.0,
                currentProgress = 0,
                targetProgress = 1
            ),
            DailyMission(
                id = "m4",
                title = "Gem Collector",
                description = "Spin Fortune Gems 5 times",
                reward = 2000.0,
                currentProgress = 2,
                targetProgress = 5
            ),
            DailyMission(
                id = "m5",
                title = "Agency Partner Invite",
                description = "Share invitation link with 1 friend",
                reward = 3000.0,
                currentProgress = 1,
                targetProgress = 1,
                isClaimed = false
            ),
            DailyMission(
                id = "m6",
                title = "Daily Master Challenge",
                description = "Reach Rs 40,000 turnover to unlock super bonus",
                reward = 32000.0,
                currentProgress = 18500,
                targetProgress = 40000
            )
        )

        val initialWithdrawals = listOf(
            WithdrawalRecord(
                id = "W981742",
                date = "Today 04:22 PM",
                amount = 15000.00,
                method = "EasyPaisa",
                account = "0341****242",
                status = "Approved"
            ),
            WithdrawalRecord(
                id = "W980311",
                date = "Yesterday 09:10 PM",
                amount = 25000.00,
                method = "JazzCash",
                account = "0300****119",
                status = "Approved"
            )
        )

        val initialChat = listOf(
            ChatMessage("1", false, "Welcome to JJRS 24/7 VIP Customer Service! How can we assist you with deposits, withdrawals, or earning up to Rs 40,000 daily via Agency?", "05:58 PM"),
            ChatMessage("2", false, "Quick tips: Aviator Crash and the Agency 3-level team system give the highest daily returns!", "05:58 PM")
        )

        _uiState.update {
            it.copy(
                games = initialGames,
                checkInDays = checkIns,
                missions = missionsList,
                withdrawals = initialWithdrawals,
                chatMessages = initialChat
            )
        }
    }

    private fun startLiveAnnouncementTicker() {
        viewModelScope.launch {
            while (true) {
                delay(12000)
                // random ticker simulator
            }
        }
    }

    fun selectTab(tab: ScreenTab) {
        _uiState.update { it.copy(currentTab = tab, activeModal = ActiveGameModal.NONE) }
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun openGame(gameId: String) {
        when {
            gameId.startsWith("aviator") -> _uiState.update { it.copy(activeModal = ActiveGameModal.AVIATOR) }
            gameId == "fortune_gems" -> _uiState.update { it.copy(activeModal = ActiveGameModal.FORTUNE_GEMS) }
            gameId == "roulette" -> _uiState.update { it.copy(activeModal = ActiveGameModal.LUCKY_ROULETTE) }
            else -> {
                // Default to Fortune Gems or Aviator for playable experience
                _uiState.update { it.copy(activeModal = ActiveGameModal.FORTUNE_GEMS) }
            }
        }
    }

    fun openModal(modal: ActiveGameModal) {
        _uiState.update { it.copy(activeModal = modal) }
    }

    fun closeModal() {
        _uiState.update { it.copy(activeModal = ActiveGameModal.NONE) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun deposit(amount: Double, method: String, account: String) {
        _uiState.update { state ->
            val newBalance = state.balance + amount
            val newVipProgress = state.vipProgress + (amount * 0.8)
            val newLevel = if (newVipProgress >= state.vipTarget) state.vipNextLevel else state.vipLevel
            state.copy(
                balance = newBalance,
                vipProgress = newVipProgress,
                vipLevel = newLevel,
                toastMessage = "Deposit of Rs ${"%.2f".format(amount)} via $method was successful!"
            )
        }
    }

    fun withdraw(amount: Double, method: String, account: String): Boolean {
        val currentBalance = _uiState.value.balance
        if (amount > currentBalance) {
            _uiState.update { it.copy(toastMessage = "Insufficient balance! Current: Rs ${"%.2f".format(currentBalance)}") }
            return false
        }
        if (amount < 200) {
            _uiState.update { it.copy(toastMessage = "Minimum withdrawal is Rs 200.00") }
            return false
        }

        val record = WithdrawalRecord(
            id = "W" + Random.nextInt(100000, 999999),
            date = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date()),
            amount = amount,
            method = method,
            account = account,
            status = "Processing"
        )

        _uiState.update { state ->
            state.copy(
                balance = state.balance - amount,
                withdrawals = listOf(record) + state.withdrawals,
                toastMessage = "Withdrawal request for Rs ${"%.2f".format(amount)} submitted successfully!"
            )
        }
        return true
    }

    fun claimDailyCheckIn(day: Int) {
        _uiState.update { state ->
            val updatedDays = state.checkInDays.map {
                if (it.day == day && !it.isClaimed) it.copy(isClaimed = true) else it
            }
            val reward = state.checkInDays.firstOrNull { it.day == day }?.reward ?: 0.0
            state.copy(
                balance = state.balance + reward,
                checkInDays = updatedDays,
                toastMessage = "Claimed Day $day Reward: Rs ${"%.2f".format(reward)}!"
            )
        }
    }

    fun claimMission(missionId: String) {
        _uiState.update { state ->
            val mission = state.missions.firstOrNull { it.id == missionId }
            if (mission != null && mission.isCompleted && !mission.isClaimed) {
                val updatedMissions = state.missions.map {
                    if (it.id == missionId) it.copy(isClaimed = true) else it
                }
                state.copy(
                    balance = state.balance + mission.reward,
                    missions = updatedMissions,
                    toastMessage = "Claimed Mission Bonus: Rs ${"%.2f".format(mission.reward)}!"
                )
            } else {
                state
            }
        }
    }

    fun claimAgencyCommission() {
        _uiState.update { state ->
            val comm = state.claimableCommission
            if (comm > 0) {
                state.copy(
                    balance = state.balance + comm,
                    claimableCommission = 0.0,
                    toastMessage = "Successfully transferred Rs ${"%.2f".format(comm)} agency commission to balance!"
                )
            } else {
                state.copy(toastMessage = "No commission available to claim right now. Invite more players!")
            }
        }
    }

    fun recordGameWin(amountWon: Double, betAmount: Double, gameName: String) {
        _uiState.update { state ->
            val newBalance = (state.balance - betAmount + amountWon).coerceAtLeast(0.0)
            val currentStats = state.stats
            val updatedStats = currentStats.copy(
                totalBet = currentStats.totalBet + betAmount,
                totalWon = currentStats.totalWon + amountWon,
                totalRounds = currentStats.totalRounds + 1,
                highestWin = maxOf(currentStats.highestWin, amountWon)
            )
            // progress mission 1 (Aviator play count)
            val updatedMissions = state.missions.map { m ->
                if (m.id == "m1" && !m.isCompleted) {
                    m.copy(currentProgress = (m.currentProgress + 1).coerceAtMost(m.targetProgress))
                } else if (m.id == "m4" && gameName == "Fortune Gems" && !m.isCompleted) {
                    m.copy(currentProgress = (m.currentProgress + 1).coerceAtMost(m.targetProgress))
                } else if (m.id == "m6") {
                    m.copy(currentProgress = (m.currentProgress + betAmount.toInt()).coerceAtMost(m.targetProgress))
                } else {
                    m
                }
            }

            state.copy(
                balance = newBalance,
                stats = updatedStats,
                missions = updatedMissions
            )
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            isFromUser = true,
            text = text,
            time = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        )
        _uiState.update { it.copy(chatMessages = it.chatMessages + userMsg) }

        // Automated helpful response
        viewModelScope.launch {
            delay(1000)
            val replyText = when {
                text.contains("withdraw", ignoreCase = true) ->
                    "Withdrawals are processed instantly via EasyPaisa and JazzCash. You can request withdrawals anytime in the 'Withdrawal' section with 0% fee."
                text.contains("earn", ignoreCase = true) || text.contains("40,000", ignoreCase = true) || text.contains("40000", ignoreCase = true) ->
                    "To earn Rs 40,000 daily: 1) Share your Agency invite link with 10-20 active players. As they play Aviator or Slots, you receive 30% team rebate commission! 2) Play Aviator crash strategically with auto cash-out at 1.50x to 2.0x."
                text.contains("deposit", ignoreCase = true) ->
                    "Deposits are credited automatically within seconds. Tap 'Deposit' at the top or bottom bar to select EasyPaisa, JazzCash, or Bank transfer."
                text.contains("aviator", ignoreCase = true) ->
                    "Aviator is a crash multiplier game. Place your bet, watch the lucky plane climb from 1.00x upwards, and hit 'Cash Out' before it flies away to win!"
                else ->
                    "Thank you for contacting JJRS Support! Our specialist is reviewing your request. You can also explore our Daily Check-in rewards and Agency commission tier for more rewards."
            }
            val botMsg = ChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                isFromUser = false,
                text = replyText,
                time = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
            )
            _uiState.update { it.copy(chatMessages = it.chatMessages + botMsg) }
        }
    }
}
