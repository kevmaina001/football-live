package com.tvfootballhd.liveandstream.ads;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.tvfootballhd.liveandstream.R;

public class OpenAdsManager {
    public OpenAdsManager(final Activity activity) {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AppOpenAd.load((Context) activity, activity.getResources().getString(R.string.admob_app_open), new AdRequest.Builder().build(), 1, (AppOpenAd.AppOpenAdLoadCallback) new AppOpenAd.AppOpenAdLoadCallback() {
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                    }

                    public void onAdLoaded(AppOpenAd appOpenAd) {
                        appOpenAd.show(activity);
                    }
                });
            }
        });
    }
}
