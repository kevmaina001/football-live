package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public abstract class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerAdapterHolder> {
    private Context context;
    private EmranClick emranClick;
    private ArrayList<FixtureResponse.Response> responseArrayList;

    public interface EmranClick {
        void click(int i);
    }

    public void setEmranClick(EmranClick emranClick2) {
        this.emranClick = emranClick2;
    }

    public ViewPagerAdapter(Context context2, ArrayList<FixtureResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public ViewPagerAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewPagerAdapterHolder(LayoutInflater.from(this.context).inflate(R.layout.slider_layout, viewGroup, false), this.emranClick);
    }

    public void onBindViewHolder(ViewPagerAdapterHolder viewPagerAdapterHolder, int i) {
        FixtureResponse.Response response = this.responseArrayList.get(i);
        if (response.getLeague().getName() != null) {
            viewPagerAdapterHolder.leagueNameTV.setText(response.getLeague().getName());
        }
        if (response.getLeague().getRound() != null) {
            viewPagerAdapterHolder.roundNameTV.setText(response.getLeague().getRound());
        }
        if (response.getTeams().getHome().getName() != null) {
            viewPagerAdapterHolder.homeName.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            viewPagerAdapterHolder.awayName.setText(response.getTeams().getAway().getName());
        }
        if (response.getFixture().getDate() != null) {
            viewPagerAdapterHolder.timeTV.setText(new SimpleDateFormat("dd-MMM-yy").format(response.getFixture().getDate()) + "\n" + new SimpleDateFormat("hh:mm aa").format(response.getFixture().getDate()));
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(viewPagerAdapterHolder.homeImageView);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(viewPagerAdapterHolder.awayImageView);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class ViewPagerAdapterHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView awayImageView;
        TextView awayName;
        /* access modifiers changed from: private */
        public ImageView homeImageView;
        TextView homeName;
        TextView leagueNameTV;
        TextView roundNameTV;
        TextView timeTV;

        public ViewPagerAdapterHolder(View view, EmranClick emranClick) {
            super(view);
            this.leagueNameTV = (TextView) view.findViewById(R.id.leagueId);
            this.roundNameTV = (TextView) view.findViewById(R.id.roundId);
            this.homeName = (TextView) view.findViewById(R.id.homeNameId);
            this.awayName = (TextView) view.findViewById(R.id.awayNameId);
            this.timeTV = (TextView) view.findViewById(R.id.timeId);
            this.homeImageView = (ImageView) view.findViewById(R.id.homeImageId);
            this.awayImageView = (ImageView) view.findViewById(R.id.awayImageId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emranClick.click(getAdapterPosition());

                }
            });
        }


    }
}
