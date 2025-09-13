package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.MatchResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchAdapterHolder> {
    private Context context;
    private long currentTime;
    private MatchResponse matchResponse;
    private long matchTime;

    public MatchAdapter(MatchResponse matchResponse2, Context context2) {
        this.matchResponse = matchResponse2;
        this.context = context2;
    }

    public MatchAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MatchAdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.match_layout_3, viewGroup, false));
    }

    public void onBindViewHolder(MatchAdapterHolder matchAdapterHolder, int i) {
        MatchResponse.Response response = this.matchResponse.getResponse().get(i);
        if (response.getTeams().getHome().getName() != null) {
            matchAdapterHolder.homeNameTV.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            matchAdapterHolder.awayNameTV.setText(response.getTeams().getAway().getName());
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(matchAdapterHolder.homeFlag);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(matchAdapterHolder.awayFlag);
        }
        this.currentTime = new Date().getTime();
        long time = response.getFixture().getDate().getTime();
        this.matchTime = time;
        if (this.currentTime < time) {
            matchAdapterHolder.dateTimeTV.setVisibility(0);
            matchAdapterHolder.dateTV.setVisibility(0);
            matchAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.black));
            matchAdapterHolder.dateTV.setTextColor(this.context.getResources().getColor(R.color.black));
            matchAdapterHolder.dateTV.setText(new SimpleDateFormat("dd-MMM-yy").format(response.getFixture().getDate()));
            matchAdapterHolder.dateTimeTV.setText(new SimpleDateFormat("hh:mm aa").format(response.getFixture().getDate()));
            matchAdapterHolder.homeGoalTV.setVisibility(8);
            matchAdapterHolder.awayGoalTv.setVisibility(8);
            return;
        }
        matchAdapterHolder.dateTV.setVisibility(8);
        if (response.getFixture().getStatus().getElapsed() >= 90 || response.getFixture().getStatus().getElapsed() == 0) {
            if (response.getFixture().getStatus().getMyshort().contentEquals("FT")) {
                matchAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.green));
            } else if (response.getFixture().getStatus().getMyshort().contentEquals("CANC")) {
                matchAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.red));
            } else {
                matchAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.active_color));
            }
            matchAdapterHolder.dateTimeTV.setText(response.getFixture().getStatus().getMyshort());
        } else {
            matchAdapterHolder.dateTimeTV.setText(response.getFixture().getStatus().getElapsed() + "'");
            matchAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.active_color));
        }
        matchAdapterHolder.homeGoalTV.setVisibility(0);
        matchAdapterHolder.awayGoalTv.setVisibility(0);
        matchAdapterHolder.homeGoalTV.setText(String.valueOf(response.getGoals().getHome()));
        matchAdapterHolder.awayGoalTv.setText(String.valueOf(response.getGoals().getAway()));
    }

    public int getItemCount() {
        return this.matchResponse.getResponse().size();
    }

    public class MatchAdapterHolder extends RecyclerView.ViewHolder {
        ImageView awayFlag;
        TextView awayGoalTv;
        TextView awayNameTV;
        TextView dateTV;
        TextView dateTimeTV;
        ImageView homeFlag;
        TextView homeGoalTV;
        TextView homeNameTV;
        RelativeLayout matchMainLL;

        public MatchAdapterHolder(View view) {
            super(view);
            this.homeFlag = (ImageView) view.findViewById(R.id.leagueHomeFlagId);
            this.awayFlag = (ImageView) view.findViewById(R.id.leagueAwayFlagId);
            this.homeNameTV = (TextView) view.findViewById(R.id.leagueHomeNameId);
            this.awayNameTV = (TextView) view.findViewById(R.id.leagueAwayNameId);
            this.matchMainLL = (RelativeLayout) view.findViewById(R.id.matchMainLL);
            this.dateTV = (TextView) view.findViewById(R.id.dateTV);
            this.dateTimeTV = (TextView) view.findViewById(R.id.timeTVId);
            this.homeGoalTV = (TextView) view.findViewById(R.id.homeGoal);
            this.awayGoalTv = (TextView) view.findViewById(R.id.awayGoal);
        }
    }
}
