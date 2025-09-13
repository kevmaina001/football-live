package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.tvfootballhd.liveandstream.Activity.ScheduleDetailsActivity;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;
    private EmranClick emranClick;
    private ArrayList<FixtureResponse.Response> responseArrayList;

    public interface EmranClick {
        void click(int i);
    }

    public void setEmranClick(EmranClick emranClick2) {
        this.emranClick = emranClick2;
    }

    public SliderAdapter(Context context2, ArrayList<FixtureResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public SliderAdapterVH onCreateViewHolder(ViewGroup viewGroup) {
        return new SliderAdapterVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slider_layout, (ViewGroup) null));
    }

    public void onBindViewHolder(SliderAdapterVH sliderAdapterVH, int i) {
        FixtureResponse.Response response = this.responseArrayList.get(i);
        if (response.getLeague().getName() != null) {
            sliderAdapterVH.leagueNameTV.setText(response.getLeague().getName());
        }
        if (response.getLeague().getRound() != null) {
            sliderAdapterVH.roundNameTV.setText(response.getLeague().getRound());
        }
        if (response.getTeams().getHome().getName() != null) {
            sliderAdapterVH.homeName.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            sliderAdapterVH.awayName.setText(response.getTeams().getAway().getName());
        }
        if (response.getFixture().getDate() != null) {
            sliderAdapterVH.timeTV.setText(new SimpleDateFormat("dd-MMM-yy").format(response.getFixture().getDate()) + "\n" + new SimpleDateFormat("hh:mm aa").format(response.getFixture().getDate()));
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(sliderAdapterVH.homeImageView);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(sliderAdapterVH.awayImageView);
        }
        sliderAdapterVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fixtureResponses(response, view);
            }
        });
    }

    public void fixtureResponses(FixtureResponse.Response response, View view) {
        Intent intent = new Intent(this.context, ScheduleDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("FixtureResponseClass", response);
        intent.putExtra("FixtureId", response.getFixture().getId());
        intent.putExtra("HomeId", response.getTeams().getHome().getId());
        intent.putExtra("AwayId", response.getTeams().getAway().getId());
        intent.putExtras(bundle);
        this.context.startActivity(intent);
    }

    public int getCount() {
        if (this.responseArrayList.size() > 5) {
            return 5;
        }
        return this.responseArrayList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView awayImageView;
        TextView awayName;
        /* access modifiers changed from: private */
        public ImageView homeImageView;
        TextView homeName;
        TextView leagueNameTV;
        TextView roundNameTV;
        TextView timeTV;

        public SliderAdapterVH(View view) {
            super(view);
            this.leagueNameTV = (TextView) view.findViewById(R.id.leagueId);
            this.roundNameTV = (TextView) view.findViewById(R.id.roundId);
            this.homeName = (TextView) view.findViewById(R.id.homeNameId);
            this.awayName = (TextView) view.findViewById(R.id.awayNameId);
            this.timeTV = (TextView) view.findViewById(R.id.timeId);
            this.homeImageView = (ImageView) view.findViewById(R.id.homeImageId);
            this.awayImageView = (ImageView) view.findViewById(R.id.awayImageId);
        }
    }
}
