package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.PlayerStateResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;

public class PlayerStateAdapter extends RecyclerView.Adapter<PlayerStateAdapter.PlayerStateAdapterHolder> {
    private Context context;
    private PlayerStateResponse playerStateResponse;

    public PlayerStateAdapter(PlayerStateResponse playerStateResponse2, Context context2) {
        this.playerStateResponse = playerStateResponse2;
        this.context = context2;
    }

    public PlayerStateAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PlayerStateAdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.player_state_list, viewGroup, false));
    }

    public void onBindViewHolder(PlayerStateAdapterHolder playerStateAdapterHolder, int i) {
        PlayerStateResponse.Statistic statistic = this.playerStateResponse.getResponse().get(this.playerStateResponse.getResponse().size() - 1).getStatistics().get(i);
        if (statistic.getLeague().getName() != null) {
            playerStateAdapterHolder.leagueNameTV.setText(statistic.getLeague().getName());
        }
        if (statistic.getLeague().getLogo() != null) {
            Picasso.get().load(statistic.getLeague().getLogo()).into(playerStateAdapterHolder.leagueIcon);
        }
        if (statistic.getGames() != null) {
            playerStateAdapterHolder.totalGameTV.setText(String.valueOf(statistic.getGames().getAppearences()));
        } else {
            playerStateAdapterHolder.totalGameTV.setText("0");
        }
        if (statistic.getGoals() != null) {
            playerStateAdapterHolder.totalGoalTV.setText(String.valueOf(statistic.getGoals().getTotal()));
        } else {
            playerStateAdapterHolder.totalGoalTV.setText("0");
        }
        if (statistic.getShots() != null) {
            playerStateAdapterHolder.totalShotTV.setText(String.valueOf(statistic.getShots().getTotal()));
        } else {
            playerStateAdapterHolder.totalShotTV.setText("0");
        }
        if (statistic.getCards() != null) {
            playerStateAdapterHolder.totalYellowCardTV.setText(String.valueOf(statistic.getCards().getYellow()));
        } else {
            playerStateAdapterHolder.totalYellowCardTV.setText("0");
        }
        if (statistic.getCards() != null) {
            playerStateAdapterHolder.totalRedCardTV.setText(String.valueOf(statistic.getCards().getRed()));
        } else {
            playerStateAdapterHolder.totalRedCardTV.setText("0");
        }
    }

    public int getItemCount() {
        return this.playerStateResponse.getResponse().get(this.playerStateResponse.getResponse().size() - 1).getStatistics().size();
    }

    public class PlayerStateAdapterHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView leagueIcon;
        /* access modifiers changed from: private */
        public TextView leagueNameTV;
        /* access modifiers changed from: private */
        public TextView totalGameTV;
        /* access modifiers changed from: private */
        public TextView totalGoalTV;
        /* access modifiers changed from: private */
        public TextView totalRedCardTV;
        /* access modifiers changed from: private */
        public TextView totalShotTV;
        /* access modifiers changed from: private */
        public TextView totalYellowCardTV;

        public PlayerStateAdapterHolder(View view) {
            super(view);
            this.totalGameTV = (TextView) view.findViewById(R.id.totalGameId);
            this.totalGoalTV = (TextView) view.findViewById(R.id.totalGoalId);
            this.totalShotTV = (TextView) view.findViewById(R.id.totalShotId);
            this.totalYellowCardTV = (TextView) view.findViewById(R.id.totalYellowCardId);
            this.totalRedCardTV = (TextView) view.findViewById(R.id.totalRedCardId);
            this.leagueNameTV = (TextView) view.findViewById(R.id.leagueNameId);
            this.leagueIcon = (ImageView) view.findViewById(R.id.leagueIconId);
        }
    }
}
