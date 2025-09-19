/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.model

data class TopScorer(
    val player: TopScorerPlayer,
    val statistics: List<PlayerStatistics>
)

data class TopScorerPlayer(
    val id: Int,
    val name: String,
    val firstname: String?,
    val lastname: String?,
    val age: Int?,
    val birth: PlayerBirth?,
    val nationality: String?,
    val height: String?,
    val weight: String?,
    val injured: Boolean,
    val photo: String?
)

data class PlayerBirth(
    val date: String?,
    val place: String?,
    val country: String?
)

data class PlayerStatistics(
    val team: Team,
    val league: League,
    val games: PlayerGames,
    val substitutes: PlayerSubstitutes?,
    val shots: PlayerShots?,
    val goals: PlayerGoals,
    val passes: PlayerPasses?,
    val tackles: PlayerTackles?,
    val duels: PlayerDuels?,
    val dribbles: PlayerDribbles?,
    val fouls: PlayerFouls?,
    val cards: PlayerCards?,
    val penalty: PlayerPenalty?
)


data class PlayerGames(
    val appearences: Int?,
    val lineups: Int?,
    val minutes: Int?,
    val number: Int?,
    val position: String?,
    val rating: String?,
    val captain: Boolean
)

data class PlayerSubstitutes(
    val substitutesIn: Int?,
    val substitutesOut: Int?,
    val bench: Int?
)

data class PlayerShots(
    val total: Int?,
    val on: Int?
)

data class PlayerGoals(
    val total: Int?,
    val conceded: Int?,
    val assists: Int?,
    val saves: Int?
)

data class PlayerPasses(
    val total: Int?,
    val key: Int?,
    val accuracy: Int?
)

data class PlayerTackles(
    val total: Int?,
    val blocks: Int?,
    val interceptions: Int?
)

data class PlayerDuels(
    val total: Int?,
    val won: Int?
)

data class PlayerDribbles(
    val attempts: Int?,
    val success: Int?,
    val past: Int?
)

data class PlayerFouls(
    val drawn: Int?,
    val committed: Int?
)

data class PlayerCards(
    val yellow: Int?,
    val yellowred: Int?,
    val red: Int?
)

data class PlayerPenalty(
    val won: Int?,
    val commited: Int?,
    val scored: Int?,
    val missed: Int?,
    val saved: Int?
)