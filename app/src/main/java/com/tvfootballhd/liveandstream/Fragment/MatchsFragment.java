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
import com.tvfootballhd.liveandstream.Adapter.MatchAdapter;
import com.tvfootballhd.liveandstream.Model.MatchResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class MatchsFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private LinearLayout mainLL;
    /* access modifiers changed from: private */
    public MatchResponse matchResponse;
    private LinearLayout nothingLL;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_matchs, viewGroup, false);

        AdsManager.LoadInterstitialAd2(getActivity());
        AdsManager.LoadInterstitialAd(getActivity());
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.layoutManager = linearLayoutManager;
        this.recyclerView.setLayoutManager(linearLayoutManager);
        getData();
        return inflate;
    }

    public void getData() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?league=" + getActivity().getIntent().getIntExtra("LeagueId", 0) + "&season=" + getActivity().getIntent().getIntExtra("Season", 0), new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    MatchResponse unused = MatchsFragment.this.matchResponse = (MatchResponse) new Gson().fromJson(jSONObject.toString(), MatchResponse.class);
                    MatchsFragment.this.lottieAnimationView.setVisibility(8);
                    MatchsFragment.this.createMainPage();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                MatchsFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(MatchsFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        Collections.sort(this.matchResponse.getResponse(), new Comparator<MatchResponse.Response>() {
            public int compare(MatchResponse.Response response, MatchResponse.Response response2) {
                return response.getFixture().getDate().compareTo(response2.getFixture().getDate());
            }
        });
        this.recyclerView.setAdapter(new MatchAdapter(this.matchResponse, getContext()));
        int i = 0;
        int i2 = 0;
        while (i < this.matchResponse.response.size() && new Date().getTime() > this.matchResponse.getResponse().get(i).getFixture().getDate().getTime()) {
            i2 = i;
            i++;
        }
        this.layoutManager.scrollToPosition(i2);
    }
}
