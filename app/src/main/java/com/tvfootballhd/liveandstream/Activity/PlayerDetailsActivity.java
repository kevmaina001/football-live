package com.tvfootballhd.liveandstream.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Adapter.ScheduleDetailsViewPagerAdapter;
import com.tvfootballhd.liveandstream.Fragment.PlayerStateFragment;
import com.tvfootballhd.liveandstream.Fragment.ProfileFragment;
import com.tvfootballhd.liveandstream.Model.PlayerStateResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import com.tvfootballhd.liveandstream.R;

import com.squareup.picasso.Picasso;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerDetailsActivity extends AppCompatActivity {
    private LinearLayout backLL;
    private String leagueCountryName;
    private int leagueId;
    private String leagueName;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private ImageView playerIcon;
    int playerId;
    private TextView playerNameTV;
    /* access modifiers changed from: private */
    public PlayerStateResponse playerStateResponse;
    private int session;
    private TabLayout tabLayout;
    private ImageView teamIcon;
    private TextView teamNameTV;
    private TextView titleTV;
    /* access modifiers changed from: private */
    public ViewPager viewPager;
    int year;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player_details);
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
        this.playerId = getIntent().getIntExtra("PlayerId", 0);
        this.year = getIntent().getIntExtra("Season", 0);
        this.titleTV = (TextView) findViewById(R.id.titleId);
        this.lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottiId);
        this.backLL = (LinearLayout) findViewById(R.id.backLL);
        this.playerNameTV = (TextView) findViewById(R.id.playerNameId);
        this.teamNameTV = (TextView) findViewById(R.id.teamNameId);
        this.playerIcon = (ImageView) findViewById(R.id.playerImageViewId);
        this.teamIcon = (ImageView) findViewById(R.id.teamIconId);
        this.playerNameTV.setText(getIntent().getStringExtra("PlayerName"));
        this.teamNameTV.setText(getIntent().getStringExtra("PlayerTeam"));
        Picasso.get().load(getIntent().getStringExtra("PlayerIcon")).into(this.playerIcon);
        Picasso.get().load(getIntent().getStringExtra("TeamIcon")).into(this.teamIcon);
        this.titleTV.setText("Profile");
        this.backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.leagueScheduleDetailsTabLayout);
        this.tabLayout = tabLayout2;
        tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) "Profile"));
        TabLayout tabLayout3 = this.tabLayout;
        tabLayout3.addTab(tabLayout3.newTab().setText((CharSequence) "States"));
        this.tabLayout.setTabMode(0);
        this.viewPager = (ViewPager) findViewById(R.id.viewPagerId);
        getData();
    }


    public void getData() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(this).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/players?id=" + this.playerId + "&season=" + this.year, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    PlayerStateResponse unused = PlayerDetailsActivity.this.playerStateResponse = (PlayerStateResponse) new Gson().fromJson(jSONObject.toString(), PlayerStateResponse.class);
                    PlayerDetailsActivity.this.lottieAnimationView.setVisibility(8);
                    PlayerDetailsActivity.this.createMainPage();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                PlayerDetailsActivity.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(PlayerDetailsActivity.this, "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
            }
        }) {
            public Map getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("X-RapidAPI-Key", Config.getApiKey());
                hashMap.put("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
                return hashMap;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createMainPage() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("PlayerStateResponseClass", this.playerStateResponse);
        getIntent().putExtras(bundle);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ProfileFragment());
        arrayList.add(new PlayerStateFragment());
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.viewPagerId);
        this.viewPager = viewPager2;
        viewPager2.setAdapter(new ScheduleDetailsViewPagerAdapter(getSupportFragmentManager(), this, arrayList));
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                PlayerDetailsActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }
}
