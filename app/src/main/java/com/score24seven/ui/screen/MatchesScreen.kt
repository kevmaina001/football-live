/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.score24seven.domain.model.LeagueInfo
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.getTimeDisplay
import com.score24seven.domain.model.isLive
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.MatchesViewModel
import com.score24seven.ui.viewmodel.MatchesState
import com.score24seven.ui.components.CountryFlag
import com.score24seven.ui.components.TeamLogo
import com.score24seven.ads.BannerAdManager
import com.score24seven.ads.NativeAdManager
import com.score24seven.ui.components.LeagueLogo
import com.score24seven.ui.components.ModernMatchCard
import com.score24seven.util.Config
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMatchDetail: (Int) -> Unit,
    viewModel: MatchesViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_myplaces), // Football icon placeholder
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Football",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(android.R.drawable.arrow_down_float),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = onNavigateToSearch) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_search),
                        contentDescription = "Search"
                    )
                }
                IconButton(onClick = { /* Profile */ }) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_myplaces),
                        contentDescription = "Profile"
                    )
                }
            }
        )

        // Date Navigation - Parent filtering: redirect to competitions when date changes
        FlashScoreDateNavigation(
            selectedDate = state.selectedDate,
            availableDates = viewModel.getAvailableDates(),
            onDateSelected = { date ->
                // IMPROVED: When date changes in competition view, navigate back to competitions with new date
                if (state.selectedLeagueId != null) {
                    println("📅 DEBUG: Date changed in competition view - navigating back to competitions with date: $date")
                    viewModel.selectLeague(null) // Clear selected league first
                    viewModel.loadMatchesForDate(date) // Load all matches for new date
                } else {
                    // Normal date filtering when in competitions view
                    viewModel.loadMatchesForDate(date)
                }
            },
            getDateDisplayName = viewModel::getShortDateDisplay
        )

        // All Games Header
        AllGamesHeader(
            totalGames = if (state.selectedLeagueId != null) {
                getMatchCountForLeague(state.matches, state.selectedLeagueId!!)
            } else {
                state.totalMatchCount
            },
            liveGames = if (state.selectedLeagueId != null) {
                getLiveCountForLeague(state.matches, state.selectedLeagueId!!)
            } else {
                state.liveMatchCount
            }
        )

        // Show matches if a league is selected, otherwise show competitions
        if (state.selectedLeagueId != null) {
            // Show matches for selected league
            CompetitionMatchesView(
                matches = state.matches,
                selectedLeague = state.selectedLeagueId!!,
                selectedDate = state.selectedDate,
                onBackClick = {
                    viewModel.selectLeague(null)
                    viewModel.loadMatchesForDate(state.selectedDate)
                },
                onMatchClick = onNavigateToMatchDetail,
                onToggleFavorite = { matchId -> viewModel.toggleFavorite(matchId) }
            )
        } else {
            // Show competitions list
            CompetitionsListView(
                state = state,
                viewModel = viewModel,
                onNavigateToMatchDetail = onNavigateToMatchDetail
            )
        }
    }
}

