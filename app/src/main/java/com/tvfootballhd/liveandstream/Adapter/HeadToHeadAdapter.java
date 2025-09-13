package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.PredictionResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class HeadToHeadAdapter extends RecyclerView.Adapter<HeadToHeadAdapter.HeadToHeadAdapterHolder> {
    private Context context;
    private ArrayList<ArrayList<PredictionResponse.H2h>> responseArrayList;

    public HeadToHeadAdapter(Context context2, ArrayList<ArrayList<PredictionResponse.H2h>> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public HeadToHeadAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new HeadToHeadAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.head_to_head_list, viewGroup, false));
    }

    public void onBindViewHolder(HeadToHeadAdapterHolder headToHeadAdapterHolder, int i) {
        ArrayList arrayList = this.responseArrayList.get(i);
        if (((PredictionResponse.H2h) arrayList.get(0)).getLeague().getName() != null) {
            headToHeadAdapterHolder.leagueNameTV.setText(((PredictionResponse.H2h) arrayList.get(0)).getLeague().getName());
        }
        if (((PredictionResponse.H2h) arrayList.get(0)).getLeague().getLogo() != null) {
            Picasso.get().load(((PredictionResponse.H2h) arrayList.get(0)).getLeague().getLogo()).into(headToHeadAdapterHolder.leagueIcon);
        }
        ChildHead2HeadAdapter childHead2HeadAdapter = new ChildHead2HeadAdapter(this.context, arrayList);
        headToHeadAdapterHolder.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
        ViewCompat.setNestedScrollingEnabled(headToHeadAdapterHolder.recyclerView, false);
        headToHeadAdapterHolder.recyclerView.setAdapter(childHead2HeadAdapter);
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class HeadToHeadAdapterHolder extends RecyclerView.ViewHolder {
        ImageView leagueIcon;
        TextView leagueNameTV;
        RecyclerView recyclerView;

        public HeadToHeadAdapterHolder(View view) {
            super(view);
            this.leagueIcon = (ImageView) view.findViewById(R.id.h2hMatchLeagueIconId);
            this.leagueNameTV = (TextView) view.findViewById(R.id.h2hMatchLeagueNameId);
            this.recyclerView = (RecyclerView) view.findViewById(R.id.h2hIndividualLeagueMatchId);
        }
    }
}
