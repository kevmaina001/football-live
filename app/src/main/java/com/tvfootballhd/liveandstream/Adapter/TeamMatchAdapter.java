package com.tvfootballhd.liveandstream.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tvfootballhd.liveandstream.Activity.LeagueDetailsActivity;
import com.tvfootballhd.liveandstream.Model.InfoResponse;
import com.tvfootballhd.liveandstream.Model.NotificationData;
import com.tvfootballhd.liveandstream.OneSignalDbContract;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.Utils.MyDatabaseHelper;
import com.tvfootballhd.liveandstream.Utils.NotificationBroadCastReciever;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class TeamMatchAdapter extends RecyclerView.Adapter<TeamMatchAdapter.TeamMatchAdapterAdapterHoldser> {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public MyDatabaseHelper myDatabaseHelper;
    /* access modifiers changed from: private */
    public ArrayList<NotificationData> notificationDataArrayList = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<ArrayList<InfoResponse.Response>> responseArrayList;

    public int getItemViewType(int i) {
        return i;
    }

    public TeamMatchAdapter(Context context2, ArrayList<ArrayList<InfoResponse.Response>> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
        this.myDatabaseHelper = new MyDatabaseHelper(context2);
        createNotificationDataArrayList();
    }

    public TeamMatchAdapterAdapterHoldser onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TeamMatchAdapterAdapterHoldser(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.today_match_list, viewGroup, false));
    }

    public void onBindViewHolder(TeamMatchAdapterAdapterHoldser teamMatchAdapterAdapterHoldser, int i) {
        final ArrayList arrayList = this.responseArrayList.get(i);
        teamMatchAdapterAdapterHoldser.leagueNameTV.setText(((InfoResponse.Response) arrayList.get(0)).getLeague().getName() + ", " + ((InfoResponse.Response) arrayList.get(0)).getLeague().getCountry());
        if (((InfoResponse.Response) arrayList.get(0)).getLeague().getLogo() != null) {
            Picasso.get().load(((InfoResponse.Response) arrayList.get(0)).getLeague().getLogo()).into(teamMatchAdapterAdapterHoldser.leagueLogoImageView);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(teamMatchAdapterAdapterHoldser.individualLeagueRecyclerView.getContext());
        linearLayoutManager.setInitialPrefetchItemCount(arrayList.size());
        final TeamChildFixtureAdapter teamChildFixtureAdapter = new TeamChildFixtureAdapter(this.context, arrayList);
        teamMatchAdapterAdapterHoldser.individualLeagueRecyclerView.setLayoutManager(linearLayoutManager);
        teamMatchAdapterAdapterHoldser.individualLeagueRecyclerView.setAdapter(teamChildFixtureAdapter);
        ViewCompat.setNestedScrollingEnabled(teamMatchAdapterAdapterHoldser.individualLeagueRecyclerView, false);
        teamChildFixtureAdapter.setNotifactionItemClicListener(new TeamChildFixtureAdapter.NotificationItemClickListener() {
            public void onClick(int i) {
                Intent intent = new Intent(TeamMatchAdapter.this.context, NotificationBroadCastReciever.class);
                intent.putExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_TITLE, "Hello User");
                intent.putExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_MESSAGE, "There is match between " + ((InfoResponse.Response) arrayList.get(i)).getTeams().getHome().getName() + " and " + ((InfoResponse.Response) arrayList.get(i)).getTeams().getAway().getName() + " which is started now.\n Pls Ck Now.");
                PendingIntent broadcast = PendingIntent.getBroadcast(TeamMatchAdapter.this.context, ((InfoResponse.Response) arrayList.get(i)).getFixture().getId(), intent, 67108864);
                AlarmManager alarmManager = (AlarmManager) TeamMatchAdapter.this.context.getSystemService(Context.ALARM_SERVICE);
                if (TeamMatchAdapter.this.checkNotificationSelected(((InfoResponse.Response) arrayList.get(i)).getFixture().getId())) {
                    TeamMatchAdapter.this.myDatabaseHelper.deleteTodayMatchNotificationItem(TeamMatchAdapter.this.getNotificationItemId(((InfoResponse.Response) arrayList.get(i)).getFixture().getId()));
                    alarmManager.cancel(broadcast);
                } else {
                    TeamMatchAdapter.this.myDatabaseHelper.insertDataInTodayMatchTable(((InfoResponse.Response) arrayList.get(i)).getFixture().getId(), ((InfoResponse.Response) arrayList.get(i)).getFixture().getTimestamp(), 1);
                    alarmManager.set(0, System.currentTimeMillis() + (((InfoResponse.Response) arrayList.get(i)).getFixture().getDate().getTime() - new Date().getTime()), broadcast);
                }
                TeamMatchAdapter.this.createNotificationDataArrayList();
                teamChildFixtureAdapter.setNotificationDataArrayList(TeamMatchAdapter.this.notificationDataArrayList);
                teamChildFixtureAdapter.notifyDataSetChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    public int getNotificationItemId(int i) {
        for (int i2 = 0; i2 < this.notificationDataArrayList.size(); i2++) {
            if (i == this.notificationDataArrayList.get(i2).getFixtureId()) {
                return this.notificationDataArrayList.get(i2).getId();
            }
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public boolean checkNotificationSelected(int i) {
        for (int i2 = 0; i2 < this.notificationDataArrayList.size(); i2++) {
            if (i == this.notificationDataArrayList.get(i2).getFixtureId()) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void createNotificationDataArrayList() {
        this.notificationDataArrayList.clear();
        Cursor allTodayMatchTable = this.myDatabaseHelper.getAllTodayMatchTable();
        if (allTodayMatchTable.getCount() > 0) {
            while (allTodayMatchTable.moveToNext()) {
                this.notificationDataArrayList.add(new NotificationData(allTodayMatchTable.getInt(0), allTodayMatchTable.getInt(1), allTodayMatchTable.getInt(2), allTodayMatchTable.getInt(3)));
            }
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class TeamMatchAdapterAdapterHoldser extends RecyclerView.ViewHolder {
        RecyclerView individualLeagueRecyclerView;
        ImageView leagueLogoImageView;
        TextView leagueNameTV;

        public TeamMatchAdapterAdapterHoldser(View view) {
            super(view);
            this.leagueNameTV = (TextView) view.findViewById(R.id.todayMatchLeagueNameId);
            this.leagueLogoImageView = (ImageView) view.findViewById(R.id.todayMatchLeagueIconId);
            this.individualLeagueRecyclerView = (RecyclerView) view.findViewById(R.id.todayIndividualLeagueMatchId);
            this.leagueNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leagueID(view);
                }
            });
        }

        public void leagueID(View view) {
            InfoResponse.Response response = (InfoResponse.Response) ((ArrayList) TeamMatchAdapter.this.responseArrayList.get(getAdapterPosition())).get(0);
            Intent intent = new Intent(TeamMatchAdapter.this.context, LeagueDetailsActivity.class);
            intent.putExtra("leagueId", response.getLeague().getId());
            intent.putExtra("year", response.getLeague().getSeason());
            TeamMatchAdapter.this.context.startActivity(intent);
        }
    }
}
