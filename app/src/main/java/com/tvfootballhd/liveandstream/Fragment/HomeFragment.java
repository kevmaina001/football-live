package com.tvfootballhd.liveandstream.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tvfootballhd.liveandstream.Activity.AllRecentMatchActivity;
import com.tvfootballhd.liveandstream.Activity.RecentMatchDetailsActivity;
import com.tvfootballhd.liveandstream.Activity.WeeklyFixtureActivity;
import com.tvfootballhd.liveandstream.Adapter.CurrentLeagueAdapter;
import com.tvfootballhd.liveandstream.Adapter.RecentMatchAdapter;
import com.tvfootballhd.liveandstream.Adapter.SliderAdapter;
import com.tvfootballhd.liveandstream.Configr;
import com.tvfootballhd.liveandstream.Model.CurrentLeague;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.Model.LastFiftyMatches;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class HomeFragment extends Fragment {
    final long DELAY_MS = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    final long PERIOD_MS = 8000;
    private CurrentLeagueAdapter currentLeagueAdapter;
    private int currentPage = 0;
    public ArrayList<CurrentLeague.Response> filteredLeagueList;
    ArrayList<ArrayList<FixtureResponse.Response>> filteredList;
    public LastFiftyMatches lastFiftyMatches;
    private ArrayList<CurrentLeague.Response> leagueList;
    private RecyclerView leagueNameRecyclerView;
    public CurrentLeague leagueResponse;
    public LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    private CurrentLeague.Response returnResponse;
    private SearchView searchBar;
    private TextView seeMoreTV;
    private Timer timer;
    ArrayList<ArrayList<FixtureResponse.Response>> todayFixtureArrayList;
    public FixtureResponse todayFixtureResponse;
    private SliderView viewPager2;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.filteredList = new ArrayList<>();
        this.todayFixtureArrayList = new ArrayList<>();
        this.filteredLeagueList = new ArrayList<>();
        this.leagueList = new ArrayList<>();
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.seeMoreTV = (TextView) inflate.findViewById(R.id.seeMoreId);
        this.viewPager2 = (SliderView) inflate.findViewById(R.id.sliderLeagueUpcomingGame);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recentMatchRecyclerview);
        this.leagueNameRecyclerView = (RecyclerView) inflate.findViewById(R.id.leagueNameRecyclerViewId);
        this.searchBar = (SearchView) inflate.findViewById(R.id.searchView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.leagueNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(this.leagueNameRecyclerView);
        AdsManager.LoadInterstitialAd(getActivity());
        getTodayMatch();
        getLastFiftyMatch();
        getSevenDaysFixture();
        this.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (HomeFragment.this.filteredLeagueList.size() <= 0) {
                    return true;
                }
                HomeFragment.this.filterLeagueList(str);
                return true;
            }
        });
        return inflate;
    }

    public void filterLeagueList(String str) {
        ArrayList arrayList = new ArrayList();
        Iterator<CurrentLeague.Response> it = this.filteredLeagueList.iterator();
        while (it.hasNext()) {
            CurrentLeague.Response next = it.next();
            if (next.getLeague().getName().toLowerCase().contains(str.toLowerCase()) || next.getCountry().getName().toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            this.currentLeagueAdapter.filterList(arrayList);
        }
    }

    private void getSevenDaysFixture() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/leagues?current=true", new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    CurrentLeague unused = HomeFragment.this.leagueResponse = (CurrentLeague) new Gson().fromJson(jSONObject.toString(), CurrentLeague.class);
                    HomeFragment.this.lottieAnimationView.setVisibility(8);
                    try {
                        HomeFragment.this.createLeaguePage();
                    } catch (Exception e) {
                        Log.d("Errrrrrr", e.getMessage());
                    }
                } catch (UnsupportedEncodingException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                HomeFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(HomeFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("X-RapidAPI-Key", Configr.API_KEY);
                hashMap.put("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
                return hashMap;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createLeaguePage() {
        this.leagueList = this.leagueResponse.getResponse();
        createFilteredLeagueList();
        CurrentLeagueAdapter currentLeagueAdapter2 = new CurrentLeagueAdapter(getContext(), this.filteredLeagueList);
        this.currentLeagueAdapter = currentLeagueAdapter2;
        this.leagueNameRecyclerView.setAdapter(currentLeagueAdapter2);
        this.currentLeagueAdapter.setOnClickListener(new CurrentLeagueAdapter.OnClickListener() {

            @Override
            public void OnClick(int i) {

                AdsManager.ShowInterstitialAd(getActivity());
                leagueId(i);
            }
        });
    }

    public void leagueId(int i) {
        CurrentLeague.Response result = this.currentLeagueAdapter.getResult();
        Intent intent = new Intent(getContext(), WeeklyFixtureActivity.class);
        intent.putExtra("leagueId", result.getLeague().getId());
        intent.putExtra("leagueName", result.getLeague().getName());
        intent.putExtra("leagueLogo", result.getLeague().getLogo());
        intent.putExtra("countryName", result.getCountry().getName());
        intent.putExtra("season", result.getSeasons().get(result.getSeasons().size() - 1).getYear());
        getContext().startActivity(intent);
    }

    private void createFilteredLeagueList() {
        for (int i = 0; i < Config.leagueId.length; i++) {
            if (containLeagueTwo(Config.leagueId[i])) {
                this.filteredLeagueList.add(getLeagueResponse(Config.leagueId[i]));
                removeFromListTwo(Config.leagueId[i]);
            }
        }
        this.filteredLeagueList.addAll(this.leagueList);
    }

    private void removeFromListTwo(int i) {
        for (int i2 = 0; i2 < this.leagueList.size(); i2++) {
            if (this.leagueList.get(i2).getLeague().getId() == i) {
                this.leagueList.remove(i2);
                return;
            }
        }
    }

    private CurrentLeague.Response getLeagueResponse(int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= this.leagueList.size()) {
                break;
            } else if (this.leagueList.get(i2).getLeague().getId() == i) {
                this.returnResponse = this.leagueList.get(i2);
                break;
            } else {
                i2++;
            }
        }
        return this.returnResponse;
    }

    private boolean containLeagueTwo(int i) {
        for (int i2 = 0; i2 < this.leagueList.size(); i2++) {
            if (this.leagueList.get(i2).getLeague().getId() == i) {
                return true;
            }
        }
        return false;
    }

    private void getLastFiftyMatch() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?last=50", new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    LastFiftyMatches unused = HomeFragment.this.lastFiftyMatches = (LastFiftyMatches) new Gson().fromJson(jSONObject.toString(), LastFiftyMatches.class);
                    HomeFragment.this.lottieAnimationView.setVisibility(8);
                    try {
                        HomeFragment.this.createRecentMatches();
                    } catch (Exception e) {
                        Log.d("Errrrrrr", e.getMessage());
                    }
                } catch (UnsupportedEncodingException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                HomeFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(HomeFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("X-RapidAPI-Key", Configr.API_KEY);
                hashMap.put("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
                return hashMap;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createRecentMatches() {
        RecentMatchAdapter recentMatchAdapter = new RecentMatchAdapter(getContext(), this.lastFiftyMatches.getResponse(), 5);
        this.recyclerView.setAdapter(recentMatchAdapter);
        this.seeMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AdsManager.ShowInterstitialAd(getActivity());
                loist(view);
            }
        });
        recentMatchAdapter.setClickListener(new RecentMatchAdapter.ClickListener() {
            public void onClick(int i) {
                AdsManager.ShowInterstitialAd(getActivity());
                Intent intent = new Intent(HomeFragment.this.getActivity(), RecentMatchDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("FixtureResponseClass", HomeFragment.this.lastFiftyMatches.getResponse().get(i));
                intent.putExtra("FixtureId", HomeFragment.this.lastFiftyMatches.getResponse().get(i).getFixture().getId());
                intent.putExtra("HomeId", HomeFragment.this.lastFiftyMatches.getResponse().get(i).getTeams().getHome().getId());
                intent.putExtra("AwayId", HomeFragment.this.lastFiftyMatches.getResponse().get(i).getTeams().getAway().getId());
                intent.putExtras(bundle);
                HomeFragment.this.getContext().startActivity(intent);

            }
        });
    }

    public void loist(View view) {
        Intent intent = new Intent(getContext(), AllRecentMatchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", this.lastFiftyMatches);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getTodayMatch() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    FixtureResponse unused = HomeFragment.this.todayFixtureResponse = (FixtureResponse) new Gson().fromJson(jSONObject.toString(), FixtureResponse.class);
                    HomeFragment.this.lottieAnimationView.setVisibility(8);
                    try {
                        HomeFragment.this.createSlider();
                    } catch (Exception e) {
                        Log.d("Errrrrrr", e.getMessage());
                    }
                } catch (UnsupportedEncodingException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                HomeFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(HomeFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            public Map getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("X-RapidAPI-Key", Configr.API_KEY);
                hashMap.put("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
                return hashMap;
            }
        });
    }

    /* access modifiers changed from: private */
    public void createSlider() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.todayFixtureResponse.getResponse().size(); i++) {
            int id = this.todayFixtureResponse.getResponse().get(i).getLeague().getId();
            ArrayList arrayList2 = new ArrayList();
            if (doesNotContainLeague(arrayList, id)) {
                arrayList.add(Integer.valueOf(id));
                for (int i2 = 0; i2 < this.todayFixtureResponse.getResponse().size(); i2++) {
                    FixtureResponse.Response response = this.todayFixtureResponse.getResponse().get(i2);
                    if (id == response.getLeague().getId()) {
                        arrayList2.add(response);
                    }
                }
                this.todayFixtureArrayList.add(arrayList2);
            }
        }
        createFilteredList();
        createLastFive();
    }

    private void createLastFive() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.filteredList.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.filteredList.get(i).size()) {
                    break;
                }
                FixtureResponse.Response response = (FixtureResponse.Response) this.filteredList.get(i).get(i2);
                if (response.getFixture().getDate().compareTo(new Date()) > 0) {
                    arrayList.add(response);
                    break;
                }
                i2++;
            }
        }
        this.viewPager2.setSliderAdapter(new SliderAdapter(getContext(), arrayList));
        this.viewPager2.setIndicatorAnimation(IndicatorAnimationType.WORM);
        this.viewPager2.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        this.viewPager2.startAutoCycle();
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

    private boolean containLeague(int i) {
        for (int i2 = 0; i2 < this.todayFixtureArrayList.size(); i2++) {
            if (((FixtureResponse.Response) this.todayFixtureArrayList.get(i2).get(0)).getLeague().getId() == i) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<FixtureResponse.Response> getResponse(int i) {
        ArrayList<FixtureResponse.Response> arrayList = new ArrayList<>();
        for (int i2 = 0; i2 < this.todayFixtureArrayList.size(); i2++) {
            if (((FixtureResponse.Response) this.todayFixtureArrayList.get(i2).get(0)).getLeague().getId() == i) {
                return this.todayFixtureArrayList.get(i2);
            }
        }
        return arrayList;
    }

    private void removeFromList(int i) {
        for (int i2 = 0; i2 < this.todayFixtureArrayList.size(); i2++) {
            if (((FixtureResponse.Response) this.todayFixtureArrayList.get(i2).get(0)).getLeague().getId() == i) {
                this.todayFixtureArrayList.remove(i2);
                return;
            }
        }
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
