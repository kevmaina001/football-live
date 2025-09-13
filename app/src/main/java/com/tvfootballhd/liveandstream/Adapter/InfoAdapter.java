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
import com.tvfootballhd.liveandstream.Model.InfoResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private int id;
    private ItemClick itemClick;
    private ArrayList<ArrayList<InfoResponse.Response>> modelList;

    public interface ItemClick {
        void onClick(int i);
    }

    public InfoAdapter(Context context2, ArrayList<ArrayList<InfoResponse.Response>> arrayList, int i) {
        this.context = context2;
        this.modelList = arrayList;
        this.id = i;
    }

    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.info_list_item, viewGroup, false));
    }

    public void onBindViewHolder(MyHolder myHolder, int i) {
        ArrayList arrayList = this.modelList.get(i);
        Collections.sort(arrayList, new Comparator<InfoResponse.Response>() {
            public int compare(InfoResponse.Response response, InfoResponse.Response response2) {
                return response2.getFixture().getDate().compareTo(response.getFixture().getDate());
            }
        });
        if (((InfoResponse.Response) arrayList.get(0)).getLeague().getName() != null) {
            myHolder.leagueNaeTv.setText(((InfoResponse.Response) arrayList.get(0)).getLeague().getName());
        }
        if (((InfoResponse.Response) arrayList.get(0)).getLeague().getLogo() != null) {
            Picasso.get().load(((InfoResponse.Response) arrayList.get(0)).getLeague().getLogo()).into(myHolder.leagueIconImageView);
        }
        ChildInfoAdapter childInfoAdapter = new ChildInfoAdapter(this.context, arrayList, this.id);
        ViewCompat.setNestedScrollingEnabled(myHolder.recyclerView, false);
        myHolder.recyclerView.setAdapter(childInfoAdapter);
    }

    public int getItemCount() {
        return this.modelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView leagueIconImageView;
        TextView leagueNaeTv;
        RecyclerView recyclerView;

        public MyHolder(View view) {
            super(view);
            this.leagueNaeTv = (TextView) view.findViewById(R.id.leagueNameInfoId);
            this.leagueIconImageView = (ImageView) view.findViewById(R.id.leagueIconId);
            RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.individualItemRecyclerView);
            this.recyclerView = recyclerView2;
            recyclerView2.setLayoutManager(new LinearLayoutManager(InfoAdapter.this.context));
        }
    }
}