// FlashScore-style Date Navigation Component
@Composable
fun FlashScoreDateNavigation(
    selectedDate: LocalDate,
    availableDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    getDateDisplayName: (LocalDate) -> String
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(availableDates) { date ->
            FlashScoreDateItem(
                date = date,
                displayName = getDateDisplayName(date),
                isSelected = date == selectedDate,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun FlashScoreDateItem(
    date: LocalDate,
    displayName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

// All Games Header Component
@Composable
fun AllGamesHeader(
    totalGames: Int,
    liveGames: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "All games",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (liveGames > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFFE53E3E), CircleShape)
                    )
                    Text(
                        text = "$liveGames",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53E3E)
                    )
                }
            }

            Text(
                text = "$totalGames",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// FlashScore-style Competition Item
@Composable
fun FlashScoreCompetitionItem(
    league: LeagueInfo,
    matchCount: Int,
    liveCount: Int,
    onClick: () -> Unit,
    isFavorite: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (isFavorite) {
                // Favorite competitions: Show league logo with small country flag overlay
                Box {
                    LeagueLogo(
                        logoUrl = league.league.logo,
                        leagueName = league.league.name,
                        size = 32.dp
                    )

                    // Small country flag overlay in bottom-right corner
                    CountryFlag(
                        flagUrl = league.country.flag,
                        countryName = league.country.name,
                        size = 12.dp,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            } else {
                // Other competitions: Show country flag for easy navigation
                CountryFlag(
                    flagUrl = league.country.flag,
                    countryName = league.country.name,
                    size = 28.dp
                )
            }

            Column {
                Text(
                    text = league.league.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = league.country.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (liveCount > 0) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFE53E3E),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$liveCount",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (matchCount > 0) {
                Text(
                    text = "$matchCount",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Helper functions for match counting
fun getMatchCountForLeague(matchesState: UiState<List<Match>>, leagueId: Int): Int {
    return if (matchesState is UiState.Success) {
        matchesState.data.count { it.league.id == leagueId }
    } else {
        0
    }
}

fun getLiveCountForLeague(matchesState: UiState<List<Match>>, leagueId: Int): Int {
    return if (matchesState is UiState.Success) {
        matchesState.data.count { it.league.id == leagueId && it.isLive() }
    } else {
        0
    }
}

// Competition Matches View
@Composable
fun CompetitionMatchesView(
    matches: UiState<List<Match>>,
    selectedLeague: Int,
    selectedDate: LocalDate,
    onBackClick: () -> Unit,
    onMatchClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Back button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBackClick() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_menu_revert),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Competitions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Banner Ad 1
        item {
            BannerAdManager.BannerAdView()
        }
        // Native Ad
        item {
            NativeAdManager.SimpleNativeAdCard()
        }


        when (matches) {
            is UiState.Loading -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is UiState.Success -> {
                val leagueMatches = matches.data.filter { it.league.id == selectedLeague }
                if (leagueMatches.isNotEmpty()) {
                    items(leagueMatches) { match ->
                        ModernMatchCard(
                            match = match,
                            onClick = { onMatchClick(match.id) },
                            onLiveClick = { onMatchClick(match.id) },
                            onFavoriteClick = { onToggleFavorite(match.id) }
                        )
                    }
                } else {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No matches for this competition on selected date",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            is UiState.Error -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Error loading matches",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = matches.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onBackClick) {
                                Text("Back to Competitions")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Competitions List View
@Composable
fun CompetitionsListView(
    state: MatchesState,
    viewModel: MatchesViewModel,
    onNavigateToMatchDetail: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Favorite Competitions Section
        item {
            Text(
                text = "FAVOURITE COMPETITIONS",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
            )
        }

        when (val favoriteLeagues = state.favoriteLeagues) {
            is UiState.Success<List<LeagueInfo>> -> {
                // Filter to only show competitions with matches > 0
                val leaguesWithMatches = favoriteLeagues.data.filter { league ->
                    getMatchCountForLeague(state.matches, league.league.id) > 0
                }

                items(leaguesWithMatches) { league ->
                    FlashScoreCompetitionItem(
                        league = league,
                        matchCount = getMatchCountForLeague(state.matches, league.league.id),
                        liveCount = getLiveCountForLeague(state.matches, league.league.id),
                        isFavorite = true, // This is in the favorite competitions section
                        onClick = {
                            viewModel.selectLeague(league.league.id)
                            viewModel.loadMatchesForLeague(league.league.id, state.selectedDate)
                        }
                    )
                }
            }
            is UiState.Error -> {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "⚠️ Failed to load competitions",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = favoriteLeagues.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            is UiState.Loading -> {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }
        }

        // Other Competitions Section
        item {
            Text(
                text = "OTHER COMPETITIONS [A-Z]",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
            )
        }

        when (val allLeagues = state.allLeagues) {
            is UiState.Success<List<LeagueInfo>> -> {
                val otherLeagues = allLeagues.data.filter { league ->
                    !Config.PRIORITY_LEAGUE_IDS.contains(league.league.id) &&
                    getMatchCountForLeague(state.matches, league.league.id) > 0  // Only show if matches > 0
                }

                // Group leagues by country
                val leaguesByCountry = otherLeagues.groupBy { it.country.name }
                    .toSortedMap() // Sort countries alphabetically

                leaguesByCountry.forEach { (countryName, countryLeagues) ->
                    val isExpanded = state.expandedCountries.contains(countryName)

                    // Country header
                    item {
                        CountryHeader(
                            countryName = countryName,
                            countryFlag = countryLeagues.first().country.flag,
                            isExpanded = isExpanded,
                            leagueCount = countryLeagues.size,
                            onClick = { viewModel.toggleCountryExpansion(countryName) }
                        )
                    }

                    // Leagues in this country (only show if expanded)
                    if (isExpanded) {
                        items(countryLeagues.sortedBy { it.league.name }) { league ->
                            CompetitionItemWithLogo(
                                league = league,
                                matchCount = getMatchCountForLeague(state.matches, league.league.id),
                                liveCount = getLiveCountForLeague(state.matches, league.league.id),
                                onClick = {
                                    viewModel.selectLeague(league.league.id)
                                    viewModel.loadMatchesForLeague(league.league.id, state.selectedDate)
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                // Don't show loading for other competitions to avoid clutter
            }
        }
    }
}

// Country Header Component for grouped competitions
@Composable
fun CountryHeader(
    countryName: String,
    countryFlag: String?,
    isExpanded: Boolean,
    leagueCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            CountryFlag(
                flagUrl = countryFlag,
                countryName = countryName,
                size = 24.dp
            )

            Text(
                text = countryName.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "($leagueCount)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Expand/Collapse Icon
        Icon(
            painter = painterResource(
                if (isExpanded) android.R.drawable.arrow_up_float
                else android.R.drawable.arrow_down_float
            ),
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

// Competition Item with League Logo (for country-grouped sections)
@Composable
fun CompetitionItemWithLogo(
    league: LeagueInfo,
    matchCount: Int,
    liveCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 32.dp, vertical = 8.dp), // Indented under country
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            LeagueLogo(
                logoUrl = league.league.logo,
                leagueName = league.league.name,
                size = 28.dp
            )

            Text(
                text = league.league.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (liveCount > 0) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFE53E3E),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$liveCount",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (matchCount > 0) {
                Text(
                    text = "$matchCount",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DateFilterSection(
    selectedDate: LocalDate,
    availableDates: List<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    getDateDisplayName: (LocalDate) -> String
) {
    Column {
        Text(
            text = "📅 Select Date",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableDates) { date ->
                DateChip(
                    date = date,
                    displayName = getDateDisplayName(date),
                    isSelected = date == selectedDate,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
fun DateChip(
    date: LocalDate,
    displayName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = displayName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun FavoriteCompetitionsSection(
    favoriteLeaguesState: UiState<List<LeagueInfo>>,
    selectedLeagueId: Int?,
    selectedDate: LocalDate,
    onLeagueSelected: (Int?) -> Unit
) {
    Column {
        Text(
            text = "🏆 Favorite Competitions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (favoriteLeaguesState) {
            is UiState.Loading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
            is UiState.Success -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // "All" option
                    item {
                        CompetitionChip(
                            name = "All",
                            isSelected = selectedLeagueId == null,
                            onClick = { onLeagueSelected(null) }
                        )
                    }

                    items(favoriteLeaguesState.data) { league ->
                        CompetitionChip(
                            name = league.league.name,
                            isSelected = selectedLeagueId == league.league.id,
                            onClick = { onLeagueSelected(league.league.id) }
                        )
                    }
                }
            }
            is UiState.Error -> {
                Text(
                    text = "Failed to load competitions",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CompetitionChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun MatchesListSection(
    matchesState: UiState<List<Match>>,
    selectedLeagueId: Int?,
    onMatchClicked: (Int) -> Unit,
    onRetry: () -> Unit
) {
    when (matchesState) {
        is UiState.Loading -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            val matches = matchesState.data
            if (matches.isNotEmpty()) {
                if (selectedLeagueId == null) {
                    // Group by league when showing all matches
                    val groupedMatches = matches.groupBy { it.league.name }
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        groupedMatches.forEach { (leagueName, leagueMatches) ->
                            LeagueSection(
                                leagueName = leagueName,
                                matches = leagueMatches,
                                onMatchClicked = onMatchClicked
                            )
                        }
                    }
                } else {
                    // Show matches directly when a specific league is selected
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        matches.forEach { match ->
                            MatchCard(
                                match = match,
                                onClick = { onMatchClicked(match.id) }
                            )
                        }
                    }
                }
            } else {
                EmptyMatchesMessage(selectedLeagueId != null)
            }
        }
        is UiState.Error -> {
            ErrorMessage(
                message = matchesState.message,
                onRetry = onRetry
            )
        }
    }
}

@Composable
fun LeagueSection(
    leagueName: String,
    matches: List<Match>,
    onMatchClicked: (Int) -> Unit
) {
    Column {
        Text(
            text = "🏆 $leagueName",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            matches.forEach { match ->
                MatchCard(
                    match = match,
                    onClick = { onMatchClicked(match.id) }
                )
            }
        }
    }
}

@Composable
fun MatchCard(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Status and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (match.isLive()) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFE53E3E),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = match.status.short,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = match.getTimeDisplay(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Match details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team with logo
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    TeamLogo(
                        logoUrl = match.homeTeam.logo,
                        teamName = match.homeTeam.name,
                        size = 24.dp
                    )
                    Text(
                        text = match.homeTeam.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Score
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.score.home?.toString() ?: "-",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = match.score.away?.toString() ?: "-",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Away team with logo
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = match.awayTeam.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                    TeamLogo(
                        logoUrl = match.awayTeam.logo,
                        teamName = match.awayTeam.name,
                        size = 24.dp
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMatchesMessage(isLeagueSelected: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isLeagueSelected)
                    "No matches for this competition on selected date"
                else
                    "No matches scheduled for selected date",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️ Error loading matches",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}