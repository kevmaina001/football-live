/*
 * App Open Ad Manager - Handles app open ads
 * Based on reference project implementation with enhanced debugging
 */

package com.score24seven.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.score24seven.R

class AppOpenAdManager(private val context: Context) {

    private val TAG = "AppOpenAdManager"
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var lastAdShowTime = 0L
    private val MIN_AD_INTERVAL_MS = 3000L // 3 seconds minimum between ads

    companion object {
        @Volatile
        private var instance: AppOpenAdManager? = null

        fun getInstance(context: Context): AppOpenAdManager {
            return instance ?: synchronized(this) {
                instance ?: AppOpenAdManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        println("🚀 [APP_OPEN_INIT] AppOpenAdManager initialized")
        println("🚀 [APP_OPEN_INIT] Ads Enabled: ${AdManager.adsEnabled}")
    }

    /**
     * Load App Open Ad with comprehensive debugging
     */
    fun loadAppOpenAd() {
        println("🔍 [APP_OPEN_LOAD] loadAppOpenAd() called")
        println("🔍 [APP_OPEN_LOAD] Ads Enabled: ${AdManager.adsEnabled}")
        println("🔍 [APP_OPEN_LOAD] Is Loading: $isLoadingAd")
        println("🔍 [APP_OPEN_LOAD] Ad Cached: ${appOpenAd != null}")

        if (!AdManager.adsEnabled) {
            println("❌ [APP_OPEN_LOAD] Ads are DISABLED - cannot load")
            return
        }

        if (isLoadingAd) {
            println("⚠️ [APP_OPEN_LOAD] Already loading - skipping")
            return
        }

        if (appOpenAd != null) {
            println("⚠️ [APP_OPEN_LOAD] Ad already cached - skipping")
            return
        }

        isLoadingAd = true
        val adUnitId = context.getString(R.string.admob_app_open)
        val adRequest = AdRequest.Builder().build()

        println("📱 [APP_OPEN_LOAD] Starting App Open ad load")
        println("📱 [APP_OPEN_LOAD] Ad Unit ID: $adUnitId")
        println("🚀 [APP_OPEN_LOAD] Calling AppOpenAd.load()...")

        AppOpenAd.load(
            context,
            adUnitId,
            adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    println("✅ [APP_OPEN_CALLBACK] onAdLoaded() called!")
                    appOpenAd = ad
                    isLoadingAd = false
                    println("✅ [APP_OPEN_CALLBACK] App Open ad loaded successfully and cached")
                    println("✅ [APP_OPEN_CALLBACK] Ad ready status: ${appOpenAd != null}")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    println("❌ [APP_OPEN_CALLBACK] onAdFailedToLoad() called!")
                    println("❌ [APP_OPEN_CALLBACK] Error message: ${error.message}")
                    println("❌ [APP_OPEN_CALLBACK] Error code: ${error.code}")
                    println("❌ [APP_OPEN_CALLBACK] Error domain: ${error.domain}")
                    println("❌ [APP_OPEN_CALLBACK] Error cause: ${error.cause}")
                    println("❌ [APP_OPEN_CALLBACK] Response info: ${error.responseInfo}")
                    appOpenAd = null
                    isLoadingAd = false
                }
            }
        )
    }

    /**
     * Show App Open Ad with comprehensive debugging
     */
    fun showAppOpenAdIfAvailable(activity: Activity) {
        println("🎯 [APP_OPEN_SHOW] showAppOpenAdIfAvailable() called")
        println("🎯 [APP_OPEN_SHOW] Ads Enabled: ${AdManager.adsEnabled}")
        println("🎯 [APP_OPEN_SHOW] Ad Ready: ${appOpenAd != null}")
        println("🎯 [APP_OPEN_SHOW] Is Showing: $isShowingAd")
        println("🎯 [APP_OPEN_SHOW] Activity: ${activity::class.simpleName}")

        if (!AdManager.adsEnabled) {
            println("❌ [APP_OPEN_SHOW] Ads are DISABLED - cannot show")
            return
        }

        if (isShowingAd) {
            println("⚠️ [APP_OPEN_SHOW] Ad already showing - skipping")
            return
        }

        // Check minimum time interval between ads
        val currentTime = System.currentTimeMillis()
        val timeSinceLastAd = currentTime - lastAdShowTime

        if (timeSinceLastAd < MIN_AD_INTERVAL_MS && lastAdShowTime > 0) {
            val waitTime = (MIN_AD_INTERVAL_MS - timeSinceLastAd) / 1000
            println("⏱️ [APP_OPEN_SHOW] Too soon to show another ad. Wait ${waitTime}s")
            return
        }

        if (appOpenAd == null) {
            println("⚠️ [APP_OPEN_SHOW] App Open ad NOT ready - loading now")
            loadAppOpenAd()
            return
        }

        println("📱 [APP_OPEN_SHOW] Ad is ready - showing now!")

        try {
            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    println("📱 [APP_OPEN_CALLBACK] App Open ad dismissed")
                    appOpenAd = null
                    isShowingAd = false
                    // Preload next ad
                    loadAppOpenAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    println("❌ [APP_OPEN_CALLBACK] App Open ad failed to show: ${adError.message}")
                    println("❌ [APP_OPEN_CALLBACK] Error code: ${adError.code}, domain: ${adError.domain}")
                    appOpenAd = null
                    isShowingAd = false
                    // Try loading again
                    loadAppOpenAd()
                }

                override fun onAdShowedFullScreenContent() {
                    println("📱 [APP_OPEN_CALLBACK] App Open ad shown successfully")
                    isShowingAd = true
                    lastAdShowTime = System.currentTimeMillis()
                }
            }

            appOpenAd?.show(activity)
            println("✅ [APP_OPEN_SHOW] show() called successfully")
        } catch (e: Exception) {
            println("❌ [APP_OPEN_SHOW] Error calling show(): ${e.message}")
            e.printStackTrace()
            isShowingAd = false
        }
    }

    /**
     * Check if ad is ready to be shown
     */
    fun isAdReady(): Boolean {
        return appOpenAd != null && !isShowingAd
    }

    /**
     * Get ad loading status
     */
    fun isLoading(): Boolean {
        return isLoadingAd
    }

    /**
     * Get debug information about the ad state
     */
    fun getDebugInfo(): Map<String, String> {
        val timeSinceLastAd = if (lastAdShowTime > 0) {
            (System.currentTimeMillis() - lastAdShowTime) / 1000
        } else {
            -1L
        }

        return mapOf(
            "Ads Enabled" to AdManager.adsEnabled.toString(),
            "Ad Ready" to (appOpenAd != null).toString(),
            "Is Loading" to isLoadingAd.toString(),
            "Is Showing" to isShowingAd.toString(),
            "Last Ad Shown" to if (lastAdShowTime > 0) "${timeSinceLastAd}s ago" else "Never",
            "Min Interval" to "${MIN_AD_INTERVAL_MS / 1000}s",
            "Can Show Now" to (appOpenAd != null && !isShowingAd && AdManager.adsEnabled &&
                (System.currentTimeMillis() - lastAdShowTime >= MIN_AD_INTERVAL_MS || lastAdShowTime == 0L)).toString()
        )
    }

    /**
     * Force reload ad (for testing)
     */
    fun forceReload() {
        println("🔄 [APP_OPEN_DEBUG] Force reload requested")
        appOpenAd = null
        isLoadingAd = false
        loadAppOpenAd()
    }
}
