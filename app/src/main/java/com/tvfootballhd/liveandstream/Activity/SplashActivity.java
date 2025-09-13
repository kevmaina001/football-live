package com.tvfootballhd.liveandstream.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.facebook.ads.AudienceNetworkAds;
import com.tvfootballhd.liveandstream.MainActivity;

public class SplashActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        AppCompatDelegate.setDefaultNightMode(1);
        super.onCreate(bundle);
        AudienceNetworkAds.initialize(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
