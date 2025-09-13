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
import androidx.core.app.NotificationCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.onesignal.OneSignalDbContract;
import com.tvfootballhd.liveandstream.Activity.LeagueDetailsActivity;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.Model.NotificationData;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.Utils.MyDatabaseHelper;
import com.tvfootballhd.liveandstream.Utils.NotificationBroadCastReciever;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;

public class TodayMatchAdapter extends RecyclerView.Adapter<TodayMatchAdapter.TodayMatchAdapterHoldser> {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public MyDatabaseHelper myDatabaseHelper;
    /* access modifiers changed from: private */
    public ArrayList<NotificationData> notificationDataArrayList = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<ArrayList<FixtureResponse.Response>> responseArrayList;

    public int getItemViewType(int i) {
        return i;
    }

    public TodayMatchAdapter(Context context2, ArrayList<ArrayList<FixtureResponse.Response>> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
        this.myDatabaseHelper = new MyDatabaseHelper(context2);
        createNotificationDataArrayList();
    }

    public TodayMatchAdapterHoldser onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TodayMatchAdapterHoldser(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.today_match_list, viewGroup, false));
    }

    public void onBindViewHolder(TodayMatchAdapterHoldser todayMatchAdapterHoldser, int i) {
        final ArrayList arrayList = this.responseArrayList.get(i);
        todayMatchAdapterHoldser.leagueNameTV.setText(((FixtureResponse.Response) arrayList.get(0)).getLeague().getName() + ", " + ((FixtureResponse.Response) arrayList.get(0)).getLeague().getCountry());
        if (((FixtureResponse.Response) arrayList.get(0)).getLeague().getLogo() != null) {
            Picasso.get().load(((FixtureResponse.Response) arrayList.get(0)).getLeague().getLogo()).into(todayMatchAdapterHoldser.leagueLogoImageView);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(todayMatchAdapterHoldser.individualLeagueRecyclerView.getContext());
        linearLayoutManager.setInitialPrefetchItemCount(arrayList.size());
        final TodayFixtureAdapter todayFixtureAdapter = new TodayFixtureAdapter(this.context, arrayList);
        todayMatchAdapterHoldser.individualLeagueRecyclerView.setLayoutManager(linearLayoutManager);
        todayMatchAdapterHoldser.individualLeagueRecyclerView.setAdapter(todayFixtureAdapter);
        ViewCompat.setNestedScrollingEnabled(todayMatchAdapterHoldser.individualLeagueRecyclerView, false);
        todayFixtureAdapter.setNotifactionItemClicListener(new TodayFixtureAdapter.NotificationItemClickListener() {
            public void onClick(int i) {
                Intent intent = new Intent(TodayMatchAdapter.this.context, NotificationBroadCastReciever.class);
               // intent.putExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_TITLE, "Hello User");
                //intent.putExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_MESSAGE, "There is match between " + ((FixtureResponse.Response) arrayList.get(i)).getTeams().getHome().getName() + " and " + ((FixtureResponse.Response) arrayList.get(i)).getTeams().getAway().getName() + " which is started now.\n Pls Ck Now.");
                PendingIntent broadcast = PendingIntent.getBroadcast(TodayMatchAdapter.this.context, ((FixtureResponse.Response) arrayList.get(i)).getFixture().getId(), intent, 67108864);
                AlarmManager alarmManager = (AlarmManager) TodayMatchAdapter.this.context.getSystemService(NotificationCompat.CATEGORY_ALARM);
                if (TodayMatchAdapter.this.checkNotificationSelected(((FixtureResponse.Response) arrayList.get(i)).getFixture().getId())) {
                    TodayMatchAdapter.this.myDatabaseHelper.deleteTodayMatchNotificationItem(TodayMatchAdapter.this.getNotificationItemId(((FixtureResponse.Response) arrayList.get(i)).getFixture().getId()));
                    alarmManager.cancel(broadcast);
                } else {
                    TodayMatchAdapter.this.myDatabaseHelper.insertDataInTodayMatchTable(((FixtureResponse.Response) arrayList.get(i)).getFixture().getId(), ((FixtureResponse.Response) arrayList.get(i)).getFixture().getTimestamp(), 1);
                    alarmManager.set(0, System.currentTimeMillis() + (((FixtureResponse.Response) arrayList.get(i)).getFixture().getDate().getTime() - new Date().getTime()), broadcast);
                }
                TodayMatchAdapter.this.createNotificationDataArrayList();
                todayFixtureAdapter.setNotificationDataArrayList(TodayMatchAdapter.this.notificationDataArrayList);
                todayFixtureAdapter.notifyDataSetChanged();
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

    public class TodayMatchAdapterHoldser extends RecyclerView.ViewHolder {
        RecyclerView individualLeagueRecyclerView;
        ImageView leagueLogoImageView;
        TextView leagueNameTV;

        public TodayMatchAdapterHoldser(View view) {
            super(view);
            this.leagueNameTV = (TextView) view.findViewById(R.id.todayMatchLeagueNameId);
            this.leagueLogoImageView = (ImageView) view.findViewById(R.id.todayMatchLeagueIconId);
            this.individualLeagueRecyclerView = (RecyclerView) view.findViewById(R.id.todayIndividualLeagueMatchId);
            this.leagueNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leagueId(view);
                }
            });
        }

        public void leagueId(View view) {
            FixtureResponse.Response response = (FixtureResponse.Response) ((ArrayList) TodayMatchAdapter.this.responseArrayList.get(getAdapterPosition())).get(0);
            Intent intent = new Intent(TodayMatchAdapter.this.context, LeagueDetailsActivity.class);
            intent.putExtra("leagueId", response.getLeague().getId());
            intent.putExtra("year", response.getLeague().getSeason());
            TodayMatchAdapter.this.context.startActivity(intent);
        }
    }
}
