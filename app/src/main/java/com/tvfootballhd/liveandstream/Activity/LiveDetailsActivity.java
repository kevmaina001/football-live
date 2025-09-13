package com.tvfootballhd.liveandstream.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.Adapter.ScheduleDetailsViewPagerAdapter;
import com.tvfootballhd.liveandstream.Fragment.EventFragment;
import com.tvfootballhd.liveandstream.Fragment.HeadToHeadFragment;
import com.tvfootballhd.liveandstream.Fragment.LineUpFragment;
import com.tvfootballhd.liveandstream.Fragment.StandingFragment;
import com.tvfootballhd.liveandstream.Fragment.TeamStatisSticsFragment;
import com.tvfootballhd.liveandstream.Model.LiveResponse;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import com.tvfootballhd.liveandstream.R;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;

public class LiveDetailsActivity extends AppCompatActivity {
    private ImageView awayImageView;
    private LinearLayout awayLL;
    private TextView awayNameTv;
    private LinearLayout backLL;
    private long currentTime;
    private TextView goalTV;
    private ImageView homeImageView;
    private LinearLayout homeLL;
    private TextView homeNameTv;
    private TextView leagueNameTV;
    private long matchTime;
    private LiveResponse.Response response;
    private TabLayout tabLayout;
    private TextView timeTV;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_live_details);
        this.response = (LiveResponse.Response) getIntent().getSerializableExtra("LiveResponseClass");
        getIntent().putExtra("Season", this.response.getLeague().getSeason());
        getIntent().putExtra("LeagueId", this.response.getLeague().getId());
        getIntent().putExtra("HomeId", this.response.getTeams().getHome().getId());
        getIntent().putExtra("AwayId", this.response.getTeams().getAway().getId());
        getIntent().putExtra("FixtureId", this.response.getFixture().getId());
        this.homeImageView = (ImageView) findViewById(R.id.LeagueScheduleActivitystatisticsHomeIconId);
        this.homeLL = (LinearLayout) findViewById(R.id.homeLL);
        this.awayLL = (LinearLayout) findViewById(R.id.awayLL);
        this.backLL = (LinearLayout) findViewById(R.id.backLL);
        this.awayImageView = (ImageView) findViewById(R.id.LeagueScheduleActivitystatisticsAwayIconId);
        this.homeNameTv = (TextView) findViewById(R.id.LeagueScheduleActivitystatisticsHomeNameId);
        this.awayNameTv = (TextView) findViewById(R.id.LeagueScheduleActivitystatisticsAwayNameId);
        this.goalTV = (TextView) findViewById(R.id.LeagueScheduleActivitystatisticsGoalTvId);
        this.leagueNameTV = (TextView) findViewById(R.id.titleId);
        this.timeTV = (TextView) findViewById(R.id.LeagueScheduleActivitystatisticsTimeTvId);
        if (this.response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(this.response.getTeams().getHome().getLogo()).into(this.homeImageView);
        }
        if (this.response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(this.response.getTeams().getAway().getLogo()).into(this.awayImageView);
        }
        if (this.response.getTeams().getHome().getName() != null) {
            this.homeNameTv.setText(this.response.getTeams().getHome().getName());
        }
        if (this.response.getTeams().getAway().getName() != null) {
            this.awayNameTv.setText(this.response.getTeams().getAway().getName());
        }
        this.leagueNameTV.setText(this.response.getLeague().getCountry() + " - " + this.response.getLeague().getName());
        this.currentTime = new Date().getTime();
        this.matchTime = this.response.getFixture().getDate().getTime();
        this.goalTV.setText(this.response.getGoals().getHome() + "-" + this.response.getGoals().getAway());
        if (this.response.getFixture().getStatus().getMyshort().contentEquals("HT")) {
            this.timeTV.setText("HT");
        } else {
            this.timeTV.setText(this.response.getFixture().getStatus().getElapsed() + "'");
        }
        this.backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.homeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teams(view);
            }
        });
        this.awayLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamId(view);
            }
        });
        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.leagueScheduleDetailsTabLayout);
        this.tabLayout = tabLayout2;
        tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) "Event"));
        TabLayout tabLayout3 = this.tabLayout;
        tabLayout3.addTab(tabLayout3.newTab().setText((CharSequence) "Team States"));
        TabLayout tabLayout4 = this.tabLayout;
        tabLayout4.addTab(tabLayout4.newTab().setText((CharSequence) "Line Up"));
        TabLayout tabLayout5 = this.tabLayout;
        tabLayout5.addTab(tabLayout5.newTab().setText((CharSequence) "Head To Head"));
        TabLayout tabLayout6 = this.tabLayout;
        tabLayout6.addTab(tabLayout6.newTab().setText((CharSequence) "Standing"));
        this.tabLayout.setTabMode(0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new EventFragment());
        arrayList.add(new TeamStatisSticsFragment());
        arrayList.add(new LineUpFragment());
        arrayList.add(new HeadToHeadFragment());
        arrayList.add(new StandingFragment());
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
                LiveDetailsActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
    }


    public void teams(View view) {
        Intent intent = new Intent(this, TeamDetailsActivity.class);
        intent.putExtra("TeamId", this.response.getTeams().getHome().getId());
        intent.putExtra("TeamName", this.response.getTeams().getHome().getName());
        intent.putExtra("TeamIcon", this.response.getTeams().getHome().getLogo());
        intent.putExtra("Season", this.response.getLeague().getSeason());
        intent.putExtra("LeagueId", this.response.getLeague().getId());
        startActivity(intent);
    }
    public void teamId(View view) {
        Intent intent = new Intent(this, TeamDetailsActivity.class);
        intent.putExtra("TeamId", this.response.getTeams().getAway().getId());
        intent.putExtra("TeamName", this.response.getTeams().getAway().getName());
        intent.putExtra("TeamIcon", this.response.getTeams().getAway().getLogo());
        intent.putExtra("Season", this.response.getLeague().getSeason());
        intent.putExtra("LeagueId", this.response.getLeague().getId());
        startActivity(intent);
    }
}
