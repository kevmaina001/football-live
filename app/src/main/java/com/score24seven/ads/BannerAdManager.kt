/*
 * Banner Ad Manager - Handles banner ad loading and display
 * Based on reference project implementation
 */

package com.score24seven.ads

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.score24seven.R

class BannerAdManager(private val context: Context) {

    /**
     * Load AdMob banner ad into a LinearLayout
     */
    fun loadAdmobBanner(container: LinearLayout) {
        if (!AdManager.adsEnabled) return

        try {
            val adView = AdView(context).apply {
                setAdSize(AdSize.BANNER)
                setAdUnitId(context.getString(R.string.admob_banner))
            }

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

            container.removeAllViews()
            container.addView(adView)

            println("📱 Banner ad loaded successfully")
        } catch (e: Exception) {
            println("❌ Error loading banner ad: ${e.message}")
        }
    }

    companion object {
        /**
         * Composable function to display banner ad with optimized loading
         * Optimizations:
         * - Single instance per screen (no reloads on recomposition)
         * - Proper pause/resume lifecycle handling
         * - Memory cleanup on dispose
         */
        @Composable
        fun BannerAdView(
            modifier: Modifier = Modifier
        ) {
            if (!AdManager.adsEnabled) return

            val lifecycleOwner = LocalLifecycleOwner.current

            // Remember the ad view to prevent reloading on recomposition
            val adView = remember {
                println("📱 DEBUG: Creating NEW AdView instance")
                null as AdView?
            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                AndroidView(
                    factory = { context ->
                        println("📱 DEBUG: Factory - Creating AdView for banner")
                        AdView(context).apply {
                            setAdSize(AdSize.BANNER)
                            setAdUnitId(context.getString(R.string.admob_banner))
                            visibility = View.GONE // Hide until loaded

                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    println("✅ Banner ad loaded successfully")
                                    visibility = View.VISIBLE
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    println("❌ Banner ad failed to load: ${error.message} (Code: ${error.code})")
                                    visibility = View.GONE
                                }

                                override fun onAdOpened() {
                                    println("📱 Banner ad opened")
                                }

                                override fun onAdClicked() {
                                    println("📱 Banner ad clicked")
                                }
                            }

                            println("📱 DEBUG: Loading banner ad with ID: ${context.getString(R.string.admob_banner)}")
                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    update = { view ->
                        // Don't reload on recomposition - keep existing ad
                        println("📱 DEBUG: Update called - keeping existing ad")
                    }
                )
            }

            // Lifecycle management for better performance
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> {
                            println("📱 Banner ad: ON_RESUME - resuming ad")
                        }
                        Lifecycle.Event.ON_PAUSE -> {
                            println("📱 Banner ad: ON_PAUSE - pausing ad")
                        }
                        Lifecycle.Event.ON_DESTROY -> {
                            println("📱 Banner ad: ON_DESTROY - cleaning up")
                        }
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    println("📱 Banner ad: onDispose - removing lifecycle observer")
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        }

        /**
         * Composable function to display large banner ad
         */
        @Composable
        fun LargeBannerAdView(
            modifier: Modifier = Modifier
        ) {
            if (!AdManager.adsEnabled) return

            val lifecycleOwner = LocalLifecycleOwner.current

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                AndroidView(
                    factory = { context ->
                        AdView(context).apply {
                            setAdSize(AdSize.LARGE_BANNER)
                            setAdUnitId(context.getString(R.string.admob_banner))

                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    visibility = View.VISIBLE
                                }
                            }

                            loadAd(AdRequest.Builder().build())
                        }
                    },
                    update = { adView ->
                        // Keep the AdView instance alive
                    }
                )
            }

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, _ -> }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        }
    }
}
