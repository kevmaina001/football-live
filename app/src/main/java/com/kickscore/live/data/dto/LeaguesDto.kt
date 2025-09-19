/*
 * Leagues API response models matching friend's working implementation
 */

package com.kickscore.live.data.dto

import com.google.gson.annotations.SerializedName

data class LeaguesApiResponse(
    @SerializedName("get")
    val get: String,

    @SerializedName("parameters")
    val parameters: Map<String, String>? = null,

    @SerializedName("errors")
    val errors: List<Any>? = null,

    @SerializedName("results")
    val results: Int,

    @SerializedName("paging")
    val paging: PagingResponseDto? = null,

    @SerializedName("response")
    val response: List<LeagueResponseDto>
)

data class PagingResponseDto(
    @SerializedName("current")
    val current: Int,

    @SerializedName("total")
    val total: Int
)

// Using existing LeagueInfoDto from ApiResponse.kt
typealias LeagueResponseDto = LeagueInfoDto