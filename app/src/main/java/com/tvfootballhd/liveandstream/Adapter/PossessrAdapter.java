package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tvfootballhd.liveandstream.Model.TeamStatisticsResponse;
import com.tvfootballhd.liveandstream.R;

import java.util.ArrayList;

public class PossessrAdapter extends RecyclerView.Adapter<PossessrAdapter.PossessAdapterHolder> {
    private Context context;
    private ArrayList<TeamStatisticsResponse.Response> responseArrayList;

    public PossessrAdapter(Context context2, ArrayList<TeamStatisticsResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
        arrayList.get(0).getStatistics().remove(9);
        arrayList.get(1).getStatistics().remove(9);
    }

    public PossessAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PossessAdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.posses_list, viewGroup, false));
    }

    public void onBindViewHolder(PossessAdapterHolder possessAdapterHolder, int i) {
        if (this.responseArrayList.get(0).getStatistics().get(i).getType() != null) {
            possessAdapterHolder.type.setText(this.responseArrayList.get(0).getStatistics().get(i).getType());
        }
        String valueOf = String.valueOf(this.responseArrayList.get(1).getStatistics().get(i).getValue());
        String valueOf2 = String.valueOf(this.responseArrayList.get(0).getStatistics().get(i).getValue());
        if (valueOf.contentEquals("null")) {
            valueOf = "0";
        }
        if (valueOf.contains(".")) {
            valueOf = valueOf.substring(0, valueOf.indexOf("."));
        }
        if (valueOf.contains("%")) {
            valueOf = valueOf.substring(0, valueOf.indexOf("%"));
        }
        if (valueOf2.contentEquals("null")) {
            valueOf2 = "0";
        }
        if (valueOf2.contains(".")) {
            valueOf2 = valueOf2.substring(0, valueOf2.indexOf("."));
        }
        if (valueOf2.contains("%")) {
            valueOf2 = valueOf2.substring(0, valueOf2.indexOf("%"));
        }
        possessAdapterHolder.homeCount.setText(valueOf2);
        possessAdapterHolder.awayCount.setText(valueOf);
        int intValue = Integer.valueOf(valueOf2).intValue();
        int intValue2 = Integer.valueOf(valueOf).intValue();
        int i2 = intValue + intValue2;
        if (i2 > 0) {
            possessAdapterHolder.home.setProgress((intValue * 100) / i2);
            possessAdapterHolder.away.setProgress((intValue2 * 100) / i2);
            return;
        }
        possessAdapterHolder.home.setProgress(0);
        possessAdapterHolder.away.setProgress(0);
    }

    public int getItemCount() {
        return this.responseArrayList.get(0).getStatistics().size();
    }

    public class PossessAdapterHolder extends RecyclerView.ViewHolder {
        LinearProgressIndicator away;
        TextView awayCount;
        LinearProgressIndicator home;
        TextView homeCount;
        TextView type;

        public PossessAdapterHolder(View view) {
            super(view);
            this.home = (LinearProgressIndicator) view.findViewById(R.id.homeTypeId);
            this.type = (TextView) view.findViewById(R.id.typeId);
            this.away = (LinearProgressIndicator) view.findViewById(R.id.awayTypeId);
            this.homeCount = (TextView) view.findViewById(R.id.homeCountId);
            this.awayCount = (TextView) view.findViewById(R.id.awayCountId);
        }
    }
}
