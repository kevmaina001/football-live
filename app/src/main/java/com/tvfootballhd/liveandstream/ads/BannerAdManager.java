package com.tvfootballhd.liveandstream.ads;

import android.content.Context;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.tvfootballhd.liveandstream.R;
import com.startapp.sdk.ads.banner.Banner;

public class BannerAdManager {
    Context context;

    public BannerAdManager(Context context2, LinearLayout linearLayout) {
        this.context = context2;
        if (!AdsPojo.dataloaded) {
            return;
        }
        if (AdsPojo.activeNetwork == 1) {
            LoadAdmobBanner(linearLayout);
        } else if (AdsPojo.activeNetwork == 2) {
            LoadFacebookBanner(linearLayout);
        } else if (AdsPojo.activeNetwork == 3) {
            LoadStartAppBanner(linearLayout);
        }
    }

    public void LoadAdmobBanner(LinearLayout linearLayout) {
        AdView adView = new AdView(this.context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(adView.getResources().getString(R.string.admob_banner));
        adView.loadAd(new AdRequest.Builder().build());
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
    }

    public void LoadFacebookBanner(LinearLayout linearLayout) {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this.context, AdsPojo.facebookBanner, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.removeAllViews();
        linearLayout.addView(adView);
        adView.loadAd();
    }

    public void LoadStartAppBanner(LinearLayout linearLayout) {
        Banner banner = new Banner(this.context);
        linearLayout.removeAllViews();
        linearLayout.addView(banner);
    }
}
