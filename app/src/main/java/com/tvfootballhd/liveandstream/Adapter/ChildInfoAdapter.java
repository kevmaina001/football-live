package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.InfoResponse;
import com.tvfootballhd.liveandstream.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChildInfoAdapter extends RecyclerView.Adapter<ChildInfoAdapter.MyHolder> {
    private Context context;
    int id;
    private ItemClick itemClick;
    private ArrayList<InfoResponse.Response> modelList;

    public interface ItemClick {
        void onClick(int i);
    }

    public ChildInfoAdapter(Context context2, ArrayList<InfoResponse.Response> arrayList, int i) {
        this.context = context2;
        this.modelList = arrayList;
        this.id = i;
    }

    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.child_info_list, viewGroup, false));
    }

    public void onBindViewHolder(MyHolder myHolder, int i) {
        InfoResponse.Response response = this.modelList.get(i);
        myHolder.goalCountTV.setText(response.getGoals().getHome() + "-" + response.getGoals().getAway());
        if (response.getFixture().getDate() != null) {
            myHolder.dateTV.setText(new SimpleDateFormat("dd MMM yyyy").format(response.getFixture().getDate()));
        }
        if (response.getTeams().getHome().getId() == this.id) {
            if (response.getTeams().getAway().getName() != null) {
                myHolder.teamNameTV.setText(response.getTeams().getAway().getName());
            }
            if (response.getTeams().getHome().isWinner()) {
                myHolder.mainLL.setBackground(this.context.getDrawable(R.drawable.win));
                myHolder.statusTV.setText(ExifInterface.LONGITUDE_WEST);
                myHolder.statusTV.setTextColor(this.context.getColor(R.color.black));
            } else if (response.getTeams().getHome().isWinner() || response.getTeams().getAway().isWinner()) {
                myHolder.mainLL.setBackground(this.context.getDrawable(R.drawable.lost));
                myHolder.statusTV.setText("L");
            } else {
                myHolder.mainLL.setBackground(this.context.getDrawable(R.drawable.draw));
                myHolder.statusTV.setText("D");
            }
        } else {
            if (response.getTeams().getHome().getName() != null) {
                myHolder.teamNameTV.setText(response.getTeams().getHome().getName());
            }
            if (response.getTeams().getAway().isWinner()) {
                myHolder.mainLL.setBackground(this.context.getDrawable(R.drawable.win));
                myHolder.statusTV.setText(ExifInterface.LONGITUDE_WEST);
                myHolder.statusTV.setTextColor(this.context.getColor(R.color.black));
            } else if (response.getTeams().getHome().isWinner() || response.getTeams().getAway().isWinner()) {
                myHolder.mainLL.setBackground(this.context.getDrawable(R.drawable.lost));
                myHolder.statusTV.setText("L");
            } else {
                myHolder.mainLL.setBackground(this.context.getDrawable(R.drawable.draw));
                myHolder.statusTV.setText("D");
            }
        }
    }

    public int getItemCount() {
        return this.modelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView dateTV;
        TextView goalCountTV;
        LinearLayout mainLL;
        TextView statusTV;
        TextView teamNameTV;

        public MyHolder(View view) {
            super(view);
            this.mainLL = (LinearLayout) view.findViewById(R.id.childLL);
            this.statusTV = (TextView) view.findViewById(R.id.statusId);
            this.goalCountTV = (TextView) view.findViewById(R.id.goalCountTV);
            this.teamNameTV = (TextView) view.findViewById(R.id.oponentNameTV);
            this.dateTV = (TextView) view.findViewById(R.id.playDateTV);
        }
    }
}
