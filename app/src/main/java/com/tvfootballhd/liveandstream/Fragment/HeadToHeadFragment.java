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
import com.tvfootballhd.liveandstream.Adapter.HeadToHeadAdapter;
import com.tvfootballhd.liveandstream.Model.PredictionResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class HeadToHeadFragment extends Fragment {
    private ArrayList<ArrayList<PredictionResponse.H2h>> head2headlist;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingFoundLL;
    /* access modifiers changed from: private */
    public PredictionResponse predictionResponse;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.head2headlist = new ArrayList<>();
        View inflate = layoutInflater.inflate(R.layout.fragment_head_to_head, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.headToHeadLottieAnimationId);
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.headToHeadMainLL);
        this.nothingFoundLL = (LinearLayout) inflate.findViewById(R.id.headToHeadNothingFoundLLId);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.headToHeadRecyclerViewId);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        getPrediction(getActivity().getIntent().getIntExtra("FixtureId", 0));
        return inflate;
    }

    private void getPrediction(int i) {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/predictions?fixture=" + i, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    PredictionResponse unused = HeadToHeadFragment.this.predictionResponse = (PredictionResponse) new Gson().fromJson(jSONObject.toString(), PredictionResponse.class);
                    if (HeadToHeadFragment.this.predictionResponse.getResponse().get(0).getH2h().size() == 0) {
                        HeadToHeadFragment.this.nothingFoundLL.setVisibility(0);
                        HeadToHeadFragment.this.mainLL.setVisibility(8);
                    } else {
                        HeadToHeadFragment.this.mainLL.setVisibility(0);
                        HeadToHeadFragment.this.nothingFoundLL.setVisibility(8);
                        HeadToHeadFragment.this.createMainPage();
                    }
                    HeadToHeadFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                HeadToHeadFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(HeadToHeadFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        this.head2headlist.clear();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.predictionResponse.getResponse().get(0).getH2h().size(); i++) {
            int id = this.predictionResponse.getResponse().get(0).getH2h().get(i).getLeague().getId();
            ArrayList arrayList2 = new ArrayList();
            if (doesNotContainLeague(arrayList, id)) {
                arrayList.add(Integer.valueOf(id));
                for (int i2 = 0; i2 < this.predictionResponse.getResponse().get(0).getH2h().size(); i2++) {
                    PredictionResponse.H2h h2h = this.predictionResponse.getResponse().get(0).getH2h().get(i2);
                    if (id == h2h.getLeague().getId()) {
                        arrayList2.add(h2h);
                    }
                }
                this.head2headlist.add(arrayList2);
            }
        }
        this.recyclerView.setAdapter(new HeadToHeadAdapter(getContext(), this.head2headlist));
    }

    private boolean doesNotContainLeague(ArrayList<Integer> arrayList, int i) {
        if (arrayList.size() == 0) {
            return true;
        }
        int i2 = 0;
        boolean z = false;
        while (i2 < arrayList.size()) {
            if (i == arrayList.get(i2).intValue()) {
                return false;
            }
            i2++;
            z = true;
        }
        return z;
    }
}
