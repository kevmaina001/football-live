/*
 * Interstitial Ad Manager - Handles interstitial ad loading and display
 * Based on reference project implementation with click counter mechanism
 * Optimized with caching, preloading, and better error handling
 */

package com.score24seven.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.score24seven.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InterstitialAdManager(private val context: Context) {

    companion object {
        private var mInterstitialAd: InterstitialAd? = null
        private var counter = 0
        private var isLoading = false
        private var loadAttempts = 0
        private const val MAX_LOAD_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 5000L // 5 seconds
        private var lastAdShowTime = 0L
        private const val MIN_AD_INTERVAL_MS = 3000L // 3 seconds minimum between ads (for testing - increase to 30000-60000 for production)

        // Thread-safe flag to prevent rapid-fire calls (same as reference implementation)
        @Volatile
        private var shouldEdit = true
    }

    init {
        println("🚀 [AD_INIT] InterstitialAdManager initialized")
        println("🚀 [AD_INIT] Ads Enabled: ${AdManager.adsEnabled}")
        println("🚀 [AD_INIT] Clicks before showing: ${AdManager.clicksBeforeInterstitial}")
        loadInterstitialAd()
    }

    /**
     * Load interstitial ad with retry mechanism
     */
    private fun loadInterstitialAd() {
        println("🔍 [AD_LOAD] loadInterstitialAd() called")
        println("🔍 [AD_LOAD] Ads Enabled: ${AdManager.adsEnabled}")
        println("🔍 [AD_LOAD] Is Loading: $isLoading")

        if (!AdManager.adsEnabled) {
            println("❌ [AD_LOAD] Ads are DISABLED - cannot load")
            return
        }

        if (isLoading) {
            println("⚠️ [AD_LOAD] Already loading - skipping")
            return
        }

        isLoading = true
        val adRequest = AdRequest.Builder().build()
        val adUnitId = context.getString(R.string.admob_inters)

        println("📱 [AD_LOAD] Starting ad load (attempt ${loadAttempts + 1}/$MAX_LOAD_ATTEMPTS)")
        println("📱 [AD_LOAD] Ad Unit ID: $adUnitId")

        println("🚀 [AD_LOAD] Calling InterstitialAd.load()...")

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    println("✅ [AD_CALLBACK] onAdLoaded() called!")
                    mInterstitialAd = interstitialAd
                    isLoading = false
                    loadAttempts = 0 // Reset attempts on successful load
                    println("✅ [AD_CALLBACK] Interstitial ad loaded successfully and cached")
                    println("✅ [AD_CALLBACK] Ad ready status: ${mInterstitialAd != null}")

                    // Set up callbacks for when ad is dismissed
                    interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            println("📱 [AD_CALLBACK] Interstitial ad dismissed")
                            mInterstitialAd = null
                            // Preload next ad immediately for better UX
                            loadInterstitialAd()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            println("❌ [AD_CALLBACK] Interstitial ad failed to show: ${adError.message}")
                            println("❌ [AD_CALLBACK] Error code: ${adError.code}, domain: ${adError.domain}")
                            mInterstitialAd = null
                            loadInterstitialAd()
                        }

                        override fun onAdShowedFullScreenContent() {
                            println("📱 [AD_CALLBACK] Interstitial ad shown successfully")
                            lastAdShowTime = System.currentTimeMillis()
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    println("❌ [AD_CALLBACK] onAdFailedToLoad() called!")
                    println("❌ [AD_CALLBACK] Error message: ${loadAdError.message}")
                    println("❌ [AD_CALLBACK] Error code: ${loadAdError.code}")
                    println("❌ [AD_CALLBACK] Error domain: ${loadAdError.domain}")
                    println("❌ [AD_CALLBACK] Error cause: ${loadAdError.cause}")
                    mInterstitialAd = null
                    isLoading = false
                    loadAttempts++

                    // Retry with exponential backoff if under max attempts
                    if (loadAttempts < MAX_LOAD_ATTEMPTS) {
                        val delayMs = RETRY_DELAY_MS * loadAttempts
                        println("🔄 [AD_CALLBACK] Retrying in ${delayMs/1000} seconds...")
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(delayMs)
                            loadInterstitialAd()
                        }
                    } else {
                        println("⚠️ [AD_CALLBACK] Max retry attempts reached. Will retry on next show request.")
                        loadAttempts = 0 // Reset for next manual trigger
                    }
                }
            }
        )
    }

    /**
     * Show interstitial ad based on click counter with frequency control
     * Call this method when user performs actions (navigate, click, etc.)
     * Using thread-safe pattern from reference implementation
     */
    fun showInterstitialAd(activity: Activity) {
        println("🎯 showInterstitialAd() called - shouldEdit: $shouldEdit, adsEnabled: ${AdManager.adsEnabled}")

        if (!AdManager.adsEnabled) {
            println("⚠️ Ads disabled")
            return
        }

        // Thread-safe check - prevent rapid-fire calls
        if (shouldEdit) {
            counter++
            shouldEdit = false
            println("📊 Click counter: $counter / ${AdManager.clicksBeforeInterstitial}")

            if (counter >= AdManager.clicksBeforeInterstitial) {
                // Check minimum time interval between ads
                val currentTime = System.currentTimeMillis()
                val timeSinceLastAd = currentTime - lastAdShowTime

                if (timeSinceLastAd < MIN_AD_INTERVAL_MS && lastAdShowTime > 0) {
                    val waitTime = (MIN_AD_INTERVAL_MS - timeSinceLastAd)/1000
                    println("⏱️ Too soon to show another ad. Wait ${waitTime}s")
                    // Reset flag after delay
                    resetShouldEditFlag()
                    return
                }

                if (mInterstitialAd != null) {
                    println("📱 Ad ready - SHOWING NOW!")
                    counter = 0
                    mInterstitialAd?.show(activity)
                } else {
                    println("⚠️ Interstitial ad not ready yet. Loading now...")
                    counter = 0
                    loadInterstitialAd() // Try loading if not available
                }
            }
        }

        // Reset the flag after 500ms delay (same as reference implementation)
        resetShouldEditFlag()
    }

    /**
     * Reset shouldEdit flag after delay to allow next ad trigger
     */
    private fun resetShouldEditFlag() {
        Thread {
            try {
                Thread.sleep(500) // 500ms delay like reference implementation
                shouldEdit = true
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

    /**
     * Show interstitial ad immediately without counter check
     * Use for specific actions that should always show an ad
     */
    fun showInterstitialAdNow(activity: Activity) {
        println("🎯 [AD_SHOW] showInterstitialAdNow() called")
        println("🎯 [AD_SHOW] Ads Enabled: ${AdManager.adsEnabled}")
        println("🎯 [AD_SHOW] Ad Ready: ${mInterstitialAd != null}")
        println("🎯 [AD_SHOW] Activity: ${activity::class.simpleName}")

        if (!AdManager.adsEnabled) {
            println("❌ [AD_SHOW] Ads are DISABLED - cannot show")
            return
        }

        if (mInterstitialAd != null) {
            println("📱 [AD_SHOW] Ad is ready - showing now!")
            try {
                mInterstitialAd?.show(activity)
                counter = 0
                println("✅ [AD_SHOW] show() called successfully")
            } catch (e: Exception) {
                println("❌ [AD_SHOW] Error calling show(): ${e.message}")
                e.printStackTrace()
            }
        } else {
            println("⚠️ [AD_SHOW] Interstitial ad NOT ready - loading now")
            loadInterstitialAd()
        }
    }

    /**
     * Reset counter manually if needed
     */
    fun resetCounter() {
        counter = 0
        println("🔄 Interstitial counter reset")
    }

    /**
     * Check if ad is ready to be shown
     */
    fun isAdReady(): Boolean {
        return mInterstitialAd != null
    }
}
