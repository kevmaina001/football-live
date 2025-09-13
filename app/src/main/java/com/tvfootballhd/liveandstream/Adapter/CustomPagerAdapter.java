package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.Prediction;
import com.tvfootballhd.liveandstream.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomPagerAdapter extends RecyclerView.Adapter<CustomPagerAdapter.AdapterHolder> {
    private ArrayList<Prediction> arrayList;
    private Context context;
    private Date gameDate;

    public CustomPagerAdapter(Context context2, ArrayList<Prediction> arrayList2, Date date) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.gameDate = date;
    }

    public AdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.prediction_list, viewGroup, false));
    }

    public void onBindViewHolder(AdapterHolder adapterHolder, int i) {
        Prediction prediction = this.arrayList.get(i);
        if (prediction.getName() != null) {
            adapterHolder.titleTextView.setText(prediction.getName());
        }
        if (prediction.getHomeName() != null) {
            adapterHolder.homeName.setText(prediction.getHomeName());
        }
        if (prediction.getAwayName() != null) {
            adapterHolder.awayName.setText(prediction.getAwayName());
        }
        if (prediction.getHomeValue() != null) {
            adapterHolder.homeResultTV.setText(prediction.getHomeValue());
        }
        if (prediction.getAwayValue() != null) {
            adapterHolder.awayResultTV.setText(prediction.getAwayValue());
        }
        if (prediction.getDrawName() != null) {
            adapterHolder.drawName.setText(prediction.getDrawName());
        }
        if (prediction.getDrawValue() != null) {
            adapterHolder.drawResultTV.setText(prediction.getDrawValue());
        }
        if (this.gameDate != null) {
            adapterHolder.dateTV.setText(new SimpleDateFormat("dd-MMM-yy").format(this.gameDate) + "\n" + new SimpleDateFormat("hh:mm a").format(this.gameDate));
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder {
        TextView awayName = ((TextView) this.itemView.findViewById(R.id.awayNameId));
        TextView awayResultTV = ((TextView) this.itemView.findViewById(R.id.awayResultId));
        TextView dateTV = ((TextView) this.itemView.findViewById(R.id.dateTV));
        TextView drawName = ((TextView) this.itemView.findViewById(R.id.drawNameId));
        TextView drawResultTV = ((TextView) this.itemView.findViewById(R.id.drawResultId));
        TextView homeName = ((TextView) this.itemView.findViewById(R.id.homeNameId));
        TextView homeResultTV = ((TextView) this.itemView.findViewById(R.id.homeResultId));
        TextView titleTextView = ((TextView) this.itemView.findViewById(R.id.titleId));

        public AdapterHolder(View view) {
            super(view);
        }
    }
}
