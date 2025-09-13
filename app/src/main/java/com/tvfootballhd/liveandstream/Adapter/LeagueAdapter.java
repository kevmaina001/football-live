package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.CurrentLeague;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.LeagueAdapterHolder> {
    private Context context;
    CurrentLeague.Response leagueResponse;
    private OnClickListener onClickListener;
    /* access modifiers changed from: private */
    public ArrayList<CurrentLeague.Response> responseArrayList;

    public interface OnClickListener {
        void OnClick(int i);
    }

    public LeagueAdapter(Context context2, ArrayList<CurrentLeague.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public void filterList(ArrayList<CurrentLeague.Response> arrayList) {
        this.responseArrayList = arrayList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener2) {
        this.onClickListener = onClickListener2;
    }

    public LeagueAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LeagueAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.league_layout, viewGroup, false), this.onClickListener);
    }

    public void onBindViewHolder(LeagueAdapterHolder leagueAdapterHolder, int i) {
        CurrentLeague.Response response = this.responseArrayList.get(i);
        if (response.getLeague().getName() != null) {
            leagueAdapterHolder.leagueNameTV.setText(response.getLeague().getName());
        }
        if (response.getCountry().getName() != null) {
            leagueAdapterHolder.leagueCountryName.setText(response.getCountry().getName());
        }
        if (response.getLeague().getLogo() != null) {
            Picasso.get().load(response.getLeague().getLogo()).into(leagueAdapterHolder.leagueIcon);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class LeagueAdapterHolder extends RecyclerView.ViewHolder {
        TextView leagueCountryName;
        ImageView leagueIcon;
        TextView leagueNameTV;

        public LeagueAdapterHolder(View view, final OnClickListener onClickListener) {
            super(view);
            this.leagueIcon = (ImageView) view.findViewById(R.id.leagueIconId);
            this.leagueNameTV = (TextView) view.findViewById(R.id.leagueNameId);
            this.leagueCountryName = (TextView) view.findViewById(R.id.durationDate);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    LeagueAdapter.this.leagueResponse = (CurrentLeague.Response) LeagueAdapter.this.responseArrayList.get(LeagueAdapterHolder.this.getAdapterPosition());
                    onClickListener.OnClick(LeagueAdapterHolder.this.getAdapterPosition());
                }
            });
        }
    }

    public CurrentLeague.Response getResult() {
        return this.leagueResponse;
    }
}
