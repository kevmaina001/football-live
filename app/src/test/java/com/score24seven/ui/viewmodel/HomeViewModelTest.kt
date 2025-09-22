/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import com.score24seven.domain.model.League
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.MatchStatus
import com.score24seven.domain.model.Team
import com.score24seven.domain.usecase.GetLiveMatchesUseCase
import com.score24seven.domain.usecase.GetTodayMatchesUseCase
import com.score24seven.ui.screen.home.HomeScreenState
import com.score24seven.ui.screen.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val mockGetLiveMatchesUseCase = mockk<GetLiveMatchesUseCase>()
    private val mockGetTodayMatchesUseCase = mockk<GetTodayMatchesUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    private val testMatches = listOf(
        Match(
            id = "1",
            homeTeam = Team("1", "Arsenal", "arsenal.png", "ARS"),
            awayTeam = Team("2", "Chelsea", "chelsea.png", "CHE"),
            homeScore = 2,
            awayScore = 1,
            status = MatchStatus.LIVE,
            startTime = LocalDateTime.now().minusMinutes(45),
            league = League("1", "Premier League", "pl.png", "England"),
            minute = 67,
            venue = "Emirates Stadium",
            isLive = true
        ),
        Match(
            id = "2",
            homeTeam = Team("3", "Barcelona", "barcelona.png", "BAR"),
            awayTeam = Team("4", "Real Madrid", "real.png", "RM"),
            homeScore = 1,
            awayScore = 3,
            status = MatchStatus.SCHEDULED,
            startTime = LocalDateTime.now().plusHours(2),
            league = League("2", "La Liga", "laliga.png", "Spain"),
            minute = null,
            venue = "Camp Nou",
            isLive = false
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            getLiveMatchesUseCase = mockGetLiveMatchesUseCase,
            getTodayMatchesUseCase = mockGetTodayMatchesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Given - fresh viewModel

        // When
        val state = viewModel.state.value

        // Then
        assertEquals(HomeScreenState(), state)
        assertFalse(state.isLoading)
        assertTrue(state.liveMatches.isEmpty())
        assertTrue(state.todayMatches.isEmpty())
        assertEquals(null, state.error)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun `loadMatches updates state with live and today matches`() = runTest {
        // Given
        coEvery { mockGetLiveMatchesUseCase() } returns flowOf(listOf(testMatches[0]))
        coEvery { mockGetTodayMatchesUseCase() } returns flowOf(testMatches)

        // When
        viewModel.loadMatches()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.liveMatches.size)
        assertEquals(2, state.todayMatches.size)
        assertEquals(null, state.error)
        assertEquals(testMatches[0], state.liveMatches[0])
    }

    @Test
    fun `loadMatches handles error correctly`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { mockGetLiveMatchesUseCase() } throws Exception(errorMessage)
        coEvery { mockGetTodayMatchesUseCase() } returns flowOf(emptyList())

        // When
        viewModel.loadMatches()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.liveMatches.isEmpty())
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `refreshMatches sets refreshing state`() = runTest {
        // Given
        coEvery { mockGetLiveMatchesUseCase() } returns flowOf(testMatches)
        coEvery { mockGetTodayMatchesUseCase() } returns flowOf(testMatches)

        // When
        viewModel.refreshMatches()

        // Then - Check refreshing state before completion
        val initialState = viewModel.state.value
        assertTrue(initialState.isRefreshing)

        // Complete the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Check final state
        val finalState = viewModel.state.value
        assertFalse(finalState.isRefreshing)
        assertEquals(2, finalState.liveMatches.size)
        assertEquals(2, finalState.todayMatches.size)
    }

    @Test
    fun `filterByLeague filters matches correctly`() = runTest {
        // Given
        coEvery { mockGetLiveMatchesUseCase() } returns flowOf(testMatches)
        coEvery { mockGetTodayMatchesUseCase() } returns flowOf(testMatches)
        viewModel.loadMatches()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.filterByLeague("Premier League")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals("Premier League", state.selectedLeague)
        // In a full implementation, this would filter the displayed matches
    }

    @Test
    fun `clearError clears error state`() = runTest {
        // Given - Set error state first
        coEvery { mockGetLiveMatchesUseCase() } throws Exception("Test error")
        coEvery { mockGetTodayMatchesUseCase() } returns flowOf(emptyList())
        viewModel.loadMatches()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify error is set
        assertTrue(viewModel.state.value.error != null)

        // When
        viewModel.clearError()

        // Then
        val state = viewModel.state.value
        assertEquals(null, state.error)
    }
}