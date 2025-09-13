package com.tvfootballhd.liveandstream;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.tvfootballhd.liveandstream.Fragment.HomeFragment;
import com.tvfootballhd.liveandstream.Fragment.LiveFragmentTwo;
import com.tvfootballhd.liveandstream.Fragment.MatchFragment;
import com.tvfootballhd.liveandstream.Fragment.SettingFragment;
import com.tvfootballhd.liveandstream.Fragment.TournamentFragment;
import com.tvfootballhd.liveandstream.Utils.NetworkChangeReceiver;
import com.tvfootballhd.liveandstream.ads.AdsPojo;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import com.tvfootballhd.liveandstream.ads.InitAdManager;
import com.tvfootballhd.liveandstream.ads.IntertestialAdManager;
import com.tvfootballhd.liveandstream.ads.OpenAdsManager;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ConsentInformation consentInformation;
    private NetworkChangeReceiver networkChangeReceiver;

    static /* synthetic */ void lambda$requestConsentent$2(FormError formError) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestConsentent();
        getAllData();
        setContentView(R.layout.activity_main);

        //ToponTesting
//       ATSDK.setNetworkLogDebug(true);
//        ATSDK.integrationChecking(this);

        AdsManager.LoadBannerAd(this);
        AdsManager.LoadInterstitialAd(this);
        loadBanner();
        this.networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(this.networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainBottomNavigationId);
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
        getHomeFragment();
        this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction beginTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                if (menuItem.getItemId() == R.id.homeId) {
                    AdsManager.ShowInterstitialAd(MainActivity.this);
                    IntertestialAdManager.ShowIntertestialAd(MainActivity.this);
                    beginTransaction.replace(R.id.mainFrameLayout, new HomeFragment()).addToBackStack("HomeFragment");
                    menuItem.setChecked(true);
                }
                if (menuItem.getItemId() == R.id.matches) {
                    AdsManager.ShowInterstitialAd(MainActivity.this);
                    IntertestialAdManager.ShowIntertestialAd(MainActivity.this);
                    beginTransaction.replace(R.id.mainFrameLayout, new MatchFragment()).addToBackStack("HomeFragment");
                    menuItem.setChecked(true);
                }
                if (menuItem.getItemId() == R.id.liveTvId) {
                    AdsManager.ShowInterstitialAd(MainActivity.this);
                    IntertestialAdManager.ShowIntertestialAd(MainActivity.this);
                    beginTransaction.replace(R.id.mainFrameLayout, new LiveFragmentTwo()).addToBackStack("LiveFragmentTwo");
                    menuItem.setChecked(true);
                }
                if (menuItem.getItemId() == R.id.leagueId) {
                    AdsManager.ShowInterstitialAd(MainActivity.this);
                    IntertestialAdManager.ShowIntertestialAd(MainActivity.this);
                    beginTransaction.replace(R.id.mainFrameLayout, new TournamentFragment()).addToBackStack("TournamentFragment");
                    menuItem.setChecked(true);
                }
                if (menuItem.getItemId() == R.id.moreId) {
                    beginTransaction.replace(R.id.mainFrameLayout, new SettingFragment()).addToBackStack("SettingFragment");
                    menuItem.setChecked(true);
                }
                beginTransaction.commit();
                return true;
            }
        });
    }

    public void getAllData() {
        StringRequest stringRequest = new StringRequest("https://tipicofixedmatches.online/ads.json", new Response.Listener<String>() {
            public void onResponse(String str) {
                try {
                    Log.e("ContentValues", "onResponse: " + str);
                    JSONObject jSONObject = new JSONObject(str);
                    AdsPojo.admob = jSONObject.getString("admob");
                    AdsPojo.facebook = jSONObject.getString("facebook");
                    AdsPojo.startapp = jSONObject.getString("startapp");
                    AdsPojo.admobBannerid = jSONObject.getString("admob_bannerid");
                    AdsPojo.admob_open_ad = jSONObject.getString("admob_open_ad");
                    AdsPojo.admobIntertestialId = jSONObject.getString("admob_intertestial_id");
                    AdsPojo.facebookBanner = jSONObject.getString("facebook_banner");
                    AdsPojo.facebookIntertesial = jSONObject.getString("facebook_intertesial");
                    AdsPojo.startappId = jSONObject.getString("startapp_id");
                    AdsPojo.ad_clicks = Integer.parseInt(jSONObject.getString("intertestial_clicks"));
                    if (AdsPojo.admob.toLowerCase().equals("true")) {
                        AdsPojo.activeNetwork = 1;
                    } else if (AdsPojo.facebook.toLowerCase().equals("true")) {
                        AdsPojo.activeNetwork = 2;
                    } else if (AdsPojo.startapp.toLowerCase().equals("true")) {
                        AdsPojo.activeNetwork = 3;
                    } else {
                        AdsPojo.activeNetwork = 0;
                    }
                    new InitAdManager(MainActivity.this);
                    new IntertestialAdManager(MainActivity.this);
                    Log.i("ContentValues", "activeNetwork: " + AdsPojo.activeNetwork);
                    AdsPojo.dataloaded = true;
                    if (AdsPojo.activeNetwork == 1) {
                        MainActivity.this.showOpenAds();
                    }
                    MainActivity.this.loadBanner();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Volley_error: ", "" + volleyError.toString());
            }
        });
        RequestQueue newRequestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(600000, 1, 1.0f));
        newRequestQueue.add(stringRequest);
    }

    private void requestConsentent() {
        ConsentRequestParameters build = new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build();
        ConsentInformation consentInformation2 = UserMessagingPlatform.getConsentInformation(this);
        this.consentInformation = consentInformation2;
        consentInformation2.requestConsentInfoUpdate(this, build, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
            @Override
            public void onConsentInfoUpdateSuccess() {
                dismisso();
            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() {
            @Override
            public void onConsentInfoUpdateFailure(@NonNull FormError formError) {
                MainActivity.lambda$requestConsentent$2(formError);
            }
        });
        if (this.consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }

    public void dismisso() {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(this, new ConsentForm.OnConsentFormDismissedListener() {
            @Override
            public void onConsentFormDismissed(@Nullable FormError formError) {
                requestconse(formError);
            }
        });
    }

    public void requestconse(FormError formError) {
        if (this.consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }

    private void initializeMobileAdsSdk() {
        MobileAds.initialize(this);
    }

    private void getHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.mainFrameLayout, homeFragment).addToBackStack("HomeFragment");
        beginTransaction.commit();
    }

    public void onBackPressed() {
        finish();
    }

    /* access modifiers changed from: private */
    public void showOpenAds() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                new OpenAdsManager(MainActivity.this);
            }
        }, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    public void loadBanner() {
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
    }
}
