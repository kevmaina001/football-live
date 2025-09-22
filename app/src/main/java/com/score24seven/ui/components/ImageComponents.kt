/*
 * Image loading components for Score24Seven
 * Handles team logos, country flags, and league logos with Coil
 */

package com.score24seven.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size

/**
 * Team logo component with fallback to team initials
 */
@Composable
fun TeamLogo(
    logoUrl: String?,
    teamName: String,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (!logoUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = contentDescription ?: "$teamName logo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                error = painterResource(android.R.drawable.ic_menu_gallery)
            )
        } else {
            TeamInitialsPlaceholder(
                teamName = teamName,
                size = size
            )
        }
    }
}

/**
 * Country flag component with fallback to country code
 */
/**
 * Get fallback flag URL from country name using flagcdn.com service
 */
private fun getFallbackFlagUrl(countryName: String): String? {
    val countryCode = when (countryName.lowercase()) {
        "england" -> "gb-eng"
        "spain" -> "es"
        "germany" -> "de"
        "italy" -> "it"
        "france" -> "fr"
        "netherlands" -> "nl"
        "portugal" -> "pt"
        "slovenia" -> "si"
        "croatia" -> "hr"
        "belgium" -> "be"
        "austria" -> "at"
        "switzerland" -> "ch"
        "poland" -> "pl"
        "czech republic" -> "cz"
        "slovakia" -> "sk"
        "hungary" -> "hu"
        "romania" -> "ro"
        "bulgaria" -> "bg"
        "serbia" -> "rs"
        "bosnia and herzegovina" -> "ba"
        "montenegro" -> "me"
        "north macedonia" -> "mk"
        "albania" -> "al"
        "greece" -> "gr"
        "turkey" -> "tr"
        "ukraine" -> "ua"
        "russia" -> "ru"
        "belarus" -> "by"
        "lithuania" -> "lt"
        "latvia" -> "lv"
        "estonia" -> "ee"
        "finland" -> "fi"
        "sweden" -> "se"
        "norway" -> "no"
        "denmark" -> "dk"
        "iceland" -> "is"
        "ireland" -> "ie"
        "scotland" -> "gb-sct"
        "wales" -> "gb-wls"
        "northern ireland" -> "gb-nir"
        "united states", "usa" -> "us"
        "canada" -> "ca"
        "mexico" -> "mx"
        "brazil" -> "br"
        "argentina" -> "ar"
        "chile" -> "cl"
        "uruguay" -> "uy"
        "colombia" -> "co"
        "peru" -> "pe"
        "ecuador" -> "ec"
        "bolivia" -> "bo"
        "paraguay" -> "py"
        "venezuela" -> "ve"
        else -> null
    }

    return countryCode?.let { "https://flagcdn.com/w40/$it.png" }
}

@Composable
fun CountryFlag(
    flagUrl: String?,
    countryName: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    shape: Shape = RoundedCornerShape(4.dp),
    contentDescription: String? = null
) {
    val effectiveFlagUrl = if (!flagUrl.isNullOrBlank()) {
        println("🏳️ DEBUG: Using API flag URL for '$countryName': '$flagUrl'")
        flagUrl
    } else {
        val fallbackUrl = getFallbackFlagUrl(countryName)
        println("🏳️ DEBUG: API flag URL is empty for '$countryName', using fallback: '$fallbackUrl'")
        fallbackUrl
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (!effectiveFlagUrl.isNullOrBlank()) {
            println("🏳️ DEBUG: Loading flag for '$countryName' from URL: '$effectiveFlagUrl'")

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(effectiveFlagUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .listener(
                        onStart = {
                            println("🏳️ DEBUG: Started loading flag for $countryName")
                        },
                        onSuccess = { _, result ->
                            println("🏳️ DEBUG: Successfully loaded flag for $countryName")
                        },
                        onError = { _, result ->
                            println("🔴 DEBUG: Failed to load flag for $countryName: ${result.throwable.message}")
                            println("🔴 DEBUG: Flag URL was: $effectiveFlagUrl")
                        }
                    )
                    .build(),
                contentDescription = contentDescription ?: "$countryName flag",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
                contentScale = ContentScale.Crop,
                error = painterResource(android.R.drawable.ic_menu_mapmode)
            )
        } else {
            println("🔴 DEBUG: No flag URL available for '$countryName', showing country code placeholder")
            CountryCodePlaceholder(
                countryName = countryName,
                size = size,
                shape = shape
            )
        }
    }
}

