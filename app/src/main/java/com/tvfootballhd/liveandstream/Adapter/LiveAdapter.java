package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Activity.LiveDetailsActivity;
import com.tvfootballhd.liveandstream.Model.LiveResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.LiveAdapterHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private OnclickListener onclickListener;
    /* access modifiers changed from: private */
    public ArrayList<LiveResponse.Response> responseArrayList;

    public interface OnclickListener {
        void onClick(int i);
    }

    public void setOnclickListener(OnclickListener onclickListener2) {
        this.onclickListener = onclickListener2;
    }

    public LiveAdapter(Context context2, ArrayList<LiveResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public LiveAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LiveAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.live_match_layout, viewGroup, false), this.onclickListener);
    }

    public void onBindViewHolder(LiveAdapterHolder liveAdapterHolder, int i) {
        LiveResponse.Response response = this.responseArrayList.get(i);
        if (response.getTeams().getHome().getName() != null) {
            liveAdapterHolder.homeNameTV.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            liveAdapterHolder.awayNameTV.setText(response.getTeams().getAway().getName());
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(liveAdapterHolder.homeFlag);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(liveAdapterHolder.awayFlag);
        }
        if (response.getFixture().getStatus().getMyshort().contentEquals("HT")) {
            liveAdapterHolder.timeTV.setText("HT");
        } else {
            liveAdapterHolder.timeTV.setText(response.getFixture().getStatus().getElapsed() + "'");
        }
        liveAdapterHolder.homeGoalTV.setText(response.getGoals().getHome() + "");
        liveAdapterHolder.awayGoalTV.setText(response.getGoals().getAway() + "");
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class LiveAdapterHolder extends RecyclerView.ViewHolder {
        ImageView awayFlag;
        TextView awayGoalTV;
        TextView awayNameTV;
        ImageView homeFlag;
        TextView homeGoalTV;
        TextView homeNameTV;
        TextView timeTV;

        public LiveAdapterHolder(View view, OnclickListener onclickListener) {
            super(view);
            this.homeFlag = (ImageView) view.findViewById(R.id.liveHomeFlagId);
            this.awayFlag = (ImageView) view.findViewById(R.id.liveAwayFlagId);
            this.homeNameTV = (TextView) view.findViewById(R.id.liveHomeNameId);
            this.awayNameTV = (TextView) view.findViewById(R.id.liveAwayNameId);
            this.homeGoalTV = (TextView) view.findViewById(R.id.homeGoalId);
            this.awayGoalTV = (TextView) view.findViewById(R.id.awayGoalId);
            this.timeTV = (TextView) view.findViewById(R.id.liveTimeId);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(LiveAdapter.this.context, LiveDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LiveResponseClass", (Serializable) LiveAdapter.this.responseArrayList.get(LiveAdapterHolder.this.getAdapterPosition()));
                    intent.putExtras(bundle);
                    LiveAdapter.this.context.startActivity(intent);
                }
            });
        }
    }
}
