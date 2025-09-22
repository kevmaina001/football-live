/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.score24seven.data.database.dao.MatchDao
import com.score24seven.data.database.entity.MatchEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class MatchDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: Score24SevenDatabase
    private lateinit var matchDao: MatchDao

    private val testMatches = listOf(
        MatchEntity(
            id = "1",
            homeTeamId = "1",
            homeTeamName = "Arsenal",
            homeTeamLogo = "arsenal.png",
            homeTeamCode = "ARS",
            awayTeamId = "2",
            awayTeamName = "Chelsea",
            awayTeamLogo = "chelsea.png",
            awayTeamCode = "CHE",
            homeScore = 2,
            awayScore = 1,
            status = "LIVE",
            startTime = LocalDateTime.now().minusMinutes(45),
            leagueId = "1",
            leagueName = "Premier League",
            leagueLogo = "pl.png",
            leagueCountry = "England",
            minute = 67,
            venue = "Emirates Stadium",
            isLive = true,
            lastUpdated = LocalDateTime.now()
        ),
        MatchEntity(
            id = "2",
            homeTeamId = "3",
            homeTeamName = "Barcelona",
            homeTeamLogo = "barcelona.png",
            homeTeamCode = "BAR",
            awayTeamId = "4",
            awayTeamName = "Real Madrid",
            awayTeamLogo = "real.png",
            awayTeamCode = "RM",
            homeScore = 1,
            awayScore = 3,
            status = "FINISHED",
            startTime = LocalDateTime.now().minusHours(2),
            leagueId = "2",
            leagueName = "La Liga",
            leagueLogo = "laliga.png",
            leagueCountry = "Spain",
            minute = 90,
            venue = "Camp Nou",
            isLive = false,
            lastUpdated = LocalDateTime.now()
        ),
        MatchEntity(
            id = "3",
            homeTeamId = "5",
            homeTeamName = "Liverpool",
            homeTeamLogo = "liverpool.png",
            homeTeamCode = "LIV",
            awayTeamId = "6",
            awayTeamName = "Manchester United",
            awayTeamLogo = "manutd.png",
            awayTeamCode = "MUN",
            homeScore = null,
            awayScore = null,
            status = "SCHEDULED",
            startTime = LocalDateTime.now().plusHours(3),
            leagueId = "1",
            leagueName = "Premier League",
            leagueLogo = "pl.png",
            leagueCountry = "England",
            minute = null,
            venue = "Anfield",
            isLive = false,
            lastUpdated = LocalDateTime.now()
        )
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Score24SevenDatabase::class.java
        ).allowMainThreadQueries().build()

        matchDao = database.matchDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetMatches() = runBlocking {
        // When
        matchDao.insertMatches(testMatches)
        val allMatches = matchDao.getAllMatches().first()

        // Then
        assertEquals(3, allMatches.size)
        assertTrue(allMatches.contains(testMatches[0]))
        assertTrue(allMatches.contains(testMatches[1]))
        assertTrue(allMatches.contains(testMatches[2]))
    }

    @Test
    fun getLiveMatches() = runBlocking {
        // Given
        matchDao.insertMatches(testMatches)

        // When
        val liveMatches = matchDao.getLiveMatches().first()

        // Then
        assertEquals(1, liveMatches.size)
        assertEquals("1", liveMatches[0].id)
        assertEquals("LIVE", liveMatches[0].status)
        assertTrue(liveMatches[0].isLive)
    }

    @Test
    fun getMatchesByDate() = runBlocking {
        // Given
        matchDao.insertMatches(testMatches)
        val today = LocalDate.now()

        // When
        val todayMatches = matchDao.getMatchesByDate(today).first()

        // Then
        // Should return matches that start today
        assertTrue(todayMatches.isNotEmpty())
        todayMatches.forEach { match ->
            assertEquals(today, match.startTime.toLocalDate())
        }
    }

    @Test
    fun getMatchById() = runBlocking {
        // Given
        matchDao.insertMatches(testMatches)

        // When
        val match = matchDao.getMatchById("1").first()

        // Then
        assertEquals("1", match?.id)
        assertEquals("Arsenal", match?.homeTeamName)
        assertEquals("Chelsea", match?.awayTeamName)
    }

    @Test
    fun getMatchesByLeague() = runBlocking {
        // Given
        matchDao.insertMatches(testMatches)

        // When
        val premierLeagueMatches = matchDao.getMatchesByLeague("1").first()

        // Then
        assertEquals(2, premierLeagueMatches.size)
        premierLeagueMatches.forEach { match ->
            assertEquals("1", match.leagueId)
            assertEquals("Premier League", match.leagueName)
        }
    }

    @Test
    fun updateMatch() = runBlocking {
        // Given
        matchDao.insertMatches(testMatches)
        val updatedMatch = testMatches[0].copy(
            homeScore = 3,
            awayScore = 1,
            minute = 75
        )

        // When
        matchDao.insertMatches(listOf(updatedMatch))
        val retrievedMatch = matchDao.getMatchById("1").first()

        // Then
        assertEquals(3, retrievedMatch?.homeScore)
        assertEquals(1, retrievedMatch?.awayScore)
        assertEquals(75, retrievedMatch?.minute)
    }

    @Test
    fun deleteOldMatches() = runBlocking {
        // Given
        val oldMatch = testMatches[0].copy(
            id = "old",
            startTime = LocalDateTime.now().minusDays(2),
            lastUpdated = LocalDateTime.now().minusDays(2)
        )
        matchDao.insertMatches(testMatches + oldMatch)

        // When
        val cutoffTime = LocalDateTime.now().minusDays(1)
        matchDao.deleteMatchesOlderThan(cutoffTime)
        val remainingMatches = matchDao.getAllMatches().first()

        // Then
        assertEquals(3, remainingMatches.size)
        assertTrue(remainingMatches.none { it.id == "old" })
    }

    @Test
    fun getMatchesByStatus() = runBlocking {
        // Given
        matchDao.insertMatches(testMatches)

        // When
        val finishedMatches = matchDao.getMatchesByStatus("FINISHED").first()
        val liveMatches = matchDao.getMatchesByStatus("LIVE").first()
        val scheduledMatches = matchDao.getMatchesByStatus("SCHEDULED").first()

        // Then
        assertEquals(1, finishedMatches.size)
        assertEquals("2", finishedMatches[0].id)

        assertEquals(1, liveMatches.size)
        assertEquals("1", liveMatches[0].id)

        assertEquals(1, scheduledMatches.size)
        assertEquals("3", scheduledMatches[0].id)
    }

    @Test
    fun insertMatchesReplacesExisting() = runBlocking {
        // Given
        matchDao.insertMatches(listOf(testMatches[0]))
        val originalMatch = matchDao.getMatchById("1").first()
        assertEquals(2, originalMatch?.homeScore)

        // When - Insert same match with different score
        val updatedMatch = testMatches[0].copy(homeScore = 4)
        matchDao.insertMatches(listOf(updatedMatch))

        // Then - Should replace, not add new
        val allMatches = matchDao.getAllMatches().first()
        val retrievedMatch = matchDao.getMatchById("1").first()

        assertEquals(1, allMatches.count { it.id == "1" })
        assertEquals(4, retrievedMatch?.homeScore)
    }
}