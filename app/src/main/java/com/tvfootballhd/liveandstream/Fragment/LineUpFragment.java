package com.tvfootballhd.liveandstream.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.tvfootballhd.liveandstream.AdsManager;
import com.google.gson.Gson;
import com.tvfootballhd.liveandstream.Model.LineUpResponse;
import com.tvfootballhd.liveandstream.Utils.CacheRequest;
import com.tvfootballhd.liveandstream.Utils.Config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.tvfootballhd.liveandstream.R;

public class LineUpFragment extends Fragment {
    private int awayTeamId;
    private int fixtureId;
    private int height = 0;
    private int homeTeamId;
    private int image;
    private ImageView imageView;
    /* access modifiers changed from: private */
    public LinearLayout lineUpNothingFoundLL;
    /* access modifiers changed from: private */
    public LineUpResponse lineUpResponse;
    /* access modifiers changed from: private */
    public LinearLayout lineupMainLL;
    /* access modifiers changed from: private */
    public LottieAnimationView lottieAnimationView;
    private int width = 0;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_line_up, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.imageView = (ImageView) inflate.findViewById(R.id.lineUpImageView);
        this.lottieAnimationView = (LottieAnimationView) inflate.findViewById(R.id.lottiId);
        this.lineUpNothingFoundLL = (LinearLayout) inflate.findViewById(R.id.nothingFoundLL);
        this.lineupMainLL = (LinearLayout) inflate.findViewById(R.id.mainLL);
        this.homeTeamId = getActivity().getIntent().getIntExtra("HomeId", 0);
        this.awayTeamId = getActivity().getIntent().getIntExtra("AwayId", 0);
        this.fixtureId = getActivity().getIntent().getIntExtra("FixtureId", 0);
        getLineUp();
        return inflate;
    }

    private void getLineUp() {
        this.lottieAnimationView.setVisibility(0);
        Volley.newRequestQueue(getContext()).add(new CacheRequest(0, "https://api-football-v1.p.rapidapi.com/v3/fixtures/lineups?fixture=" + this.fixtureId, new Response.Listener<NetworkResponse>() {
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)));
                    LineUpResponse unused = LineUpFragment.this.lineUpResponse = (LineUpResponse) new Gson().fromJson(jSONObject.toString(), LineUpResponse.class);
                    if (LineUpFragment.this.lineUpResponse.getResponse().size() == 0) {
                        LineUpFragment.this.lineUpNothingFoundLL.setVisibility(0);
                    } else {
                        LineUpFragment.this.lineupMainLL.setVisibility(0);
                        LineUpFragment.this.createMainPage();
                    }
                    LineUpFragment.this.lottieAnimationView.setVisibility(8);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                LineUpFragment.this.lottieAnimationView.setVisibility(8);
                Toast.makeText(LineUpFragment.this.getContext(), "onErrorResponse:\n\n" + volleyError.toString(), 0).show();
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
        Bitmap copy = ((BitmapDrawable) getResources().getDrawable(R.drawable.field)).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        this.width = copy.getWidth();
        this.height = copy.getHeight();
        Canvas canvas = new Canvas(copy);
        Paint paint = new Paint();
        paint.setColor(-16776961);
        paint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint();
        paint2.setColor(-1);
        paint2.setTextSize(60.0f);
        paint2.setTextAlign(Paint.Align.CENTER);
        Paint paint3 = new Paint();
        paint3.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint3.setTextAlign(Paint.Align.CENTER);
        int i = 0;
        LineUpResponse.Response response = this.lineUpResponse.getResponse().get(0);
        LineUpResponse.Response response2 = this.lineUpResponse.getResponse().get(1);
        paint3.setTextSize(40.0f);
        if (response.getStartXI() != null && response2.getStartXI() != null) {
            canvas.drawCircle(1000.0f, 377.0f, 60.0f, paint);
            if (response.getStartXI().get(0).getPlayer() != null) {
                canvas.drawText(response.getStartXI().get(response.getStartXI().size() - 1).getPlayer().getNumber() + "", 1000.0f, 397.0f, paint2);
            } else {
                canvas.drawText("", 1000.0f, 397.0f, paint2);
            }
            canvas.drawText(response.getStartXI().get(0).getPlayer().getName() + "", 1000.0f, 517.0f, paint3);
            canvas.drawCircle(1000.0f, 2650.0f, 60.0f, paint);
            if (response2.getStartXI().get(0).getPlayer() != null) {
                canvas.drawText(response2.getStartXI().get(0).getPlayer().getNumber() + "", 1000.0f, 2670.0f, paint2);
            } else {
                canvas.drawText("", 1000.0f, 397.0f, paint2);
            }
            canvas.drawText(response2.getStartXI().get(0).getPlayer().getName() + "", 1000.0f, 2790.0f, paint3);
            this.imageView.setImageBitmap(copy);
            int[] formation = getFormation(response);
            int[] formation2 = getFormation(response2);
            int length = 760 / (formation.length - 1);
            int i2 = 1;
            while (i2 < response.getStartXI().size()) {
                String grid = response.getStartXI().get(i2).getPlayer().getGrid();
                int parseInt = Integer.parseInt(grid.charAt(i) + "") - 2;
                int parseInt2 = Integer.parseInt(grid.charAt(grid.length() - 1) + "");
                int i3 = formation[parseInt] + 1;
                int i4 = (this.width / i3) * (i3 - parseInt2);
                int i5 = 600 + (parseInt * length);
                float f = (float) i4;
                canvas.drawCircle(f, (float) i5, 60.0f, paint);
                int i6 = i5 + 20;
                canvas.drawText(response.getStartXI().get(i2).getPlayer().getNumber() + "", f, (float) i6, paint2);
                canvas.drawText(response.getStartXI().get(i2).getPlayer().getName() + "", f, (float) (i6 + 120), paint3);
                i2++;
                i = 0;
            }
            int i7 = this.height - 600;
            int length2 = 760 / (formation2.length - 1);
            for (int i8 = 1; i8 < response2.getStartXI().size(); i8++) {
                String grid2 = response2.getStartXI().get(i8).getPlayer().getGrid();
                int parseInt3 = Integer.parseInt(grid2.charAt(0) + "") - 2;
                int parseInt4 = (this.width / (formation2[parseInt3] + 1)) * Integer.parseInt(grid2.charAt(grid2.length() - 1) + "");
                int i9 = i7 - (parseInt3 * length2);
                float f2 = (float) parseInt4;
                canvas.drawCircle(f2, (float) i9, 60.0f, paint);
                int i10 = i9 + 20;
                canvas.drawText(response2.getStartXI().get(i8).getPlayer().getNumber() + "", f2, (float) i10, paint2);
                canvas.drawText(response2.getStartXI().get(i8).getPlayer().getName() + "", f2, (float) (i10 + 120), paint3);
            }
            canvas.drawText(response.getTeam().getName(), 350.0f, 80.0f, paint3);
            canvas.drawText(response2.getTeam().getName(), 350.0f, (float) (this.height - 30), paint3);
            canvas.drawText("Formation: " + response.getFormation(), (float) (this.width - 350), 80.0f, paint3);
            canvas.drawText("Formation: " + response2.getFormation(), (float) (this.width - 350), (float) (this.height - 30), paint3);
        }
    }

    private int[] getFormation(LineUpResponse.Response response) {
        String replace = response.getFormation().replace("-", "");
        int length = replace.length();
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = Integer.parseInt(replace.charAt(i) + "");
        }
        return iArr;
    }
}
