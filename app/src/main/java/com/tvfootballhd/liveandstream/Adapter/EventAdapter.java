package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.EventResponse;
import com.tvfootballhd.liveandstream.R;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterHolder> {
    private int awayTeamId;
    private Context context;
    private int homeTeamId;
    private ArrayList<EventResponse.Response> responseArrayList;

    public EventAdapter(Context context2, ArrayList<EventResponse.Response> arrayList, int i, int i2) {
        this.context = context2;
        this.responseArrayList = arrayList;
        this.homeTeamId = i;
        this.awayTeamId = i2;
    }

    public EventAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new EventAdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.event_list, viewGroup, false));
    }

    public void onBindViewHolder(EventAdapterHolder eventAdapterHolder, int i) {
        EventResponse.Response response = this.responseArrayList.get(i);
        eventAdapterHolder.timeTextView.setText(response.getTime().getElapsed() + "'");
        eventAdapterHolder.homeEventReplacedPlayerName.setVisibility(8);
        if (response.getTeam().getName() != null) {
            eventAdapterHolder.teamName.setText(response.getTeam().getName());
        }
        if (response.getTeam().getLogo() != null) {
            Picasso.get().load(response.getTeam().getLogo()).into(eventAdapterHolder.teamIcon);
        }
        if (response.getType().contentEquals("Goal")) {
            eventAdapterHolder.homeEventImageView.setImageDrawable(this.context.getDrawable(R.drawable.football));
            if (response.getPlayer().getName() != null) {
                eventAdapterHolder.homeEventPlayerName.setText(response.getPlayer().getName());
            }
        }
        if (response.getType().contentEquals("Card")) {
            if (response.getDetail().contentEquals("Yellow Card")) {
                eventAdapterHolder.homeEventImageView.setImageDrawable(this.context.getDrawable(R.drawable.yellow_card));
                if (response.getPlayer().getName() != null) {
                    eventAdapterHolder.homeEventPlayerName.setText(response.getPlayer().getName());
                }
            } else {
                eventAdapterHolder.homeEventImageView.setImageDrawable(this.context.getDrawable(R.drawable.red_card));
                if (response.getPlayer().getName() != null) {
                    eventAdapterHolder.homeEventPlayerName.setText(response.getPlayer().getName());
                }
            }
        }
        if (response.getType().contentEquals("subst")) {
            eventAdapterHolder.homeEventImageView.setImageDrawable(this.context.getDrawable(R.drawable.player_change));
            if (response.getPlayer().getName() != null) {
                eventAdapterHolder.homeEventPlayerName.setText(response.getPlayer().getName());
            }
            if (response.getAssist().getName() != null) {
                eventAdapterHolder.homeEventReplacedPlayerName.setText(response.getAssist().getName());
            }
            eventAdapterHolder.homeEventReplacedPlayerName.setTextColor(this.context.getResources().getColor(R.color.red));
            eventAdapterHolder.homeEventPlayerName.setTextColor(this.context.getResources().getColor(R.color.green));
            eventAdapterHolder.homeEventReplacedPlayerName.setVisibility(0);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class EventAdapterHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView homeEventImageView;
        /* access modifiers changed from: private */
        public TextView homeEventPlayerName;
        /* access modifiers changed from: private */
        public TextView homeEventReplacedPlayerName;
        /* access modifiers changed from: private */
        public ImageView teamIcon;
        /* access modifiers changed from: private */
        public TextView teamName;
        /* access modifiers changed from: private */
        public TextView timeTextView;

        public EventAdapterHolder(View view) {
            super(view);
            this.timeTextView = (TextView) view.findViewById(R.id.elapseTimeTVId);
            this.homeEventPlayerName = (TextView) view.findViewById(R.id.homePlayerId);
            this.homeEventImageView = (ImageView) view.findViewById(R.id.homeEventImageId);
            this.homeEventReplacedPlayerName = (TextView) view.findViewById(R.id.homeReplacedPlayerId);
            this.teamName = (TextView) view.findViewById(R.id.teamNameId);
            this.teamIcon = (ImageView) view.findViewById(R.id.teamImageId);
        }
    }
}
