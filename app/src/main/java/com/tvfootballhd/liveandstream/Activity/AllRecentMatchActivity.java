package com.tvfootballhd.liveandstream.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.Adapter.RecentMatchAdapter;
import com.tvfootballhd.liveandstream.Model.LastFiftyMatches;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import com.tvfootballhd.liveandstream.R;


public class AllRecentMatchActivity extends AppCompatActivity {
    private LinearLayout backLL;
    /* access modifiers changed from: private */
    public LastFiftyMatches lastFiftyMatches;
    private LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    private TextView titleTV;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_recent_match);
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.titleTV = (TextView) findViewById(R.id.titleId);
        this.backLL = (LinearLayout) findViewById(R.id.backLL);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.lastFiftyMatches = (LastFiftyMatches) getIntent().getSerializableExtra("list");
        RecentMatchAdapter recentMatchAdapter = new RecentMatchAdapter(this, this.lastFiftyMatches.getResponse(), this.lastFiftyMatches.getResponse().size());
        this.recyclerView.setAdapter(recentMatchAdapter);
        this.titleTV.setText("Recent Match");
        this.backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recentMatchAdapter.setClickListener(new RecentMatchAdapter.ClickListener() {
            public void onClick(int i) {
                Intent intent = new Intent(AllRecentMatchActivity.this, RecentMatchDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("FixtureResponseClass", AllRecentMatchActivity.this.lastFiftyMatches.getResponse().get(i));
                intent.putExtra("FixtureId", AllRecentMatchActivity.this.lastFiftyMatches.getResponse().get(i).getFixture().getId());
                intent.putExtra("HomeId", AllRecentMatchActivity.this.lastFiftyMatches.getResponse().get(i).getTeams().getHome().getId());
                intent.putExtra("AwayId", AllRecentMatchActivity.this.lastFiftyMatches.getResponse().get(i).getTeams().getAway().getId());
                intent.putExtras(bundle);
                AllRecentMatchActivity.this.startActivity(intent);
            }
        });
    }


}
