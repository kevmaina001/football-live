package com.tvfootballhd.liveandstream;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.banner.api.ATBannerListener;
import com.anythink.banner.api.ATBannerView;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;


public class AdsManager {




    private static ATInterstitial TopOnInterstitialAd;
    public static void LoadBannerAd(Activity activity) {
        ATBannerView mBannerView = new ATBannerView(activity);
        mBannerView.setPlacementId(activity.getString(R.string.TopOnBanner));
        int width = activity.getResources().getDisplayMetrics().widthPixels;//Set a width value, such as screen width
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mBannerView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        final FrameLayout frameLayout = activity.findViewById(R.id.my_banner);
        frameLayout.addView(mBannerView);
        mBannerView.setBannerAdListener(new ATBannerListener() {
            @Override
            public void onBannerLoaded() {
                mBannerView.setVisibility(View.VISIBLE);
                //Toast.makeText(activity, "banner loaded", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onBannerFailed(AdError adError) {
            }
            @Override
            public void onBannerClicked(ATAdInfo atAdInfo) {
            }
            @Override
            public void onBannerShow(ATAdInfo atAdInfo) {
            }
            @Override
            public void onBannerClose(ATAdInfo atAdInfo) {
            }
            @Override
            public void onBannerAutoRefreshed(ATAdInfo atAdInfo) {
            }
            @Override
            public void onBannerAutoRefreshFail(AdError adError) {
            }
        }); mBannerView.loadAd();

    }
    public static void LoadInterstitialAd(Activity activity) {
        TopOnInterstitialAd = new ATInterstitial(activity, activity.getString(R.string.TopOnInterstitial));
        TopOnInterstitialAd.setAdListener(new ATInterstitialListener() {
            @Override
            public void onInterstitialAdLoaded() {
               // Toast.makeText(activity, "inter loaded", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialAdLoadFail(AdError adError) {}
            @Override
            public void onInterstitialAdClicked(ATAdInfo atAdInfo) {}
            @Override
            public void onInterstitialAdShow(ATAdInfo atAdInfo) {}
            @Override
            public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                TopOnInterstitialAd.load();
            }
            @Override
            public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) { TopOnInterstitialAd.load();}
            @Override
            public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {}
            @Override
            public void onInterstitialAdVideoError(AdError adError) {}
        });
        TopOnInterstitialAd.load();

    }

    public static void LoadInterstitialAd2(Activity activity) {
        TopOnInterstitialAd = new ATInterstitial(activity, activity.getString(R.string.TopOnInterstitial));
        TopOnInterstitialAd.setAdListener(new ATInterstitialListener() {
            @Override
            public void onInterstitialAdLoaded() {
                if (TopOnInterstitialAd.isAdReady()) {
                    TopOnInterstitialAd.show(activity);
                    TopOnInterstitialAd.setAdListener(new ATInterstitialListener() {
                        @Override
                        public void onInterstitialAdLoaded() {

                        }

                        @Override
                        public void onInterstitialAdLoadFail(AdError adError) {

                        }

                        @Override
                        public void onInterstitialAdClicked(ATAdInfo atAdInfo) {

                        }

                        @Override
                        public void onInterstitialAdShow(ATAdInfo atAdInfo) {

                        }

                        @Override
                        public void onInterstitialAdClose(ATAdInfo atAdInfo) {

                        }

                        @Override
                        public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {

                        }

                        @Override
                        public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {

                        }

                        @Override
                        public void onInterstitialAdVideoError(AdError adError) {

                        }
                    });
                }
            }
            @Override
            public void onInterstitialAdLoadFail(AdError adError) {}
            @Override
            public void onInterstitialAdClicked(ATAdInfo atAdInfo) {}
            @Override
            public void onInterstitialAdShow(ATAdInfo atAdInfo) {}
            @Override
            public void onInterstitialAdClose(ATAdInfo atAdInfo) {
                TopOnInterstitialAd.load();
            }
            @Override
            public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) { TopOnInterstitialAd.load();}
            @Override
            public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {}
            @Override
            public void onInterstitialAdVideoError(AdError adError) {}
        });
        TopOnInterstitialAd.load();

    }
    public static void ShowInterstitialAd(Activity activity) {

        if (TopOnInterstitialAd.isAdReady()) {
            TopOnInterstitialAd.show(activity);
            TopOnInterstitialAd.setAdListener(new ATInterstitialListener() {
                @Override
                public void onInterstitialAdLoaded() {

                }

                @Override
                public void onInterstitialAdLoadFail(AdError adError) {

                }

                @Override
                public void onInterstitialAdClicked(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdShow(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdClose(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdVideoStart(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdVideoEnd(ATAdInfo atAdInfo) {

                }

                @Override
                public void onInterstitialAdVideoError(AdError adError) {

                }
            });
        } else {
            TopOnInterstitialAd.load();

        }


    }



}
