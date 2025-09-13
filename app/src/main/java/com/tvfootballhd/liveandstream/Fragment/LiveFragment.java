package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Adapter.AllLiveAdapter;
import com.tvfootballhd.liveandstream.Model.LiveResponse;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class LiveFragment extends Fragment {
    ArrayList<ArrayList<LiveResponse.Response>> filteredList;
    /* access modifiers changed from: private */
    public LiveResponse liveResponse;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private LinearLayout mainLL;
    private LinearLayout noMatchFoundLL;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ArrayList<LiveResponse.Response>> todayFixtureArrayList;
    private Toolbar toolbar;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        
        this.todayFixtureArrayList = new ArrayList<>();
        this.filteredList = new ArrayList<>();
        this.swipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.liveLayoutId);
        this.noMatchFoundLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.liveRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        getData();
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                LiveFragment.this.getData();
                LiveFragment.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
        return inflate;
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

    /* access modifiers changed from: private */
    public void getData() {
        this.todayFixtureArrayList.clear();
        this.filteredList.clear();
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new JsonObjectRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?live=all", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                LiveResponse unused = LiveFragment.this.liveResponse = (LiveResponse) new Gson().fromJson(jSONObject.toString(), LiveResponse.class);
                LiveFragment.this.createMainPage();
                LiveFragment.this.lottieAnimationView.setVisibility(8);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                LiveFragment.this.lottieAnimationView.setVisibility(8);
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
        if (this.liveResponse.getResponse().size() == 0) {
            this.mainLL.setVisibility(8);
            this.noMatchFoundLL.setVisibility(0);
            return;
        }
        this.mainLL.setVisibility(0);
        this.noMatchFoundLL.setVisibility(8);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.liveResponse.getResponse().size(); i++) {
            int id = this.liveResponse.getResponse().get(i).getLeague().getId();
            ArrayList arrayList2 = new ArrayList();
            if (doesNotContainLeague(arrayList, id)) {
                arrayList.add(Integer.valueOf(id));
                for (int i2 = 0; i2 < this.liveResponse.getResponse().size(); i2++) {
                    LiveResponse.Response response = this.liveResponse.getResponse().get(i2);
                    if (id == response.getLeague().getId()) {
                        arrayList2.add(response);
                    }
                }
                this.todayFixtureArrayList.add(arrayList2);
            }
        }
        createFilteredList();
        this.recyclerView.setAdapter(new AllLiveAdapter(getContext(), this.filteredList));
    }

    private void createFilteredList() {
        for (int i = 0; i < Config.leagueId.length; i++) {
            if (containLeague(Config.leagueId[i])) {
                this.filteredList.add(getResponse(Config.leagueId[i]));
                removeFromList(Config.leagueId[i]);
            }
        }
        this.filteredList.addAll(this.todayFixtureArrayList);
    }

    private void removeFromList(int i) {
        for (int i2 = 0; i2 < this.todayFixtureArrayList.size(); i2++) {
            if (((LiveResponse.Response) this.todayFixtureArrayList.get(i2).get(0)).getLeague().getId() == i) {
                this.todayFixtureArrayList.remove(i2);
                return;
            }
        }
    }

    private ArrayList<LiveResponse.Response> getResponse(int i) {
        ArrayList<LiveResponse.Response> arrayList = new ArrayList<>();
        for (int i2 = 0; i2 < this.todayFixtureArrayList.size(); i2++) {
            if (((LiveResponse.Response) this.todayFixtureArrayList.get(i2).get(0)).getLeague().getId() == i) {
                return this.todayFixtureArrayList.get(i2);
            }
        }
        return arrayList;
    }

    private boolean containLeague(int i) {
        for (int i2 = 0; i2 < this.todayFixtureArrayList.size(); i2++) {
            if (((LiveResponse.Response) this.todayFixtureArrayList.get(i2).get(0)).getLeague().getId() == i) {
                return true;
            }
        }
        return false;
    }
}
