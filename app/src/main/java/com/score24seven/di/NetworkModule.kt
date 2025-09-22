/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.score24seven.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.score24seven.data.api.FootballApiService
import com.score24seven.data.api.RateLimitingInterceptor
import com.score24seven.util.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()
    }

    @Provides
    @Singleton
    @Named("ApiKeyInterceptor")
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("X-RapidAPI-Key", Config.API_KEY)
                .header("X-RapidAPI-Host", Config.RAPIDAPI_HOST)
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideCustomDns(): Dns {
        return object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                return try {
                    // Try default DNS first
                    val defaultResults = Dns.SYSTEM.lookup(hostname)
                    if (defaultResults.isNotEmpty()) {
                        println("🟡 DEBUG: DNS resolved $hostname via system DNS")
                        defaultResults
                    } else {
                        throw Exception("No addresses found via system DNS")
                    }
                } catch (e: Exception) {
                    println("🔴 DEBUG: System DNS failed for $hostname: ${e.message}")
                    try {
                        // Fallback to manual IP resolution for known hosts
                        when (hostname) {
                            Config.RAPIDAPI_HOST -> {
                                println("🟡 DEBUG: Using fallback IP for RapidAPI")
                                // These are example IPs - in production you'd want to resolve these properly
                                listOf(
                                    InetAddress.getByName("104.18.36.231"),
                                    InetAddress.getByName("104.18.37.231")
                                )
                            }
                            else -> {
                                println("🔴 DEBUG: No fallback available for $hostname")
                                throw e
                            }
                        }
                    } catch (fallbackError: Exception) {
                        println("🔴 DEBUG: Fallback DNS also failed: ${fallbackError.message}")
                        throw e
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("ApiKeyInterceptor") apiKeyInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        rateLimitingInterceptor: RateLimitingInterceptor,
        customDns: Dns
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(rateLimitingInterceptor) // Add rate limiting first
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .dns(customDns) // Add custom DNS resolver
            .connectTimeout(15, TimeUnit.SECONDS) // Reduced timeout for faster failover
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // Enable automatic retry
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FootballApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideFootballApiService(retrofit: Retrofit): FootballApiService {
        return retrofit.create(FootballApiService::class.java)
    }
}