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

public class CurrentLeagueAdapter extends RecyclerView.Adapter<CurrentLeagueAdapter.CurrentLeagueAdapterHolder> {
    private Context context;
    CurrentLeague.Response leagueResponse;
    private OnClickListener onClickListener;
    /* access modifiers changed from: private */
    public ArrayList<CurrentLeague.Response> responseArrayList;

    public interface OnClickListener {
        void OnClick(int i);
    }

    public CurrentLeagueAdapter(Context context2, ArrayList<CurrentLeague.Response> arrayList) {
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

    public CurrentLeagueAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CurrentLeagueAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.current_league_layout, viewGroup, false), this.onClickListener);
    }

    public void onBindViewHolder(CurrentLeagueAdapterHolder currentLeagueAdapterHolder, int i) {
        CurrentLeague.Response response = this.responseArrayList.get(i);
        if (response.getLeague().getName() != null) {
            currentLeagueAdapterHolder.leagueNameTV.setText(response.getLeague().getName());
        }
        if (response.getCountry().getName() != null) {
            currentLeagueAdapterHolder.leagueCountryName.setText(response.getCountry().getName());
        }
        if (response.getLeague().getLogo() != null) {
            Picasso.get().load(response.getLeague().getLogo()).into(currentLeagueAdapterHolder.leagueIcon);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class CurrentLeagueAdapterHolder extends RecyclerView.ViewHolder {
        TextView leagueCountryName;
        ImageView leagueIcon;
        TextView leagueNameTV;

        public CurrentLeagueAdapterHolder(View view, final OnClickListener onClickListener) {
            super(view);
            this.leagueIcon = (ImageView) view.findViewById(R.id.leagueIconId);
            this.leagueNameTV = (TextView) view.findViewById(R.id.leagueNameId);
            this.leagueCountryName = (TextView) view.findViewById(R.id.leagueCountryNameId);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CurrentLeagueAdapter.this.leagueResponse = (CurrentLeague.Response) CurrentLeagueAdapter.this.responseArrayList.get(CurrentLeagueAdapterHolder.this.getAdapterPosition());
                    onClickListener.OnClick(CurrentLeagueAdapterHolder.this.getAdapterPosition());
                }
            });
        }
    }

    public CurrentLeague.Response getResult() {
        return this.leagueResponse;
    }
}
