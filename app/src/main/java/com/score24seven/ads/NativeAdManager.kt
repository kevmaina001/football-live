/*
 * Native Ad Manager - Handles native advanced ads
 * Based on reference project implementation with Jetpack Compose
 */

package com.score24seven.ads

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.score24seven.R

class NativeAdManager(private val context: Context) {

    private val TAG = "NativeAdManager"
    private var nativeAd: NativeAd? = null
    private var isLoadingAd = false

    /**
     * Load native ad with callback
     */
    fun loadNativeAd(onAdLoaded: (NativeAd) -> Unit, onAdFailed: (String) -> Unit) {
        println("🔍 [NATIVE_AD] loadNativeAd() called")
        println("🔍 [NATIVE_AD] Ads Enabled: ${AdManager.adsEnabled}")

        if (!AdManager.adsEnabled) {
            println("❌ [NATIVE_AD] Ads are DISABLED - cannot load")
            onAdFailed("Ads disabled")
            return
        }

        if (isLoadingAd) {
            println("⚠️ [NATIVE_AD] Already loading - skipping")
            return
        }

        isLoadingAd = true
        val adUnitId = context.getString(R.string.admob_native)

        println("📱 [NATIVE_AD] Starting native ad load")
        println("📱 [NATIVE_AD] Ad Unit ID: $adUnitId")

        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                println("✅ [NATIVE_AD] Native ad loaded successfully")
                nativeAd = ad
                isLoadingAd = false
                onAdLoaded(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    println("❌ [NATIVE_AD] Native ad failed to load: ${error.message}")
                    println("❌ [NATIVE_AD] Error code: ${error.code}, domain: ${error.domain}")
                    isLoadingAd = false
                    onAdFailed(error.message)
                }

                override fun onAdClicked() {
                    println("📱 [NATIVE_AD] Native ad clicked")
                }

                override fun onAdOpened() {
                    println("📱 [NATIVE_AD] Native ad opened")
                }

                override fun onAdClosed() {
                    println("📱 [NATIVE_AD] Native ad closed")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
        println("🚀 [NATIVE_AD] AdLoader.loadAd() called")
    }

    /**
     * Clean up native ad resources
     */
    fun destroy() {
        println("🧹 [NATIVE_AD] Destroying native ad")
        nativeAd?.destroy()
        nativeAd = null
    }

    companion object {
        /**
         * Simple Compose-only Native Ad Card (fallback without layout XML)
         */
        @Composable
        fun SimpleNativeAdCard(
            modifier: Modifier = Modifier
        ) {
            if (!AdManager.adsEnabled) return

            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            // Load ad
            LaunchedEffect(Unit) {
                val manager = NativeAdManager(context)
                manager.loadNativeAd(
                    onAdLoaded = { ad ->
                        nativeAd = ad
                        isLoading = false
                    },
                    onAdFailed = { isLoading = false }
                )
            }

            // Cleanup
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        nativeAd?.destroy()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                    nativeAd?.destroy()
                }
            }

            // Display
            nativeAd?.let { ad ->
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        // Ad badge
                        Text(
                            text = "Ad",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Headline
                        ad.headline?.let {
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Body
                        ad.body?.let {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Call to Action
                        ad.callToAction?.let { cta ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cta,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
