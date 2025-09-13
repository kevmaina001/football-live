package com.tvfootballhd.liveandstream.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.tvfootballhd.liveandstream.R;
import com.startapp.sdk.adsbase.StartAppAd;

public class IntertestialAdManager {
    private static final String TAG = "IntertestialAdManager";
    public static int counter = 0;
    public static InterstitialAd interstitialAd = null;
    public static com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd = null;
    public static boolean shouldEdit = true;
    Context context;

    public IntertestialAdManager(Context context2) {
        this.context = context2;
        LoadAdMobIntertestial(context2);
        LoadFbIntertestial(context2);
    }

    public static void LoadFbIntertestial(Context context2) {
        interstitialAd = new InterstitialAd(context2, AdsPojo.facebookIntertesial);
        InterstitialAdListener r2 = new InterstitialAdListener() {
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(IntertestialAdManager.TAG, "Interstitial ad displayed.");
            }

            public void onInterstitialDismissed(Ad ad) {
                Log.e(IntertestialAdManager.TAG, "Interstitial ad dismissed.");
                IntertestialAdManager.interstitialAd.loadAd();
            }

            public void onError(Ad ad, AdError adError) {
                Log.e(IntertestialAdManager.TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            public void onAdLoaded(Ad ad) {
                Log.d(IntertestialAdManager.TAG, "Interstitial ad is loaded and ready to be displayed!");
            }

            public void onAdClicked(Ad ad) {
                Log.d(IntertestialAdManager.TAG, "Interstitial ad clicked!");
            }

            public void onLoggingImpression(Ad ad) {
                Log.d(IntertestialAdManager.TAG, "Interstitial ad impression logged!");
            }
        };
        InterstitialAd interstitialAd2 = interstitialAd;
        interstitialAd2.loadAd(interstitialAd2.buildLoadAdConfig().withAdListener(r2).build());
    }

    public static void LoadAdMobIntertestial(final Context context2) {
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context2,context2.getResources().getString(R.string.admob_inters), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            public void onAdLoaded(com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                IntertestialAdManager.mInterstitialAd = interstitialAd;
                Log.i(IntertestialAdManager.TAG, "onAdLoaded");
                IntertestialAdManager.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    public void onAdDismissedFullScreenContent() {
                        Log.d("TAG", "The ad was dismissed.");
                        IntertestialAdManager.LoadAdMobIntertestial(context2);
                    }

                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        Log.d("TAG", "The ad failed to show.");
                    }

                    public void onAdShowedFullScreenContent() {
                        IntertestialAdManager.mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            }

            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.i(IntertestialAdManager.TAG, loadAdError.getMessage());
                IntertestialAdManager.mInterstitialAd = null;
            }
        });
    }

    public static void ShowIntertestialAd(Context context2) {
        com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd2;
        if (shouldEdit) {
            counter++;
            shouldEdit = false;
            Log.e(TAG, "ShowIntertestialAd: " + counter);
            Log.e(TAG, "ShowIntertestialAd: " + AdsPojo.ad_clicks);
            if (counter >= AdsPojo.ad_clicks) {
                Log.e(TAG, "counter >= AdsPojo.ad_clicks: " + counter);
                if (AdsPojo.activeNetwork == 1 && (interstitialAd2 = mInterstitialAd) != null) {
                    interstitialAd2.show((Activity) context2);
                    counter = 0;
                } else if (AdsPojo.activeNetwork == 2 && interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                    counter = 0;
                } else if (AdsPojo.activeNetwork == 3) {
                    StartAppAd.showAd(context2);
                    counter = 0;
                }
            }
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                    IntertestialAdManager.shouldEdit = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
