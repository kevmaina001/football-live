/*
 * Rate limiting interceptor to prevent API 429 errors
 */

package com.score24seven.data.api

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateLimitingInterceptor @Inject constructor() : Interceptor {

    private var lastRequestTime = 0L
    private val minRequestInterval = 2000L // Minimum 2 seconds between requests

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentTime = System.currentTimeMillis()
        val timeSinceLastRequest = currentTime - lastRequestTime

        if (timeSinceLastRequest < minRequestInterval) {
            val delay = minRequestInterval - timeSinceLastRequest
            println("🟡 RateLimiting: Delaying request by ${delay}ms to prevent rate limiting")
            Thread.sleep(delay)
        }

        lastRequestTime = System.currentTimeMillis()

        val response = chain.proceed(chain.request())

        // If we get a 429, add additional delay before next request
        if (response.code == 429) {
            println("🔴 RateLimiting: Received 429, setting longer delay for next request")
            lastRequestTime = System.currentTimeMillis() + 10000 // Add 10 second penalty
        }

        return response
    }
}