package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Model.TeamResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class StatisticsFragment extends Fragment {
    private TextView a105;
    private TextView a120;
    private TextView a15;
    private TextView a30;
    private TextView a45;
    private TextView a60;
    private TextView a75;
    private TextView a90;
    private TextView drawTV;
    private TextView f105;
    private TextView f120;
    private TextView f15;
    private TextView f30;
    private TextView f45;
    private TextView f60;
    private TextView f75;
    private TextView f90;
    private int id;
    private int leagueId;
    private TextView lostTV;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public ScrollView mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingLL;
    private TextView r105;
    private TextView r120;
    private TextView r15;
    private TextView r30;
    private TextView r45;
    private TextView r60;
    private TextView r75;
    private TextView r90;
    private int seasonId;
    private String teamIcon;
    private String teamName;
    /* access modifiers changed from: private */
    public TeamResponse teamResponse;
    private TextView winTV;
    private TextView y105;
    private TextView y120;
    private TextView y15;
    private TextView y30;
    private TextView y45;
    private TextView y60;
    private TextView y75;
    private TextView y90;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_statistics, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.mainLL = (ScrollView) inflate.findViewById(R.id.mainLL);
        this.nothingLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.winTV = (TextView) inflate.findViewById(R.id.wonTVId);
        this.drawTV = (TextView) inflate.findViewById(R.id.drawTVId);
        this.lostTV = (TextView) inflate.findViewById(R.id.lostTVId);
        this.y15 = (TextView) inflate.findViewById(R.id.y15);
        this.y30 = (TextView) inflate.findViewById(R.id.y30);
        this.y45 = (TextView) inflate.findViewById(R.id.y45);
        this.y60 = (TextView) inflate.findViewById(R.id.y60);
        this.y75 = (TextView) inflate.findViewById(R.id.y75);
        this.y90 = (TextView) inflate.findViewById(R.id.y90);
        this.y105 = (TextView) inflate.findViewById(R.id.y105);
        this.y120 = (TextView) inflate.findViewById(R.id.y120);
        this.r15 = (TextView) inflate.findViewById(R.id.r15);
        this.r30 = (TextView) inflate.findViewById(R.id.r30);
        this.r45 = (TextView) inflate.findViewById(R.id.r45);
        this.r60 = (TextView) inflate.findViewById(R.id.r60);
        this.r75 = (TextView) inflate.findViewById(R.id.r75);
        this.r90 = (TextView) inflate.findViewById(R.id.r90);
        this.r105 = (TextView) inflate.findViewById(R.id.r105);
        this.r120 = (TextView) inflate.findViewById(R.id.r120);
        this.f15 = (TextView) inflate.findViewById(R.id.f15);
        this.f30 = (TextView) inflate.findViewById(R.id.f30);
        this.f45 = (TextView) inflate.findViewById(R.id.f45);
        this.f60 = (TextView) inflate.findViewById(R.id.f60);
        this.f75 = (TextView) inflate.findViewById(R.id.f75);
        this.f90 = (TextView) inflate.findViewById(R.id.f90);
        this.f105 = (TextView) inflate.findViewById(R.id.f105);
        this.f120 = (TextView) inflate.findViewById(R.id.f120);
        this.a15 = (TextView) inflate.findViewById(R.id.a15);
        this.a30 = (TextView) inflate.findViewById(R.id.a30);
        this.a45 = (TextView) inflate.findViewById(R.id.a45);
        this.a60 = (TextView) inflate.findViewById(R.id.a60);
        this.a75 = (TextView) inflate.findViewById(R.id.a75);
        this.a90 = (TextView) inflate.findViewById(R.id.a90);
        this.a105 = (TextView) inflate.findViewById(R.id.a105);
        this.a120 = (TextView) inflate.findViewById(R.id.a120);
        this.id = getActivity().getIntent().getIntExtra("TeamId", 0);
        this.leagueId = getActivity().getIntent().getIntExtra("LeagueId", 0);
        this.seasonId = getActivity().getIntent().getIntExtra("Season", 0);
        this.teamName = getActivity().getIntent().getStringExtra("TeamName");
        this.teamIcon = getActivity().getIntent().getStringExtra("TeamIcon");
        getTeamStatistics();
        return inflate;
    }

    private void getTeamStatistics() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/teams/statistics?league=" + this.leagueId + "&season=" + this.seasonId + "&team=" + this.id, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    TeamResponse unused = StatisticsFragment.this.teamResponse = (TeamResponse) new Gson().fromJson(jSONObject.toString(), TeamResponse.class);
                    if (StatisticsFragment.this.teamResponse.getResponse() == null) {
                        StatisticsFragment.this.nothingLL.setVisibility(0);
                    } else {
                        StatisticsFragment.this.mainLL.setVisibility(0);
                        StatisticsFragment.this.createMainPage();
                    }
                    StatisticsFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StatisticsFragment.this.getContext(), e.getMessage(), 0).show();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                StatisticsFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(StatisticsFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
            }
        }) {
            public Map getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("X-RapidAPI-Key", Config.getApiKey());
                hashMap.put("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
                return hashMap;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createMainPage() {
        this.winTV.setText(String.valueOf(this.teamResponse.getResponse().getFixtures().getWins().getTotal()));
        this.drawTV.setText(String.valueOf(this.teamResponse.getResponse().getFixtures().getDraws().getTotal()));
        this.lostTV.setText(String.valueOf(this.teamResponse.getResponse().getFixtures().getLoses().getTotal()));
        TeamResponse.Yellow yellow = this.teamResponse.getResponse().getCards().getYellow();
        this.y15.setText(String.valueOf(yellow.getFifteen().getTotal()));
        this.y30.setText(String.valueOf(yellow.getThirty().getTotal()));
        this.y45.setText(String.valueOf(yellow.getFortiefive().getTotal()));
        this.y60.setText(String.valueOf(yellow.getSixtey().getTotal()));
        this.y75.setText(String.valueOf(yellow.getSeventiFive().getTotal()));
        this.y90.setText(String.valueOf(yellow.getNineTy().getTotal()));
        this.y105.setText(String.valueOf(yellow.getOneZeroFive().getTotal()));
        this.y120.setText(String.valueOf(yellow.getOneTwenty().getTotal()));
        TeamResponse.Red red = this.teamResponse.getResponse().getCards().getRed();
        this.r15.setText(String.valueOf(red.getFifteen().getTotal()));
        this.r30.setText(String.valueOf(red.getThirty().getTotal()));
        this.r45.setText(String.valueOf(red.getFortiefive().getTotal()));
        this.r60.setText(String.valueOf(red.getSixtey().getTotal()));
        this.r75.setText(String.valueOf(red.getSeventiFive().getTotal()));
        this.r90.setText(String.valueOf(red.getNineTy().getTotal()));
        this.r105.setText(String.valueOf(red.getOneZeroFive().getTotal()));
        this.r120.setText(String.valueOf(red.getOneTwenty().getTotal()));
        TeamResponse.For myfor = this.teamResponse.getResponse().getGoals().getMyfor();
        this.f15.setText(String.valueOf(myfor.getMinute().getFifteen().getTotal()));
        this.f30.setText(String.valueOf(myfor.getMinute().getThirty().getTotal()));
        this.f45.setText(String.valueOf(myfor.getMinute().getFortiefive().getTotal()));
        this.f60.setText(String.valueOf(myfor.getMinute().getSixtey().getTotal()));
        this.f75.setText(String.valueOf(myfor.getMinute().getSeventiFive().getTotal()));
        this.f90.setText(String.valueOf(myfor.getMinute().getNineTy().getTotal()));
        this.f105.setText(String.valueOf(myfor.getMinute().getOneZeroFive().getTotal()));
        this.f120.setText(String.valueOf(myfor.getMinute().getOneTwenty().getTotal()));
        TeamResponse.Against against = this.teamResponse.getResponse().getGoals().getAgainst();
        this.a15.setText(String.valueOf(against.getMinute().getFifteen().getTotal()));
        this.a30.setText(String.valueOf(against.getMinute().getThirty().getTotal()));
        this.a45.setText(String.valueOf(against.getMinute().getFortiefive().getTotal()));
        this.a60.setText(String.valueOf(against.getMinute().getSixtey().getTotal()));
        this.a75.setText(String.valueOf(against.getMinute().getSeventiFive().getTotal()));
        this.a90.setText(String.valueOf(against.getMinute().getNineTy().getTotal()));
        this.a105.setText(String.valueOf(against.getMinute().getOneZeroFive().getTotal()));
        this.a120.setText(String.valueOf(against.getMinute().getOneTwenty().getTotal()));
    }
}
