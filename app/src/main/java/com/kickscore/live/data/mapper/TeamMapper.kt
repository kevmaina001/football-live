/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.api.TeamDto
import com.kickscore.live.data.database.entity.TeamEntity
import com.kickscore.live.domain.model.Team

object TeamMapper {

    fun mapDtoToEntity(dto: TeamDto): TeamEntity {
        return TeamEntity(
            id = dto.id,
            name = dto.name,
            code = dto.code,
            country = dto.country,
            founded = dto.founded,
            national = dto.national,
            logo = dto.logo
        )
    }

    fun mapDtosToEntities(dtos: List<TeamDto>): List<TeamEntity> {
        return dtos.map { mapDtoToEntity(it) }
    }

    fun mapEntityToDomain(entity: TeamEntity): Team {
        return Team(
            id = entity.id,
            name = entity.name,
            code = entity.code,
            logo = entity.logo,
            country = entity.country,
            founded = entity.founded,
            isNational = entity.national,
            isFavorite = entity.isFavorite
        )
    }

    fun mapEntitiesToDomain(entities: List<TeamEntity>): List<Team> {
        return entities.map { mapEntityToDomain(it) }
    }
}