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
import com.tvfootballhd.liveandstream.Adapter.EventAdapter;
import com.tvfootballhd.liveandstream.Model.EventResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class EventFragment extends Fragment {
    private int awayTeamId;
    /* access modifiers changed from: private */
    public EventResponse eventResponse;
    private int fixtureId;
    private int homeTeamId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingLL;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_event, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.fixtureId = getActivity().getIntent().getIntExtra("FixtureId", 0);
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.nothingLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.homeTeamId = getActivity().getIntent().getIntExtra("HomeId", 0);
        this.awayTeamId = getActivity().getIntent().getIntExtra("AwayId", 0);
        getEvents();
        return inflate;
    }

    private void getEvents() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures/events?fixture=" + this.fixtureId, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    EventResponse unused = EventFragment.this.eventResponse = (EventResponse) new Gson().fromJson(jSONObject.toString(), EventResponse.class);
                    if (EventFragment.this.eventResponse.getResponse().size() == 0) {
                        EventFragment.this.nothingLL.setVisibility(0);
                        EventFragment.this.mainLL.setVisibility(8);
                    } else {
                        EventFragment.this.mainLL.setVisibility(0);
                        EventFragment.this.nothingLL.setVisibility(8);
                        EventFragment.this.createMainPage();
                    }
                    EventFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                EventFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(EventFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        this.recyclerView.setAdapter(new EventAdapter(getContext(), this.eventResponse.getResponse(), this.homeTeamId, this.awayTeamId));
    }
}
