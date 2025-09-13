package com.tvfootballhd.liveandstream.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Adapter.WeeklyFixtureAdapter;
import com.tvfootballhd.liveandstream.Configr;
import com.tvfootballhd.liveandstream.Model.NotificationData;
import com.tvfootballhd.liveandstream.Model.WeeklyFixture;
import com.tvfootballhd.liveandstream.OneSignalDbContract;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.MyDatabaseHelper;
import com.tvfootballhd.liveandstream.Utils.NotificationBroadCastReciever;
import com.tvfootballhd.liveandstream.Utils.Utils;
import com.tvfootballhd.liveandstream.ads.BannerAdManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeeklyFixtureActivity extends AppCompatActivity {
    private LinearLayout backLL;
    private String countryName;
    private TextView countryNameTv;
    private ImageView leagueIcon;
    private int leagueId;
    private String leagueLogo;
    private String leagueName;
    private TextView leagueNameTv;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private LinearLayout mainLL;
    /* access modifiers changed from: private */
    public MyDatabaseHelper myDatabaseHelper;
    private LinearLayout nothingLL;
    /* access modifiers changed from: private */
    public ArrayList<NotificationData> notificationDataArrayList;
    private RecyclerView recyclerView;
    private int season;
    private TextView titleTV;
    /* access modifiers changed from: private */
    public WeeklyFixture weeklyFixture;
    /* access modifiers changed from: private */
    public WeeklyFixtureAdapter weeklyFixtureAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_weekly_fixture);
        this.myDatabaseHelper = new MyDatabaseHelper(this);
        this.notificationDataArrayList = new ArrayList<>();
        this.lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottiId);
        this.backLL = (LinearLayout) findViewById(R.id.backLL);
        this.leagueName = getIntent().getStringExtra("leagueName");
        this.leagueLogo = getIntent().getStringExtra("leagueLogo");
        this.countryName = getIntent().getStringExtra("countryName");
        this.season = getIntent().getIntExtra("season", 2022);
        this.leagueId = getIntent().getIntExtra("leagueId", 39);
        this.leagueNameTv = (TextView) findViewById(R.id.leagueNameId);
        this.countryNameTv = (TextView) findViewById(R.id.leagueCountryNameId);
        this.leagueIcon = (ImageView) findViewById(R.id.leagueIconId);
        this.mainLL = (LinearLayout) findViewById(R.id.mainLL);
        this.nothingLL = (LinearLayout) findViewById(R.id.nothingFoundLL);
        this.titleTV = (TextView) findViewById(R.id.titleId);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        this.leagueNameTv.setText(this.leagueName);
        this.countryNameTv.setText(this.countryName);
        Picasso.get().load(this.leagueLogo).into(this.leagueIcon);
        this.titleTV.setText(getString(R.string.weekly_fixture));
        getWeeklyFixture();
        this.backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new BannerAdManager(this, (LinearLayout) findViewById(R.id.banner_linear));
    }


    private void getWeeklyFixture() {
        if (Build.VERSION.SDK_INT >= 26) {
            LocalDate now = LocalDate.now();
            this.lottieAnimationView.setVisibility(0);
            Volley.newRequestQueue(this).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + this.leagueId + "&season=" + this.season + "&from=" + Utils.convertLocalDateToString(now) + "&to=" + Utils.convertLocalDateToString(now.plusDays(6)), new Response.Listener<NetworkResponse>() {
                public void onResponse(NetworkResponse networkResponse) {
                    try {
                        JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                        WeeklyFixture unused = WeeklyFixtureActivity.this.weeklyFixture = (WeeklyFixture) new Gson().fromJson(jSONObject.toString(), WeeklyFixture.class);
                        WeeklyFixtureActivity.this.lottieAnimationView.setVisibility(8);
                        try {
                            WeeklyFixtureActivity.this.createMainPage();
                        } catch (Exception e) {
                            Toast.makeText(WeeklyFixtureActivity.this, "", 0).show();
                            Log.d("Errrrrrr", e.getMessage());
                        }
                    } catch (UnsupportedEncodingException | JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    WeeklyFixtureActivity.this.lottieAnimationView.setVisibility(8);
                    Toast.makeText(WeeklyFixtureActivity.this, "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
                }
            }) {
                public Map getHeaders() throws AuthFailureError {
                    HashMap hashMap = new HashMap();
                    hashMap.put("X-RapidAPI-Key", Configr.API_KEY);
                    hashMap.put("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
                    return hashMap;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void createMainPage() {
        if (this.weeklyFixture.getResponse().size() == 0) {
            this.mainLL.setVisibility(8);
            this.nothingLL.setVisibility(0);
            return;
        }
        this.mainLL.setVisibility(0);
        this.nothingLL.setVisibility(8);
        Collections.sort(this.weeklyFixture.getResponse(), new Comparator<WeeklyFixture.Response>() {
            public int compare(WeeklyFixture.Response response, WeeklyFixture.Response response2) {
                return response.getFixture().getDate().compareTo(response2.getFixture().getDate());
            }
        });
        WeeklyFixtureAdapter weeklyFixtureAdapter2 = new WeeklyFixtureAdapter(this, this.weeklyFixture.getResponse());
        this.weeklyFixtureAdapter = weeklyFixtureAdapter2;
        this.recyclerView.setAdapter(weeklyFixtureAdapter2);
        this.weeklyFixtureAdapter.setNotifactionItemClicListener(new WeeklyFixtureAdapter.NotificationItemClickListener() {
            public void onClick(int i) {
                Intent intent = new Intent(WeeklyFixtureActivity.this, NotificationBroadCastReciever.class);
                intent.putExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_TITLE, "Hello User");
                intent.putExtra(OneSignalDbContract.NotificationTable.COLUMN_NAME_MESSAGE, "There is match between " + WeeklyFixtureActivity.this.weeklyFixture.getResponse().get(i).getTeams().getHome().getName() + " and " + WeeklyFixtureActivity.this.weeklyFixture.getResponse().get(i).getTeams().getAway().getName() + " which is started now.\n Pls Ck Now.");
                WeeklyFixtureActivity weeklyFixtureActivity = WeeklyFixtureActivity.this;
                PendingIntent broadcast = PendingIntent.getBroadcast(weeklyFixtureActivity, weeklyFixtureActivity.weeklyFixture.getResponse().get(i).getFixture().getId(), intent, 67108864);
                AlarmManager alarmManager = (AlarmManager) WeeklyFixtureActivity.this.getSystemService(Context.ALARM_SERVICE);
                WeeklyFixtureActivity weeklyFixtureActivity2 = WeeklyFixtureActivity.this;
                if (weeklyFixtureActivity2.checkNotificationSelected(weeklyFixtureActivity2.weeklyFixture.getResponse().get(i).getFixture().getId())) {
                    MyDatabaseHelper access$500 = WeeklyFixtureActivity.this.myDatabaseHelper;
                    WeeklyFixtureActivity weeklyFixtureActivity3 = WeeklyFixtureActivity.this;
                    access$500.deleteTodayMatchNotificationItem(weeklyFixtureActivity3.getNotificationItemId(weeklyFixtureActivity3.weeklyFixture.getResponse().get(i).getFixture().getId()));
                    alarmManager.cancel(broadcast);
                } else {
                    WeeklyFixtureActivity.this.myDatabaseHelper.insertDataInTodayMatchTable(WeeklyFixtureActivity.this.weeklyFixture.getResponse().get(i).getFixture().getId(), WeeklyFixtureActivity.this.weeklyFixture.getResponse().get(i).getFixture().getTimestamp(), 1);
                    alarmManager.set(0, System.currentTimeMillis() + (WeeklyFixtureActivity.this.weeklyFixture.getResponse().get(i).getFixture().getDate().getTime() - new Date().getTime()), broadcast);
                }
                WeeklyFixtureActivity.this.createNotificationDataArrayList();
                WeeklyFixtureActivity.this.weeklyFixtureAdapter.setNotificationDataArrayList(WeeklyFixtureActivity.this.notificationDataArrayList);
                WeeklyFixtureActivity.this.weeklyFixtureAdapter.notifyDataSetChanged();
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
}
