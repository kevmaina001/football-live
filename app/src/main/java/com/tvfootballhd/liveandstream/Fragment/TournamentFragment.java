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
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.tvfootballhd.liveandstream.Configr;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Activity.LeagueDetailsActivity;
import com.tvfootballhd.liveandstream.Adapter.LeagueAdapter;
import com.tvfootballhd.liveandstream.Model.CurrentLeague;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class TournamentFragment extends Fragment {
    /* access modifiers changed from: private */
    public ArrayList<CurrentLeague.Response> filteredList;
    /* access modifiers changed from: private */
    public LeagueAdapter leagueAdapter;
    /* access modifiers changed from: private */
    public CurrentLeague leagueResponse;
    private ArrayList<CurrentLeague.Response> list;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    private CurrentLeague.Response returnResponse;
    private SearchView searchView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tournament, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.list = new ArrayList<>();
        this.filteredList = new ArrayList<>();
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        SearchView searchView2 = (SearchView) inflate.findViewById(R.id.leagueSearchViewId);
        this.searchView = searchView2;
        TextView textView = (TextView) this.searchView.findViewById(searchView2.getContext().getResources().getIdentifier("android:id/search_src_text", (String) null, (String) null));
        textView.setTextColor(-1);
        textView.setPadding(10, 0, 0, 0);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (TournamentFragment.this.filteredList.size() > 0) {
                    TournamentFragment.this.filter(str);
                } else {
                    Toast.makeText(TournamentFragment.this.getContext(), "Nothing to search!!!", 0).show();
                }
                return false;
            }
        });
        return inflate;
    }

    /* access modifiers changed from: private */
    public void filter(String str) {
        ArrayList arrayList = new ArrayList();
        Iterator<CurrentLeague.Response> it = this.filteredList.iterator();
        while (it.hasNext()) {
            CurrentLeague.Response next = it.next();
            if ((next.getLeague().getName().toLowerCase() + next.getCountry().getName().toLowerCase()).contains(str.toLowerCase())) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", 0).show();
        } else {
            this.leagueAdapter.filterList(arrayList);
        }
    }

    private void getData() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/leagues?current=true", new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    CurrentLeague unused = TournamentFragment.this.leagueResponse = (CurrentLeague) new Gson().fromJson(jSONObject.toString(), CurrentLeague.class);
                    TournamentFragment.this.lottieAnimationView.setVisibility(8);
                    try {
                        TournamentFragment.this.createMainPage();
                    } catch (Exception e) {
                        Log.d("Errrrrrr", e.getMessage());
                    }
                } catch (UnsupportedEncodingException | JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                TournamentFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(TournamentFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
    public void createMainPage() {
        createCurrentLeagueList();
    }

    private void createCurrentLeagueList() {
        this.list = this.leagueResponse.getResponse();
        createFilteredList();
        LeagueAdapter leagueAdapter2 = new LeagueAdapter(getContext(), this.filteredList);
        this.leagueAdapter = leagueAdapter2;
        this.recyclerView.setAdapter(leagueAdapter2);
        this.leagueAdapter.setOnClickListener(new LeagueAdapter.OnClickListener() {
            public void OnClick(int i) {
                CurrentLeague.Response result = TournamentFragment.this.leagueAdapter.getResult();
                Intent intent = new Intent(TournamentFragment.this.getActivity(), LeagueDetailsActivity.class);
                intent.putExtra("leagueId", result.getLeague().getId());
                intent.putExtra("leagueName", result.getLeague().getName());
                intent.putExtra("leagueCountryName", result.getCountry().getName());
                intent.putExtra("leagueId", result.getLeague().getId());
                intent.putExtra("year", result.getSeasons().get(result.getSeasons().size() - 1).getYear());
                TournamentFragment.this.startActivity(intent);
            }
        });
    }

    private void createFilteredList() {
        for (int i = 0; i < Config.leagueId.length; i++) {
            if (containLeagueTwo(Config.leagueId[i])) {
                this.filteredList.add(getLeagueResponse(Config.leagueId[i]));
                removeFromListTwo(Config.leagueId[i]);
            }
        }
        this.filteredList.addAll(this.list);
    }

    private void removeFromListTwo(int i) {
        for (int i2 = 0; i2 < this.list.size(); i2++) {
            if (this.list.get(i2).getLeague().getId() == i) {
                this.list.remove(i2);
                return;
            }
        }
    }

    private CurrentLeague.Response getLeagueResponse(int i) {
        int i2 = 0;
        while (true) {
            if (i2 >= this.list.size()) {
                break;
            } else if (this.list.get(i2).getLeague().getId() == i) {
                this.returnResponse = this.list.get(i2);
                break;
            } else {
                i2++;
            }
        }
        return this.returnResponse;
    }

    private boolean containLeagueTwo(int i) {
        for (int i2 = 0; i2 < this.list.size(); i2++) {
            if (this.list.get(i2).getLeague().getId() == i) {
                return true;
            }
        }
        return false;
    }
}
