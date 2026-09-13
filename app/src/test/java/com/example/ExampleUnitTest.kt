package com.example

import com.example.viewmodel.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testGameViewModelDepositAndVipProgress() {
    val vm = GameViewModel()
    val initialBalance = vm.uiState.value.balance
    val initialVipProgress = vm.uiState.value.vipProgress

    vm.deposit(1000.0, "EasyPaisa", "03419666242")

    assertEquals(initialBalance + 1000.0, vm.uiState.value.balance, 0.01)
    assertEquals(initialVipProgress + 800.0, vm.uiState.value.vipProgress, 0.01)
  }

  @Test
  fun testGameViewModelClaimAgencyCommission() {
    val vm = GameViewModel()
    val initialBalance = vm.uiState.value.balance
    val claimable = vm.uiState.value.claimableCommission
    assertTrue(claimable > 0)

    vm.claimAgencyCommission()

    assertEquals(initialBalance + claimable, vm.uiState.value.balance, 0.01)
    assertEquals(0.0, vm.uiState.value.claimableCommission, 0.01)
  }

  @Test
  fun testGameViewModelRecordWin() {
    val vm = GameViewModel()
    val initialBalance = vm.uiState.value.balance
    val initialRounds = vm.uiState.value.stats.totalRounds

    vm.recordGameWin(amountWon = 500.0, betAmount = 100.0, gameName = "Aviator")

    // Net change +400.0
    assertEquals(initialBalance + 400.0, vm.uiState.value.balance, 0.01)
    assertEquals(initialRounds + 1, vm.uiState.value.stats.totalRounds)
  }
}

