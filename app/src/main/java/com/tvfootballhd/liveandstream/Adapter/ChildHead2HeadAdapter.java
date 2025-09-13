package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.PredictionResponse;
import com.tvfootballhd.liveandstream.R;

import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChildHead2HeadAdapter extends RecyclerView.Adapter<ChildHead2HeadAdapter.ChildHead2HeadAdapterHolder> {
    private Context context;
    private ArrayList<PredictionResponse.H2h> responseArrayList;

    public ChildHead2HeadAdapter(Context context2, ArrayList<PredictionResponse.H2h> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public ChildHead2HeadAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ChildHead2HeadAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_head2head_layout, viewGroup, false));
    }

    public void onBindViewHolder(ChildHead2HeadAdapterHolder childHead2HeadAdapterHolder, int i) {
        PredictionResponse.H2h h2h = this.responseArrayList.get(i);
        if (h2h.getTeams().getHome().getName() != null) {
            childHead2HeadAdapterHolder.homeNameTV.setText(h2h.getTeams().getHome().getName());
        }
        if (h2h.getTeams().getAway().getName() != null) {
            childHead2HeadAdapterHolder.awayNameTV.setText(h2h.getTeams().getAway().getName());
        }
        if (h2h.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(h2h.getTeams().getHome().getLogo()).into(childHead2HeadAdapterHolder.homeFlag);
        }
        if (h2h.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(h2h.getTeams().getAway().getLogo()).into(childHead2HeadAdapterHolder.awayFlag);
        }
        if (h2h.getFixture().getDate() != null) {
            childHead2HeadAdapterHolder.dateTV.setText(new SimpleDateFormat("dd-MMM-yy").format(h2h.getFixture().getDate()));
            childHead2HeadAdapterHolder.dateTimeTV.setText(new SimpleDateFormat("hh:mm aa").format(h2h.getFixture().getDate()));
        }
        childHead2HeadAdapterHolder.homeGoalTV.setText(String.valueOf(h2h.getGoals().getHome()));
        childHead2HeadAdapterHolder.awayGoalTv.setText(String.valueOf(h2h.getGoals().getAway()));
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class ChildHead2HeadAdapterHolder extends RecyclerView.ViewHolder {
        ImageView awayFlag;
        TextView awayGoalTv;
        TextView awayNameTV;
        TextView dateTV;
        TextView dateTimeTV;
        ImageView homeFlag;
        TextView homeGoalTV;
        TextView homeNameTV;
        ImageView notificationIcon;

        public ChildHead2HeadAdapterHolder(View view) {
            super(view);
            this.homeFlag = (ImageView) view.findViewById(R.id.leagueHomeFlagId);
            this.awayFlag = (ImageView) view.findViewById(R.id.leagueAwayFlagId);
            this.homeNameTV = (TextView) view.findViewById(R.id.leagueHomeNameId);
            this.awayNameTV = (TextView) view.findViewById(R.id.leagueAwayNameId);
            this.dateTV = (TextView) view.findViewById(R.id.dateTV);
            this.dateTimeTV = (TextView) view.findViewById(R.id.timeTVId);
            this.homeGoalTV = (TextView) view.findViewById(R.id.homeGoal);
            this.awayGoalTv = (TextView) view.findViewById(R.id.awayGoal);
        }
    }
}
