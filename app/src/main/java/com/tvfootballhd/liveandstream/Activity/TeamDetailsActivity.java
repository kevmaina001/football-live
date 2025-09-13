package com.tvfootballhd.liveandstream.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.Adapter.ScheduleDetailsViewPagerAdapter;
import com.tvfootballhd.liveandstream.Fragment.FixtureFragment;
import com.tvfootballhd.liveandstream.Fragment.InfoFragment;
import com.tvfootballhd.liveandstream.Fragment.SquadFragment;
import com.tvfootballhd.liveandstream.Fragment.StatisticsFragment;
import com.tvfootballhd.liveandstream.Model.PlayerStateResponse;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import com.tvfootballhd.liveandstream.R;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class TeamDetailsActivity extends AppCompatActivity {
    private LinearLayout backLL;
    private int id;
    private String leagueCountryName;
    private int leagueId;
    private String leagueName;
    private LottieAnimationView lottieAnimationView;
    private PlayerStateResponse playerStateResponse;
    private int seasonId;
    private TabLayout tabLayout;
    private String teamIcon;
    private ImageView teamIconImageView;
    private String teamName;
    private TextView teamNameTV;
    private TextView titleTV;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_team_details);
        this.id = getIntent().getIntExtra("TeamId", 0);
        this.leagueId = getIntent().getIntExtra("LeagueId", 0);
        this.seasonId = getIntent().getIntExtra("Season", 0);
        this.teamName = getIntent().getStringExtra("TeamName");
        this.teamIcon = getIntent().getStringExtra("TeamIcon");
        this.titleTV = (TextView) findViewById(R.id.titleId);
        this.lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottiId);
        this.backLL = (LinearLayout) findViewById(R.id.backLL);
        this.viewPager = (ViewPager) findViewById(R.id.viewPagerId);
        this.teamNameTV = (TextView) findViewById(R.id.teamNameId);
        this.teamIconImageView = (ImageView) findViewById(R.id.imageView);
        this.titleTV.setText("Team Details");
        this.backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tab_layout);
        this.tabLayout = tabLayout2;
        tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) "Recent"));
        TabLayout tabLayout3 = this.tabLayout;
        tabLayout3.addTab(tabLayout3.newTab().setText((CharSequence) "Fixtures"));
        TabLayout tabLayout4 = this.tabLayout;
        tabLayout4.addTab(tabLayout4.newTab().setText((CharSequence) "Statistics"));
        TabLayout tabLayout5 = this.tabLayout;
        tabLayout5.addTab(tabLayout5.newTab().setText((CharSequence) "Squad"));
        this.tabLayout.setTabMode(0);
        this.teamNameTV.setText(this.teamName);
        Picasso.get().load(this.teamIcon).into(this.teamIconImageView);
        this.tabLayout.setTabMode(0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new InfoFragment());
        arrayList.add(new FixtureFragment());
        arrayList.add(new StatisticsFragment());
        arrayList.add(new SquadFragment());
        this.viewPager.setAdapter(new ScheduleDetailsViewPagerAdapter(getSupportFragmentManager(), this, arrayList));
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                TeamDetailsActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
    }

}
