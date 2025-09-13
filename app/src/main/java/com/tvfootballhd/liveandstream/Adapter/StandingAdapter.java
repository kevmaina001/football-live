package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.StandingResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class StandingAdapter extends RecyclerView.Adapter<StandingAdapter.StandingAdapterHolder> {
    private int awayId;
    private Context context;
    private int homeId;
    private ArrayList<StandingResponse.Standing> responseArrayList;
    private TextView textView;

    public StandingAdapter(Context context2, ArrayList<StandingResponse.Standing> arrayList, TextView textView2, int i, int i2) {
        this.context = context2;
        this.responseArrayList = arrayList;
        this.textView = textView2;
        this.homeId = i;
        this.awayId = i2;
    }

    public StandingAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StandingAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.standing_list, viewGroup, false));
    }

    public void onBindViewHolder(StandingAdapterHolder standingAdapterHolder, int i) {
        StandingResponse.Standing standing = this.responseArrayList.get(i);
        if (standing.getTeam().getLogo() != null) {
            Picasso.get().load(standing.getTeam().getLogo()).into(standingAdapterHolder.teamIcon);
        }
        if (standing.getGroup() != null) {
            this.textView.setText(standing.getGroup());
        }
        if (standing.getTeam().getName() != null) {
            standingAdapterHolder.teamNameTV.setText(standing.getTeam().getName());
        }
        standingAdapterHolder.rankTV.setText(String.valueOf(standing.getRank()));
        standingAdapterHolder.playTV.setText(String.valueOf(standing.getAll().getPlayed()));
        standingAdapterHolder.winTV.setText(String.valueOf(standing.getAll().getWin()));
        standingAdapterHolder.lostTV.setText(String.valueOf(standing.getAll().getLose()));
        standingAdapterHolder.drawTV.setText(String.valueOf(standing.getAll().getDraw()));
        standingAdapterHolder.pointsTV.setText(String.valueOf(standing.getPoints()));
        standingAdapterHolder.goalTV.setText(String.valueOf((standing.getAll().getGoals().getAge() + standing.getGoalsDiff()) + ":" + standing.getAll().getGoals().getAge()));
        if (standing.getTeam().getId() != this.homeId && standing.getTeam().getId() != this.awayId) {
            standingAdapterHolder.view.setVisibility(8);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class StandingAdapterHolder extends RecyclerView.ViewHolder {
        TextView drawTV;
        TextView goalTV;
        LinearLayout linearLayout;
        TextView lostTV;
        TextView playTV;
        TextView pointsTV;
        TextView rankTV;
        ImageView teamIcon;
        TextView teamNameTV;
        View view;
        TextView winTV;

        public StandingAdapterHolder(View view2) {
            super(view2);
            this.rankTV = (TextView) view2.findViewById(R.id.rankTVId);
            this.teamNameTV = (TextView) view2.findViewById(R.id.teamNameId);
            this.playTV = (TextView) view2.findViewById(R.id.playedTVId);
            this.winTV = (TextView) view2.findViewById(R.id.wonTVId);
            this.lostTV = (TextView) view2.findViewById(R.id.lostTVId);
            this.drawTV = (TextView) view2.findViewById(R.id.drawTVId);
            this.goalTV = (TextView) view2.findViewById(R.id.goalsTVId);
            this.pointsTV = (TextView) view2.findViewById(R.id.pointsTVId);
            this.teamIcon = (ImageView) view2.findViewById(R.id.teamIconId);
            this.view = view2.findViewById(R.id.viewId);
            this.linearLayout = (LinearLayout) view2.findViewById(R.id.linearLayout);
        }
    }
}
