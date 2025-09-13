/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.data.mapper

import com.kickscore.live.data.api.TeamDto
import com.kickscore.live.data.database.entity.TeamEntity

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
}