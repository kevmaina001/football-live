package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.LastFiftyMatches;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RecentMatchAdapter extends RecyclerView.Adapter<RecentMatchAdapter.RecentMatchAdapterHolder> {
    private ClickListener clickListener;
    private Context context;
    private ArrayList<LastFiftyMatches.Response> responseArrayList;
    private int size;

    public interface ClickListener {
        void onClick(int i);
    }

    public void setClickListener(ClickListener clickListener2) {
        this.clickListener = clickListener2;
    }

    public RecentMatchAdapter(Context context2, ArrayList<LastFiftyMatches.Response> arrayList, int i) {
        this.context = context2;
        this.responseArrayList = arrayList;
        this.size = i;
    }

    public RecentMatchAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecentMatchAdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.recent_match_layout, viewGroup, false), this.clickListener);
    }

    public void onBindViewHolder(RecentMatchAdapterHolder recentMatchAdapterHolder, int i) {
        LastFiftyMatches.Response response = this.responseArrayList.get(i);
        if (response.getTeams().getHome().getName() != null) {
            recentMatchAdapterHolder.homeName.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            recentMatchAdapterHolder.awayName.setText(response.getTeams().getAway().getName());
        }
        if (response.getFixture().getStatus().getMyshort() != null) {
            recentMatchAdapterHolder.statusTV.setText(response.getFixture().getStatus().getMyshort());
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(recentMatchAdapterHolder.homeImageView);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(recentMatchAdapterHolder.awayImageView);
        }
        recentMatchAdapterHolder.goalTV.setText(response.getGoals().getHome() + "-" + response.getGoals().getAway());
    }

    public int getItemCount() {
        return this.size;
    }

    public class RecentMatchAdapterHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView awayImageView;
        TextView awayName;
        TextView goalTV;
        /* access modifiers changed from: private */
        public ImageView homeImageView;
        TextView homeName;
        TextView statusTV;

        public RecentMatchAdapterHolder(View view, ClickListener clickListener) {
            super(view);
            this.homeName = (TextView) view.findViewById(R.id.homeNameId);
            this.awayName = (TextView) view.findViewById(R.id.awayNameId);
            this.statusTV = (TextView) view.findViewById(R.id.statusId);
            this.goalTV = (TextView) view.findViewById(R.id.goalId);
            this.homeImageView = (ImageView) view.findViewById(R.id.homeImageId);
            this.awayImageView = (ImageView) view.findViewById(R.id.awayImageId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(getAdapterPosition());
                }
            });
        }

    }
}
