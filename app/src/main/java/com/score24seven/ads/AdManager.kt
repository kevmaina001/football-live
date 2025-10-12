/*
 * Ad Manager - Initializes and manages ads
 * Based on reference project implementation
 */

package com.score24seven.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    init {
        initializeAds()
    }

    private fun initializeAds() {
        println("🚀 [ADMOB_INIT] Starting AdMob SDK initialization...")

        // Initialize Mobile Ads SDK
        MobileAds.initialize(context) { initializationStatus ->
            println("✅ [ADMOB_INIT] AdMob SDK initialization complete!")
            println("✅ [ADMOB_INIT] Adapter status map:")
            initializationStatus.adapterStatusMap.forEach { (key, value) ->
                println("   - $key: ${value.initializationState} (${value.description})")
            }
        }

        // Configure test devices for development
        val testDeviceIds = listOf(
            "TEST_DEVICE_ID_HERE" // Replace with your test device ID from logcat
        )

        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDeviceIds)
            .build()

        MobileAds.setRequestConfiguration(configuration)

        println("✅ [ADMOB_INIT] AdManager initialized with test mode enabled")
        println("✅ [ADMOB_INIT] Test device IDs configured: $testDeviceIds")
    }

    companion object {
        // Ad click counter for interstitial frequency control
        var adClicks = 0
        var clicksBeforeInterstitial = 1 // Show interstitial after every 1 click (for testing - increase to 3-5 for production)
        var adsEnabled = true

        fun incrementClicks() {
            adClicks++
        }

        fun resetClicks() {
            adClicks = 0
        }

        fun shouldShowInterstitial(): Boolean {
            return adsEnabled && adClicks >= clicksBeforeInterstitial
        }
    }
}
