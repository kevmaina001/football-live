/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import com.score24seven.R
import com.score24seven.domain.model.Match
import com.score24seven.domain.model.getTimeDisplay
import com.score24seven.domain.model.isLive
import com.score24seven.ui.state.HomeScreenAction
import com.score24seven.ui.state.HomeScreenEffect
import com.score24seven.ui.state.UiState
import com.score24seven.ui.viewmodel.HomeViewModel
import com.score24seven.ui.components.TeamLogo
import com.score24seven.ui.components.FavoriteMatchesSection
import com.score24seven.ui.navigation.Screen
import com.score24seven.util.Config

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToMatchDetail: (Int) -> Unit,
    navController: NavHostController? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Handle effects
    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is HomeScreenEffect.NavigateToMatchDetail -> {
                    onNavigateToMatchDetail(effect.matchId)
                }
                is HomeScreenEffect.ShowError -> {
                    // TODO: Show error snackbar
                }
                is HomeScreenEffect.ShowSnackbar -> {
                    // TODO: Show snackbar
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Section
        item {
            HeroSection(
                onRefresh = { viewModel.handleAction(HomeScreenAction.RefreshData) }
            )
        }

        // Quick Access Dashboard
        item {
            QuickAccessDashboard(navController = navController)
        }

        // Stats Overview
        item {
            StatsOverview(
                liveMatchesCount = when (val liveState = state.liveMatches) {
                    is UiState.Success -> liveState.data.size
                    else -> 0
                },
                todayMatchesCount = when (val todayState = state.todayMatches) {
                    is UiState.Success -> todayState.data.size
                    else -> 0
                },
                favoritesCount = when (val favoriteState = state.favoriteMatches) {
                    is UiState.Success -> favoriteState.data.size
                    else -> 0
                },
                navController = navController
            )
        }

        // Favorite Matches Section
        when (val favoriteMatchesState = state.favoriteMatches) {
            is UiState.Loading -> {
                item {
                    LoadingCard("Loading favorite matches...")
                }
            }
            is UiState.Success -> {
                val favoriteMatches = favoriteMatchesState.data
                if (favoriteMatches.isNotEmpty()) {
                    item {
                        FavoriteMatchesSection(
                            matches = favoriteMatches,
                            onMatchClick = onNavigateToMatchDetail,
                            onToggleFavorite = { matchId ->
                                viewModel.handleAction(HomeScreenAction.ToggleMatchFavorite(matchId))
                            }
                        )
                    }
                }
            }
            is UiState.Error -> {
                item {
                    ErrorCard(
                        title = "Favorite Matches Unavailable",
                        message = favoriteMatchesState.message ?: "Unable to load favorite matches",
                        onRetry = { viewModel.handleAction(HomeScreenAction.RefreshData) }
                    )
                }
            }
        }

        // Live Matches Carousel
        when (val liveMatchesState = state.liveMatches) {
            is UiState.Loading -> {
                item {
                    LoadingCard("Loading live matches...")
                }
            }
            is UiState.Success -> {
                val liveMatches = liveMatchesState.data
                if (liveMatches.isNotEmpty()) {
                    item {
                        LiveMatchesCarousel(
                            matches = liveMatches,
                            onMatchClick = onNavigateToMatchDetail
                        )
                    }
                }
            }
            is UiState.Error -> {
                item {
                    ErrorCard(
                        title = "Live Matches Unavailable",
                        message = liveMatchesState.message ?: "Unable to load live matches",
                        onRetry = { viewModel.handleAction(HomeScreenAction.RefreshData) }
                    )
                }
            }
        }

        // Featured Matches Preview
        when (val todayMatchesState = state.todayMatches) {
            is UiState.Loading -> {
                item {
                    LoadingCard("Loading today's highlights...")
                }
            }
            is UiState.Success -> {
                val todayMatches = todayMatchesState.data
                if (todayMatches.isNotEmpty()) {
                    item {
                        FeaturedMatchesPreview(
                            matches = filterFavoriteLeagueMatches(todayMatches).take(3), // Show top 3 matches from favorite leagues
                            onMatchClick = onNavigateToMatchDetail,
                            navController = navController // Add navigation support
                        )
                    }
                }
            }
            is UiState.Error -> {
                item {
                    ErrorCard(
                        title = "Today's Matches Unavailable",
                        message = todayMatchesState.message ?: "Unable to load today's matches",
                        onRetry = { viewModel.handleAction(HomeScreenAction.RefreshData) }
                    )
                }
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Hero Section with football images and modern design
@Composable
fun HeroSection(
    onRefresh: () -> Unit
) {
    val footballImages = listOf(
        "https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?w=800&h=400&fit=crop",
        "https://images.unsplash.com/photo-1553778263-73a83bab9b0c?w=800&h=400&fit=crop",
        "https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=800&h=400&fit=crop",
        "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=800&h=400&fit=crop",
        "https://images.unsplash.com/photo-1526232761682-d26e85d9d53e?w=800&h=400&fit=crop",
        "https://images.unsplash.com/photo-1522778119026-d647f0596c20?w=800&h=400&fit=crop"
    )

    val currentImageIndex = remember { mutableStateOf(0) }

    // Auto-rotate images every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            currentImageIndex.value = (currentImageIndex.value + 1) % footballImages.size
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image
            AsyncImage(
                model = footballImages[currentImageIndex.value],
                contentDescription = "Football background",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.score24seven_logo),
                            contentDescription = "Score24Seven Logo",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Column {
                            Text(
                                text = "Score24Seven",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Your Ultimate Football Hub",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Image indicators
                        footballImages.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (index == currentImageIndex.value) Color.White
                                        else Color.White.copy(alpha = 0.4f),
                                        CircleShape
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = "Live scores • Fixtures • Standings",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Follow your favorite teams and leagues",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Quick Access Dashboard with navigation tiles
@Composable
fun QuickAccessDashboard(
    navController: NavHostController? = null
) {
    Column {
        Text(
            text = "🚀 Quick Access",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(
                listOf(
                    QuickAccessItem("Live TV", Icons.Default.PlayArrow, Color(0xFFE53E3E), Screen.LiveTV.route),
                    QuickAccessItem("Leagues", Icons.Default.List, Color(0xFF2E7D32), Screen.Leagues.route),
                    QuickAccessItem("Matches", Icons.Default.DateRange, Color(0xFF1976D2), Screen.Matches.route),
                    QuickAccessItem("Settings", Icons.Default.Settings, Color(0xFF7B1FA2), Screen.Settings.route)
                )
            ) { item ->
                QuickAccessCard(
                    item = item,
                    onClick = {
                        navController?.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    item: QuickAccessItem,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            item.color.copy(alpha = 0.1f),
                            item.color.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = item.color
            )
        }
    }
}

// Stats Overview with live data
@Composable
fun StatsOverview(
    liveMatchesCount: Int,
    todayMatchesCount: Int,
    favoritesCount: Int,
    navController: NavHostController? = null
) {
    Column {
        Text(
            text = "📊 Today's Overview",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatsCard(
                modifier = Modifier.weight(1f),
                title = "Live Now",
                value = liveMatchesCount.toString(),
                icon = Icons.Default.PlayArrow,
                color = Color(0xFFE53E3E),
                onClick = {
                    navController?.navigate(Screen.Matches.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            StatsCard(
                modifier = Modifier.weight(1f),
                title = "Favorites",
                value = favoritesCount.toString(),
                icon = Icons.Default.Favorite,
                color = Color(0xFFE91E63),
                onClick = null
            )
            StatsCard(
                modifier = Modifier.weight(1f),
                title = "Today",
                value = todayMatchesCount.toString(),
                icon = Icons.Default.DateRange,
                color = Color(0xFF1976D2),
                onClick = {
                    navController?.navigate(Screen.Matches.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .let { cardModifier ->
                if (onClick != null) {
                    cardModifier.clickable { onClick() }
                } else {
                    cardModifier
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// Live Matches Carousel
@Composable
fun LiveMatchesCarousel(
    matches: List<Match>,
    onMatchClick: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔴 Live Matches",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53E3E)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFE53E3E), CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "LIVE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE53E3E)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(matches) { match ->
                CompactMatchCard(
                    match = match,
                    onClick = { onMatchClick(match.id) }
                )
            }
        }
    }
}


// Compact match card for carousel
@Composable
fun CompactMatchCard(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.league.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )

                if (match.isLive()) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE53E3E), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Teams and score with logos
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TeamLogo(
                            logoUrl = match.homeTeam.logo,
                            teamName = match.homeTeam.name,
                            size = 16.dp
                        )
                        Text(
                            text = match.homeTeam.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        text = match.score?.home?.toString() ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TeamLogo(
                            logoUrl = match.awayTeam.logo,
                            teamName = match.awayTeam.name,
                            size = 16.dp
                        )
                        Text(
                            text = match.awayTeam.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        text = match.score?.away?.toString() ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Featured match card
@Composable
fun FeaturedMatchCard(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = match.league.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TeamLogo(
                        logoUrl = match.homeTeam.logo,
                        teamName = match.homeTeam.name,
                        size = 20.dp
                    )
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TeamLogo(
                        logoUrl = match.awayTeam.logo,
                        teamName = match.awayTeam.name,
                        size = 20.dp
                    )
                    Text(
                        text = "${match.homeTeam.name} vs ${match.awayTeam.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                if (match.isLive()) {
                    Text(
                        text = "LIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFE53E3E),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = match.getTimeDisplay(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Loading card
@Composable
fun LoadingCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Error card
@Composable
fun ErrorCard(
    title: String,
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Retry")
            }
        }
    }
}

// Helper function to filter matches from favorite leagues
private fun filterFavoriteLeagueMatches(matches: List<Match>): List<Match> {
    return matches.filter { match ->
        Config.PRIORITY_LEAGUE_IDS.contains(match.league.id)
    }.ifEmpty {
        // If no favorite league matches, return original list
        matches
    }
}

// Data classes
data class QuickAccessItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@Composable
fun RealMatchCard(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // League and date
            Text(
                text = match.league.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                        text = match.score?.home?.toString() ?: "-",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = match.score?.away?.toString() ?: "-",
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

// Featured Matches Preview
@Composable
fun FeaturedMatchesPreview(
    matches: List<Match>,
    onMatchClick: (Int) -> Unit,
    navController: NavHostController? = null
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⭐ Featured Matches",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            TextButton(
                onClick = {
                    // NAVIGATION: Navigate to matches screen
                    navController?.navigate(Screen.Matches.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(300.dp) // Fixed height for preview
        ) {
            items(matches) { match ->
                CompactMatchCard(
                    match = match,
                    onClick = { onMatchClick(match.id) }
                )
            }
        }
    }
}