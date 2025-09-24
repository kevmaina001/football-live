/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.score24seven.data.api.PredictionDto
import com.score24seven.domain.model.*
import com.score24seven.domain.repository.MatchDetailRepository
import com.score24seven.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailTabsViewModel @Inject constructor(
    private val repository: MatchDetailRepository
) : ViewModel() {

    private val _predictionsState = MutableStateFlow<Resource<List<PredictionDto>>>(Resource.Loading())
    val predictionsState: StateFlow<Resource<List<PredictionDto>>> = _predictionsState.asStateFlow()

    private val _lineupsState = MutableStateFlow<Resource<List<Lineup>>>(Resource.Loading())
    val lineupsState: StateFlow<Resource<List<Lineup>>> = _lineupsState.asStateFlow()

    private val _statisticsState = MutableStateFlow<Resource<List<MatchStatistic>>>(Resource.Loading())
    val statisticsState: StateFlow<Resource<List<MatchStatistic>>> = _statisticsState.asStateFlow()

    private val _eventsState = MutableStateFlow<Resource<List<MatchEvent>>>(Resource.Loading())
    val eventsState: StateFlow<Resource<List<MatchEvent>>> = _eventsState.asStateFlow()

    private val _headToHeadState = MutableStateFlow<Resource<List<Match>>>(Resource.Loading())
    val headToHeadState: StateFlow<Resource<List<Match>>> = _headToHeadState.asStateFlow()

    private val _leagueMatchesState = MutableStateFlow<Resource<List<Match>>>(Resource.Loading())
    val leagueMatchesState: StateFlow<Resource<List<Match>>> = _leagueMatchesState.asStateFlow()

    fun loadPredictions(fixtureId: Int) {
        viewModelScope.launch {
            repository.getMatchPredictions(fixtureId).collect {
                _predictionsState.value = it
            }
        }
    }

    fun loadLineups(fixtureId: Int) {
        viewModelScope.launch {
            repository.getMatchLineups(fixtureId).collect {
                _lineupsState.value = it
            }
        }
    }

    fun loadStatistics(fixtureId: Int) {
        viewModelScope.launch {
            repository.getMatchStatistics(fixtureId).collect {
                _statisticsState.value = it
            }
        }
    }

    fun loadEvents(fixtureId: Int) {
        viewModelScope.launch {
            repository.getMatchEvents(fixtureId).collect {
                _eventsState.value = it
            }
        }
    }

    fun loadHeadToHead(homeTeamId: Int, awayTeamId: Int) {
        viewModelScope.launch {
            repository.getHeadToHead(homeTeamId, awayTeamId).collect {
                _headToHeadState.value = it
            }
        }
    }

    fun loadLeagueMatches(leagueId: Int, season: Int) {
        viewModelScope.launch {
            repository.getLeagueMatches(leagueId, season).collect {
                _leagueMatchesState.value = it
            }
        }
    }
}