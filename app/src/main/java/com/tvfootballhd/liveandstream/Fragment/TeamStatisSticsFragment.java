package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Adapter.PossessrAdapter;
import com.tvfootballhd.liveandstream.Model.TeamStatisticsResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class TeamStatisSticsFragment extends Fragment {
    private TextView awayPossesTv;
    private int awayTeamId;
    private CircularProgressIndicator circularProgressIndicator;
    private int fixtureId;
    private TextView homePossesTv;
    private int homeTeamId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingLL;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public TeamStatisticsResponse teamStatisticsResponse;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_team_statis_stics, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.homePossesTv = (TextView) inflate.findViewById(R.id.homePossessId);
        this.awayPossesTv = (TextView) inflate.findViewById(R.id.awayPossessId);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.statisticsRecyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        this.circularProgressIndicator = (CircularProgressIndicator) inflate.findViewById(R.id.possesId);
        this.homeTeamId = getActivity().getIntent().getIntExtra("HomeId", 0);
        this.awayTeamId = getActivity().getIntent().getIntExtra("AwayId", 0);
        this.fixtureId = getActivity().getIntent().getIntExtra("FixtureId", 0);
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.nothingLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        getStatistics();
        return inflate;
    }

    private void getStatistics() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures/statistics?fixture=" + this.fixtureId, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    TeamStatisticsResponse unused = TeamStatisSticsFragment.this.teamStatisticsResponse = (TeamStatisticsResponse) new Gson().fromJson(jSONObject.toString(), TeamStatisticsResponse.class);
                    if (TeamStatisSticsFragment.this.teamStatisticsResponse.getResponse().size() == 0) {
                        TeamStatisSticsFragment.this.nothingLL.setVisibility(0);
                    } else {
                        TeamStatisSticsFragment.this.mainLL.setVisibility(0);
                        TeamStatisSticsFragment.this.createMainPage();
                    }
                    TeamStatisSticsFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                TeamStatisSticsFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(TeamStatisSticsFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        if (!(this.teamStatisticsResponse.getResponse().get(0).getStatistics().get(9).getValue() == null || this.teamStatisticsResponse.getResponse().get(1).getStatistics().get(9).getValue() == null)) {
            String obj = this.teamStatisticsResponse.getResponse().get(0).getStatistics().get(9).getValue().toString();
            String obj2 = this.teamStatisticsResponse.getResponse().get(1).getStatistics().get(9).getValue().toString();
            this.circularProgressIndicator.setProgress(getPossess(obj));
            this.circularProgressIndicator.animate();
            this.homePossesTv.setText(obj);
            this.awayPossesTv.setText(obj2);
        }
        this.recyclerView.setAdapter(new PossessrAdapter(getContext(), this.teamStatisticsResponse.getResponse()));
    }

    private int getPossess(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.deleteCharAt(sb.lastIndexOf("%"));
        return Integer.valueOf(sb.toString()).intValue();
    }
}
