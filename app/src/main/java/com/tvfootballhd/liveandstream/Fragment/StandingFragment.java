package com.tvfootballhd.liveandstream.Fragment;

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
import com.tvfootballhd.liveandstream.Adapter.StandingAdapter2;
import com.tvfootballhd.liveandstream.Model.StandingResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class StandingFragment extends Fragment {
    private int awayId;
    private int homeId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout nothingFoundLL;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public LinearLayout standingMainLL;
    /* access modifiers changed from: private */
    public StandingResponse standingResponse;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_standing, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.homeId = getActivity().getIntent().getIntExtra("HomeId", 0);
        this.awayId = getActivity().getIntent().getIntExtra("AwayId", 0);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.standingRecyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        this.nothingFoundLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.standingMainLL = (LinearLayout) inflate.findViewById(R.id.standingMainLL);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        getData();
        return inflate;
    }

    public void getData() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/standings?season=" + getActivity().getIntent().getIntExtra("Season", 0) + "&league=" + getActivity().getIntent().getIntExtra("LeagueId", 0), new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    StandingResponse unused = StandingFragment.this.standingResponse = (StandingResponse) new Gson().fromJson(jSONObject.toString(), StandingResponse.class);
                    if (StandingFragment.this.standingResponse.getResponse().size() == 0) {
                        StandingFragment.this.nothingFoundLL.setVisibility(0);
                    } else {
                        StandingFragment.this.standingMainLL.setVisibility(0);
                        StandingFragment.this.createMainPage();
                    }
                    StandingFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                StandingFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(StandingFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        this.recyclerView.setAdapter(new StandingAdapter2(getContext(), this.standingResponse.getResponse().get(0).getLeague().getStandings(), this.homeId, this.awayId));
    }
}
