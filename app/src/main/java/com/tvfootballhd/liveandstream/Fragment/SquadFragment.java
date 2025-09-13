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
import com.tvfootballhd.liveandstream.Adapter.SquadAdapter;
import com.tvfootballhd.liveandstream.Model.SquadResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class SquadFragment extends Fragment {
    private int id;
    private int leagueId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingLL;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public int seasonId;
    /* access modifiers changed from: private */
    public SquadResponse squadResponse;
    private String teamIcon;
    private String teamName;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_squad, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.id = getActivity().getIntent().getIntExtra("TeamId", 0);
        this.leagueId = getActivity().getIntent().getIntExtra("LeagueId", 0);
        this.seasonId = getActivity().getIntent().getIntExtra("Season", 0);
        this.teamName = getActivity().getIntent().getStringExtra("TeamName");
        this.teamIcon = getActivity().getIntent().getStringExtra("TeamIcon");
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.nothingLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getTeamSquad();
        return inflate;
    }

    private void getTeamSquad() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/players/squads?team=" + this.id, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    SquadResponse unused = SquadFragment.this.squadResponse = (SquadResponse) new Gson().fromJson(jSONObject.toString(), SquadResponse.class);
                    if (SquadFragment.this.squadResponse.getResponse().size() == 0) {
                        SquadFragment.this.nothingLL.setVisibility(0);
                    } else {
                        SquadFragment.this.mainLL.setVisibility(0);
                        SquadFragment.this.createMainPage();
                    }
                    SquadFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                SquadFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(SquadFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        SquadAdapter squadAdapter = new SquadAdapter(getContext(), this.squadResponse.response.get(0).players);
        this.recyclerView.setAdapter(squadAdapter);
        squadAdapter.setEmranClickListener(new SquadAdapter.EmranClickListener() {
            public void onClick(int i) {
                SquadResponse.Response response = SquadFragment.this.squadResponse.getResponse().get(SquadFragment.this.squadResponse.getResponse().size() - 1);
                Intent intent = new Intent(SquadFragment.this.getActivity(), PlayerDetailsActivity.class);
                intent.putExtra("PlayerId", response.getPlayers().get(i).getId());
                intent.putExtra("Season", SquadFragment.this.seasonId);
                intent.putExtra("PlayerName", response.getPlayers().get(i).getName());
                intent.putExtra("PlayerTeam", response.getTeam().getName());
                intent.putExtra("TeamIcon", response.getTeam().getName());
                intent.putExtra("PlayerIcon", response.getPlayers().get(i).getPhoto());
                SquadFragment.this.startActivity(intent);
            }
        });
    }
}
