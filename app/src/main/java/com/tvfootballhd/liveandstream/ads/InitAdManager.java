package com.tvfootballhd.liveandstream.ads;

import android.content.Context;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.RequestConfiguration;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import java.util.Arrays;

public class InitAdManager {
    public InitAdManager(Context context) {
        StartAppSDK.init(context, AdsPojo.startappId);
        StartAppSDK.enableReturnAds(false);
        StartAppAd.disableSplash();
        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(new String[]{"0BA5E8E77FAA86A191A0AACB1D6F8889"}));
        AdSettings.addTestDevice("fbf3e8e7-aee3-48ab-849b-f6594c484bf0");
    }
}
