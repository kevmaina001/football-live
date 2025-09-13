package com.tvfootballhd.liveandstream.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Activity.PlayerDetailsActivity;
import com.tvfootballhd.liveandstream.Adapter.PlayerStatesAdapter;
import com.tvfootballhd.liveandstream.Model.TopScorerResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class TopScorerFragment extends Fragment {
    private int leagueId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingFoundLL;
    /* access modifiers changed from: private */
    public TopScorerResponse playerStatesResponse;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public int year;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_top_scorer, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.nothingFoundLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.leagueId = getActivity().getIntent().getIntExtra("LeagueId", 0);
        this.year = getActivity().getIntent().getIntExtra("Season", 0);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        getPlayerStates();
        return inflate;
    }

    private void getPlayerStates() {
        this.lottieAnimationView.setVisibility(0);
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/players/topscorers?league=" + this.leagueId + "&season=" + this.year, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    TopScorerResponse unused = TopScorerFragment.this.playerStatesResponse = (TopScorerResponse) new Gson().fromJson(jSONObject.toString(), TopScorerResponse.class);
                    if (TopScorerFragment.this.playerStatesResponse.getResponse().size() == 0) {
                        TopScorerFragment.this.nothingFoundLL.setVisibility(0);
                    } else {
                        TopScorerFragment.this.mainLL.setVisibility(0);
                        TopScorerFragment.this.createMainPage();
                    }
                    TopScorerFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                TopScorerFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(TopScorerFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        PlayerStatesAdapter playerStatesAdapter = new PlayerStatesAdapter(getContext(), this.playerStatesResponse.getResponse());
        this.recyclerView.setAdapter(playerStatesAdapter);
        playerStatesAdapter.setOnClickListener(new PlayerStatesAdapter.OnClickListener() {
            public void onClick(int i) {
                TopScorerResponse.Response response = TopScorerFragment.this.playerStatesResponse.getResponse().get(i);
                Intent intent = new Intent(TopScorerFragment.this.getActivity(), PlayerDetailsActivity.class);
                intent.putExtra("PlayerId", response.getPlayer().getId());
                intent.putExtra("Season", TopScorerFragment.this.year);
                intent.putExtra("PlayerName", response.getPlayer().getName());
                intent.putExtra("PlayerTeam", response.getStatistics().get(0).getTeam().getName());
                intent.putExtra("TeamIcon", response.getStatistics().get(0).getTeam().getLogo());
                intent.putExtra("PlayerIcon", response.getPlayer().getPhoto());
                TopScorerFragment.this.startActivity(intent);
            }
        });
    }
}
