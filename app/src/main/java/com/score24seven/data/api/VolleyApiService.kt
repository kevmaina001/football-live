/*
 * Volley-based API service following working implementation
 */

package com.score24seven.data.api

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.score24seven.data.model.SimpleLeagueResponse
import com.score24seven.data.model.SimpleStandingsResponse
import com.score24seven.data.model.SimpleFixturesResponse
import com.score24seven.data.model.SimpleTopScorersResponse
import com.score24seven.util.Config
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class VolleyApiService(private val context: Context) {

    companion object {
        private const val BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/"
        private const val LEAGUES_ENDPOINT = "leagues"
        private const val STANDINGS_ENDPOINT = "standings"
        private const val FIXTURES_ENDPOINT = "fixtures"
        private const val PLAYERS_ENDPOINT = "players/topscorers"
    }

    private val gson = Gson()
    private val requestQueue = Volley.newRequestQueue(context)

    fun getCurrentLeagues(
        onSuccess: (SimpleLeagueResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${BASE_URL}${LEAGUES_ENDPOINT}?current=true"

        println("🔥 DEBUG: VolleyApiService - Fetching leagues from: $url")

        val request = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    println("🔥 DEBUG: VolleyApiService - Raw response: ${response.take(200)}...")
                    val leagueResponse = gson.fromJson(response, SimpleLeagueResponse::class.java)
                    println("🔥 DEBUG: VolleyApiService - Parsed ${leagueResponse.response?.size ?: 0} leagues")
                    onSuccess(leagueResponse)
                } catch (e: Exception) {
                    println("🔴 DEBUG: VolleyApiService - Parse error: ${e.message}")
                    onError("Failed to parse response: ${e.message}")
                }
            },
            { error ->
                val errorMsg = when {
                    error.networkResponse != null -> {
                        "HTTP ${error.networkResponse.statusCode}: ${String(error.networkResponse.data, Charset.defaultCharset())}"
                    }
                    error.message != null -> error.message!!
                    else -> "Unknown network error"
                }
                println("🔴 DEBUG: VolleyApiService - Network error: $errorMsg")
                onError(errorMsg)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-RapidAPI-Key"] = Config.API_KEY
                headers["X-RapidAPI-Host"] = Config.RAPIDAPI_HOST
                return headers
            }
        }

        requestQueue.add(request)
    }

    fun getLeagueStandings(
        leagueId: Int,
        season: Int,
        onSuccess: (SimpleStandingsResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${BASE_URL}${STANDINGS_ENDPOINT}?season=$season&league=$leagueId"

        println("🏆 DEBUG: VolleyApiService - Fetching standings from: $url")

        val request = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    println("🏆 DEBUG: VolleyApiService - Raw standings response (first 500 chars): ${response.take(500)}")
                    val standingsResponse = gson.fromJson(response, SimpleStandingsResponse::class.java)
                    println("🏆 DEBUG: VolleyApiService - Response object: ${standingsResponse}")
                    println("🏆 DEBUG: VolleyApiService - Results count: ${standingsResponse.results}")
                    println("🏆 DEBUG: VolleyApiService - Response list size: ${standingsResponse.response?.size}")
                    println("🏆 DEBUG: VolleyApiService - Errors: ${standingsResponse.errors}")

                    standingsResponse.response?.forEach { standingResponse ->
                        println("🏆 DEBUG: League: ${standingResponse.league?.name} (ID: ${standingResponse.league?.id})")
                        println("🏆 DEBUG: League season: ${standingResponse.league?.season}")
                        println("🏆 DEBUG: Standings groups: ${standingResponse.league?.standings?.size}")
                        standingResponse.league?.standings?.forEach { standingGroup ->
                            println("🏆 DEBUG: Standing group size: ${standingGroup.size}")
                        }
                    }

                    onSuccess(standingsResponse)
                } catch (e: Exception) {
                    println("🔴 DEBUG: VolleyApiService - Parse error: ${e.message}")
                    e.printStackTrace()
                    onError("Failed to parse standings: ${e.message}")
                }
            },
            { error ->
                val errorMsg = when {
                    error.networkResponse != null -> {
                        "HTTP ${error.networkResponse.statusCode}: ${String(error.networkResponse.data, Charset.defaultCharset())}"
                    }
                    error.message != null -> error.message!!
                    else -> "Unknown network error"
                }
                println("🔴 DEBUG: VolleyApiService - Network error: $errorMsg")
                onError(errorMsg)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-RapidAPI-Key"] = Config.API_KEY
                headers["X-RapidAPI-Host"] = Config.RAPIDAPI_HOST
                return headers
            }
        }

        requestQueue.add(request)
    }

    fun getLeagueFixtures(
        leagueId: Int,
        season: Int,
        onSuccess: (SimpleFixturesResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${BASE_URL}${FIXTURES_ENDPOINT}?season=$season&league=$leagueId"

        println("⚽ DEBUG: VolleyApiService - Fetching fixtures from: $url")

        val request = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    println("⚽ DEBUG: VolleyApiService - Raw fixtures response (first 500 chars): ${response.take(500)}")
                    val fixturesResponse = gson.fromJson(response, SimpleFixturesResponse::class.java)
                    println("⚽ DEBUG: VolleyApiService - Fixtures count: ${fixturesResponse.response?.size}")
                    onSuccess(fixturesResponse)
                } catch (e: Exception) {
                    println("🔴 DEBUG: VolleyApiService - Fixtures parse error: ${e.message}")
                    e.printStackTrace()
                    onError("Failed to parse fixtures: ${e.message}")
                }
            },
            { error ->
                val errorMsg = when {
                    error.networkResponse != null -> {
                        "HTTP ${error.networkResponse.statusCode}: ${String(error.networkResponse.data, Charset.defaultCharset())}"
                    }
                    error.message != null -> error.message!!
                    else -> "Unknown network error"
                }
                println("🔴 DEBUG: VolleyApiService - Fixtures network error: $errorMsg")
                onError(errorMsg)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-RapidAPI-Key"] = Config.API_KEY
                headers["X-RapidAPI-Host"] = Config.RAPIDAPI_HOST
                return headers
            }
        }

        requestQueue.add(request)
    }

    fun getTopScorers(
        leagueId: Int,
        season: Int,
        onSuccess: (SimpleTopScorersResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${BASE_URL}${PLAYERS_ENDPOINT}?season=$season&league=$leagueId"

        println("🏆 DEBUG: VolleyApiService - Fetching top scorers from: $url")

        val request = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    println("🏆 DEBUG: VolleyApiService - Raw top scorers response (first 500 chars): ${response.take(500)}")
                    val topScorersResponse = gson.fromJson(response, SimpleTopScorersResponse::class.java)
                    println("🏆 DEBUG: VolleyApiService - Top scorers count: ${topScorersResponse.response?.size}")
                    onSuccess(topScorersResponse)
                } catch (e: Exception) {
                    println("🔴 DEBUG: VolleyApiService - Top scorers parse error: ${e.message}")
                    e.printStackTrace()
                    onError("Failed to parse top scorers: ${e.message}")
                }
            },
            { error ->
                val errorMsg = when {
                    error.networkResponse != null -> {
                        "HTTP ${error.networkResponse.statusCode}: ${String(error.networkResponse.data, Charset.defaultCharset())}"
                    }
                    error.message != null -> error.message!!
                    else -> "Unknown network error"
                }
                println("🔴 DEBUG: VolleyApiService - Top scorers network error: $errorMsg")
                onError(errorMsg)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["X-RapidAPI-Key"] = Config.API_KEY
                headers["X-RapidAPI-Host"] = Config.RAPIDAPI_HOST
                return headers
            }
        }

        requestQueue.add(request)
    }
}