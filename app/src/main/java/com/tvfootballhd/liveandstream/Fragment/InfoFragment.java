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
import com.tvfootballhd.liveandstream.Adapter.InfoAdapter;
import com.tvfootballhd.liveandstream.Model.InfoResponse;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoFragment extends Fragment {
    private ArrayList<ArrayList<InfoResponse.Response>> convertedInfoResponseLis;
    private int id;
    /* access modifiers changed from: private */
    public InfoResponse infoResponse;
    private int leagueId;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    /* access modifiers changed from: private */
    public LinearLayout mainLL;
    /* access modifiers changed from: private */
    public LinearLayout nothingLL;
    private RecyclerView recyclerView;
    private ArrayList<InfoResponse.Response> responseArrayList;
    private int seasonId;
    private String teamIcon;
    private String teamName;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_info, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.convertedInfoResponseLis = new ArrayList<>();
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
        getTeamInfo();
        return inflate;
    }

    private void getTeamInfo() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures?season=" + this.seasonId + "&team=" + this.id, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    InfoResponse unused = InfoFragment.this.infoResponse = (InfoResponse) new Gson().fromJson(jSONObject.toString(), InfoResponse.class);
                    if (InfoFragment.this.infoResponse.getResponse().size() == 0) {
                        InfoFragment.this.nothingLL.setVisibility(0);
                    } else {
                        InfoFragment.this.mainLL.setVisibility(0);
                        InfoFragment.this.createMainPage();
                    }
                    InfoFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                InfoFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(InfoFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        this.convertedInfoResponseLis.clear();
        this.responseArrayList = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i < this.infoResponse.getResponse().size(); i++) {
            if (this.infoResponse.getResponse().get(i).getFixture().getDate().before(date)) {
                this.responseArrayList.add(this.infoResponse.getResponse().get(i));
            }
        }
        Collections.sort(this.responseArrayList, new Comparator<InfoResponse.Response>() {
            public int compare(InfoResponse.Response response, InfoResponse.Response response2) {
                return response.getFixture().getDate().compareTo(response2.getFixture().getDate());
            }
        });
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.responseArrayList.size(); i2++) {
            int id2 = this.responseArrayList.get(i2).getLeague().getId();
            ArrayList arrayList2 = new ArrayList();
            if (doesNotContainLeague(arrayList, id2)) {
                arrayList.add(Integer.valueOf(id2));
                for (int i3 = 0; i3 < this.responseArrayList.size(); i3++) {
                    InfoResponse.Response response = this.responseArrayList.get(i3);
                    if (id2 == response.getLeague().getId()) {
                        arrayList2.add(response);
                    }
                }
                this.convertedInfoResponseLis.add(arrayList2);
            }
        }
        this.recyclerView.setAdapter(new InfoAdapter(getContext(), this.convertedInfoResponseLis, this.id));
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
