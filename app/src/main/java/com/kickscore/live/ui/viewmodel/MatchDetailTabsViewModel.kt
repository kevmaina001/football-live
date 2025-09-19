/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kickscore.live.data.api.EventDto
import com.kickscore.live.data.api.LineupDto
import com.kickscore.live.data.api.PredictionDto
import com.kickscore.live.data.api.StatisticsDto
import com.kickscore.live.data.dto.MatchDto
import com.kickscore.live.domain.repository.MatchDetailRepository
import com.kickscore.live.domain.util.Resource
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

    private val _lineupsState = MutableStateFlow<Resource<List<LineupDto>>>(Resource.Loading())
    val lineupsState: StateFlow<Resource<List<LineupDto>>> = _lineupsState.asStateFlow()

    private val _statisticsState = MutableStateFlow<Resource<List<StatisticsDto>>>(Resource.Loading())
    val statisticsState: StateFlow<Resource<List<StatisticsDto>>> = _statisticsState.asStateFlow()

    private val _eventsState = MutableStateFlow<Resource<List<EventDto>>>(Resource.Loading())
    val eventsState: StateFlow<Resource<List<EventDto>>> = _eventsState.asStateFlow()

    private val _headToHeadState = MutableStateFlow<Resource<List<MatchDto>>>(Resource.Loading())
    val headToHeadState: StateFlow<Resource<List<MatchDto>>> = _headToHeadState.asStateFlow()

    private val _leagueMatchesState = MutableStateFlow<Resource<List<MatchDto>>>(Resource.Loading())
    val leagueMatchesState: StateFlow<Resource<List<MatchDto>>> = _leagueMatchesState.asStateFlow()

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