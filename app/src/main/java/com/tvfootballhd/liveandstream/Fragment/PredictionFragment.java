package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Adapter.CustomPagerAdapter;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.Model.Prediction;
import com.tvfootballhd.liveandstream.Model.PredictionResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import me.relex.circleindicator.CircleIndicator3;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class PredictionFragment extends Fragment {
    private LinearProgressIndicator awayAttack;
    private TextView awayAttackTV;
    private LinearProgressIndicator awayDefence;
    private TextView awayDefenceTV;
    private LinearProgressIndicator awayFormation;
    private TextView awayFormationTV;
    private ImageView awayImageView;
    private TextView awayNameTv;
    private CircleIndicator3 circleIndicator;
    private TextView dateTV;
    /* access modifiers changed from: private */
    public FixtureResponse fixtureResponse;
    /* access modifiers changed from: private */
    public Date gameDate;
    private LinearProgressIndicator homeAttack;
    private TextView homeAttackTV;
    private LinearProgressIndicator homeDefence;
    private TextView homeDefenceTV;
    private LinearProgressIndicator homeFormation;
    private TextView homeFormationTV;
    private ImageView homeImageView;
    private TextView homeNameTv;
    private int leagueId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingFoundLL;
    /* access modifiers changed from: private */
    public PredictionResponse predictionResponse;
    private RecyclerView recyclerView;
    private ViewPager2 viewPager2;
    private int year;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_prediction, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.viewPager2 = (ViewPager2) inflate.findViewById(R.id.predictionViewPagerId);
        this.circleIndicator = (CircleIndicator3) inflate.findViewById(R.id.indicator);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.nothingFoundLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.homeFormation = (LinearProgressIndicator) inflate.findViewById(R.id.homeTypeId);
        this.awayFormation = (LinearProgressIndicator) inflate.findViewById(R.id.awayTypeId);
        this.homeAttack = (LinearProgressIndicator) inflate.findViewById(R.id.homeTypeId2);
        this.awayAttack = (LinearProgressIndicator) inflate.findViewById(R.id.awayTypeId2);
        this.homeDefence = (LinearProgressIndicator) inflate.findViewById(R.id.homeTypeId3);
        this.awayDefence = (LinearProgressIndicator) inflate.findViewById(R.id.awayTypeId3);
        this.homeFormationTV = (TextView) inflate.findViewById(R.id.homeCountId);
        this.awayFormationTV = (TextView) inflate.findViewById(R.id.awayCountId);
        this.homeAttackTV = (TextView) inflate.findViewById(R.id.homeCountId2);
        this.awayAttackTV = (TextView) inflate.findViewById(R.id.awayCountId2);
        this.homeDefenceTV = (TextView) inflate.findViewById(R.id.homeCountId3);
        this.awayDefenceTV = (TextView) inflate.findViewById(R.id.awayCountId3);
        this.leagueId = getActivity().getIntent().getIntExtra("LeagueId", 0);
        this.year = getActivity().getIntent().getIntExtra("Season", 0);
        getRecentFixture();
        return inflate;
    }

    private void getRecentFixture() {
        this.lottieAnimationView.setVisibility(0);
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + this.leagueId + "&season=" + this.year, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    FixtureResponse unused = PredictionFragment.this.fixtureResponse = (FixtureResponse) new Gson().fromJson(new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers))).toString(), FixtureResponse.class);
                    Collections.sort(PredictionFragment.this.fixtureResponse.getResponse(), new Comparator<FixtureResponse.Response>() {
                        public int compare(FixtureResponse.Response response, FixtureResponse.Response response2) {
                            return response.getFixture().getDate().compareTo(response2.getFixture().getDate());
                        }
                    });
                    int i = 0;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= PredictionFragment.this.fixtureResponse.getResponse().size()) {
                            break;
                        } else if (PredictionFragment.this.fixtureResponse.getResponse().get(i2).getFixture().getDate().getTime() > new Date().getTime()) {
                            i = PredictionFragment.this.fixtureResponse.getResponse().get(i2).getFixture().getId();
                            PredictionFragment predictionFragment = PredictionFragment.this;
                            Date unused2 = predictionFragment.gameDate = predictionFragment.fixtureResponse.getResponse().get(i2).getFixture().getDate();
                            break;
                        } else {
                            i2++;
                        }
                    }
                    PredictionFragment.this.getPrediction(i);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                PredictionFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(PredictionFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
    public void getPrediction(int i) {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/predictions?fixture=" + i, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    PredictionResponse unused = PredictionFragment.this.predictionResponse = (PredictionResponse) new Gson().fromJson(jSONObject.toString(), PredictionResponse.class);
                    if (PredictionFragment.this.predictionResponse.getResponse().size() == 0) {
                        PredictionFragment.this.nothingFoundLL.setVisibility(0);
                        PredictionFragment.this.mainLL.setVisibility(8);
                    } else {
                        PredictionFragment.this.mainLL.setVisibility(0);
                        PredictionFragment.this.nothingFoundLL.setVisibility(8);
                        PredictionFragment.this.createMainPage();
                    }
                    PredictionFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                PredictionFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(PredictionFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        PredictionResponse.Teams teams = this.predictionResponse.getResponse().get(this.predictionResponse.getResponse().size() - 1).getTeams();
        ArrayList arrayList = new ArrayList();
        PredictionResponse.Predictions predictions = this.predictionResponse.getResponse().get(this.predictionResponse.getResponse().size() - 1).getPredictions();
        arrayList.add(new Prediction("Who will win?", teams.getHome().getName(), teams.getAway().getName(), predictions.getPercent().getHome(), predictions.getPercent().getAway(), "Draw", predictions.getPercent().getDraw()));
        arrayList.add(new Prediction("How much goal?", teams.getHome().getName(), teams.getAway().getName(), predictions.getGoals().getHome(), predictions.getGoals().getAway(), "", "X"));
        this.viewPager2.setAdapter(new CustomPagerAdapter(getContext(), arrayList, this.gameDate));
        this.circleIndicator.setViewPager(this.viewPager2);
        this.homeFormation.setProgress(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getForm().getHome()));
        this.awayFormation.setProgress(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getForm().getAway()));
        this.homeAttack.setProgress(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getAtt().getHome()));
        this.awayAttack.setProgress(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getAtt().getAway()));
        this.homeDefence.setProgress(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getDef().getHome()));
        this.awayDefence.setProgress(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getDef().getAway()));
        this.homeFormationTV.setText(String.valueOf(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getForm().getHome())));
        this.awayFormationTV.setText(String.valueOf(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getForm().getAway())));
        this.homeAttackTV.setText(String.valueOf(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getAtt().getHome())));
        this.awayAttackTV.setText(String.valueOf(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getAtt().getAway())));
        this.homeDefenceTV.setText(String.valueOf(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getDef().getHome())));
        this.awayDefenceTV.setText(String.valueOf(getIntValue(this.predictionResponse.getResponse().get(0).getComparison().getDef().getAway())));
    }

    private int getIntValue(String str) {
        str.contentEquals("null");
        return str.contains("%") ? Integer.valueOf(str.substring(0, str.indexOf("%"))).intValue() : str.contains(".") ? Integer.valueOf(str.substring(0, str.indexOf("."))).intValue() : 0;
    }
}