/**
 * League logo component with fallback to league initials
 */
/**
 * Get fallback logo URL for major competitions
 */
private fun getFallbackLeagueLogoUrl(leagueName: String): String? {
    return when (leagueName.lowercase()) {
        "premier league" -> "https://logos-world.net/wp-content/uploads/2020/06/Premier-League-Logo.png"
        "uefa champions league", "champions league" -> "https://logos-world.net/wp-content/uploads/2020/06/UEFA-Champions-League-Logo.png"
        "uefa europa league", "europa league" -> "https://logos-world.net/wp-content/uploads/2020/06/UEFA-Europa-League-Logo.png"
        "major league soccer", "mls" -> "https://logos-world.net/wp-content/uploads/2020/06/MLS-Logo.png"
        "fa cup" -> "https://logos-world.net/wp-content/uploads/2020/06/FA-Cup-Logo.png"
        "la liga" -> "https://logos-world.net/wp-content/uploads/2020/06/La-Liga-Logo.png"
        "bundesliga" -> "https://logos-world.net/wp-content/uploads/2020/06/Bundesliga-Logo.png"
        "serie a" -> "https://logos-world.net/wp-content/uploads/2020/06/Serie-A-Logo.png"
        "ligue 1" -> "https://logos-world.net/wp-content/uploads/2020/06/Ligue-1-Logo.png"
        else -> null
    }
}

@Composable
fun LeagueLogo(
    logoUrl: String?,
    leagueName: String,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    contentDescription: String? = null
) {
    val effectiveLogoUrl = if (!logoUrl.isNullOrBlank()) {
        println("🏆 DEBUG: Using API logo URL for '$leagueName': '$logoUrl'")
        logoUrl
    } else {
        val fallbackUrl = getFallbackLeagueLogoUrl(leagueName)
        println("🏆 DEBUG: API logo URL is empty for '$leagueName', using fallback: '$fallbackUrl'")
        fallbackUrl
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (!effectiveLogoUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(effectiveLogoUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .listener(
                        onStart = {
                            println("🏆 DEBUG: Started loading logo for $leagueName")
                        },
                        onSuccess = { _, _ ->
                            println("🏆 DEBUG: Successfully loaded logo for $leagueName")
                        },
                        onError = { _, result ->
                            println("🔴 DEBUG: Failed to load logo for $leagueName: ${result.throwable.message}")
                            println("🔴 DEBUG: Logo URL was: $effectiveLogoUrl")
                        }
                    )
                    .build(),
                contentDescription = contentDescription ?: "$leagueName logo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                error = painterResource(android.R.drawable.ic_menu_info_details)
            )
        } else {
            println("🔴 DEBUG: No logo URL available for '$leagueName', showing initials placeholder")
            LeagueInitialsPlaceholder(
                leagueName = leagueName,
                size = size
            )
        }
    }
}

/**
 * Team initials placeholder when logo is unavailable
 */
