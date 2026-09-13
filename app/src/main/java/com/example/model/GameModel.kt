package com.example.model

data class GameItem(
    val id: String,
    val title: String,
    val provider: String,
    val category: String,
    val isHot: Boolean = false,
    val isFavorite: Boolean = false,
    val multiplierText: String = "",
    val accentColor: Long = 0xFFE5A93C,
    val iconType: String = "plane" // "plane", "gems", "piggy", "money", "cowboy", "box", "bomb", "wheel"
)

data class DailyMission(
    val id: String,
    val title: String,
    val description: String,
    val reward: Double,
    val currentProgress: Int,
    val targetProgress: Int,
    val isClaimed: Boolean = false
) {
    val isCompleted: Boolean get() = currentProgress >= targetProgress
    val progressFraction: Float get() = (currentProgress.toFloat() / targetProgress.toFloat()).coerceIn(0f, 1f)
}

data class DailyCheckInDay(
    val day: Int,
    val reward: Double,
    val isClaimed: Boolean,
    val isCurrent: Boolean
)

data class WithdrawalRecord(
    val id: String,
    val date: String,
    val amount: Double,
    val method: String,
    val account: String,
    val status: String // "Approved", "Processing", "Submitted"
)

data class TransactionItem(
    val id: String,
    val title: String,
    val date: String,
    val amount: Double,
    val isCredit: Boolean,
    val type: String
)

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val time: String
)
