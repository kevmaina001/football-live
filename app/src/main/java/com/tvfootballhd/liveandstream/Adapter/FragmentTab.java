package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Model.FixtureResponse;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.Utils.Config;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentTab extends Fragment {
    Context context;
    String date;
    ArrayList<ArrayList<FixtureResponse.Response>> filteredList;
    private FixtureAdapter fixtureAdapter;
    /* access modifiers changed from: private */
    public FixtureResponse fixtureResponse;
    private RecyclerView.LayoutManager layoutManager;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    ArrayList<ArrayList<FixtureResponse.Response>> todayFixtureArrayList;

    public FragmentTab(String str, Context context2) {
        this.date = str;
        this.context = context2;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.tab_data, viewGroup, false);
        this.filteredList = new ArrayList<>();
        this.todayFixtureArrayList = new ArrayList<>();
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.mainRecyclerViewId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        this.layoutManager = linearLayoutManager;
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.animationView);
        getData("https://api-football-v1.p.rapidapi.com/v3/fixtures?date=" + this.date);
        return inflate;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void getData(String str) {
        Volley.newRequestQueue(this.context).add(new CacheRequest(0, str, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    FixtureResponse unused = FragmentTab.this.fixtureResponse = (FixtureResponse) new Gson().fromJson(jSONObject.toString(), FixtureResponse.class);
                    FragmentTab.this.lottieAnimationView.setVisibility(8);
                    FragmentTab.this.createMainPage();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FragmentTab.this.context, "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        this.todayFixtureArrayList = new ArrayList<>();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.fixtureResponse.getResponse().size(); i++) {
            int id = this.fixtureResponse.getResponse().get(i).getLeague().getId();
            ArrayList arrayList2 = new ArrayList();
            if (doesNotContainLeague(arrayList, id)) {
                arrayList.add(Integer.valueOf(id));
                for (int i2 = 0; i2 < this.fixtureResponse.getResponse().size(); i2++) {
                    FixtureResponse.Response response = this.fixtureResponse.getResponse().get(i2);
                    if (id == response.getLeague().getId()) {
                        arrayList2.add(response);
                    }
                }
                this.todayFixtureArrayList.add(arrayList2);
            }
        }
        createFilteredList();
        this.recyclerView.setAdapter(new TodayMatchAdapter(getContext(), this.filteredList));
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

    private class CacheRequest extends Request<NetworkResponse> {
        private final Response.ErrorListener mErrorListener;
        private final Response.Listener<NetworkResponse> mListener;

        public CacheRequest(int i, String str, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(i, str, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
        }

        /* access modifiers changed from: protected */
        public Response<NetworkResponse> parseNetworkResponse(NetworkResponse networkResponse) {
            Cache.Entry parseCacheHeaders = HttpHeaderParser.parseCacheHeaders(networkResponse);
            if (parseCacheHeaders == null) {
                parseCacheHeaders = new Cache.Entry();
            }
            long currentTimeMillis = System.currentTimeMillis();
            parseCacheHeaders.data = networkResponse.data;
            parseCacheHeaders.softTtl = 180000 + currentTimeMillis;
            parseCacheHeaders.ttl = currentTimeMillis + 86400000;
            String str = networkResponse.headers.get("Date");
            if (str != null) {
                parseCacheHeaders.serverDate = HttpHeaderParser.parseDateAsEpoch(str);
            }
            String str2 = networkResponse.headers.get("Last-Modified");
            if (str2 != null) {
                parseCacheHeaders.lastModified = HttpHeaderParser.parseDateAsEpoch(str2);
            }
            parseCacheHeaders.responseHeaders = networkResponse.headers;
            return Response.success(networkResponse, parseCacheHeaders);
        }

        /* access modifiers changed from: protected */
        public void deliverResponse(NetworkResponse networkResponse) {
            this.mListener.onResponse(networkResponse);
        }

        /* access modifiers changed from: protected */
        public VolleyError parseNetworkError(VolleyError volleyError) {
            return super.parseNetworkError(volleyError);
        }

        public void deliverError(VolleyError volleyError) {
            this.mErrorListener.onErrorResponse(volleyError);
        }
    }
}
