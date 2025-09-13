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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Adapter.LiveTVAdapter;
import com.tvfootballhd.liveandstream.Model.LiveTvResponse;
import com.tvfootballhd.liveandstream.VideoPlayer.ExoPlayerActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class VideoFragment extends Fragment {
    /* access modifiers changed from: private */
    public ArrayList<LiveTvResponse> arrayList;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private LinearLayout mainLL;
    private LinearLayout noMatchFoundLL;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.arrayList = new ArrayList<>();
        this.noMatchFoundLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.mainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        getData();
        return inflate;
    }

    private void getData() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new JsonObjectRequest(0, "https://sportsfevers.com/saifulfoot/api.php?get_all_channels&fbclid=IwAR0LFY5OY3TuVQdN74CQuT01r0nAY1KO4otBW2_Y0uZYnCLNBv63ZpfulRs", (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    JSONArray jSONArray = (JSONArray) jSONObject.get("LIVETV");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        VideoFragment.this.arrayList.add((LiveTvResponse) new Gson().fromJson(jSONArray.get(i).toString(), LiveTvResponse.class));
                    }
                    VideoFragment.this.createMainPage();
                    VideoFragment.this.lottieAnimationView.setVisibility(8);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VideoFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(VideoFragment.this.getContext(), volleyError.toString(), 0).show();
            }
        }));
    }

    /* access modifiers changed from: private */
    public void createMainPage() {
        Collections.sort(this.arrayList, new Comparator<LiveTvResponse>() {
            public int compare(LiveTvResponse liveTvResponse, LiveTvResponse liveTvResponse2) {
                return liveTvResponse.getId().compareTo(liveTvResponse2.getId());
            }
        });
        LiveTVAdapter liveTVAdapter = new LiveTVAdapter(getContext(), this.arrayList);
        this.recyclerView.setAdapter(liveTVAdapter);
        liveTVAdapter.setOnclickListener(new LiveTVAdapter.OnclickListener() {
            public void onClick(int i) {
                Intent intent = new Intent(VideoFragment.this.getContext(), ExoPlayerActivity.class);
                intent.putExtra("videourl", ((LiveTvResponse) VideoFragment.this.arrayList.get(i)).getChannel_url());
                VideoFragment.this.startActivity(intent);
            }
        });
    }
}
