/*
 * API Cache Manager to reduce API calls and prevent rate limiting
 */

package com.kickscore.live.data.cache

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiCacheManager @Inject constructor() {

    private val cache = mutableMapOf<String, CacheEntry>()

    data class CacheEntry(
        val timestamp: Long,
        val data: Any
    )

    companion object {
        private const val LIVE_MATCHES_CACHE_KEY = "live_matches"
        private const val TODAY_MATCHES_CACHE_KEY = "today_matches"

        // Cache durations in milliseconds
        private const val LIVE_MATCHES_CACHE_DURATION = 30_000L // 30 seconds for live matches
        private const val TODAY_MATCHES_CACHE_DURATION = 300_000L // 5 minutes for today's matches
    }

    fun shouldFetchLiveMatches(): Boolean {
        return shouldFetch(LIVE_MATCHES_CACHE_KEY, LIVE_MATCHES_CACHE_DURATION)
    }

    fun shouldFetchTodayMatches(): Boolean {
        return shouldFetch(TODAY_MATCHES_CACHE_KEY, TODAY_MATCHES_CACHE_DURATION)
    }

    fun markLiveMatchesFetched() {
        markFetched(LIVE_MATCHES_CACHE_KEY)
    }

    fun markTodayMatchesFetched() {
        markFetched(TODAY_MATCHES_CACHE_KEY)
    }

    private fun shouldFetch(key: String, cacheDuration: Long): Boolean {
        val entry = cache[key]
        if (entry == null) {
            println("🟡 Cache: No cache entry for $key, should fetch")
            return true
        }

        val currentTime = System.currentTimeMillis()
        val isExpired = (currentTime - entry.timestamp) > cacheDuration

        if (isExpired) {
            println("🟡 Cache: Cache expired for $key (age: ${currentTime - entry.timestamp}ms), should fetch")
        } else {
            println("🟡 Cache: Cache valid for $key (age: ${currentTime - entry.timestamp}ms), skip fetch")
        }

        return isExpired
    }

    private fun markFetched(key: String) {
        cache[key] = CacheEntry(
            timestamp = System.currentTimeMillis(),
            data = Unit // We don't need to store data, just timestamp
        )
        println("🟡 Cache: Marked $key as fetched at ${System.currentTimeMillis()}")
    }

    fun clearCache() {
        cache.clear()
        println("🟡 Cache: Cleared all cache entries")
    }
}