package com.tvfootballhd.liveandstream.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.Adapter.ScheduleDetailsViewPagerAdapter;
import com.tvfootballhd.liveandstream.Fragment.MatchsFragment;
import com.tvfootballhd.liveandstream.Fragment.PredictionFragment;
import com.tvfootballhd.liveandstream.Fragment.StandingFragment;
import com.tvfootballhd.liveandstream.Fragment.TopScorerFragment;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import java.util.ArrayList;

public class LeagueDetailsActivity extends AppCompatActivity {
    private LinearLayout backLL;
    private String leagueCountryName;
    private int leagueId;
    private String leagueName;
    private TextView leagueNameTV;
    private int session;
    private TabLayout tabLayout;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_league_details);
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
        this.leagueName = getIntent().getStringExtra("leagueName");
        this.leagueCountryName = getIntent().getStringExtra("leagueCountryName");
        this.leagueId = getIntent().getIntExtra("leagueId", 1);
        this.session = getIntent().getIntExtra("year", 2023);
        getIntent().putExtra("LeagueId", this.leagueId);
        getIntent().putExtra("Season", this.session);
        this.leagueNameTV = (TextView) findViewById(R.id.titleId);
        this.backLL = (LinearLayout) findViewById(R.id.backLL);
        this.leagueNameTV.setText(this.leagueCountryName + " - " + this.leagueName);
        this.backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.leagueScheduleDetailsTabLayout);
        this.tabLayout = tabLayout2;
        tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) "Matches"));
        TabLayout tabLayout3 = this.tabLayout;
        tabLayout3.addTab(tabLayout3.newTab().setText((CharSequence) "Standing"));
        TabLayout tabLayout4 = this.tabLayout;
        tabLayout4.addTab(tabLayout4.newTab().setText((CharSequence) "Prediction"));
        TabLayout tabLayout5 = this.tabLayout;
        tabLayout5.addTab(tabLayout5.newTab().setText((CharSequence) "Top Scorer"));
        this.tabLayout.setTabMode(0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new MatchsFragment());
        arrayList.add(new StandingFragment());
        arrayList.add(new PredictionFragment());
        arrayList.add(new TopScorerFragment());
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.scheduleDetailsViewPager);
        this.viewPager = viewPager2;
        viewPager2.setAdapter(new ScheduleDetailsViewPagerAdapter(getSupportFragmentManager(), this, arrayList));
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                LeagueDetailsActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }


}
