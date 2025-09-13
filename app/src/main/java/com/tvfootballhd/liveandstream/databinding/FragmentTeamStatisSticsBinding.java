package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.tvfootballhd.liveandstream.R;

public final class FragmentTeamStatisSticsBinding implements ViewBinding {
    public final TextView awayPossessId;
    public final TextView homePossessId;
    public final LottieAnimationView lottiId;
    public final LinearLayout mainLL;
    public final NothingFoundBinding nothingFoundLL;
    public final CircularProgressIndicator possesId;
    private final FrameLayout rootView;
    public final RecyclerView statisticsRecyclerView;

    private FragmentTeamStatisSticsBinding(FrameLayout frameLayout, TextView textView, TextView textView2, LottieAnimationView lottieAnimationView, LinearLayout linearLayout, NothingFoundBinding nothingFoundBinding, CircularProgressIndicator circularProgressIndicator, RecyclerView recyclerView) {
        this.rootView = frameLayout;
        this.awayPossessId = textView;
        this.homePossessId = textView2;
        this.lottiId = lottieAnimationView;
        this.mainLL = linearLayout;
        this.nothingFoundLL = nothingFoundBinding;
        this.possesId = circularProgressIndicator;
        this.statisticsRecyclerView = recyclerView;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentTeamStatisSticsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentTeamStatisSticsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_team_statis_stics, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002c, code lost:
        r0 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentTeamStatisSticsBinding bind(android.view.View r11) {
        /*
            int r0 = com.livefoota.footballpro.R.id.awayPossessId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            r4 = r1
            android.widget.TextView r4 = (android.widget.TextView) r4
            if (r4 == 0) goto L_0x0058
            int r0 = com.livefoota.footballpro.R.id.homePossessId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            r5 = r1
            android.widget.TextView r5 = (android.widget.TextView) r5
            if (r5 == 0) goto L_0x0058
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            r6 = r1
            com.airbnb.lottie.LottieAnimationView r6 = (com.airbnb.lottie.LottieAnimationView) r6
            if (r6 == 0) goto L_0x0058
            int r0 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            r7 = r1
            android.widget.LinearLayout r7 = (android.widget.LinearLayout) r7
            if (r7 == 0) goto L_0x0058
            int r0 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            if (r1 == 0) goto L_0x0058
            com.livefoota.footballpro.databinding.NothingFoundBinding r8 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r1)
            int r0 = com.livefoota.footballpro.R.id.possesId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            r9 = r1
            com.google.android.material.progressindicator.CircularProgressIndicator r9 = (com.google.android.material.progressindicator.CircularProgressIndicator) r9
            if (r9 == 0) goto L_0x0058
            int r0 = com.livefoota.footballpro.R.id.statisticsRecyclerView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r11, r0)
            r10 = r1
            androidx.recyclerview.widget.RecyclerView r10 = (androidx.recyclerview.widget.RecyclerView) r10
            if (r10 == 0) goto L_0x0058
            com.livefoota.footballpro.databinding.FragmentTeamStatisSticsBinding r0 = new com.livefoota.footballpro.databinding.FragmentTeamStatisSticsBinding
            r3 = r11
            android.widget.FrameLayout r3 = (android.widget.FrameLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)
            return r0
        L_0x0058:
            android.content.res.Resources r11 = r11.getResources()
            java.lang.String r11 = r11.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r11 = r1.concat(r11)
            r0.<init>(r11)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentTeamStatisSticsBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentTeamStatisSticsBinding");
    }
}
