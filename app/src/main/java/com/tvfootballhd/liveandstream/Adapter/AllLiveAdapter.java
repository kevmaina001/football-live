package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Activity.LeagueDetailsActivity;
import com.tvfootballhd.liveandstream.Model.LiveResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AllLiveAdapter extends RecyclerView.Adapter<AllLiveAdapter.AllLiveAdapterHoldser> {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public ArrayList<ArrayList<LiveResponse.Response>> responseArrayList;

    public AllLiveAdapter(Context context2, ArrayList<ArrayList<LiveResponse.Response>> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public AllLiveAdapterHoldser onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AllLiveAdapterHoldser(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.today_match_list, viewGroup, false));
    }

    public void onBindViewHolder(AllLiveAdapterHoldser allLiveAdapterHoldser, int i) {
        ArrayList arrayList = this.responseArrayList.get(i);
        allLiveAdapterHoldser.leagueNameTV.setText(((LiveResponse.Response) arrayList.get(0)).getLeague().getName() + ", " + ((LiveResponse.Response) arrayList.get(0)).getLeague().getCountry());
        if (((LiveResponse.Response) arrayList.get(0)).getLeague().getLogo() != null) {
            Picasso.get().load(((LiveResponse.Response) arrayList.get(0)).getLeague().getLogo()).into(allLiveAdapterHoldser.leagueLogoImageView);
        }
        ViewCompat.setNestedScrollingEnabled(allLiveAdapterHoldser.individualLeagueRecyclerView, false);
        allLiveAdapterHoldser.individualLeagueRecyclerView.setAdapter(new LiveAdapter(this.context, arrayList));
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class AllLiveAdapterHoldser extends RecyclerView.ViewHolder {
        RecyclerView individualLeagueRecyclerView;
        ImageView leagueLogoImageView;
        TextView leagueNameTV;

        public AllLiveAdapterHoldser(View view) {
            super(view);
            this.leagueNameTV = (TextView) view.findViewById(R.id.todayMatchLeagueNameId);
            this.leagueLogoImageView = (ImageView) view.findViewById(R.id.todayMatchLeagueIconId);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.todayIndividualLeagueMatchId);
            this.individualLeagueRecyclerView = recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(AllLiveAdapter.this.context));
            this.leagueNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newResponse(view);
                }
            });
        }

        public void newResponse(View view) {
            LiveResponse.Response response = (LiveResponse.Response) ((ArrayList) AllLiveAdapter.this.responseArrayList.get(getAdapterPosition())).get(0);
            Intent intent = new Intent(AllLiveAdapter.this.context, LeagueDetailsActivity.class);
            intent.putExtra("leagueId", response.getLeague().getId());
            intent.putExtra("year", response.getLeague().getSeason());
            AllLiveAdapter.this.context.startActivity(intent);
        }
    }
}