@Composable
private fun TeamInitialsPlaceholder(
    teamName: String,
    size: Dp
) {
    val initials = teamName.split(" ")
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")
        .ifEmpty { teamName.take(2).uppercase() }

    Box(
        modifier = Modifier
            .size(size)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = when {
                size <= 24.dp -> MaterialTheme.typography.labelSmall
                size <= 32.dp -> MaterialTheme.typography.labelMedium
                else -> MaterialTheme.typography.labelLarge
            },
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Country code placeholder when flag is unavailable
 */
@Composable
private fun CountryCodePlaceholder(
    countryName: String,
    size: Dp,
    shape: Shape
) {
    // Use proper country codes or first 2 letters
    val countryCode = when (countryName.lowercase()) {
        "england" -> "EN"
        "spain" -> "ES"
        "germany" -> "DE"
        "italy" -> "IT"
        "france" -> "FR"
        "netherlands" -> "NL"
        "portugal" -> "PT"
        "slovenia" -> "SI"
        "croatia" -> "HR"
        "belgium" -> "BE"
        "austria" -> "AT"
        "switzerland" -> "CH"
        "poland" -> "PL"
        "czech republic" -> "CZ"
        "slovakia" -> "SK"
        "hungary" -> "HU"
        "romania" -> "RO"
        "bulgaria" -> "BG"
        "serbia" -> "RS"
        "bosnia and herzegovina" -> "BA"
        "montenegro" -> "ME"
        "north macedonia" -> "MK"
        "albania" -> "AL"
        "greece" -> "GR"
        "turkey" -> "TR"
        "ukraine" -> "UA"
        "russia" -> "RU"
        "belarus" -> "BY"
        "lithuania" -> "LT"
        "latvia" -> "LV"
        "estonia" -> "EE"
        "finland" -> "FI"
        "sweden" -> "SE"
        "norway" -> "NO"
        "denmark" -> "DK"
        "iceland" -> "IS"
        "ireland" -> "IE"
        "scotland" -> "SC"
        "wales" -> "WA"
        "northern ireland" -> "NI"
        "united states", "usa" -> "US"
        "canada" -> "CA"
        "mexico" -> "MX"
        "brazil" -> "BR"
        "argentina" -> "AR"
        "chile" -> "CL"
        "uruguay" -> "UY"
        "colombia" -> "CO"
        "peru" -> "PE"
        "ecuador" -> "EC"
        "bolivia" -> "BO"
        "paraguay" -> "PY"
        "venezuela" -> "VE"
        else -> countryName.take(2).uppercase()
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countryCode,
            style = when {
                size <= 20.dp -> MaterialTheme.typography.labelSmall
                size <= 28.dp -> MaterialTheme.typography.labelMedium
                else -> MaterialTheme.typography.labelLarge
            },
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * League initials placeholder when logo is unavailable
 */
@Composable
private fun LeagueInitialsPlaceholder(
    leagueName: String,
    size: Dp
) {
    val initials = leagueName.split(" ")
        .take(2)
        .map { it.first().uppercase() }
        .joinToString("")
        .ifEmpty { leagueName.take(2).uppercase() }

    Box(
        modifier = Modifier
            .size(size)
            .background(
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = when {
                size <= 24.dp -> MaterialTheme.typography.labelSmall
                size <= 32.dp -> MaterialTheme.typography.labelMedium
                else -> MaterialTheme.typography.labelLarge
            },
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Combined team logo with country flag for team display
 */
@Composable
fun TeamLogoWithFlag(
    teamLogoUrl: String?,
    teamName: String,
    countryFlagUrl: String?,
    countryName: String?,
    modifier: Modifier = Modifier,
    logoSize: Dp = 32.dp,
    flagSize: Dp = 16.dp
) {
    Box(modifier = modifier) {
        TeamLogo(
            logoUrl = teamLogoUrl,
            teamName = teamName,
            size = logoSize
        )

        if (!countryName.isNullOrBlank()) {
            CountryFlag(
                flagUrl = countryFlagUrl,
                countryName = countryName,
                size = flagSize,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

/**
 * Loading placeholder for images
 */
@Composable
fun ImageLoadingPlaceholder(
    size: Dp,
    shape: Shape = CircleShape
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size * 0.4f),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}