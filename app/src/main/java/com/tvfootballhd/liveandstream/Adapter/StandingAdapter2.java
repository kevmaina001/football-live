package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.StandingResponse;
import com.tvfootballhd.liveandstream.R;

import java.util.ArrayList;

public class StandingAdapter2 extends RecyclerView.Adapter<StandingAdapter2.StandingAdapter2Holder> {
    private int awayId;
    /* access modifiers changed from: private */
    public Context context;
    private int homeId;
    private ArrayList<ArrayList<StandingResponse.Standing>> listArrayList;

    public StandingAdapter2(Context context2, ArrayList<ArrayList<StandingResponse.Standing>> arrayList, int i, int i2) {
        this.context = context2;
        this.listArrayList = arrayList;
        this.homeId = i;
        this.awayId = i2;
    }

    public StandingAdapter2Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StandingAdapter2Holder(LayoutInflater.from(this.context).inflate(R.layout.standing_list2, viewGroup, false));
    }

    public void onBindViewHolder(StandingAdapter2Holder standingAdapter2Holder, int i) {
        standingAdapter2Holder.recyclerView.setAdapter(new StandingAdapter(this.context, this.listArrayList.get(i), standingAdapter2Holder.textView, this.homeId, this.awayId));
    }

    public int getItemCount() {
        return this.listArrayList.size();
    }

    public class StandingAdapter2Holder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public RecyclerView recyclerView;
        /* access modifiers changed from: private */
        public TextView textView;

        public StandingAdapter2Holder(View view) {
            super(view);
            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.standing2RecyclerView);
            this.recyclerView = recyclerView2;
            recyclerView2.setLayoutManager(new LinearLayoutManager(StandingAdapter2.this.context));
            this.textView = (TextView) view.findViewById(R.id.groupTextId);
        }
    }
}
