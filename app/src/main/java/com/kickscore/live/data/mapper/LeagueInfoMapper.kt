/*
 * Mapper for leagues data following friend's working approach
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.dto.*
import com.kickscore.live.domain.model.*

object LeagueInfoMapper {

    fun mapDtoToDomain(dto: LeagueResponseDto): LeagueInfo {
        return LeagueInfo(
            league = mapLeagueDetailsDtoToDomain(dto.league),
            country = mapCountryDetailsDtoToDomain(dto.country),
            seasons = dto.seasons.map { mapSeasonDetailsDtoToDomain(it) }
        )
    }

    fun mapDtosToDomain(dtos: List<LeagueResponseDto>): List<LeagueInfo> {
        return dtos.map { mapDtoToDomain(it) }
    }

    private fun mapLeagueDetailsDtoToDomain(dto: LeagueDto): LeagueDetails {
        return LeagueDetails(
            id = dto.id,
            name = dto.name,
            type = "League", // LeagueDto doesn't have type field, set default
            logo = dto.logo
        )
    }

    private fun mapCountryDetailsDtoToDomain(dto: CountryDto): CountryDetails {
        return CountryDetails(
            name = dto.name,
            code = dto.code,
            flag = dto.flag
        )
    }

    private fun mapSeasonDetailsDtoToDomain(dto: SeasonDto): SeasonDetails {
        return SeasonDetails(
            year = dto.year,
            start = dto.start,
            end = dto.end,
            current = dto.current,
            coverage = dto.coverage?.let { mapCoverageDetailsDtoToDomain(it) }
        )
    }

    private fun mapCoverageDetailsDtoToDomain(dto: CoverageDto): CoverageDetails {
        return CoverageDetails(
            standings = dto.standings ?: false,
            topScorers = dto.topScorers ?: false,
            predictions = dto.predictions ?: false,
            fixtures = dto.fixtures?.events ?: false
        )
    }
}