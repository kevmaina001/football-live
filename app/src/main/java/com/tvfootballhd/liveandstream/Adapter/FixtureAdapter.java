package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public abstract class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureAdapterHoldser> {
    /* access modifiers changed from: private */
    public Context context;
    private EmranClick emranClick;
    /* access modifiers changed from: private */
    public ArrayList<FixtureResponse.Response> responseArrayList;

    public interface EmranClick {
        void onItemClicked(int i);
    }

    public void setEmranClick(EmranClick emranClick2) {
        this.emranClick = emranClick2;
    }

    public FixtureAdapter(Context context2, ArrayList<FixtureResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public FixtureAdapterHoldser onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FixtureAdapterHoldser(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match_layout_2, viewGroup, false), this.emranClick);
    }

    public void onBindViewHolder(FixtureAdapterHoldser fixtureAdapterHoldser, int i) {
        FixtureResponse.Response response = this.responseArrayList.get(i);
        if (response.getTeams().getHome().getName() != null) {
            fixtureAdapterHoldser.homeNameTV.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            fixtureAdapterHoldser.awayNameTV.setText(response.getTeams().getAway().getName());
        }
        if (response.getLeague().getName() != null) {
            fixtureAdapterHoldser.leagueTV.setText(response.getLeague().getName());
        }
        if (response.getFixture().getDate() != null) {
            fixtureAdapterHoldser.timeTextView.setText(new SimpleDateFormat("hh:mm a").format(response.getFixture().getDate()));
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(fixtureAdapterHoldser.homeFlag);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(fixtureAdapterHoldser.awayFlag);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class FixtureAdapterHoldser extends RecyclerView.ViewHolder {
        ImageView awayFlag;
        TextView awayNameTV;
        TextView dateTV;
        ImageView homeFlag;
        TextView homeNameTV;
        TextView leagueTV;
        TextView timeTextView;

        public FixtureAdapterHoldser(View view, final EmranClick emranClick) {
            super(view);
            this.homeFlag = (ImageView) view.findViewById(R.id.matchHomeFlagId);
            this.awayFlag = (ImageView) view.findViewById(R.id.matchAwayFlagId);
            this.homeNameTV = (TextView) view.findViewById(R.id.matchHomeNameId);
            this.awayNameTV = (TextView) view.findViewById(R.id.matchAwayNameId);
            this.timeTextView = (TextView) view.findViewById(R.id.matchTimeOrGoalTVId);
            this.leagueTV = (TextView) view.findViewById(R.id.matchLeagueId);
            this.dateTV = (TextView) view.findViewById(R.id.matchDateId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(FixtureAdapter.this.context, String.valueOf(((FixtureResponse.Response) FixtureAdapter.this.responseArrayList.get(getAdapterPosition())).getFixture().getId()), Toast.LENGTH_SHORT).show();

                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    emranClick.onItemClicked(FixtureAdapterHoldser.this.getAdapterPosition());
                }
            });
        }

    }
}
