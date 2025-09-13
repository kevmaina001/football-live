package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.InfoResponse;
import com.tvfootballhd.liveandstream.Model.NotificationData;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.Utils.MyDatabaseHelper;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeamChildFixtureAdapter extends RecyclerView.Adapter<TeamChildFixtureAdapter.TodayFixtureAdapterHolder> {
    private Context context;
    private long currentTime;
    private long matchTime;
    private MyDatabaseHelper myDatabaseHelper;
    private NotificationItemClickListener notifactionItemClicListener;
    private ArrayList<NotificationData> notificationDataArrayList = new ArrayList<>();
    private OnclickListener onclickListener;
    private ArrayList<InfoResponse.Response> responseArrayList;

    public interface NotificationItemClickListener {
        void onClick(int i);
    }

    public interface OnclickListener {
        void onClick(int i);
    }

    public void setOnclickListener(OnclickListener onclickListener2) {
        this.onclickListener = onclickListener2;
    }

    public void setNotifactionItemClicListener(NotificationItemClickListener notificationItemClickListener) {
        this.notifactionItemClicListener = notificationItemClickListener;
    }

    public TeamChildFixtureAdapter(Context context2, ArrayList<InfoResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
        this.myDatabaseHelper = new MyDatabaseHelper(context2);
        this.notificationDataArrayList = createNotificationDataArrayList();
    }

    public void setNotificationDataArrayList(ArrayList<NotificationData> arrayList) {
        this.notificationDataArrayList = arrayList;
        notifyDataSetChanged();
    }

    private ArrayList<NotificationData> createNotificationDataArrayList() {
        ArrayList<NotificationData> arrayList = new ArrayList<>();
        Cursor allTodayMatchTable = this.myDatabaseHelper.getAllTodayMatchTable();
        if (allTodayMatchTable.getCount() > 0) {
            while (allTodayMatchTable.moveToNext()) {
                arrayList.add(new NotificationData(allTodayMatchTable.getInt(0), allTodayMatchTable.getInt(1), allTodayMatchTable.getInt(2), allTodayMatchTable.getInt(3)));
            }
        }
        return arrayList;
    }

    public TodayFixtureAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TodayFixtureAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.league_match_layout, viewGroup, false), this.onclickListener, this.notifactionItemClicListener);
    }

    private boolean checkNotificationSelected(int i) {
        for (int i2 = 0; i2 < this.notificationDataArrayList.size(); i2++) {
            if (i == this.notificationDataArrayList.get(i2).getFixtureId()) {
                return true;
            }
        }
        return false;
    }

    public void onBindViewHolder(TodayFixtureAdapterHolder todayFixtureAdapterHolder, int i) {
        InfoResponse.Response response = this.responseArrayList.get(i);
        if (response.getTeams().getHome().getName() != null) {
            todayFixtureAdapterHolder.homeNameTV.setText(response.getTeams().getHome().getName());
        }
        if (response.getTeams().getAway().getName() != null) {
            todayFixtureAdapterHolder.awayNameTV.setText(response.getTeams().getAway().getName());
        }
        if (response.getTeams().getHome().getLogo() != null) {
            Picasso.get().load(response.getTeams().getHome().getLogo()).into(todayFixtureAdapterHolder.homeFlag);
        }
        if (response.getTeams().getAway().getLogo() != null) {
            Picasso.get().load(response.getTeams().getAway().getLogo()).into(todayFixtureAdapterHolder.awayFlag);
        }
        this.currentTime = new Date().getTime();
        long time = response.getFixture().getDate().getTime();
        this.matchTime = time;
        if (this.currentTime < time) {
            todayFixtureAdapterHolder.dateTV.setText(new SimpleDateFormat("dd-MMM-yy").format(response.getFixture().getDate()));
            todayFixtureAdapterHolder.dateTimeTV.setText(new SimpleDateFormat("hh:mm aa").format(response.getFixture().getDate()));
            todayFixtureAdapterHolder.notificationIcon.setVisibility(0);
            todayFixtureAdapterHolder.homeGoalTV.setVisibility(8);
            todayFixtureAdapterHolder.awayGoalTv.setVisibility(8);
        } else {
            todayFixtureAdapterHolder.dateTV.setVisibility(8);
            if (response.getFixture().getStatus().getElapsed() >= 90 || response.getFixture().getStatus().getElapsed() == 0) {
                if (response.getFixture().getStatus().getMyshort().contentEquals("FT")) {
                    todayFixtureAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.green));
                } else if (response.getFixture().getStatus().getMyshort().contentEquals("CANC")) {
                    todayFixtureAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.red));
                } else {
                    todayFixtureAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.active_color));
                }
                todayFixtureAdapterHolder.dateTimeTV.setText(response.getFixture().getStatus().getMyshort());
            } else {
                todayFixtureAdapterHolder.dateTimeTV.setText(response.getFixture().getStatus().getElapsed() + "'");
                todayFixtureAdapterHolder.dateTimeTV.setTextColor(this.context.getResources().getColor(R.color.active_color));
            }
            todayFixtureAdapterHolder.notificationIcon.setVisibility(8);
            todayFixtureAdapterHolder.homeGoalTV.setVisibility(0);
            todayFixtureAdapterHolder.awayGoalTv.setVisibility(0);
            todayFixtureAdapterHolder.homeGoalTV.setText(String.valueOf(response.getGoals().getHome()));
            todayFixtureAdapterHolder.awayGoalTv.setText(String.valueOf(response.getGoals().getAway()));
        }
        if (checkNotificationSelected(response.getFixture().getId())) {
            todayFixtureAdapterHolder.notificationIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.baseline_notifications_active_24));
        } else {
            todayFixtureAdapterHolder.notificationIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.baseline_notifications_24));
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class TodayFixtureAdapterHolder extends RecyclerView.ViewHolder {
        ImageView awayFlag;
        TextView awayGoalTv;
        TextView awayNameTV;
        TextView dateTV;
        TextView dateTimeTV;
        ImageView homeFlag;
        TextView homeGoalTV;
        TextView homeNameTV;
        ImageView notificationIcon;

        public TodayFixtureAdapterHolder(View view, OnclickListener onclickListener, NotificationItemClickListener notificationItemClickListener) {
            super(view);
            this.homeFlag = (ImageView) view.findViewById(R.id.leagueHomeFlagId);
            this.awayFlag = (ImageView) view.findViewById(R.id.leagueAwayFlagId);
            this.homeNameTV = (TextView) view.findViewById(R.id.leagueHomeNameId);
            this.awayNameTV = (TextView) view.findViewById(R.id.leagueAwayNameId);
            this.dateTV = (TextView) view.findViewById(R.id.dateTV);
            this.dateTimeTV = (TextView) view.findViewById(R.id.timeTVId);
            this.homeGoalTV = (TextView) view.findViewById(R.id.homeGoal);
            this.awayGoalTv = (TextView) view.findViewById(R.id.awayGoal);
            ImageView imageView = (ImageView) view.findViewById(R.id.notificationIconId);
            this.notificationIcon = imageView;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationItemClickListener.onClick(getAdapterPosition());

                }
            });
        }


    }
}
